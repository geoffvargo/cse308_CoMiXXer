package com.neonyellow.comixxer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.neonyellow.comixxer.service.implementation.ComixUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
@Controller
@RequestMapping("/")
public class HomeController {
  private ComixUserDetailsService userService;
    @GetMapping
    public String getIndex(){
        return "index.html";
    }
}
