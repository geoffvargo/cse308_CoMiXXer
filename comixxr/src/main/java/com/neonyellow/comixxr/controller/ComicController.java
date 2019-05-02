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
import java.io.IOException;
import java.util.List;
import javax.jws.WebParam;
import javax.xml.bind.DatatypeConverter;

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
    public ModelAndView comicSave(@RequestBody MultiValueMap<String,Object> data){
        List<Object> x = data.get("comicId[]");
        String id = (String)x.get(0);
        ObjectId comicId = new ObjectId(id);
        Comic comic = comicService.findBy_id(comicId);

        List<Object> y = data.get("comicData[]");
        String comicData = (String)y.get(0);
        comic.setRaw_data(comicData);

        List<Object> z = data.get("imageData[]");

        for (Object ob: z) {
            String pg = (String)ob;
            comic.getImage_data().add(pg);
        }

        comicService.save(comic);

        return null;
    }

    @RequestMapping(value = {"/viewComic/{comicId}"}, method = RequestMethod.GET)
    public ModelAndView viewComic(@PathVariable("comicId") String comicId){
        ModelAndView mv = getMAVWithUser();
        ObjectId id = new ObjectId(comicId);

        Comic comic = comicService.findBy_id(id);

        List<String> pages = comic.getImage_data();

        int len = pages.size();
        for(int i = 0; i<len; i++){
            int pageNum = i+1;
            createComicPageFile(pages.get(i), String.valueOf(pageNum));
        }

        mv.addObject("numPages", len);
        mv.setViewName("viewComic");

        User currUser = (User)mv.getModel().get("currentUser");
        mv.setViewName("viewComic");
        mv.addObject("comic",comic);
        mv.addObject("upvoted",comic.containsUpvote(currUser.get_id()));
        mv.addObject("downvoted",comic.containsDownvote(currUser.get_id()));

        return mv;

    }


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

    private void createComicPageFile(String imageURI, String pageNum) {
        String fileSeparator = System.getProperty("file.separator");

        String[] strings = imageURI.split(",");
        String extension;
        switch (strings[0]) {//check image's extension
            case "data:image/jpeg;base64":
                extension = "jpeg";
                break;
            case "data:image/png;base64":
                extension = "png";
                break;
            default://should write cases for more images types
                extension = "jpg";
                break;
        }
        //convert base64 string to binary data
        byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);

        String path = "src"+fileSeparator+"main"+fileSeparator+"resources"+fileSeparator+"static"+fileSeparator+"img"+fileSeparator+"comicPages"+fileSeparator+"page"+pageNum+"." + extension;
        File file = new File(path);

            try {
                if(file.createNewFile()){
                    OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
                    outputStream.write(data);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }





    }
}
