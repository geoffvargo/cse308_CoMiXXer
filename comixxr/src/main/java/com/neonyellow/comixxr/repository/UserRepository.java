package com.neonyellow.comixxr.repository;

import com.neonyellow.comixxr.model.ComicCollection;
import com.neonyellow.comixxr.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByEmail(String email);
    User findBy_id(ObjectId id);
    List<User> findAllByEnabled(boolean isEnabled);
    List<User> findAllByFullnameContainingAllIgnoreCase(String name);
}
