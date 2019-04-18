package com.neonyellow.comixxr.service;

import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.service.interfaces.IComicService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComicService implements IComicService {
    public List<Comic> findAllComicsByUserId(Object userId){
        return null;
    }
}
