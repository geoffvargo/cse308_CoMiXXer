package com.neonyellow.comixxr.service;

import com.neonyellow.comixxr.model.Post;
import com.neonyellow.comixxr.repository.PostRepository;
import com.neonyellow.comixxr.service.interfaces.IPostService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService implements IPostService {

    @Autowired
    PostRepository postRepository;

    @Override
    public Post findBy_id(ObjectId id) {
        return postRepository.findBy_id(id);
    }

    @Override
    public List<Post> findAllByUserId(ObjectId id) {
        return postRepository.findAllByUserId(id);
    }

    @Override
    public void save(Post post) {
        postRepository.save(post);
    }

    @Override
    public void deleteBy_id(ObjectId id) {
        postRepository.deleteBy_id(id);
    }
}
