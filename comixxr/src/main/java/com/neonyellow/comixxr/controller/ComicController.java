package com.neonyellow.comixxr.controller;

import com.neonyellow.comixxr.model.*;
import com.neonyellow.comixxr.repository.UserRepository;
import com.neonyellow.comixxr.service.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import javax.websocket.server.PathParam;
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
    @Autowired
    private ComicCollectionService ccService;
    @Autowired
    UserService userService;

    @RequestMapping(value = {"createDraft"}, method = RequestMethod.POST)
    public ModelAndView createDraft(@RequestParam("comicName") String comicName, @RequestParam("privacy") String privacy, @RequestParam("genre") String genre, @RequestParam("thumbnail") MultipartFile thumbnail){
        ModelAndView mv = getMAVWithUser();
        User user = (User)mv.getModel().get("currentUser");

        Comic draft = new Comic(user.get_id());

        draft.setTitle(comicName);
        draft.setPrivacy(comicService.getPrivacy(privacy));
        draft.setGenre(comicService.getGenre(genre));
        draft.setAuthor(user.getFullname());

        if(!thumbnail.isEmpty()){
            try{
                draft.setCustomThumbnail("data:image/" + (thumbnail.getOriginalFilename().endsWith(".png")?"png":"jpg")+";base64," + Base64.getEncoder().encodeToString(thumbnail.getBytes()));
            }
            catch(Exception e){
                draft.setCustomThumbnail(null);
            }
        }

        comicService.save(draft);

        user.addToComics(draft);
        userRepository.save(user);

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
        comic.setThumbnail(comic.getImage_data().get(0));
        comicService.save(comic);
    }

    @RequestMapping(value = {"/editComic/{comicId}"}, method = RequestMethod.GET)
    public ModelAndView editComicPage(@PathVariable ObjectId comicId){
        ModelAndView modelAndView = getMAVWithUser();
        Comic c = comicService.findBy_id(comicId);
        modelAndView.addObject("comic",c);
        modelAndView.setViewName("editComic");
        return modelAndView;
    }

    @RequestMapping(value = {"/makeChanges"}, method = RequestMethod.POST)
    public ModelAndView makeChanges(@RequestParam("thumbnail") MultipartFile thumbnail,
                                    @RequestParam("comicName") String comicName,
                                    @RequestParam("privacyBox") String privacy,
                                    @RequestParam("comicId") ObjectId comicId){
        ModelAndView modelAndView = getMAVWithUser();
        Comic currentComic = comicService.findBy_id(comicId);
        if(!thumbnail.isEmpty()){
            try {
                currentComic.setCustomThumbnail("data:image/" + (thumbnail.getOriginalFilename().endsWith(".png") ? "png" : "jpg") + ";base64," + Base64.getEncoder().encodeToString(thumbnail.getBytes()));
                currentComic.setThumbnail("data:image/" + (thumbnail.getOriginalFilename().endsWith(".png")?"png":"jpg")+";base64," + Base64.getEncoder().encodeToString(thumbnail.getBytes()));
            }
            catch(Exception e){
                currentComic.setCustomThumbnail("");
                currentComic.setThumbnail("");
            }
        }
        if(comicName.length() < 2){
            modelAndView.setViewName("editComic");
            modelAndView.addObject("nameError",true);
            return modelAndView;
        }
        currentComic.setTitle(comicName);
        currentComic.setPrivacy(comicService.getPrivacy(privacy));
        comicService.save(currentComic);
        modelAndView.setViewName("redirect:/user/myProfile");
        return modelAndView;
    }

    @RequestMapping(value = {"/private"}, method = RequestMethod.GET)
    public ModelAndView getPrivate(){
        ModelAndView modelAndView = getMAVWithUser();
        modelAndView.setViewName("private");
        return modelAndView;
    }

    @RequestMapping(value = {"/viewComic/{comicId}"}, method = RequestMethod.GET)
    public ModelAndView viewComic(@PathVariable("comicId") String comicId){
        ModelAndView mv = getMAVWithUser();
        ObjectId id = new ObjectId(comicId);
        User currUser = (User)mv.getModel().get("currentUser");
        Comic comic = comicService.findBy_id(id);
        if(comic.getPrivacy() == Privacy.PRIVATE && !comic.getUserId().equals(currUser.get_id())){
            mv.setViewName("viewComic");
            mv.addObject("private",true);
            return mv;
        }

        List<String> pages = comic.getImage_data();

        int len = pages.size();

        if(comic.getParent() != null){
            Comic parent = comicService.findBy_id(comic.getParent());
            mv.addObject("parentObject", parent);
        }

        List<Comic> children = comicService.findAllRemixes(id);

        if(children.size() > 0){
            mv.addObject("children",children);
        }

        mv.addObject("numPages", len);
        mv.addObject("pages",pages);
        mv.setViewName("viewComic");

        mv.setViewName("viewComic");
        mv.addObject("comic",comic);
        mv.addObject("upvoted",comic.containsUpvote(currUser.get_id()));
        mv.addObject("downvoted",comic.containsDownvote(currUser.get_id()));
        mv.addObject("active","browse");

        List<Comic> blah = comicService.getAllChildren(comic.get_id());

        blah.forEach(c -> System.out.println(c.get_id()));

        return mv;

    }

    @RequestMapping(value = {"/viewAllRemixes/{comicId}"}, method = RequestMethod.GET)
    public ModelAndView viewRemixes(@PathVariable("comicId") ObjectId comicId) {
        ModelAndView modelAndView = getMAVWithUser();

        List<Comic> remixList = comicService.getAllChildren(comicId);
        Comic by_id = comicService.findBy_id(comicId);

        modelAndView.addObject("remixList", remixList);
        modelAndView.addObject("comicTitle", by_id.getTitle());
        modelAndView.addObject("comicThumbnail", by_id.getThumbnail());

        modelAndView.setViewName("viewAllRemixes");

        return modelAndView;
    }

    @RequestMapping(value = {"/publish"}, method = RequestMethod.POST)
    public void comicPublish(@RequestBody MultiValueMap<String, Object> data){
        ModelAndView mv = getMAVWithUser();

        List<Object> res = saveCanvasDataAndImageData(data);
        Comic comic = (Comic)res.get(0);
        comic.setPublished(true);
        if(comic.getCustomThumbnail() == null){
            comic.setThumbnail(comic.getImage_data().get(0));
        }
        else{
            comic.setThumbnail(comic.getCustomThumbnail());
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
        Comic remix = new Comic(currUser.get_id());
        Comic parentComic = comicService.findBy_id(new ObjectId(data.getFirst("parentComic")));

        remix.setRemix(true);
        remix.setTitle(data.getFirst("comicName"));
        remix.setPrivacy(comicService.getPrivacy(data.getFirst("privacy")));
        remix.setGenre(comicService.getGenre(data.getFirst("genre")));
        remix.setAuthor(currUser.getFullname());
        remix.setParent(parentComic.get_id());
        remix.setRaw_data(parentComic.getRaw_data());
        remix.setImage_data(parentComic.getImage_data());
        remix.setThumbnail(parentComic.getThumbnail());
        comicService.save(remix);

        parentComic.getRemixes().add(remix.get_id());
        comicService.save(parentComic);


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

    @RequestMapping(value = {"/delete/{comicId}"}, method = RequestMethod.GET)
    public ModelAndView comicDelete(@PathVariable("comicId") ObjectId comicId){
        ModelAndView mv = getMAVWithUser();
        mv.setViewName("redirect:/user/myProfile");
        comicService.delete(comicId);
        User user = (User)mv.getModel().get("currentUser");
        for (ObjectId id: user.getComics()) {
            if(id.equals(comicId)){
                user.getComics().remove(id);
            }
        }

        return mv;
    }

    @RequestMapping(value = {"/deleteCuration/{curationId}"}, method = RequestMethod.GET)
    public ModelAndView curationDelete(@PathVariable("curationId") ObjectId curationId){
        ModelAndView mv = getMAVWithUser();

        ccService.delete(curationId);
        mv.setViewName("redirect:/user/curation/"+((User)mv.getModel().get("currentUser")).get_id().toHexString());
        return mv;
    }

    @RequestMapping(value = {"/search"}, method = RequestMethod.GET)
    public ModelAndView search(@RequestParam("search_param") String itemName){
        ModelAndView mv = getMAVWithUser();
        mv.setViewName("search");

        List<User> users = userService.searchUsersWithName(itemName);
        List<Comic> comics = comicService.searchComicsWithTitle(itemName);

        mv.addObject("users", users);
        mv.addObject("comics", comics);
        mv.addObject("category",itemName);

        return mv;
    }

    @RequestMapping(value = {"/genres"}, method = RequestMethod.GET)
    public List<String> getGenreList(){
        List<String> genres = new ArrayList<>();
        genres.add("HORROR");
        genres.add("TEEN");
        genres.add("FANTASY");
        genres.add("CRIME");
        genres.add("COMEDY");
        genres.add("ROMANCE");
        genres.add("MANGA");
        genres.add("ALTERNATIVE");
        genres.add("GAG");
        genres.add("SCIFI");
        genres.add("SUPERHERO");
        genres.add("CHILD");
        genres.add("WAR");
        genres.add("DAILY");
        genres.add("WESTERN");
        genres.add("ABSTRACT");
        genres.add("ADVENTURE");

        return genres;
    }

    @RequestMapping(value = {"/activity"}, method = RequestMethod.GET)
    public ModelAndView getActivity(){
        ModelAndView mv = getMAVWithUser();
        mv.setViewName("activity");
        User user = (User)mv.getModel().get("currentUser");

        mv.addObject("remixes", comicService.getRemixesForActivityFeed(user));

        return mv;
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

        List<String> image_data = new ArrayList<>();

        for (Object ob: z) {
            String pg = (String)ob;
            image_data.add(pg);
        }

        comic.setImage_data(image_data);

        List<Object> result = new ArrayList<>();
        result.add(comic);
        result.add(comicId);

        return result;
    }

    //    @RequestMapping(value = {"createDraft"}, method = RequestMethod.POST)
//    public ModelAndView createDraft(@RequestBody MultiValueMap<String,String> formData){
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = userRepository.findByEmail(auth.getName());
//
//        Comic draft = new Comic(user.get_id(),null);
//        draft.setTitle(formData.getFirst("comicName"));
//        draft.setPrivacy(getPrivacy(formData.getFirst("privacy")));
//        draft.setGenre(getGenre(formData.getFirst("genre")));
//        draft.setAuthor(user.getFullname());
//
//        comicService.save(draft);
//
//        user.addToComics(draft);
//        userRepository.save(user);
//
//        ModelAndView mv = new ModelAndView();
//        mv.addObject("comicId", draft.get_id().toString());
//        mv.addObject("currentUser", user);
//        mv.addObject("isLoad", "false");
//        mv.addObject("active","create_comic");
//
//        mv.setViewName("draw");
//
//        return mv;
//    }
}
