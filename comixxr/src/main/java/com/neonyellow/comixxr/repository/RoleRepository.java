package com.neonyellow.comixxr.repository;

import com.neonyellow.comixxr.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
    Role findByRole(String role);
}
