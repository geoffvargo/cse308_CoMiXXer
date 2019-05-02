package com.neonyellow.comixxr.model;

import com.neonyellow.comixxr.repository.CcRepository;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Data
@Document(collection = "user")
public class User {

    @Id
    private ObjectId _id;
    @Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
    private String email;
    private String password;
    private String fullname;
    private boolean enabled;
    @DBRef
    private Set<Role> roles;
    private List<ObjectId> subscribers;
    private List<ObjectId> subscriptions;
    private List<ObjectId> comics;
    private List<ObjectId> curations;
    private String bio;

    CcRepository ccRepository;

    public User(){
        this._id = new ObjectId();
        this.subscribers = new ArrayList<>();
        this.subscriptions = new ArrayList<>();
        this.curations = new ArrayList<>();
        this.comics = new ArrayList<>();
    }

    public void addToComics(Comic comic) {
        if (comic != null) {
            this.comics.add(comic.get_id());
        }
    }

    public int getNumOfSubscibers() {
        return this.subscribers.size();
    }

    public int getNumOfSubsriptions() {
        return this.subscriptions.size();
    }

    public void addCuration(ComicCollection curation) {
        if (curation != null) {
            this.curations.add(curation.get_id());
        }
    }

    public boolean addToCuration(ObjectId curation, ObjectId comic) {
        boolean ans = false;
        int index = Collections.binarySearch(curations, curation);
        if (index >= 0) {
            ComicCollection currCollection = ccRepository.findBy_id(this.curations.get(index));
            currCollection.addToCollection(comic);
            ans = true;
        }
        return ans;
    }

    public void addToSubscriptions(User user) {
        if (user != null) {
            this.subscriptions.add(user.get_id());
        }
    }

    public void addSubsciber(User user) {
        if (user != null) {
            this.subscribers.add(user.get_id());
        }
    }

    public void removeFromSubscriptions(User user) {
        if (user != null) {
            this.subscriptions.remove(user.get_id());
        }
    }

    public void removeFromSubscribers(User user) {
        if (user != null) {
            this.subscribers.remove(user.get_id());
        }
    }
}
