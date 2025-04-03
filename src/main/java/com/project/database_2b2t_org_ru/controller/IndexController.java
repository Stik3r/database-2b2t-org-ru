package com.project.database_2b2t_org_ru.controller;

import com.project.database_2b2t_org_ru.entity.Thread;
import com.project.database_2b2t_org_ru.service.ThreadServiceImpl;
import com.project.database_2b2t_org_ru.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private ThreadServiceImpl threadService;

    @RequestMapping("/")
    public String showIndex(Model model) {
        List<Thread> threads = threadService.findAllOnPage(0, 5);


        Thread thread = new Thread();
        model.addAttribute("threads", threads);
        model.addAttribute("newThread", thread);
        return "index";
    }

    @GetMapping("/api/threads")
    @ResponseBody
    public List<Thread> getThreadsAsJson(@RequestParam int page, @RequestParam int size) {
        return CommonUtils.sortByField(new ArrayList<>(threadService.findAllOnPage(page, size)),
                Comparator.comparing(Thread::getLastUpdate).reversed());
    }
}
