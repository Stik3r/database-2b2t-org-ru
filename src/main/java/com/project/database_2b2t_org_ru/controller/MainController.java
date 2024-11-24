package com.project.database_2b2t_org_ru.controller;

import com.project.database_2b2t_org_ru.entity.Thread;
import com.project.database_2b2t_org_ru.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private ThreadService threadService;
    @RequestMapping("/")
    public String showIndex(Model model) {
        List<Thread> threads = threadService.getAllThreads();
        model.addAttribute("threads", threads);
        return "index";
    }
}
