package com.neonyellow.comixxr.service.interfaces;

import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.model.User;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public interface UserService {
    List<Comic> getAllComics();
    Comic findComicBy_id(ObjectId id);
    ArrayList<Comic> getPublishedComics(User user);
    ArrayList<Comic> getDrafts(User user);
    int getNumRemixes(User user);
}