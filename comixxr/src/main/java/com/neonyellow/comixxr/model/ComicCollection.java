package com.neonyellow.comixxr.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document (collection = "comic_collection")
public class ComicCollection {
    @Id
    private ObjectId _id;
    private ObjectId userId;
    private String title = "";
    private Privacy privacy = Privacy.PRIVATE;
    private String synopsis = "";
    private boolean isSeries = false;
    private List<ObjectId> comics;

    public ComicCollection(ObjectId userId){
        this._id = new ObjectId();
        this.userId = userId;
        this.comics = new ArrayList<>();
    }
}
