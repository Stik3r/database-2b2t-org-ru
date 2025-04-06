package com.project.database_2b2t_org_ru.service;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.project.database_2b2t_org_ru.dao.AttachedFilesRepository;
import com.project.database_2b2t_org_ru.entity.AttachedFiles;
import com.project.database_2b2t_org_ru.entity.Message;
import com.project.database_2b2t_org_ru.service.interfaces.MainService;
import org.apache.tika.Tika;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class AttachedFilesServiceImpl implements MainService<AttachedFiles> {

    @Autowired
    private AttachedFilesRepository attachedFilesRepository;

    @Autowired
    private MessageServiceImpl messageService;

    private static final long MAX_FILE_SIZE = 25 * 1024 * 1024; // 50MB
    private final Tika tika = new Tika();


    @Override
    public List<AttachedFiles> getAllObjects() {
        return attachedFilesRepository.findAll();
    }

    @Override
    public void saveObject(AttachedFiles object) {
        attachedFilesRepository.save(object);
    }

    @Override
    public AttachedFiles getObjectById(long id) {
        return attachedFilesRepository.findById((long) id).orElse(null);
    }

    @Override
    public void deleteObjectById(long id) {
        attachedFilesRepository.deleteById(id);
    }


    public List<AttachedFiles> processFiles(MultipartFile[] files, long messageId) throws IOException {
        List<AttachedFiles> attachedFiles = new ArrayList<>();
        Message message = messageService.getObjectById(messageId);

        if (message == null) {
            throw new IllegalArgumentException("Message not found");
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            // Проверка размера файла
            if (file.getSize() > MAX_FILE_SIZE) {
                throw new IllegalArgumentException("File " + file.getOriginalFilename() + " exceeds maximum size of 25MB");
            }

            var fileName = file.getOriginalFilename();

            String detectedType;

            if (fileName.substring(fileName.length() - 5, fileName.length()).equals(".webm")) {
                ContentInfoUtil util = new ContentInfoUtil();
                ContentInfo info = util.findMatch(file.getBytes());
                detectedType = info.getMimeType();
            }
            else {
                detectedType = tika.detect(file.getBytes());
            }


            // Проверка типа файла
            if (!isAllowedFileType(detectedType)) {
                throw new IllegalArgumentException("File type " + detectedType + " is not allowed");
            }

            // Создание и настройка AttachedFiles
            AttachedFiles attachedFile = new AttachedFiles();
            attachedFile.setFileName(file.getOriginalFilename());
            attachedFile.setFileType(detectedType);
            attachedFile.setFileBytea(file.getBytes());
            attachedFile.setMessage(message);

            attachedFiles.add(attachedFile);
        }

        return attachedFilesRepository.saveAll(attachedFiles);
    }

    public List<AttachedFiles> findAllByMessageId(long messageId) {
        return attachedFilesRepository.findByMessageId(messageId);
    }

    private boolean isAllowedFileType(String contentType) {
        return contentType != null &&
                (contentType.startsWith("image/") || contentType.startsWith("video/"));
    }

    public byte[] createImageThumbnail(byte[] originalFile, int width, int height, boolean isPicture) throws IOException {

        if (isPicture) { // Обработка фоток
            ImageIO.scanForPlugins();

            ByteArrayInputStream bis = new ByteArrayInputStream(originalFile);
            BufferedImage originalBufferedImage = ImageIO.read(bis);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();


            Thumbnails.of(originalBufferedImage)
                    .size(width, height)
                    .outputFormat("jpg")
                    .toOutputStream(baos);


            return baos.toByteArray();
        }
        //Обработка видео
        Path tempVideoFile = Files.createTempFile("video", ".mp4");
        Files.write(tempVideoFile, originalFile);

        VideoCapture capture = new VideoCapture(tempVideoFile.toString());
        Mat frame = new Mat();


        if (!capture.isOpened()) {
            capture.release();
            Files.deleteIfExists(tempVideoFile);
            throw new RuntimeException("Не удалось открыть видеофайл.");
        }

        boolean readSuccess = capture.read(frame);
        capture.release();

        Files.deleteIfExists(tempVideoFile);

        if (!readSuccess) {
            throw new RuntimeException("Не удалось извлечь кадр из видео.");
        }

        int originalWidth = frame.width();
        int originalHeight = frame.height();

        double scaleWidth = (double) width / originalWidth;
        double scaleHeight = (double) height / originalHeight;
        double scaleFactor = Math.min(scaleWidth, scaleHeight);

        //масштабируем его
        if (scaleFactor < 1.0) {
            int newWidth = (int) Math.round(originalWidth * scaleFactor);
            int newHeight = (int) Math.round(originalHeight * scaleFactor);
            Mat resizedFrame = new Mat();
            Imgproc.resize(frame, resizedFrame, new Size(newWidth, newHeight));
            frame = resizedFrame;
        }

        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".jpg", frame, mob);
        return mob.toArray();
    }

    public List<Message.Thumbnail> createImageThumbnailList(List<AttachedFiles> originalFiles, int width, int height) {
        List<Message.Thumbnail> result = new ArrayList<>();
        for (AttachedFiles file : originalFiles) {
            try {
                var thumbnail = new Message.Thumbnail();

                if (file.getFileType().startsWith("image/")) {
                    thumbnail.setBase64Image(
                            convertToBase64(
                                    createImageThumbnail(file.getFileBytea(), width, height, true)));
                    thumbnail.setSourceType("image");
                } else {
                    thumbnail.setBase64Image(
                            convertToBase64(
                                    createImageThumbnail(file.getFileBytea(), width, height, false)));
                    thumbnail.setSourceType("video");
                }
                thumbnail.setImageId(file.getId());
                result.add(thumbnail);
            } catch (IOException e) {
                return null;
            }
        }
        return result;
    }

    public String convertToBase64(byte[] thumbnail) {
        return Base64.getEncoder().encodeToString(thumbnail);
    }

} 