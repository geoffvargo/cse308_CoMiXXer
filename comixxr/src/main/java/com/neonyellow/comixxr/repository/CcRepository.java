package com.neonyellow.comixxr.repository;

import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.model.ComicCollection;
import com.neonyellow.comixxr.model.Genre;
import com.neonyellow.comixxr.model.Privacy;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CcRepository extends MongoRepository<ComicCollection, ObjectId> {
    ComicCollection findBy_id(ObjectId _id);
    void deleteBy_id(ObjectId id);

    List<ComicCollection> findByUserId(ObjectId userId);

    ComicCollection findFirstByComicsContainingAndSeries(ObjectId id, boolean truth);

    List<ComicCollection> findAllByPrivacyOrderByAggregateVotesDesc(Privacy privacy);

    List<ComicCollection> findAllByGenreAndPrivacy(Genre genre, Privacy privacy);

    List<ComicCollection> findAllByComicsContaining(ObjectId id);
}
