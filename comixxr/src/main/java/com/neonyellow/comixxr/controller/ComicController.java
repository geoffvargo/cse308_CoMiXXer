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
import java.util.ArrayList;
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

        Comic draft = new Comic(user.get_id(),null);
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
        mv.addObject("active","create_comic");

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
    public void comicSave(@RequestBody MultiValueMap<String,Object> data){

        Comic comic = (Comic)saveCanvasDataAndImageData(data).get(0);
        comicService.save(comic);
    }

    @RequestMapping(value = {"/viewComic/{comicId}"}, method = RequestMethod.GET)
    public ModelAndView viewComic(@PathVariable("comicId") String comicId){
        ModelAndView mv = getMAVWithUser();
        ObjectId id = new ObjectId(comicId);

        Comic comic = comicService.findBy_id(id);

        List<String> pages = comic.getImage_data();

        int len = pages.size();

        mv.addObject("numPages", len);
        mv.addObject("pages",pages);
        mv.setViewName("viewComic");

        User currUser = (User)mv.getModel().get("currentUser");
        mv.setViewName("viewComic");
        mv.addObject("comic",comic);
        mv.addObject("upvoted",comic.containsUpvote(currUser.get_id()));
        mv.addObject("downvoted",comic.containsDownvote(currUser.get_id()));
        mv.addObject("active","browse");

        return mv;

    }


    @RequestMapping(value = {"/publish"}, method = RequestMethod.POST)
    public void comicPublish(@RequestBody MultiValueMap<String, Object> data){
        ModelAndView mv = getMAVWithUser();

        List<Object> res = saveCanvasDataAndImageData(data);
        Comic comic = (Comic)res.get(0);
        comic.setPublished(true);
        if(comic.getThumbNail() == null){
            comic.setThumbNail(comic.getImage_data().get(0));
        }
        comicService.save(comic);

        Post newPost = new Post(((User)mv.getModel().get("currentUser")).get_id());
        newPost.setComicId((ObjectId)res.get(1));
        postService.save(newPost);
    }

    @RequestMapping(value = {"/load/{comicId}"}, method = RequestMethod.GET)
    public ModelAndView comicLoad(@PathVariable("comicId") String comicId){
        ModelAndView mv = getMAVWithUser();
        mv.setViewName("draw");

        ObjectId comicid = new ObjectId(comicId);
        String raw_data = comicService.findBy_id(comicid).getRaw_data();

        mv.addObject("isLoad", "true");
        mv.addObject("raw_data", raw_data);
        mv.addObject("active","create_comic");
        return mv;
    }

    @RequestMapping(value = {"/remix"}, method = RequestMethod.POST)
    public ModelAndView remixComic(@RequestBody MultiValueMap<String, String> data){
        ModelAndView mv = getMAVWithUser();
        User currUser = (User)mv.getModel().get("currentUser");
        Comic remix = new Comic(currUser.get_id(), null);
        Comic parentComic = comicService.findBy_id(new ObjectId(data.getFirst("parentComic")));

        remix.setRemix(true);
        remix.setTitle(data.getFirst("comicName"));
        remix.setPrivacy(getPrivacy(data.getFirst("privacy")));
        remix.setGenre(getGenre(data.getFirst("genre")));
        remix.setAuthor(currUser.getFullname());
        remix.setParent(parentComic.get_id());
        remix.setRaw_data(parentComic.getRaw_data());
        remix.setImage_data(parentComic.getImage_data());

        comicService.save(remix);

        currUser.addToComics(remix);
        userRepository.save(currUser);

        String remix_raw_data = remix.getRaw_data();

        mv.setViewName("draw");
        mv.addObject("comicId", remix.get_id().toString());
        mv.addObject("currentUser", currUser);
        mv.addObject("isLoad", "true");
        mv.addObject("raw_data", remix_raw_data);
        mv.addObject("active","create_comic");
        return mv;
    }

    private Genre getGenre(String genre){
        if(genre == null)
            return Genre.NA;
        switch(genre.toUpperCase()){
            case "HORROR":
                return Genre.HORROR;
            case "TEEN":
                return Genre.TEEN;
            case "FANTASY":
                return Genre.FANTASY;
            case "CRIME":
                return Genre.CRIME;
            case "ROMANCE":
                return Genre.ROMANCE;
            case "MANGA":
                return Genre.MANGA;
            case "ALTERNATIVE":
                return Genre.ALTERNATIVE;
            case "GAG":
                return Genre.GAG;
            case "SCIFI":
                return Genre.SCIFI;
            case "SUPERHERO":
                return Genre.SUPERHERO;
            case "CHILD":
                return Genre.CHILD;
            case "WAR":
                return Genre.WAR;
            case "DAILY":
                return Genre.DAILY;
            case "WESTERN":
                return Genre.WESTERN;
            case "ABSTRACT":
                return Genre.ABSTRACT;
            default:
                return Genre.NA;
        }
    }

    private Privacy getPrivacy(String privacy){
        if(privacy == null)
            return Privacy.PRIVATE;
        switch(privacy.toUpperCase()){
            case "PRIVATE":
                return Privacy.PRIVATE;
            case "PUBLIC":
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

    private List<Object> saveCanvasDataAndImageData(MultiValueMap<String, Object> data){
        List<Object> x = data.get("comicId[]");
        String id = (String)x.get(0);
        ObjectId comicId = new ObjectId(id);
        Comic comic = comicService.findBy_id(comicId);

        List<Object> y = data.get("comicData[]");
        String canvasData = (String)y.get(0);
        comic.setRaw_data(canvasData);

        List<Object> z = data.get("imageData[]");

        for (Object ob: z) {
            String pg = (String)ob;
            comic.getImage_data().add(pg);
        }

        List<Object> result = new ArrayList<>();
        result.add(comic);
        result.add(comicId);

        return result;
    }
}
