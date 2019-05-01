package com.neonyellow.comixxr.controller;

import com.neonyellow.comixxr.model.User;
import com.neonyellow.comixxr.service.ComixUserDetailsService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class HomeController {
    @Autowired
    private ComixUserDetailsService userService;

    @RequestMapping(value = {"/","/index"}, method = RequestMethod.GET)
    public ModelAndView getIndex(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {

            /* The user is logged in :) */
            return new ModelAndView("forward:/browse");
        }
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.setViewName("index");

        if(user != null) {
            modelAndView.addObject("currentUser", user);
            modelAndView.addObject("fullName", "Welcome " + user.getFullname());
        }
        return modelAndView;
    }

    @RequestMapping(value = "/browse", method = RequestMethod.GET)
    public ModelAndView browse() {
        ModelAndView modelAndView = getMAVWithUser();
        modelAndView.setViewName("browse");
        return modelAndView;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView signup() {
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("signup");
        return modelAndView;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value = "/singlesGenre", method = RequestMethod.GET)
    public ModelAndView getSingleGenre() {
        ModelAndView modelAndView = getMAVWithUser();
        modelAndView.setViewName("singlesGenre");
        return modelAndView;
    }

    @RequestMapping(value = "/viewComic", method = RequestMethod.GET)
    public ModelAndView getViewComic() {
        ModelAndView modelAndView = getMAVWithUser();
        modelAndView.setViewName("viewComic");
        return modelAndView;
    }

    @RequestMapping(value = "/createComic", method = RequestMethod.GET)
    public ModelAndView getCreateComic() {
        ModelAndView modelAndView = getMAVWithUser();
        modelAndView.setViewName("createComic");
        return modelAndView;
    }

    @RequestMapping(value = "/seriesGenre", method = RequestMethod.GET)
    public ModelAndView getSeriesGenre() {
        ModelAndView modelAndView = getMAVWithUser();
        modelAndView.setViewName("seriesGenre");
        return modelAndView;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the username provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("signup");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("login");

        }
        return modelAndView;
    }

    private ModelAndView getMAVWithUser(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("currentUser", user);
        return modelAndView;
    }
}
