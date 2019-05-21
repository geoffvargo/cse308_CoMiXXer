package com.neonyellow.comixxr.model;

import java.util.ArrayList;
import java.util.List;

public enum Genre {
    HORROR("HORROR"),
    TEEN("TEEN"),
    FANTASY("FANTASY"),
    CRIME("CRIME"),
    COMEDY("COMEDY"),
    ROMANCE("ROMANCE"),
    MANGA("MANGA"),
    ALTERNATIVE("ALTERNATIVE"),
    GAG("GAG"),
    SCIFI("SCIFI"),
    SUPERHERO("SUPERHERO"),
    CHILD("CHILD"),
    WAR("WAR"),
    DAILY("DAILY"),
    WESTERN("WESTERN"),
    ABSTRACT("ABSTRACT"),
    ADVENTURE("ADVENTURE"),
    NA("NA");

    private String genreCode;

    Genre(String genre) {
        this.genreCode = genre;
    }

    @Override
    public String toString() {
        return genreCode;
    }

    public static List<String> getGenreList(){
        List<String> genres = new ArrayList<>();
        genres.add("HORROR");
        genres.add("TEEN");
        genres.add("FANTASY");
        genres.add("CRIME");
        genres.add("COMEDY");
        genres.add("ROMANCE");
        genres.add("MANGA");
        genres.add("ALTERNATIVE");
        genres.add("GAG");
        genres.add("SCIFI");
        genres.add("SUPERHERO");
        genres.add("CHILD");
        genres.add("WAR");
        genres.add("DAILY");
        genres.add("WESTERN");
        genres.add("ABSTRACT");
        genres.add("ADVENTURE");

        return genres;
    }
}
