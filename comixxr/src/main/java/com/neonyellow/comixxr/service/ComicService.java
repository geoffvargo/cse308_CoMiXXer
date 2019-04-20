package com.neonyellow.comixxr.service;

import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.repository.ComicRepository;
import com.neonyellow.comixxr.service.interfaces.IComicService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComicService implements IComicService {

    @Autowired
    ComicRepository comicRepository;

    public List<Comic> findAllByUserId(ObjectId userId){
        return comicRepository.findAllByUserId(userId);
    }

    @Override
    public List<Comic> findAllComicsByUserId(ObjectId userId) {
        return comicRepository.findAllByUserId(userId);
    }

    public Comic findBy_id(ObjectId id){
        return comicRepository.findBy_id(id);
    }

    public void save(Comic comic){
        comicRepository.save(comic);
    }

    @Override
    public void deleteComic(ObjectId id) {

    }

    public void deleteBy_id(ObjectId id) { comicRepository.deleteBy_id(id);}
}
