package com.neonyellow.comixxr.controller;

import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.model.User;
import com.neonyellow.comixxr.repository.ComicRepository;
import com.neonyellow.comixxr.repository.UserRepository;
import com.neonyellow.comixxr.service.ComixUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
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

    @RequestMapping(value = {"createDraft"}, method = RequestMethod.POST)
    public ModelAndView createDraft(@RequestBody MultiValueMap<String,String> formData){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName());

        Comic draft = new Comic(user.get_id());


        return null;

    }

    @RequestMapping(value = {"/save"}, method = RequestMethod.POST)
    public ModelAndView comicSave(@RequestBody MultiValueMap<String,String> data){
        //find user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName());

        Comic newComic = new Comic(user.get_id());

        String comicData = data.getFirst("comicData");

        newComic.setRaw_data(comicData);

        comicRepository.save(newComic);


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
