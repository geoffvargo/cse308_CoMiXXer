package com.neonyellow.comixxer.repository;

import com.neonyellow.comixxer.model.Post;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, ObjectId> {
}
