package com.neonyellow.comixxr.repository;

import com.neonyellow.comixxr.model.ComicCollection;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CcRepository extends MongoRepository<ComicCollection, ObjectId> {
    ComicCollection findBy_id(ObjectId _id);
    void deleteBy_id(ObjectId id);

    List<ComicCollection> findByUserId(ObjectId userId);

    ComicCollection findFirstByComicsContaining(ObjectId id);
}
