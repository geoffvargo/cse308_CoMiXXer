package com.neonyellow.comixxer.repository;

import com.neonyellow.comixxer.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
    Role findByRole(String role);
}
