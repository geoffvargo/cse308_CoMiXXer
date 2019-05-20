package com.neonyellow.comixxr.service.interfaces;

import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.model.User;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public interface IUserService {
    Comic findComicBy_id(ObjectId id);
    ArrayList<Comic> getPublishedComics(User user, boolean isPrivate);
    ArrayList<Comic> getDrafts(User user);
    int getNumRemixes(User user);
    List<User> searchUsersWithName(String name);
}