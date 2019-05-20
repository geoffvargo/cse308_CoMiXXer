package com.neonyellow.comixxr.service;

import com.neonyellow.comixxr.model.Comic;
import com.neonyellow.comixxr.model.Genre;
import com.neonyellow.comixxr.model.Privacy;
import com.neonyellow.comixxr.model.User;
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
import java.util.Collections;
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

    public List<Comic> findMostRecentFifty(){
        return comicRepository.findAllByAgeBeforeAndPrivacyOrderByAgeDesc(LocalDateTime.now(),Privacy.PUBLIC);
    }

    public void save(Comic comic) {
        comicRepository.save(comic);
    }

    public void delete(ObjectId id) {comicRepository.deleteBy_id(id);}

    public List<Comic> findTopFiftyComicsFromLastWeek() {
        List<Comic> temp;

        LocalDateTime present = LocalDateTime.now();
        LocalDateTime lastWeek = present.minusDays(7);

        temp = comicRepository.findAllByAgeAfterAndPrivacy(lastWeek,Privacy.PUBLIC  );

        temp.sort((o1, o2) -> Integer.compare(o2.getTotalVotes(), o1.getTotalVotes()));

        List<Comic> ans;

        if (temp.size() > 50) {
            ans = temp.subList(0, 49);
        } else {
            ans = temp;
        }

        ans.forEach(c -> System.out.println(c.getTotalVotes() + ", " + c.getAge()));

        return ans;
    }

    public List<Comic> findAllSortedByGenreDESC() {
        return comicRepository.findAll(new Sort(Sort.Direction.DESC, "genre"));
    }

    public List<Comic> findAllSortedByGenreASC() {
        return comicRepository.findAll(new Sort(Sort.Direction.ASC, "genre"));
    }

    public List<Comic> findAllRemixes(ObjectId id){
        return comicRepository.findAllByParent(id);
    }

    public List<Comic> findAllRemixesByUser(ObjectId id){
        List<Comic> c = comicRepository.findAllByUserId(id);
        c.removeIf(x->!x.isRemix());
        return c;
    }
    public List<Comic> findAllByGenre(Genre genre) {
        return comicRepository.findAllByGenre(genre);
    }

    public List<Comic> findAll() {
        return comicRepository.findAll();
    }
    public Privacy getPrivacy(String privacy){
        if(privacy == null)
            return Privacy.PRIVATE;
        switch(privacy.toUpperCase()){
            case "PRIVATE":
                return Privacy.PRIVATE;
            case "PUBLIC":
                return Privacy.PUBLIC;
            default:
                return Privacy.UNLISTED;
        }
    }

    public Genre getGenre(String genre){
        if(genre == null)
            return Genre.NA;
        switch(genre.toUpperCase()){
            case "HORROR":
                return Genre.HORROR;
            case "TEEN":
                return Genre.TEEN;
            case "FANTASY":
                return Genre.FANTASY;
            case "CRIME":
                return Genre.CRIME;
            case "ROMANCE":
                return Genre.ROMANCE;
            case "MANGA":
                return Genre.MANGA;
            case "ALTERNATIVE":
                return Genre.ALTERNATIVE;
            case "GAG":
                return Genre.GAG;
            case "SCIFI":
                return Genre.SCIFI;
            case "SUPERHERO":
                return Genre.SUPERHERO;
            case "CHILD":
                return Genre.CHILD;
            case "WAR":
                return Genre.WAR;
            case "DAILY":
                return Genre.DAILY;
            case "WESTERN":
                return Genre.WESTERN;
            case "ABSTRACT":
                return Genre.ABSTRACT;
            default:
                return Genre.NA;
        }
    }

    public List<Comic> getAllChildren(ObjectId comicId) {
        List<Comic> ans = new ArrayList<>();

        Comic current = comicRepository.findBy_id(comicId);

        List<Comic> currLevel = new ArrayList<>();
        current.getRemixes().forEach(r -> currLevel.add(comicRepository.findBy_id(r)));

        try {
            getAllChildrenHELPER(current, currLevel, ans);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ans;
    }

    private void getAllChildrenHELPER(Comic comic, List<Comic> clist, List<Comic> anslist) {
        if (clist.size() == 0) {
            return;
        } else {
            Comic temp = clist.remove(0);
            ObjectId parent = temp.getParent();
            ObjectId id = comic.get_id();
            if (parent.equals(id) && !anslist.contains(temp)) {
                anslist.add(temp);
            }
            if (temp.getRemixes().size() > 0) {
                List<Comic> tempList = new ArrayList<>();
                temp.getRemixes().forEach(r -> tempList.add(comicRepository.findBy_id(r)));
                getAllChildrenHELPER(temp, tempList, anslist);
            }
            getAllChildrenHELPER(comic, clist, anslist);
        }
    }

    public List<Comic> getComicsByIds(List<ObjectId> ids){
        List<Comic> comicList = new ArrayList();
        for(ObjectId id : ids){
            comicList.add(comicRepository.findBy_id(id));
        }
        return comicList;
    }
    public List<Comic> searchComicsWithTitle(String title){
        return comicRepository.findAllByTitleContainingAllIgnoreCaseAndPrivacy(title,Privacy.PUBLIC);
    }

    public List<Comic> getRemixesForActivityFeed(User user){
        LocalDateTime present = LocalDateTime.now();
        List<Comic> remixActivity = new ArrayList<>();

        for (ObjectId id : user.getSubscriptions()) {
            List<Comic> comics = comicRepository.findAllByUserIdAndAgeBeforeAndPrivacyOrderByAgeDesc(id, present, Privacy.PUBLIC);
            comics.removeIf(x->!x.isRemix());
            remixActivity.addAll(comics);
        }

        Collections.sort(remixActivity);
        return remixActivity;
    }
//    private void findAllParents(Comic comic, List<Comic> ansList, List<Comic> remaining) {
//        while (remaining.size() > 1) {
//            Comic temp = remaining.remove(0);
//            if (temp.getParent().equals(comic.get_id())) {
//                ansList.add(temp);
//
//                List<Comic> tempList = new ArrayList<>();
//                Collections.copy(tempList, comicRepository.findAll());
//                tempList.removeAll(ansList);
//
//                try {
//                    findAllParents(temp, ansList, tempList);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            try {
//                findAllParents(comic, ansList, remaining);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    public List<Comic> findTopRated() {
//        ArrayList<Comic> temp;
////        for (Genre g : temp = comicRepository.findAllByGenre(g)) {
////        }
//
//        return null;
//    }

}
