package com.neonyellow.comixxr.service.interfaces;

import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.model.ComicCollection;

import com.neonyellow.comixxr.model.Genre;
import com.neonyellow.comixxr.model.User;
import org.bson.types.ObjectId;

import java.util.List;

public interface IComicCollectionService {

    void save(ComicCollection cc);

    void delete(ComicCollection cc, User user);

    List<ComicCollection> findAllByGenre(Genre genre);

    List<ComicCollection> findAllByComicsContaining(ObjectId id);
}
