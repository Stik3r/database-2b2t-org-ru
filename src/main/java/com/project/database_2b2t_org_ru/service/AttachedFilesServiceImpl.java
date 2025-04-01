package com.project.database_2b2t_org_ru.service;

import com.project.database_2b2t_org_ru.dao.AttachedFilesRepository;
import com.project.database_2b2t_org_ru.entity.AttachedFiles;
import com.project.database_2b2t_org_ru.entity.Message;
import com.project.database_2b2t_org_ru.service.interfaces.AttachedFilesService;
import com.project.database_2b2t_org_ru.service.interfaces.MessageService;
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
import java.util.List;

@Service
public class AttachedFilesServiceImpl implements AttachedFilesService {

    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB
    private final Tika tika = new Tika();

    @Autowired
    private AttachedFilesRepository attachedFilesRepository;

    @Autowired
    private MessageService messageService;

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

    @Override
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

    @Override
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

            // Создаем миниатюру нужного размера (с сохранением пропорций)
            BufferedImage thumbnail = Thumbnails.of(originalBufferedImage)
                    .size(width, height)
                    .asBufferedImage();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, "jpg", baos);

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

    public List<byte[]> createImageThumbnailList(List<AttachedFiles> originalFiles, int width, int height) {
        List<byte[]> result = new ArrayList<>();
        for (AttachedFiles file : originalFiles) {
            try {
                if (file.getFileType().startsWith("image/"))
                    result.add(createImageThumbnail(file.getFileBytea(), width, height, true));
                else
                    result.add(createImageThumbnail(file.getFileBytea(), width, height, false));
            } catch (IOException e) {

            }
        }
        return result;
    }
} 