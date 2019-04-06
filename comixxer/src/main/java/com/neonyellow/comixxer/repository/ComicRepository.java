package com.neonyellow.comixxer.repository;

import com.neonyellow.comixxer.model.Comic;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

public interface ComicRepository extends CrudRepository<Comic, ObjectId> {
}
