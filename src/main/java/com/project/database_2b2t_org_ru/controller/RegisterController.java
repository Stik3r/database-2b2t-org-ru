package com.project.database_2b2t_org_ru.controller;

import com.project.database_2b2t_org_ru.entity.Thread;
import com.project.database_2b2t_org_ru.entity.User;
import com.project.database_2b2t_org_ru.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegisterController {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @RequestMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("newUser", new User());
        return "register";
    }

    @RequestMapping("/register/makeacc")
    public String getBack(@ModelAttribute("newUser") User user) {

        customUserDetailsService.registerNewUser(user);

        return "index";
    }

}
