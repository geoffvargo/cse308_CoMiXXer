package com.neonyellow.comixxr.service;


import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.model.Privacy;
import com.neonyellow.comixxr.model.User;
import com.neonyellow.comixxr.repository.ComicRepository;
import com.neonyellow.comixxr.repository.UserRepository;
import com.neonyellow.comixxr.service.interfaces.IUserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
                Comic c = this.findComicBy_id(id);
                if (!c.isPublished()) {
                    ans.add(c);
                }
            }
        }

        return ans;
    }

    public ArrayList<Comic> getPublishedComics(User user, boolean isPrivate) {
        ArrayList<Comic> ans = new ArrayList<>();

        if (!user.getComics().isEmpty()) {
            for (ObjectId id : user.getComics()) {
                Comic c = this.findComicBy_id(id);
                if (c.isPublished()) {
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

        return ans;
    }

    public int getNumRemixes(User user){
        int size = 0;
        for(ObjectId id : user.getComics()){
            if(this.findComicBy_id(id).isRemix())
                size++;
        }
        return size;
    }
}
