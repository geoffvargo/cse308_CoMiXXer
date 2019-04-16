package com.neonyellow.comixxr.controller;

import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.model.User;
import com.neonyellow.comixxr.repository.UserRepository;
import com.neonyellow.comixxr.service.ComixUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private ComixUserDetailsService userService;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = {"/draw"}, method = RequestMethod.POST)
    public ModelAndView getDrawPage(){
        //TODO: here add the comic to the user
        ModelAndView modelAndView = getMAVWithUser();
        modelAndView.setViewName("draw");
        return modelAndView;
    }

    /*GET CURRENT USER CREATIONS FROM DATABASE*/
    @RequestMapping(value = {"/myProfile"}, method = RequestMethod.GET)
    public ModelAndView getMyProfile(){
        ModelAndView modelAndView = getMAVWithUser();
        //TODO: add my creations, drafts, user information
        modelAndView.setViewName("myProfile");
        return modelAndView;
    }

    @RequestMapping(value = {"/userProfile"}, method = RequestMethod.GET)
    public ModelAndView getUserProfile(){
        ModelAndView modelAndView = getMAVWithUser();
        //TODO: add creations, drafts, user information for different user
        modelAndView.setViewName("userProfile");
        return modelAndView;
    }

    /*GET CURRENT USER CURATIONS FROM DATABASE*/
    @RequestMapping(value = {"/curations"}, method = RequestMethod.GET)
    public ModelAndView getCurations(){

        return null;
    }

    /*GET LATEST POSTS OF CURRENT USER'S SUBSCRIPTIONS*/
    @RequestMapping(value = {"/subscriptions"}, method = RequestMethod.GET)
    public ModelAndView getSubsciptions(){
        return null;
    }

//    /*GET USER BROWSE PAGE*/
//    @RequestMapping(value = {"/browse"}, method = RequestMethod.GET)
//    public ModelAndView getBrowse(){
//        return null;
//    }

    /*HELPER FUNCTIONS*/
    private ArrayList<Comic> getPopularSingles(){
        return null;
    }

    private ArrayList<Comic> getPopularSeries(){
        return null;
    }

    public ModelAndView getMAVWithUser(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("currentUser", user);
        return modelAndView;
    }
}