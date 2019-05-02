package com.neonyellow.comixxr.controller;

import com.neonyellow.comixxr.model.*;
import com.neonyellow.comixxr.repository.UserRepository;
import com.neonyellow.comixxr.service.ComicService;
import com.neonyellow.comixxr.service.ComixUserDetailsService;
import com.neonyellow.comixxr.service.PostService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;

import sun.misc.BASE64Decoder;

@RestController
@RequestMapping("/user/comic")
public class ComicController {

    @Autowired
    private ComixUserDetailsService userDetailsService;
    @Autowired
    private ComicService comicService;
    @Autowired
    private PostService postService;
    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = {"createDraft"}, method = RequestMethod.POST)
    public ModelAndView createDraft(@RequestBody MultiValueMap<String,String> formData){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName());

        Comic draft = new Comic(user.get_id());
        draft.setTitle(formData.getFirst("comicName"));
        draft.setPrivacy(getPrivacy(formData.getFirst("privacy")));
        draft.setGenre(getGenre(formData.getFirst("genre")));
        draft.setAuthor(user.getFullname());

        comicService.save(draft);

        user.addToComics(draft);
        userRepository.save(user);

        ModelAndView mv = new ModelAndView();
        mv.addObject("comicId", draft.get_id().toString());
        mv.addObject("currentUser", user);
        mv.addObject("isLoad", "false");
        mv.setViewName("draw");

        return mv;
    }

    @RequestMapping(value = {"upvote/{comicId}"}, method = RequestMethod.GET)
    public boolean upVote(@PathVariable("comicId") ObjectId comicId){
        Comic c = comicService.findBy_id(comicId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userDetailsService.findUserByEmail(auth.getName());
        for(ObjectId id : c.getDownVote()){
            if(id.equals(user.get_id())){
                c.removeDownvote(id);
                break;
            }
        }
        boolean exists = false;
        for(ObjectId id : c.getUpVote()){
            if(id.equals(user.get_id())){
                exists = true;
                break;
            }
        }
        if(!exists){
            c.addUpvote(user.get_id());
        }
        comicService.save(c);
        return true;
    }

    @RequestMapping(value = {"downvote/{comicId}"}, method = RequestMethod.GET)
    public boolean downVote(@PathVariable("comicId") ObjectId comicId){
        Comic c = comicService.findBy_id(comicId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userDetailsService.findUserByEmail(auth.getName());
        for(ObjectId id : c.getUpVote()){
            if(id.equals(user.get_id())){
                c.removeUpvote(id);
                break;
            }
        }
        boolean exists = false;
        for(ObjectId id : c.getDownVote()){
            if(id.equals(user.get_id())){
                exists = true;
                break;
            }
        }
        if(!exists){
            c.addDownvote(user.get_id());
        }
        comicService.save(c);
        return true;
    }

    @RequestMapping(value = {"/save"}, method = RequestMethod.POST)
    public ModelAndView comicSave(@RequestBody MultiValueMap<String,String[]> data){
//        ObjectId comicId = new ObjectId(id);
//        Comic comic = comicService.findBy_id(comicId);
//        String comicData = comicD;
        String[] imgData = data.getFirst("imageData");

//        comic.setRaw_data(comicData);
//
//        comicService.save(comic);

        return null;
    }

//    @RequestMapping(value = {"/view/{comicId}"}, method = RequestMethod.GET)
//    public String viewComic(@PathVariable("comicId") String comicId){
//        ModelAndView mv = getMAVWithUser();
//        mv.setViewName("viewComic");
//
//        ObjectId comicid = new ObjectId(comicId);
//        byte[] pdf_bytes = comicService.findBy_id(comicid).getPdf_data();
//
//        String pdfData = new String(pdf_bytes);
//
//        String base64encoding = pdfData.split(",")[1];
//
//        return pdfData;
//    }

    @RequestMapping(value = {"/publish"}, method = RequestMethod.POST)
    public ModelAndView comicPublish(@RequestBody MultiValueMap<String, String> data){
        ModelAndView mv = getMAVWithUser();
        ObjectId comicId = new ObjectId(data.getFirst("comicId"));
        Comic comic = comicService.findBy_id(comicId);
        String comicData = data.getFirst("comicData");

        comic.setRaw_data(comicData);
        comic.setPublished(true);
        comic.setPrivacy(Privacy.PUBLIC);
        comicService.save(comic);

        Post newPost = new Post(((User)mv.getModel().get("currentUser")).get_id());
        newPost.setComicId(comicId);
        postService.save(newPost);

        return null;
    }

    @RequestMapping(value = {"/load/{comicId}"}, method = RequestMethod.GET)
    public ModelAndView comicLoad(@PathVariable("comicId") String comicId){
        ModelAndView mv = getMAVWithUser();
        mv.setViewName("draw");

        ObjectId comicid = new ObjectId(comicId);
        String raw_data = comicService.findBy_id(comicid).getRaw_data();

        mv.addObject("isLoad", "true");
        mv.addObject("raw_data", raw_data);

        return mv;
    }

    private Genre getGenre(String genre){
        if(genre == null)
            return Genre.NA;
        switch(genre){
            case "Horror":
                return Genre.HORROR;
            case "Teen":
                return Genre.TEEN;
            case "Fantasy":
                return Genre.FANTASY;
            case "Crime":
                return Genre.CRIME;
            case "Romance":
                return Genre.ROMANCE;
            case "Manga":
                return Genre.MANGA;
            case "Alternative":
                return Genre.ALTERNATIVE;
            case "Gag":
                return Genre.GAG;
            case "Sci-Fi":
                return Genre.SCIFI;
            case "Super Hero":
                return Genre.SUPERHERO;
            case "Child":
                return Genre.CHILD;
            case "War":
                return Genre.WAR;
            case "Daily":
                return Genre.DAILY;
            case "Western":
                return Genre.WESTERN;
            case "Abstract":
                return Genre.ABSTRACT;
            default:
                return Genre.NA;
        }
    }

    private Privacy getPrivacy(String privacy){
        if(privacy == null)
            return Privacy.PRIVATE;
        switch(privacy){
            case "Private":
                return Privacy.PRIVATE;
            case "Public":
                return Privacy.PUBLIC;
            default:
                return Privacy.UNLISTED;
        }
    }

    private ModelAndView getMAVWithUser(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userDetailsService.findUserByEmail(auth.getName());
        modelAndView.addObject("currentUser", user);
        return modelAndView;
    }
}
