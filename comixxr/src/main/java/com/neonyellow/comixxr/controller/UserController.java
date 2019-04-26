package com.neonyellow.comixxr.controller;

import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.model.Genre;
import com.neonyellow.comixxr.model.User;
import com.neonyellow.comixxr.repository.UserRepository;
import com.neonyellow.comixxr.service.ComicService;
import com.neonyellow.comixxr.service.ComixUserDetailsService;
import com.neonyellow.comixxr.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private ComixUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private ComicService comicService;

    @RequestMapping(value = {"/subscribeToUser"}, method = RequestMethod.POST)
    public ModelAndView subscribeToUser(@PathVariable("email") String email) {
        ModelAndView modelAndView = getMAVWithUser();

        User currUser = (User) modelAndView.getModel().get("currentUser");
        User otherUser = userService.findUserByEmail(email);

        currUser.addToSubscriptions(otherUser);
        otherUser.addSubsciber(currUser);

        modelAndView.setViewName("mySubscriptions");

        return modelAndView;
    }

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

        User currUser = (User) modelAndView.getModel().get("currentUser");

        modelAndView.addObject("myCreations", userService.getPublishedComics(currUser));
        modelAndView.addObject("myDrafts", userService.getDrafts(currUser));
        modelAndView.addObject("subscribers", currUser.getNumOfSubscibers());
        modelAndView.addObject("subscribedTo", currUser.getNumOfSubsriptions());
        modelAndView.addObject("userName", currUser.getFullname());
        modelAndView.addObject("userBio", currUser.getBio());
        modelAndView.addObject("numRemixes", userService.getNumRemixes(currUser));

        modelAndView.setViewName("myProfile");

        return modelAndView;
    }

    @RequestMapping(value = {"/browse/{genre}"}, method = RequestMethod.GET)
    public ModelAndView comicsByGenre(@PathVariable("genre") Genre genre) {
        ModelAndView modelAndView = getMAVWithUser();

        modelAndView.addObject("comics", comicService.findAllByGenre(genre));
        modelAndView.addObject("category", genre.toString());

        modelAndView.setViewName("singlesGenre");

        return modelAndView;
    }

    @RequestMapping(value = {"/userProfile"}, method = RequestMethod.GET)
    public ModelAndView getUserProfile(){
        ModelAndView modelAndView = getMAVWithUser();
        //TODO: add creations, drafts, user information for different user
        modelAndView.setViewName("userProfile");
        return modelAndView;
    }

    @RequestMapping(value = {"/updateBio"}, method = RequestMethod.POST)
    public ModelAndView updateUserBio(@RequestBody MultiValueMap<String,String> data){
        ModelAndView mv = getMAVWithUser();
        User user = (User)mv.getModel().get("currentUser");

        user.setBio(data.getFirst("bioText"));
        userService.save(user);
        mv.setViewName("userSettings");
        return mv;
    }

    /*GET CURRENT USER CURATIONS FROM DATABASE*/
    @RequestMapping(value = {"/{curations}"}, method = RequestMethod.GET)
    public ModelAndView getCurations(){

        ModelAndView modelAndView = getMAVWithUser();
        String title = "My Curations";

        modelAndView.addObject("myCurations", title);

        modelAndView.setViewName("curations");

        return modelAndView;
    }

    @RequestMapping(value = {"/topComics"}, method = RequestMethod.GET)
    public ModelAndView getTopComics() {
        ModelAndView modelAndView = getMAVWithUser();

        modelAndView.setViewName("topComics");

        return modelAndView;
    }

    @RequestMapping(value = {"/userSettings"}, method= RequestMethod.GET)
    public ModelAndView getUserSettings(){
        ModelAndView modelAndView = getMAVWithUser();
        modelAndView.setViewName("userSettings");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userDetailsService.findUserByEmail(auth.getName());
        modelAndView.addObject("currentBio",user.getBio());
        return modelAndView;
    }

    /*GET LATEST POSTS OF CURRENT USER'S SUBSCRIPTIONS*/
    @RequestMapping(value = {"/subscriptions"}, method = RequestMethod.GET)
    public ModelAndView getSubsciptions(){
        ModelAndView modelAndView = getMAVWithUser();

        User currUser = (User) modelAndView.getModel().get("currentUser");
//        ArrayList<>

        modelAndView.addObject("subscriptions", currUser.getSubscriptions());

        modelAndView.setViewName("subscriptions");

        return modelAndView;
    }

    /*GET COMIC TO VIEW*/
    @RequestMapping(value = "/viewComic/{comicId}", method = RequestMethod.GET)
    public ModelAndView getComic(@PathVariable String comicId){
        Comic comic = comicService.findBy_id(new ObjectId(comicId));
        ModelAndView modelAndView = getMAVWithUser();
        modelAndView.setViewName("viewComic");
        modelAndView.addObject("numPages",3);
        modelAndView.addObject("comic",comic);
        //TODO: save all pages to img/comicPages/pagei
        return modelAndView;
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

    private ModelAndView getMAVWithUser(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userDetailsService.findUserByEmail(auth.getName());
        modelAndView.addObject("currentUser", user);
        return modelAndView;
    }
}