package com.neonyellow.comixxr.repository;

import com.neonyellow.comixxr.model.Comic;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ComicRepository extends MongoRepository<Comic, ObjectId> {
}
