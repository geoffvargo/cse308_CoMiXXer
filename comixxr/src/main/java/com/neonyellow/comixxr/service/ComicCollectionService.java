package com.neonyellow.comixxr.service;

import com.neonyellow.comixxr.model.ComicCollection;
import com.neonyellow.comixxr.service.interfaces.IComicCollectionService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComicCollectionService implements IComicCollectionService {

    @Autowired
    private ComicCollectionService comicCollectionService;

    @Override
    public void save(ComicCollection cc) {
        comicCollectionService.save(cc);
    }

    @Override
    public void delete(ObjectId id) {
        comicCollectionService.delete(id);
    }
}
