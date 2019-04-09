package com.neonyellow.comixxer.controller;

import com.neonyellow.comixxer.service.implementation.ComixUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class HomeController {
    @Autowired
    private ComixUserDetailsService userService;
}
