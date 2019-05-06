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
    private boolean isSeries = false;
    private List<ObjectId> comics;
    private List<String> hexComics;
    private String hexId;

    public ComicCollection(ObjectId userId){
        this._id = new ObjectId();
        this.hexId = _id.toHexString();
        this.userId = userId;
        this.comics = new ArrayList<>();
        this.hexComics = new ArrayList<>();
    }

    public void addToCollection(ObjectId comic) {

        this.comics.add(comic);
        this.hexComics.add(comic.toHexString());
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

    @Override
    public int compareTo(ComicCollection o) {
        if (this._id == o.get_id()) {
            return 0;
        } else {
            return -1;
        }
    }

}
