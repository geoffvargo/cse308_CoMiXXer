package com.neonyellow.comixxr.service;


import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.repository.ComicRepository;
import com.neonyellow.comixxr.repository.UserRepository;
import com.neonyellow.comixxr.service.interfaces.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ComicRepository comicRepository;

    public List<Comic> getAllComics(){
        return null;
    }

    public Comic findComicBy_id(ObjectId id){
      return comicRepository.findBy_id(id);
    }
}
