package com.neonyellow.comixxr.repository;

import com.neonyellow.comixxr.model.Comment;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, ObjectId> {
    Comment findBy_id(ObjectId _id);
    List<Comment> findAllByUserIdAndAgeBeforeOrderByAgeDesc(ObjectId id, LocalDateTime time);
}
