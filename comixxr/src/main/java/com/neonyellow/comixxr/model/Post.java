package com.neonyellow.comixxr.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Data
@Document(collection = "post")
public class Post {
    @Id
    private ObjectId _id;
    private ObjectId userId;
    private String text = "";
    private ObjectId comicId = null;

    public Post(ObjectId userId){
        this._id = new ObjectId();
        this.userId = userId;
    }
}
