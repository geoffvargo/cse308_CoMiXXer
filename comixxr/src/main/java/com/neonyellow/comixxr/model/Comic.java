package com.neonyellow.comixxr.model;

import lombok.Data;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "comic")
public class Comic {
    @Id
    private ObjectId _id;
    private ObjectId userId;
    private String raw_data;
    private byte[] pdf_data;
    private String author;
    private String title;
    private String synopsis;
    private Genre genre = Genre.NA;
    private ObjectId parent;
    private List<ObjectId> remixes;
    private boolean isRemix;
    private List<ObjectId> upVote;
    private List<ObjectId> downVote;
    private LocalDateTime age;
    private boolean isPublished;
    private Privacy privacy = Privacy.PRIVATE;
    private boolean isInSeries;

    public Comic(ObjectId userId){
        this._id = new ObjectId();
        this.userId = userId;
        this.age = LocalDateTime.now();
        isInSeries = false;
        this.upVote = new ArrayList();
        this.downVote = new ArrayList();
    }

    /**
     *
     * @return local URL path to post
     */
    public String getThumbnail(){
        return "";
    }

    public int getTotalVotes(){
        return upVote.size()-downVote.size();
    }

    public int getTotalComments(){
        return 100;
    }

    public int getTotalRemixes(){
        return remixes.size();
    }

    public boolean containsUpvote(ObjectId id){
        for(ObjectId id2 : upVote){
            if(id2.equals(id))
                return true;
        }
        return false;
    }

    public boolean containsDownvote(ObjectId id){
        for(ObjectId id2 : downVote){
            if(id2.equals(id))
                return true;
        }
        return false;
    }

    public void addUpvote(ObjectId id){
        upVote.add(id);
    }

    public void removeUpvote(ObjectId id){
        upVote.remove(id);
    }

    public void addDownvote(ObjectId id){
        downVote.add(id);
    }

    public void removeDownvote(ObjectId id){
        downVote.remove(id);
    }
}
