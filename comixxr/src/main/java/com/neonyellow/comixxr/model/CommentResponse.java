package com.neonyellow.comixxr.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
@Data
public class CommentResponse implements java.io.Serializable{
    private String userId;
    private String userName;
    private String pic;
    private String text = "";
    private String age;

    public CommentResponse(ObjectId userId, String userName, String pic, String text, LocalDateTime time){
        this.userId = userId.toHexString();
        this.userName = userName;
        this.pic = pic;
        this.text = text;
        this.age = timeToString(time);
    }

    public String timeToString(LocalDateTime age){
        String s = "";
        LocalDateTime now = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        long dateTime = age.atZone(zoneId).toEpochSecond();
        long nowTime = now.atZone(zoneId).toEpochSecond();
        long secondsAgo = nowTime - dateTime;
        long minutes = secondsAgo/60;
        long hours = minutes/60;
        long days = hours/24;
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

}
