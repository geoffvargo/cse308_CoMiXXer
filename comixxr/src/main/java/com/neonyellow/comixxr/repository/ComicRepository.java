package com.neonyellow.comixxr.repository;

import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.model.Genre;
import org.bson.types.ObjectId;
//import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ComicRepository extends MongoRepository<Comic, ObjectId > {
    Comic findBy_id(ObjectId _id);
    List<Comic> findAllByUserId(ObjectId userId);
    void deleteBy_id(ObjectId id);
    List<Comic> findAllByGenre(Genre genre);
    List<Comic> findAllByParent(ObjectId id);
    List<Comic> findAllByAgeAfter(LocalDateTime lastWeek);

}
