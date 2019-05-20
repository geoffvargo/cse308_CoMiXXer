package com.neonyellow.comixxr.service;

import com.neonyellow.comixxr.model.Comment;
import com.neonyellow.comixxr.repository.CommentRepository;
import com.neonyellow.comixxr.service.interfaces.k;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService implements k {
    @Autowired
    CommentRepository CommentRepository;

    public Comment findCommentBy_id(ObjectId id) { return CommentRepository.findBy_id(id); }

    public void save(Comment comment) { CommentRepository.save(comment);

    }

}

