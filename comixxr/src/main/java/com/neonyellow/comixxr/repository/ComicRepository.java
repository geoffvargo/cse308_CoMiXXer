package com.neonyellow.comixxr.repository;

import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.model.Genre;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ComicRepository extends MongoRepository<Comic, ObjectId > {
    Comic findBy_id(ObjectId _id);
    List<Comic> findAllByUserId(ObjectId userId);
    void deleteById(ObjectId id);
    List<Comic> findAllByGenre(Genre genre);
}
