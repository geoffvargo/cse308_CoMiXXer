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
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private ComixUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private ComicService comicService;

    @RequestMapping(value = {"/subscribeToUser/{userId}"}, method = RequestMethod.POST)
    public boolean subscribeToUser(@PathVariable ObjectId userId) {
        boolean ans = false;
        ModelAndView modelAndView = getMAVWithUser();

        User currUser = (User) modelAndView.getModel().get("currentUser");
        User otherUser = userService.findUserById(userId);

        if (otherUser != null && !currUser.getSubscriptions().contains((Object)(otherUser.get_id()))) {
            currUser.addToSubscriptions(otherUser);
            otherUser.addSubsciber(currUser);

            userService.save(currUser);
            userService.save(otherUser);
            ans = true;
        }

        modelAndView.setViewName("addToMySubscriptions");

        return ans;
    }

    @RequestMapping(value = {"/unsubscribeFromUser/{userId}"}, method = RequestMethod.POST)
    public boolean unsubscribeFromUser(@PathVariable ObjectId userId) {
        boolean ans = false;
        ModelAndView modelAndView = getMAVWithUser();

        User currUser = (User) modelAndView.getModel().get("currentUser");
        User otherUser = userService.findUserById(userId);

        if (otherUser != null && currUser.getSubscriptions().contains((Object)(otherUser.get_id()))) {
            currUser.removeFromSubscriptions(otherUser);
            otherUser.removeFromSubscribers(currUser);

            userService.save(currUser);
            userService.save(otherUser);
            ans = true;
        }

        modelAndView.setViewName("removeFromMySubscriptions");

        return ans;
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

    @RequestMapping(value={"/users"},method = RequestMethod.GET)
    public ModelAndView getUsers(){
        ModelAndView modelAndView = getMAVWithUser();
        List<User> users = userService.getUserList();
        modelAndView.addObject("users",users);
        modelAndView.setViewName("users");
        return modelAndView;
    }

    @RequestMapping(value = {"/userProfile/{user}"}, method = RequestMethod.GET)
    public ModelAndView getUserProfile(@PathVariable ObjectId user){
        ModelAndView modelAndView = getMAVWithUser();

        User currUser = (User) modelAndView.getModel().get("currentUser");
        User profileUser = userService.findUserById(user);
        modelAndView.addObject("myCreations", userService.getPublishedComics(profileUser));
        modelAndView.addObject("subscribers", profileUser.getNumOfSubscibers());
        modelAndView.addObject("subscribedTo", profileUser.getNumOfSubsriptions());
        modelAndView.addObject("userName", profileUser.getFullname());
        modelAndView.addObject("userBio", profileUser.getBio());
        modelAndView.addObject("numRemixes", userService.getNumRemixes(profileUser));
        modelAndView.addObject("isSubscribed",currUser.getSubscriptions().contains(profileUser));
        modelAndView.addObject("profileUser",profileUser);

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
    @RequestMapping(value = {"/curations"}, method = RequestMethod.GET)
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
