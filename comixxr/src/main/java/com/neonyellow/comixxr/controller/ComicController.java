package com.neonyellow.comixxr.controller;

import com.neonyellow.comixxr.repository.ComicRepository;
import com.neonyellow.comixxr.repository.UserRepository;
import com.neonyellow.comixxr.service.ComixUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/user/comic")
public class ComicController {

    @Autowired
    private ComixUserDetailsService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ComicRepository comicRepository;

    @RequestMapping(value = {"/save"}, method = RequestMethod.POST)
    public ModelAndView comicSave(){

        return null;
    }

    @RequestMapping(value = {"/saveAs"}, method = RequestMethod.POST)
    public ModelAndView comicSaveAs(){

        return null;
    }

    @RequestMapping(value = {"/load"}, method = RequestMethod.GET)
    public ModelAndView comicLoad(){

        return null;
    }

}
