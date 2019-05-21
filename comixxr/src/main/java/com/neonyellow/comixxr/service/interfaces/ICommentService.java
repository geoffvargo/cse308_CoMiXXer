package com.neonyellow.comixxr.service.interfaces;

import com.neonyellow.comixxr.model.Comment;
import com.neonyellow.comixxr.model.CommentResponse;


import com.neonyellow.comixxr.model.User;
import org.bson.types.ObjectId;

import java.util.List;

public interface ICommentService {
    Comment findCommentBy_id(ObjectId id);
    List<CommentResponse> getCommentsForActivityFeed(User user);
    List<Comment> findComments(ObjectId comicId);
}
