package com.neonyellow.comixxr.repository;

import com.neonyellow.comixxr.model.ComicCollection;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CcRepository extends MongoRepository<ComicCollection, ObjectId> {
    ComicCollection findBy_id(ObjectId _id);
    void deleteBy_id(ObjectId id);

    ComicCollection findByUserId(ObjectId userId);
}
