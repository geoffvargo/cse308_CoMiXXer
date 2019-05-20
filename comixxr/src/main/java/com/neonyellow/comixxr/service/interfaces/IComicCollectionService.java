package com.neonyellow.comixxr.service.interfaces;

import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.model.ComicCollection;

import com.neonyellow.comixxr.model.Genre;
import org.bson.types.ObjectId;

import java.util.List;

public interface IComicCollectionService {

    void save(ComicCollection cc);

    void delete(ObjectId id);

    List<ComicCollection> findAllByGenre(Genre genre);
}
