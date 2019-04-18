package com.neonyellow.comixxr.service;


import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.model.User;
import com.neonyellow.comixxr.repository.ComicRepository;
import com.neonyellow.comixxr.repository.UserRepository;
import com.neonyellow.comixxr.service.interfaces.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ComicRepository comicRepository;

    public List<Comic> getAllComics(){
        return null;
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

    public ArrayList<Comic> getPublishedComics(User user) {
        ArrayList<Comic> ans = new ArrayList<>();

        if (!user.getComics().isEmpty()) {
            for (ObjectId id : user.getComics()) {
                Comic c = this.findComicBy_id(id);
                if (c.isPublished()) {
                    ans.add(c);
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
