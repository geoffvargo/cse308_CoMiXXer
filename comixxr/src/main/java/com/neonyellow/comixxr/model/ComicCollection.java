package com.neonyellow.comixxr.model;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Document (collection = "comic_collection")
public class ComicCollection {
    @Id
    private ObjectId _id;
    private ObjectId userId;
    private String title;
    @Builder.Default
    private Privacy privacy = Privacy.PRIVATE;
    @Builder.Default
    private String synopsis = "";
    private boolean isSeries;
    @Builder.Default
    private List<ObjectId> comics = new ArrayList<>();

//    public ComicCollection(ObjectId userId){
//        this._id = new ObjectId();
//        this.userId = userId;
//    }

//    public ComicCollection(ObjectId userId, String title) {
//        this.userId = userId;
//        this.title = title;
//        this.isSeries = false;
//    }
}


