package com.neonyellow.comixxr.service.interfaces;

import com.neonyellow.comixxr.model.Post;
import org.bson.types.ObjectId;

import java.util.List;

public interface IPostService {
    Post findBy_id(ObjectId id);

    List<Post> findAllByUserId(ObjectId id);

    void save(Post post);

    void deleteBy_id(ObjectId id);
}
