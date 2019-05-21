package com.neonyellow.comixxr.service;

import com.neonyellow.comixxr.model.Comment;
import com.neonyellow.comixxr.model.CommentResponse;
import com.neonyellow.comixxr.model.User;
import com.neonyellow.comixxr.repository.ComicRepository;
import com.neonyellow.comixxr.repository.CommentRepository;
import com.neonyellow.comixxr.service.interfaces.ICommentService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CommentService implements ICommentService {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    CommentRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ComicRepository commicRepository;

    public Comment findCommentBy_id(ObjectId id) { return commentRepository.findBy_id(id); }

    public void save(Comment comment) { commentRepository.save(comment);}

    public List<CommentResponse> getCommentsForActivityFeed(User user){
        LocalDateTime present = LocalDateTime.now();
        List<Comment> commentActivity = new ArrayList<>();

        for (ObjectId id : user.getSubscriptions()) {
            List<Comment> comments = commentRepository.findAllByUserIdAndAgeBeforeOrderByAgeDesc(id, present);
            commentActivity.addAll(comments);
        }

        Collections.sort(commentActivity);
        List<CommentResponse> commentResponseList = new ArrayList<>(commentActivity.size());

        for(Comment comment : commentActivity){
            String title = commicRepository.findBy_id(comment.getComicId()).getTitle();
            commentResponseList.add(new CommentResponse(user.get_id(), user.getFullname(), user.getPic(), comment.getText(), comment.getAge(),title));
        }
        return commentResponseList;
    }
    public List<Comment> findComments(ObjectId comicId) { return commentRepository.findAllByComicIdOrderByAgeDesc(comicId); }




}
