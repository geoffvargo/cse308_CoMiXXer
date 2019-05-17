package com.neonyellow.comixxr.service;


import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.model.ComicCollection;
import com.neonyellow.comixxr.model.Privacy;
import com.neonyellow.comixxr.model.User;
import com.neonyellow.comixxr.repository.ComicRepository;
import com.neonyellow.comixxr.repository.UserRepository;
import com.neonyellow.comixxr.service.interfaces.IUserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ComicRepository comicRepository;

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserById(ObjectId id){
        return userRepository.findBy_id(id);
    }

    public List<User> getUserList(){
        return userRepository.findAllByEnabled(true);
    }

    public List<User> getUserListByIds(List<ObjectId> userIds){
        List<User> users = new ArrayList();
        for(ObjectId id : userIds){
            users.add(userRepository.findBy_id(id));
        }
        return users;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public Comic findComicBy_id(ObjectId id){
      return comicRepository.findBy_id(id);
    }

    public ArrayList<Comic> getDrafts(User user) {
        ArrayList<Comic> ans  = new ArrayList<>();

        if (!user.getComics().isEmpty()) {
            for (ObjectId id : user.getComics()) {
                if (id != null) {
                    Comic c = this.findComicBy_id(id);
                    if (c != null && !c.isPublished()) {
                        ans.add(c);
                    }
                }
            }
        }

        return ans;
    }

    public ArrayList<Comic> getPublishedComics(User user, boolean isPrivate) {
        ArrayList<Comic> ans = new ArrayList<>();

        if (!user.getComics().isEmpty()) {
            for (ObjectId id : user.getComics()) {
                if (id != null) {
                    Comic c = this.findComicBy_id(id);
                    if (c != null && c.isPublished()) {
                        if (c.getPrivacy() != Privacy.PRIVATE) {
                            ans.add(c);
                        } else {
                            if (isPrivate) {
                                ans.add(c);
                            }
                        }
                    }
                }
            }
        }

        return ans;
    }

    public List<Comic> getMostRecentSubscriptionComics(User user){
        List<Comic> comics = new ArrayList();
        for(ObjectId id : user.getSubscriptions()){
            User subUser = userRepository.findBy_id(id);
            for(ObjectId comicId : subUser.getComics()){
                Comic c = comicRepository.findBy_id(comicId);
                if(c.getPrivacy() == Privacy.PUBLIC)
                    comics.add(c);
            }
        }
        Collections.sort(comics);
        return comics;
    }

    public int getNumRemixes(User user){
        int size = 0;
        for(ObjectId id : user.getComics()){
            Comic temp = this.findComicBy_id(id);
            if(temp != null && temp.isRemix())
                size++;
        }
        return size;
    }
}
