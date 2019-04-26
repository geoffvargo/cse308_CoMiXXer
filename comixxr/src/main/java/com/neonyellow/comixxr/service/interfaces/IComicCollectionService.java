package com.neonyellow.comixxr.service.interfaces;

import com.neonyellow.comixxr.model.ComicCollection;

import org.bson.types.ObjectId;

public interface IComicCollectionService {

    void save(ComicCollection cc);

    void delete(ObjectId id);
}
