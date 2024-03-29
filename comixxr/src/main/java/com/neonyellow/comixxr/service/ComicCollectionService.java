package com.neonyellow.comixxr.service;

import com.neonyellow.comixxr.model.*;
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

    @Autowired
    private ComicService comicService;

    @Autowired
    private UserService userService;

    public ComicCollection findBy_id(ObjectId id) {
        return ccRepository.findBy_id(id);
    }

    @Override
    public void save(ComicCollection cc) {
        ccRepository.save(cc);
    }

    @Override
    public void delete(ComicCollection cc, User user) {
        for (ObjectId comicId: cc.getComics()) {
            Comic c = comicService.findBy_id(comicId);
            c.setInSeries(false);
            c.setParentSeriesId(null);
            comicService.save(c);
        }
        user.getCurations().remove(cc.get_id());
        userService.save(user);
        ccRepository.deleteBy_id(cc.get_id());
    }

    public List<ComicCollection> getComicsByIds(List<ObjectId> ids){
        List<ComicCollection> comicList = new ArrayList();
            for(ObjectId id : ids){
                comicList.add(ccRepository.findBy_id(id));
            }
        return comicList;
    }

    public List<ComicCollection> getTopFiftySeriesRecent(){
        return ccRepository.findAllByPrivacyAndSeriesOrderByAggregateVotesDesc(Privacy.PUBLIC,true);
    }

    public List<ComicCollection> getSeries(ObjectId id){
        List<ComicCollection> cc = ccRepository.findByUserId(id);
        cc.removeIf(n -> !n.isSeries());
        return cc;
    }

    public ComicCollection findCurrentSeries(ObjectId id){
        return ccRepository.findFirstByComicsContainingAndSeries(id,true);
    }
    public ComicCollection getComicCollectionById(ObjectId id){
        return ccRepository.findBy_id(id);
    }

    public List<ComicCollection> getComicCollectionByUserId(ObjectId userId) {
        return ccRepository.findByUserId(userId);
    }

    public List<ComicCollection> findAllByGenre(Genre genre) {
        return ccRepository.findAllByGenreAndPrivacy(genre, Privacy.PUBLIC);
    }

    public List<ComicCollection> findAllByComicsContaining(ObjectId id){
        return ccRepository.findAllByComicsContaining(id);
    }
}
