package com.neonyellow.comixxr.service.interfaces;

import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.model.Genre;

import org.bson.types.ObjectId;

import java.util.List;

public interface IComicService {
    List<Comic> findAllComicsByUserId(ObjectId userId);

    Comic findBy_id(ObjectId id);

    void save(Comic comic);

//    void deleteComic(ObjectId id);

    List<Comic> findAllSortedByGenreDESC();

    List<Comic> findAllSortedByGenreASC();

    List<Comic> findAllByGenre(Genre genre);

}