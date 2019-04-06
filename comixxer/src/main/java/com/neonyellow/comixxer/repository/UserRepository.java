package com.neonyellow.comixxer.repository;

import com.neonyellow.comixxer.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, ObjectId> {
}
