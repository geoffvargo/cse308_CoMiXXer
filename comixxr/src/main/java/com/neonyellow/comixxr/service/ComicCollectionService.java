package com.neonyellow.comixxr.service;

import com.neonyellow.comixxr.model.ComicCollection;
import com.neonyellow.comixxr.repository.CcRepository;
import com.neonyellow.comixxr.service.interfaces.IComicCollectionService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComicCollectionService implements IComicCollectionService {

    @Autowired
    private CcRepository ccRepository;

    public ComicCollection findBy_id(ObjectId id) {
        return ccRepository.findBy_id(id);
    }

    @Override
    public void save(ComicCollection cc) {
        ccRepository.save(cc);
    }

    @Override
    public void delete(ObjectId id) {
        ccRepository.deleteBy_id(id);
    }

    public List<ComicCollection> getComicsByIds(List<ObjectId> ids){
        List<ComicCollection> comicList = new ArrayList();
            for(ObjectId id : ids){
                comicList.add(ccRepository.findBy_id(id));
            }
        return comicList;
    }

    public ComicCollection getComicCollectionById(ObjectId id){
        return ccRepository.findBy_id(id);
    }

    public ComicCollection getComicCollectionByUserId(ObjectId userId) {
        return ccRepository.findByUserId(userId);
    }
}
