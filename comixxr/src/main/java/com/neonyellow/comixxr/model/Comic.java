package com.neonyellow.comixxr.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "comic")
public class Comic {
    @Id
    private ObjectId _id;
    private ObjectId userId;
    private String raw_data;
    private String author;
    private String title;
    private String synopsis;
    private Genre genre = Genre.NA;
    private ObjectId parent;
    private List<ObjectId> remixes;
    private boolean isRemix;
    private int upVote;
    private int downVote;
    private LocalDateTime age;
    private boolean isPublished;
    private Privacy privacy = Privacy.PRIVATE;
    private boolean isInSeries;

    public Comic(ObjectId userId){
        this._id = new ObjectId();
        this.userId = userId;
        this.age = LocalDateTime.now();
        isInSeries = false;
    }

    /**
     *
     * @return local URL path to post
     */
    public String getThumbnail(){
        return "";
    }

    public int getTotalVotes(){
        return upVote-downVote;
    }

    public int getTotalComments(){
        return 100;
    }

    public int getTotalRemixes(){
        return remixes.size();
    }
}
