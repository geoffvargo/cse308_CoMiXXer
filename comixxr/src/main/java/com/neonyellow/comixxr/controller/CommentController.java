package com.neonyellow.comixxr.controller;

import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.model.Comment;
import com.neonyellow.comixxr.model.CommentResponse;
import com.neonyellow.comixxr.model.User;
import com.neonyellow.comixxr.service.ComicService;
import com.neonyellow.comixxr.service.CommentService;
import com.neonyellow.comixxr.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.neonyellow.comixxr.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private ComicService comicService;
    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;


    @RequestMapping(value = {"/addComment"}, method = RequestMethod.POST)
    public CommentResponse addComment(@RequestParam("commentData") String comment, @RequestParam("comicId") ObjectId comicId){
        System.out.println("|||||||||||   I GOT HEREEE   |||||||||||");
        if(comment.length() < 1) return null;
        if(comicId == null) return null;
        // Grab User ID
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName());
        if(user == null) return null;
        // Create Comment
        Comment c = new Comment( user.get_id(), comment, comicId);
        commentService.save(c);
        // Add Comment to ComicID;
        Comic comic = comicService.findBy_id(comicId);
        comic.addComment(c);
        comicService.save(comic);

        CommentResponse comment_response = new CommentResponse(user.get_id(),user.getFullname(), user.getPic(), c.getText(), c.getAge());
        return comment_response;
    }

    @RequestMapping(value = {"/getComment/{comicId}"}, method = RequestMethod.GET)
    public List<CommentResponse> getComments(@PathVariable("comicId") ObjectId comicId){
        List<Comment> commentsList = commentService.findComments(comicId);
        List<CommentResponse> commentResponseList = new ArrayList<>(commentsList.size());

        for(Comment comment : commentsList){
            User user = userService.findUserById(comment.getUserId());
            commentResponseList.add(new CommentResponse(user.get_id(), user.getFullname(), user.getPic(), comment.getText(), comment.getAge() ));
        }
        return commentResponseList;
    }



}
