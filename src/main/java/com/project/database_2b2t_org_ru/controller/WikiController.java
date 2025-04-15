package com.project.database_2b2t_org_ru.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wiki")
public class WikiController {
    @GetMapping
    public String getWikiHome() {
        return "wikimain";
    }
}
