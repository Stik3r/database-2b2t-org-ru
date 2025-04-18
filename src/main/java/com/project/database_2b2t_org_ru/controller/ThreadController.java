package com.project.database_2b2t_org_ru.controller;

import com.project.database_2b2t_org_ru.entity.Message;
import com.project.database_2b2t_org_ru.entity.Thread;
import com.project.database_2b2t_org_ru.service.AttachedFilesServiceImpl;
import com.project.database_2b2t_org_ru.service.MessageServiceImpl;
import com.project.database_2b2t_org_ru.service.ThreadServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/thread")
public class ThreadController {

    @Autowired
    private ThreadServiceImpl threadService;
    @Autowired
    private MessageServiceImpl messageService;
    @Autowired
    private AttachedFilesServiceImpl attachedFilesService;

    @RequestMapping("/{id}")
    public String showThread(@PathVariable("id") String id, Model model) {
        Thread thread = threadService.getObjectById(Integer.parseInt(id));
        model.addAttribute("thread", thread);

        List<Message> messages = messageService.getAllMessageForThread(Long.parseLong(id));
        messages.remove(0);
        model.addAttribute("messages", messages);

        // Получаем файлы для каждого сообщения
        for (Message message : messages) {
            var files = attachedFilesService.findAllByMessageId(message.getId());

            message.setAttachedFiles(files);
            message.setThumbnails(
                    attachedFilesService.createImageThumbnailList(files));
        }

        model.addAttribute("newMessage", new Message());
        return "thread";
    }

    @RequestMapping("/makeThread")
    public String makeThread(@ModelAttribute("newThread") Thread thread, Model model) {
        LocalDateTime dateTime = LocalDateTime.now();

        thread.setDateTime(dateTime);
        threadService.saveObject(thread);

        Message message = new Message();
        message.setThread(thread);
        message.setDateTime(dateTime);
        message.setMessage_body(thread.getBody());
        messageService.saveObject(message);

        thread.setFirstMessageID(message.getId());
        threadService.saveObject(thread);

        return "redirect:/thread/" + String.valueOf(thread.getId());
    }

    @RequestMapping("/{id}/sendMessage")
    public String sendMessage(@PathVariable("id") String id,
                              @ModelAttribute("newMessage") Message newMessage,
                              @RequestParam(value = "files", required = false) MultipartFile[] files,
                              Model model) {
        LocalDateTime now = LocalDateTime.now();
        int intId = Integer.parseInt(id);

        Message message = new Message();
        Thread thread = threadService.getObjectById(intId);
        message.setMessage_body(newMessage.getMessage_body());
        message.setThread(thread);
        message.setDateTime(now);

        messageService.saveObject(message);

        // Обработка файлов
        if (files != null && files.length > 0) {
            try {
                attachedFilesService.processFiles(files, message.getId());
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
            }
        }

        return "redirect:/thread/" + id;
    }
}
