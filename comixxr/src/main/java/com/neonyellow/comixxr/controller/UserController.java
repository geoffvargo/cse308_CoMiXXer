package com.neonyellow.comixxr.controller;

import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.model.ComicCollection;
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

    @RequestMapping(value = {"/subscribeToUser/{userId}"}, method = RequestMethod.GET)
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

    @RequestMapping(value = {"/unsubscribeFromUser/{userId}"}, method = RequestMethod.GET)
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
        users.remove(modelAndView.getModel().get("currentUser"));
        modelAndView.addObject("users",users);
        modelAndView.addObject("titleText","Users");
        modelAndView.setViewName("users");
        return modelAndView;
    }

    @RequestMapping(value = {"/userProfile/{user}"}, method = RequestMethod.GET)
    public ModelAndView getUserProfile(@PathVariable ObjectId user){
        ModelAndView modelAndView = getMAVWithUser();

        User currUser = (User) modelAndView.getModel().get("currentUser");
        if(user.equals(currUser.get_id())){
            return new ModelAndView("redirect:/user/myProfile");
        }
        User profileUser = userService.findUserById(user);
        modelAndView.addObject("myCreations", userService.getPublishedComics(profileUser));
        modelAndView.addObject("subscribers", profileUser.getNumOfSubscibers());
        modelAndView.addObject("subscribedTo", profileUser.getNumOfSubsriptions());
        modelAndView.addObject("userName", profileUser.getFullname());
        modelAndView.addObject("userBio", profileUser.getBio());
        modelAndView.addObject("numRemixes", userService.getNumRemixes(profileUser));
        modelAndView.addObject("isSubscribed",currUser.getSubscriptions().contains(profileUser.get_id()));
        modelAndView.addObject("profileUser",profileUser);

        modelAndView.setViewName("userProfile");

        return modelAndView;
    }

    @RequestMapping(value = {"/updateBio"}, method = RequestMethod.POST)
    public boolean updateUserBio(@RequestBody MultiValueMap<String,String> data){
        ModelAndView mv = getMAVWithUser();
        User user = (User)mv.getModel().get("currentUser");

        user.setBio(data.getFirst("bioText"));
        userService.save(user);
        mv.setViewName("userSettings");
        return true;
    }

    /*GET CURRENT USER CURATIONS FROM DATABASE*/
    @RequestMapping(value = {"/curations"}, method = RequestMethod.GET)
    public ModelAndView getCurations(){

        ModelAndView modelAndView = getMAVWithUser();
        User currUser = (User) modelAndView.getModel().get("currentUser");

        modelAndView.addObject("curationList", currUser.getCurations());

        modelAndView.setViewName("curations");

        return modelAndView;
    }

    @RequestMapping(value = {"/addToCurations/{comic}/{curation}"}, method = RequestMethod.POST)
    public boolean addToCreation(@PathVariable ObjectId comic, @PathVariable ObjectId curation) {
        boolean ans = false;

        ModelAndView modelAndView = getMAVWithUser();
        User currUser = (User) modelAndView.getModel().get("currentUser");

        currUser.addToCuration(curation, comic);

        userService.save(currUser);

        return ans;
    }

    @RequestMapping(value = {"/createNewCuration"}, method = RequestMethod.POST)
    public boolean createCuration(@RequestBody MultiValueMap<String,String> formData) {
        boolean ans = false;

        ModelAndView modelAndView = getMAVWithUser();
        User currUser = (User) modelAndView.getModel().get("currentUser");

        ComicCollection curation = new ComicCollection(currUser.get_id());

        curation.setTitle(formData.getFirst("curationName"));

        return ans;
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

    @RequestMapping(value = {"/mySubscriptions"}, method = RequestMethod.GET)
    public ModelAndView getMySubscriptions() {
        ModelAndView modelAndView = getMAVWithUser();

        User currUser = (User) modelAndView.getModel().get("currentUser");
        List<ObjectId> userIds = currUser.getSubscriptions();
        List<User> users = userService.getUserListByIds(userIds);

        modelAndView.addObject("users",users);
        modelAndView.addObject("titleText",currUser.getFullname() + "'s Subscriptions");
        modelAndView.setViewName("users");

        return modelAndView;
    }

    /*GET LATEST POSTS OF CURRENT USER'S SUBSCRIPTIONS*/
    @RequestMapping(value = {"/getSubscribedTo/{userId}"}, method = RequestMethod.GET)
    public ModelAndView getSubsciptions(@PathVariable ObjectId userId){
        ModelAndView modelAndView = getMAVWithUser();
        User currUser = (User)modelAndView.getModel().get("currentUser");
        User profileUser = userService.findUserById(userId);
        List<ObjectId> userIds = profileUser.getSubscriptions();
        List<User> users = userService.getUserListByIds(userIds);
        modelAndView.addObject("users",users);
        modelAndView.addObject("titleText",profileUser.getFullname() + "'s Subscriptions");
        modelAndView.setViewName("users");
        return modelAndView;
    }

    @RequestMapping(value = {"/getSubscribers/{userId}"}, method = RequestMethod.GET)
    public ModelAndView getSubscibers(@PathVariable ObjectId userId){
        ModelAndView modelAndView = getMAVWithUser();
        User currUser = (User)modelAndView.getModel().get("currentUser");
        User profileUser = userService.findUserById(userId);
        List<ObjectId> userIds = profileUser.getSubscribers();
        List<User> users = userService.getUserListByIds(userIds);
        modelAndView.addObject("users",users);
        modelAndView.addObject("titleText",profileUser.getFullname() + "'s Subscribers");
        modelAndView.setViewName("users");
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
