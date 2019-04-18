package com.neonyellow.comixxr.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "comment")
public class Comment {
    @Id
    private ObjectId _id;
    private ObjectId userId;
    private String text = "";

    public Comment(ObjectId userId){
        this._id = new ObjectId();
        this.userId = userId;
    }
}
