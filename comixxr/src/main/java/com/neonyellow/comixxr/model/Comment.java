package com.neonyellow.comixxr.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "comment")
public class Comment {
    @Id
    private ObjectId _id;
    private ObjectId userId;
    private ObjectId comicId;
    private String text = "";
    private LocalDateTime age;

    public Comment(ObjectId userId, String text, ObjectId comicId){
        this._id = new ObjectId();
        this.userId = userId;
        this.text = text;
        this.comicId = comicId;
        age = LocalDateTime.now();
    }
}
