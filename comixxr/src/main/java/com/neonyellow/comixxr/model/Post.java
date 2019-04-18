package com.neonyellow.comixxr.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "post")
public class Post {
    @Id
    private ObjectId _id;
    private ObjectId userId;
    private ObjectId comicId = null;
    private List<ObjectId> comments;

    public Post(ObjectId userId){
        this._id = new ObjectId();
        this.userId = userId;
        this.comments = new ArrayList<>();
    }
}
