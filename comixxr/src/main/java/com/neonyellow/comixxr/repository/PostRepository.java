package com.neonyellow.comixxr.repository;

import com.neonyellow.comixxr.model.Post;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, ObjectId> {
    Post findBy_id(ObjectId _id);
    List<Post> findAllByUserId(ObjectId userId);
    void deleteBy_id(ObjectId id);
}
