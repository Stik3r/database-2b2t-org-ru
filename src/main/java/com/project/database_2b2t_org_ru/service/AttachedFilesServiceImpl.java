package com.project.database_2b2t_org_ru.service;

import com.project.database_2b2t_org_ru.dao.AttachedFilesRepository;
import com.project.database_2b2t_org_ru.entity.AttachedFiles;
import com.project.database_2b2t_org_ru.entity.Message;
import com.project.database_2b2t_org_ru.service.interfaces.MainService;
import org.apache.tika.Tika;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
                throw new IllegalArgumentException("File " + file.getOriginalFilename() + " exceeds maximum size of 50MB");
            }

            // Определение реального типа файла через Tika
            String detectedType = tika.detect(file.getBytes());

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

        if (isPicture) {
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

        ByteArrayInputStream inputStream = new ByteArrayInputStream(originalFile);

        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(inputStream);
        frameGrabber.start();

        Frame frame = frameGrabber.grabImage();
        frameGrabber.stop();

        if (frame == null) {
            throw new RuntimeException("Не удалось извлечь кадр из видео.");
        }

        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.convert(frame);

        BufferedImage thumbnail = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        thumbnail.getGraphics().drawImage(bufferedImage, 0, 0, width, height, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(thumbnail, "jpg", baos);

        return baos.toByteArray();
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
                } else {
                    thumbnail.setBase64Image(
                            convertToBase64(
                                    createImageThumbnail(file.getFileBytea(), width, height, false)));
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