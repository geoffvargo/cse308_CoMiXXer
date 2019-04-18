package com.neonyellow.comixxr.model;

import com.neonyellow.comixxr.repository.ComicRepository;
import com.neonyellow.comixxr.service.UserServiceImpl;
import com.neonyellow.comixxr.service.interfaces.UserService;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
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
    private String bio;

    @Autowired
    private UserService userService;

    public User(){
        this._id = new ObjectId();
        this.subscribers = new ArrayList<>();
        this.subscriptions = new ArrayList<>();
        this.comics = new ArrayList<>();
        userService = new UserServiceImpl();
    }

    public ArrayList<Comic> getDrafts() {
        ArrayList<Comic> ans  = new ArrayList<>();

        if (!this.comics.isEmpty()) {
            for (ObjectId id : this.comics) {
                Comic c = userService.findComicBy_id(id);
                if (!c.isPublished()) {
                    ans.add(c);
                }
            }
        }

        return ans;
    }

    public ArrayList<Comic> getPublishedComics() {
        ArrayList<Comic> ans = new ArrayList<>();

        if (!this.comics.isEmpty()) {
            for (ObjectId id : this.comics) {
                Comic c = userService.findComicBy_id(id);
                if (c.isPublished()) {
                    ans.add(c);
                }
            }
        }

        return ans;
    }

//    public ArrayList<Comic> getAllComics() {
//        ArrayList<Comic> ans = new ArrayList<>();
//
//        if (!this.comics.isEmpty()) {
//            ans.addAll(this.comics);
//        }
//        return ans;
//    }

    public void addToComics(Comic comic) {
        if (comic != null) {
            this.comics.add(comic.get_id());
        }
    }

    public int getNumRemixes(){
        int size = 0;
        for(ObjectId id : comics){
            if(userService.findComicBy_id(id).isRemix())
                size++;
        }
        return size;
    }

    public int getNumOfSubscibers() {
        return this.subscribers.size();
    }

    public int getNumOfSubsriptions() {
        return this.subscriptions.size();
    }
}
