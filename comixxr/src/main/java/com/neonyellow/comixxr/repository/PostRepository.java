package com.neonyellow.comixxr.repository;

import com.neonyellow.comixxr.model.Post;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, ObjectId> {
    Post findBy_id(ObjectId _id);
}
