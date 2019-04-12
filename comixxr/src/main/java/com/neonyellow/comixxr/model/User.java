package com.neonyellow.comixxr.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Document(collection = "user")
public class User {

    @Id
    private ObjectId _id;
    @Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
    private String email;
    private String password;
    private String fullname;
    private boolean enabled;
    @DBRef
    private Set<Role> roles;
//    private List<User> subscribers;
//    private List<User> subscriptions;
    private String bio;

    public User(){
        this._id = new ObjectId();
//        this.subscribers = new ArrayList<>();
//        this.subscriptions = new ArrayList<>();
    }
}
