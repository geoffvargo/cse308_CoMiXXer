package com.neonyellow.comixxr.service;

import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.model.Genre;
import com.neonyellow.comixxr.repository.ComicRepository;
import com.neonyellow.comixxr.service.interfaces.IComicService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ComicService implements IComicService {

    @Autowired
    ComicRepository comicRepository;

    public List<Comic> findAllByUserId(ObjectId userId) {
        return comicRepository.findAllByUserId(userId);
    }

    public Comic findBy_id(ObjectId id) {
        return comicRepository.findBy_id(id);
    }

    public File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public void save(Comic comic) {
        comicRepository.save(comic);
    }

    public void delete(ObjectId id) {comicRepository.deleteBy_id(id);}

    public List<Comic> findTopFiftyComicsFromLastWeek() {
        List<Comic> temp;

        LocalDateTime present = LocalDateTime.now();
        LocalDateTime lastWeek = present.minusDays(7);

        temp = comicRepository.findAllByAgeAfter(lastWeek);

        temp.sort((o1, o2) -> Integer.compare(o2.getTotalVotes(), o1.getTotalVotes()));

        List<Comic> ans;

        if (temp.size() > 50) {
            ans = temp.subList(0, 49);
        } else {
            ans = temp;
        }

        ans.forEach(c -> System.out.println(c.getTotalVotes()));

        return ans;
    }

    public List<Comic> findAllSortedByGenreDESC() {
        return comicRepository.findAll(new Sort(Sort.Direction.DESC, "genre"));
    }

    public List<Comic> findAllSortedByGenreASC() {
        return comicRepository.findAll(new Sort(Sort.Direction.ASC, "genre"));
    }

    public List<Comic> findAllByGenre(Genre genre) {
        return comicRepository.findAllByGenre(genre);
    }

    public List<Comic> findAll() {
        return comicRepository.findAll();
    }

//    public List<Comic> findTopRated() {
//        ArrayList<Comic> temp;
////        for (Genre g : temp = comicRepository.findAllByGenre(g)) {
////        }
//
//        return null;
//    }

}
