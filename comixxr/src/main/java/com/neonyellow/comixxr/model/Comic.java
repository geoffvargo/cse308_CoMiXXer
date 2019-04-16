package com.neonyellow.comixxr.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "comic")
public class Comic {
    @Id
    private ObjectId _id;
    private ObjectId userId;
    private String raw_data;

    public Comic(ObjectId userId){
        this._id = new ObjectId();
        this.userId = userId;
    }
}
