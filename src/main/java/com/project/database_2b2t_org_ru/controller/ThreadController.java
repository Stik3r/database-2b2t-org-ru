package com.project.database_2b2t_org_ru.controller;

import com.project.database_2b2t_org_ru.entity.Message;
import com.project.database_2b2t_org_ru.entity.Thread;
import com.project.database_2b2t_org_ru.service.interfaces.MessageService;
import com.project.database_2b2t_org_ru.service.interfaces.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ThreadController {

    @Autowired
    private ThreadService threadService;
    @Autowired
    private MessageService messageService;

    @RequestMapping("/{id}")
    public String showThread(@PathVariable("id") String id, Model model) {
        Thread thread = threadService.getObjectById(Integer.parseInt(id));
        model.addAttribute("thread", thread);

        List messages = messageService.getAllMessafeForThread(Integer.parseInt(id));
        messages.remove(0);
        model.addAttribute("messages", messages);


        model.addAttribute("newMessage", new Message());

        return "thread";
    }

    @RequestMapping("/{id}/refresh")
    public String refreshThread(@PathVariable("id") String id, Model model) {
        Thread thread = threadService.getObjectById(Integer.parseInt(id));
        model.addAttribute("thread", thread);
        model.addAttribute("newMessage", new Message());
        return "thread";
    }

    @RequestMapping("/makeThread")
    public String makeThread(@ModelAttribute("newThread") Thread thread, Model model) {
        LocalDateTime dateTime = LocalDateTime.now();

        thread.setDateTime(dateTime);
        thread.setLastUpdate(dateTime);
        threadService.saveObject(thread);

        Message message = new Message();
        message.setThreadId(thread.getId());
        message.setDateTime(dateTime);
        message.setMessage_body(thread.getBody());
        messageService.saveObject(message);

        thread.setFirstMessageID(message.getId());
        threadService.saveObject(thread);

        return "redirect:/" + String.valueOf(thread.getId());
    }

    @RequestMapping("/{id}/sendMessage")
    public String sendMessage(@PathVariable("id") String id, @ModelAttribute("newMessage") Message newMessage, Model model) {

        LocalDateTime now = LocalDateTime.now();
        int intId = Integer.parseInt(id);

        newMessage.setThreadId(intId);
        newMessage.setDateTime(now);

        Thread thread = threadService.getObjectById(intId);
        thread.setLastUpdate(now);

        messageService.saveObject(newMessage);
        threadService.saveObject(thread);
        
        return "redirect:/" + id;
    }
}
