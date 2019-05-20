package com.neonyellow.comixxr.controller;

import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.model.Comment;
import com.neonyellow.comixxr.model.User;
import com.neonyellow.comixxr.service.ComicService;
import com.neonyellow.comixxr.service.CommentService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.neonyellow.comixxr.repository.UserRepository;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private ComicService comicService;
    @Autowired
    UserRepository userRepository;


    @RequestMapping(value = {"/addComment"}, method = RequestMethod.POST)
    public boolean addComment(@RequestParam("commentData") String comment, @RequestParam("comicId") ObjectId comicId){
        if(comment.length() < 1) return false;
        if(comicId == null) return false;
        // Grab User ID
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName());
        if(user == null) return false;
        // Create Comment
        Comment c = new Comment( user.get_id(), comment);
        commentService.save(c);
        // Add Comment to ComicID;
        Comic comic = comicService.findBy_id(comicId);
        comic.addComment(c);
        comicService.save(comic);

        return true;
    }

}
