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
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private ThreadService threadService;
    @Autowired
    private MessageService messageService;

    @RequestMapping("/")
    public String showIndex(Model model) {
        List<Thread> threads = threadService.getAllObjects();
        Thread thread = new Thread();
        model.addAttribute("threads", threads);
        model.addAttribute("newThread", thread);
        return "index";
    }

    @RequestMapping("/{id}")
    public String showThread(@PathVariable("id") String id, Model model){
        Thread thread = threadService.getObjectById(Integer.parseInt(id));
        model.addAttribute("thread", thread);

        List messages = messageService.getAllMessafeForThread(Integer.parseInt(id));
        model.addAttribute("messages", messages);


        model.addAttribute("newMessage", new Message());

        return "thread";
    }

    @RequestMapping("/{id}/refresh")
    public String refreshThread(@PathVariable("id") String id, Model model){
        Thread thread = threadService.getObjectById(Integer.parseInt(id));
        model.addAttribute("thread", thread);
        model.addAttribute("newMessage", new Message());
        return "thread";
    }

    @RequestMapping("/makeThread")
    public String makeThread(@ModelAttribute("newThread") Thread thread, Model model){
        LocalDateTime dateTime = LocalDateTime.now();

        thread.setDateTime(dateTime);
        threadService.saveObject(thread);

        Message message = new Message();
        message.setThreadId(thread.getId());
        message.setDateTime(dateTime);
        message.setMessage_body(thread.getBody());
        messageService.saveObject(message);

        return "redirect:/" + String.valueOf(thread.getId());
    }

    @RequestMapping("/{id}/sendMessage")
    public String sendMessage(@PathVariable("id") String id, @ModelAttribute("newMessage") Message newMessage, Model model){


        newMessage.setThreadId(Integer.parseInt(id));
        newMessage.setDateTime(LocalDateTime.now());
        messageService.saveObject(newMessage);

        return "redirect:/" + id;
    }
}
