package com.neonyellow.comixxr.service;

import com.neonyellow.comixxr.model.ComicCollection;
import com.neonyellow.comixxr.repository.CcRepository;
import com.neonyellow.comixxr.service.interfaces.IComicCollectionService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComicCollectionService implements IComicCollectionService {

    @Autowired
    private CcRepository ccRepository;

    @Override
    public void save(ComicCollection cc) {
        ccRepository.save(cc);
    }

    @Override
    public void delete(ObjectId id) {
        ccRepository.deleteBy_id(id);
    }
}
