package com.neonyellow.comixxer.controller;

import com.neonyellow.comixxer.model.Comic;
import com.neonyellow.comixxer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import sun.net.www.content.text.Generic;

import java.util.ArrayList;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepository userRepository;

    /*GET CURRENT USER CREATIONS FROM DATABASE*/
    @RequestMapping(value = {"/myCreations"}, method = RequestMethod.GET)
    public ModelAndView getCreations(){

        return null;
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

    /*GET USER BROWSE PAGE*/
    @RequestMapping(value = {"/browse"}, method = RequestMethod.GET)
    public ModelAndView getBrowse(){
        return null;
    }

    /*HELPER FUNCTIONS*/
    private ArrayList<Comic> getPopularSingles(){
        return null;
    }

    private ArrayList<Comic> getPopularSeries(){
        return null;
    }
}
