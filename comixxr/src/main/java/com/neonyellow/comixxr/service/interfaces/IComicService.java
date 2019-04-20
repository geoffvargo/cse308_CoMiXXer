package com.neonyellow.comixxr.service.interfaces;

import com.neonyellow.comixxr.model.Comic;
import org.bson.types.ObjectId;

import java.util.List;

public interface IComicService {
    List<Comic> findAllByUserId(ObjectId userId);

    Comic findBy_id(ObjectId id);

    void save(Comic comic);

    void deleteBy_id(ObjectId id);

}