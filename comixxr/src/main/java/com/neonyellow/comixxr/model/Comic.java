package com.neonyellow.comixxr.model;

import lombok.Data;
import org.apache.tomcat.jni.Local;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "comic")
public class Comic implements Comparable<Comic>{
    @Id
    private ObjectId _id;
    private ObjectId userId;
    private String raw_data;
    private List<String> image_data;
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
    private String comicParentProfPic;
    private String thumbNail;

    public Comic(ObjectId userId, String thumbNail){
        this._id = new ObjectId();
        this.userId = userId;
        this.age = LocalDateTime.now();
        isInSeries = false;
        this.upVote = new ArrayList();
        this.downVote = new ArrayList();
        this.image_data = new ArrayList<>();
        if(thumbNail != null){
            this.thumbNail = thumbNail;
        }
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

    public String timeToString(){
        String s = "";
        LocalDateTime now = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        long dateTime = age.atZone(zoneId).toEpochSecond();
        long nowTime = now.atZone(zoneId).toEpochSecond();
        long secondsAgo = nowTime - dateTime;
        long minutes = secondsAgo/60;
        long hours = minutes/60;
        long days = hours/60;
        if(days > 0){
            s+=days + " days ago";
        }
        else if(hours > 0){
            s+=hours + " hours ago";
        }
        else if(minutes > 0){
            s+=minutes + " minutes ago";
        }
        else{
            s+="Less than a minute ago";
        }
        return s;
    }

    public int compareTo(Comic c){
        return c.age.compareTo(age);
    }
}
