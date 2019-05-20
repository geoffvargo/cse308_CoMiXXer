package com.neonyellow.comixxr.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document (collection = "comic_collection")
public class ComicCollection implements Comparable<ComicCollection>{
    @Id
    private ObjectId _id;
    private ObjectId userId;
    private String title = "";
    private Privacy privacy = Privacy.PRIVATE;
    private String synopsis = "";
    private boolean series = false;
    private List<ObjectId> comics;
    private List<String> hexComics;
    private String hexId;
    private String thumbnail;
    private Genre genre;
    private List<ObjectId> upVote;
    private List<ObjectId> downVote;
    private int aggregateVotes;
    public ComicCollection(ObjectId userId){
        this._id = new ObjectId();
        this.hexId = _id.toHexString();
        this.userId = userId;
        this.comics = new ArrayList<>();
        this.hexComics = new ArrayList<>();
        upVote = new ArrayList<>();
        downVote = new ArrayList<>();
        aggregateVotes = 0;
    }

    public void addToCollection(ObjectId comic) {

        this.comics.add(comic);
        this.hexComics.add(comic.toHexString());
    }

    public void removeFromCollection(ObjectId comic){
        this.comics.remove(comic);
        this.hexComics.remove(comic.toHexString());
    }

    public void toggleComic(ObjectId comic){
        if(comics.contains(comic)){
            comics.remove(comic);
            hexComics.remove(comic.toHexString());
        }
        else{
            comics.add(comic);
            hexComics.add(comic.toHexString());
        }
    }

    public void addUpvote(ObjectId id){
        upVote.add(id);aggregateVotes++;
    }

    public void removeUpvote(ObjectId id){
        upVote.remove(id);aggregateVotes--;
    }

    public void addDownvote(ObjectId id){
        downVote.add(id);aggregateVotes--;
    }

    public void removeDownvote(ObjectId id){
        downVote.remove(id);aggregateVotes++;
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

    @Override
    public int compareTo(ComicCollection o) {
        if (this._id == o.get_id()) {
            return 0;
        } else {
            return -1;
        }
    }

}
