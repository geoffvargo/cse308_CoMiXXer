package com.neonyellow.comixxr.service.interfaces;

import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.model.Genre;

import org.bson.types.ObjectId;

import java.util.List;

public interface IComicService {
    List<Comic> findAllByUserId(ObjectId userId);

    Comic findBy_id(ObjectId id);

    void save(Comic comic);

    void delete(ObjectId id);

    List<Comic> findAllSortedByGenreDESC();

    List<Comic> findAllSortedByGenreASC();

    List<Comic> findAllByGenre(Genre genre);

    List<Comic> findAll();

    List<Comic> getAllChildren(ObjectId comicId);

    List<Comic> searchComicsWithTitle(String title);

}