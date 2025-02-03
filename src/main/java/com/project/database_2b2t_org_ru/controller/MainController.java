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
    public String showThread(@PathVariable("id") int id, Model model){
        Thread thread = threadService.getObjectById(id);
        model.addAttribute("thread", thread);

        List messages = messageService.getAllMessafeForThread(id);
        model.addAttribute("messages", messages);

        Message message = new Message();
        model.addAttribute("newMessage", message);

        return "thread";
    }

    @RequestMapping("/{id}/refresh")
    public String refreshThread(@PathVariable("id") int id, Model model){
        Thread thread = threadService.getObjectById(id);
        model.addAttribute("thread", thread);
        return "thread";
    }

    @RequestMapping("/makeThread")
    public String makeThread(@ModelAttribute("newThread") Thread thread, Model model){
        thread.setDateTime(LocalDateTime.now());
        threadService.saveObject(thread);
        model.addAttribute( "thread", thread);
        return showThread(thread.getId(), model);
    }

    @RequestMapping("/{id}/sendMessage")
    public String sendMessage(@PathVariable("id") int id, @ModelAttribute("newMessage") Message newMessage, Model model){
        newMessage.setId(1);
        messageService.saveObject(newMessage);

        return showThread(1, model);
    }
}
