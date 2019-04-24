package com.neonyellow.comixxr.model;

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
}
