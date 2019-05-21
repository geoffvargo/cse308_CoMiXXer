package com.neonyellow.comixxr.model;

import com.neonyellow.comixxr.repository.CcRepository;

import com.neonyellow.comixxr.service.ComicCollectionService;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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
    private String pic;
    @DBRef
    private Set<Role> roles;
    private List<ObjectId> subscribers;
    private List<ObjectId> subscriptions;
    private List<ObjectId> comics;
    private List<ObjectId> curations;
    private String bio;

    public User(){
        this._id = new ObjectId();
        this.subscribers = new ArrayList<>();
        this.subscriptions = new ArrayList<>();
        this.curations = new ArrayList<>();
        this.comics = new ArrayList<>();
        try {
           this.pic = "data:image/png;base64,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+BpOVc9SaHQymbRt65xbWlp66KGH1tbWPv3pz17eurK0tHTlyhUi8d7fMrnpKGJejHEymTjnHn744Y9+9KOTyWQ0GKrq7u7udDoteyBmLmF9VQea8/T+qO01f5+6rgwaY0cE773C2radTqenz9z5wz/8w+vr65/73OcuX946efJk0zR7w8M3wLIWR6grIhqNRhcuXDCzU6dObW1ttW3b6w289yGEuq7L5jHlaHlvZKvlJMv02tmXLOcczbJzLlS+bH3qfg/Aic2T/+xHPvzUU0+lnK9evbqxcWJra+tYxT+6QQonxgimra2tEydOTLcvw2wwGFBOmjXGnHMjIs45FmIVIpv7oAGg+Q7TzODkNcog2QmxQXMcN01WIx/YORa3tL7+6NKS6w+//vWvnz37wCuvXBqNRkdZDW9Gz2Lbh5nN1YjRaHTl4oWxWXAuxthOpsFLjuq9D8GpqlrmTDOKJTUzEGahus22BITXFjRqjswMCKAhOGIfmTvVuq6nu7vbTXfvvfeCxLkQYxaRlI4XrIO0ZQQQTafTut9HjMWs2u0t1UzFXmDeSc5o22mvX8UYVZOZkVqMcXV1/YEHHmDm55577tLWlZlWNdfLiklOp9OqqlS1RB4xxq7r6romx1cuXRr0R459jF3osypYXKirnG1paSmapWxJMzEXKeL4wOLrYNoj3VxjbFDZCZFRr9+D5clkYspENhj2dnauqqbV1dWN9fUzZ8689a1vfdvZcznnz372s207PXfubcvLyxsbGysrKwWdouetrKyklJ577rmnnnrqL//yxZWVlaWlYUppdXn57P333Xn6zqrqfe6zT14dN8nMiobDZAQzNro1GYwjycq2R1NfiMZEBMNikb586eJwOBThrmv6g3ppafjAA/c/+PZz99z1plOnNqnuIyVL6fLlK4+96x1PPPGEBBERFkFxVyIwgznt7v76r//6s8+eP3Vq873vfc/p06dPnz69sbExGoawtIomdtPmj77wpcvbY3Ihq5J4MJniFh43AxbdICthZphLEbUPUGPD5ubm7u5u2zYPP3LuB37g7y0tDYejAQkDGTnH8ZaTQHW9urrSTpsqOHIMImi2FPNcSCaiy5cuPfaOR594//sGm5uIMY7H3ntUlU620DWxbUPVW11d/cYLL1VV1cYk3pX9o86e30zIP4qJ8U2zFR0mSBSSLlFV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";
        }
        catch(Exception e){
            e.printStackTrace();
            this.pic = "";
        }
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

    public boolean addCuration(ComicCollection curation) {
        boolean ans = false;
        if (curation != null) {
            this.curations.add(curation.get_id());
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
