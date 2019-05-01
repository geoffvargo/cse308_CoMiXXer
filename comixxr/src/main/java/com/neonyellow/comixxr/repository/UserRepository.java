package com.neonyellow.comixxr.repository;

import com.neonyellow.comixxr.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByEmail(String email);

    User findBy_id(ObjectId userId);
}
