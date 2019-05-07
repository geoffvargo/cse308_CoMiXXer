package com.neonyellow.comixxr.controller;

import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.model.ComicCollection;
import com.neonyellow.comixxr.model.Genre;
import com.neonyellow.comixxr.model.Privacy;
import com.neonyellow.comixxr.model.User;
import com.neonyellow.comixxr.service.ComicCollectionService;
import com.neonyellow.comixxr.service.ComicService;
import com.neonyellow.comixxr.service.ComixUserDetailsService;
import com.neonyellow.comixxr.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
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

    @Autowired
    private ComicCollectionService comicCollectionService;

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
        modelAndView.addObject("active","create_comic");
        return modelAndView;
    }

    /*GET CURRENT USER CREATIONS FROM DATABASE*/
    @RequestMapping(value = {"/myProfile"}, method = RequestMethod.GET)
    public ModelAndView getMyProfile(){
        ModelAndView modelAndView = getMAVWithUser();

        User currUser = (User) modelAndView.getModel().get("currentUser");

        modelAndView.addObject("myCreations", userService.getPublishedComics(currUser, true));
        modelAndView.addObject("myDrafts", userService.getDrafts(currUser));
        modelAndView.addObject("subscribers", currUser.getNumOfSubscibers());
        modelAndView.addObject("subscribedTo", currUser.getNumOfSubsriptions());
        modelAndView.addObject("userName", currUser.getFullname());
        modelAndView.addObject("userBio", currUser.getBio());
        modelAndView.addObject("numRemixes", userService.getNumRemixes(currUser));
        modelAndView.addObject("active","my_profile");
        modelAndView.setViewName("myProfile");

        return modelAndView;
    }

    @RequestMapping(value = {"/browse/{genre}"}, method = RequestMethod.GET)
    public ModelAndView comicsByGenre(@PathVariable("genre") Genre genre) {
        ModelAndView modelAndView = getMAVWithUser();

        List<Comic> allByGenre = comicService.findAllByGenre(genre);

        ArrayList<Comic> ans = new ArrayList<>();

        allByGenre.forEach(c ->{if (c.getPrivacy() != Privacy.PRIVATE) {  ans.add(c);}});

        modelAndView.addObject("comics", ans);

        modelAndView.addObject("category", genre.toString());
        modelAndView.addObject("active","browse");

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
        modelAndView.addObject("active","users");

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
        modelAndView.addObject("myCreations", userService.getPublishedComics(profileUser, false));
        modelAndView.addObject("subscribers", profileUser.getNumOfSubscibers());
        modelAndView.addObject("subscribedTo", profileUser.getNumOfSubsriptions());
        modelAndView.addObject("userName", profileUser.getFullname());
        modelAndView.addObject("userBio", profileUser.getBio());
        modelAndView.addObject("numRemixes", userService.getNumRemixes(profileUser));
        modelAndView.addObject("isSubscribed",currUser.getSubscriptions().contains(profileUser.get_id()));
        modelAndView.addObject("profileUser",profileUser);
        modelAndView.addObject("active","users");

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
    @RequestMapping(value = {"/curations/{userId}"}, method = RequestMethod.GET)
    public ModelAndView getCurations(@PathVariable ObjectId userId){

        ModelAndView modelAndView = getMAVWithUser();
        User profileUser = userService.findUserById(userId);
        modelAndView.addObject("curations", comicCollectionService.getComicsByIds(profileUser.getCurations()));
        modelAndView.addObject("profileUser",profileUser);
        modelAndView.addObject("active","curations");

        modelAndView.setViewName("curations");

        return modelAndView;
    }

    /**
     *
     * @return List<ComicCollection> to be used in javascript
     */
    @RequestMapping(value = {"/getMyCurations"}, method = RequestMethod.GET)
    public List<ComicCollection> getCurationsObject(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userDetailsService.findUserByEmail(auth.getName());
        return comicCollectionService.getComicsByIds(user.getCurations());
    }

    @RequestMapping(value = {"/toggleCuration/{comic}/{curation}"}, method = RequestMethod.GET)
    public boolean addToCreation(@PathVariable ObjectId comic, @PathVariable ObjectId curation) {
        boolean ans = false;

        ModelAndView modelAndView = getMAVWithUser();
        User currUser = (User) modelAndView.getModel().get("currentUser");

        ans = toggleCuration(curation, comic);
        return ans;
    }

    public boolean toggleCuration(ObjectId curation, ObjectId comic) {
        try {
            ComicCollection currCollection = comicCollectionService.getComicCollectionById(curation);
            currCollection.toggleComic(comic);
            comicCollectionService.save(currCollection);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    @RequestMapping(value = {"/createNewCuration"}, method = RequestMethod.POST)
    public boolean createCuration(@RequestBody MultiValueMap<String,String> formData) {
        boolean ans = false;

        ModelAndView modelAndView = getMAVWithUser();
        User currUser = (User) modelAndView.getModel().get("currentUser");

        ComicCollection curation = new ComicCollection(currUser.get_id());

        curation.setTitle(formData.getFirst("curationName"));
        if (currUser.addCuration(curation)) {
            userService.save(currUser);
            comicCollectionService.save(curation);
            ans = true;
        }


        return ans;
    }

    @RequestMapping(value = {"/curation/{curationId}"}, method=RequestMethod.GET)
    public ModelAndView getCuration(@PathVariable("curationId") ObjectId curationId){
        ComicCollection cc = comicCollectionService.getComicCollectionById(curationId);
        ModelAndView modelAndView = getMAVWithUser();
        List<Comic> c = new ArrayList();
        for(ObjectId id : cc.getComics()){
            c.add(comicService.findBy_id(id));
        }
        modelAndView.addObject("curation",cc);
        modelAndView.addObject("comics",c);
        modelAndView.addObject("active","browse");
        modelAndView.setViewName("viewCuration");
        return modelAndView;
    }

    @RequestMapping(value = {"/topComics"}, method = RequestMethod.GET)
    public ModelAndView getTopComics() {
        ModelAndView modelAndView = getMAVWithUser();
        modelAndView.addObject("active","browse");

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
        modelAndView.addObject("active","my_profile");

        return modelAndView;
    }

    @RequestMapping(value = {"/mySubscriptions"}, method = RequestMethod.GET)
    public ModelAndView getMySubscriptions() {
        ModelAndView modelAndView = getMAVWithUser();

        User currUser = (User) modelAndView.getModel().get("currentUser");
        List<Comic> comics = userService.getMostRecentSubscriptionComics(currUser);
        for(Comic c : comics){
            c.setComicParentProfPic(userService.findUserById(c.getUserId()).getPic());
        }
        modelAndView.addObject("titleText",currUser.getFullname() + "'s Subscriptions");
        modelAndView.addObject("comics",comics);
        modelAndView.addObject("active","subscriptions");
        modelAndView.setViewName("subscriptions");

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
        modelAndView.addObject("active","users");
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
        modelAndView.addObject("active","users");
        modelAndView.setViewName("users");
        return modelAndView;
    }

    @RequestMapping(value = "/createComic", method = RequestMethod.GET)
    public ModelAndView getCreateComic() {
        ModelAndView modelAndView = getMAVWithUser();
        modelAndView.setViewName("createComic");
        modelAndView.addObject("active","create_comic");
        return modelAndView;
    }

    @RequestMapping(value = "/uploadProfilePicture", method = RequestMethod.POST)
    public boolean uploadProfPic(@RequestParam("profilePicture") MultipartFile profilePicture){
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userDetailsService.findUserByEmail(auth.getName());
            user.setPic("data:image/" + (profilePicture.getOriginalFilename().endsWith(".png")?"png":"jpg")+";base64," +Base64.getEncoder().encodeToString(profilePicture.getBytes()));
            userService.save(user);
        }
        catch(Exception e){
            return false;
        }
        return true;
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
