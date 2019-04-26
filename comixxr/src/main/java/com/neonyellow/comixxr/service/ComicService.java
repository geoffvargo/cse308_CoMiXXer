package com.neonyellow.comixxr.service;

import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.model.Genre;
import com.neonyellow.comixxr.repository.ComicRepository;
import com.neonyellow.comixxr.service.interfaces.IComicService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComicService implements IComicService {

    @Autowired
    ComicRepository comicRepository;

    public List<Comic> findAllByUserId(ObjectId userId) {
        return comicRepository.findAllByUserId(userId);
    }

    public Comic findBy_id(ObjectId id) {
        return comicRepository.findBy_id(id);
    }

    public void save(Comic comic) {
        comicRepository.save(comic);
    }

    public void delete(ObjectId id) {comicRepository.deleteBy_id(id);}

    public List<Comic> findAllSortedByGenreDESC() {
        return comicRepository.findAll(new Sort(Sort.Direction.DESC, "genre"));
    }

    public List<Comic> findAllSortedByGenreASC() {
        return comicRepository.findAll(new Sort(Sort.Direction.ASC, "genre"));
    }

    public List<Comic> findAllByGenre(Genre genre) {
        return comicRepository.findAllByGenre(genre);
    }

//    public List<Comic> findTopRated() {
//        ArrayList<Comic> temp;
////        for (Genre g : temp = comicRepository.findAllByGenre(g)) {
////        }
//
//        return null;
//    }

}
