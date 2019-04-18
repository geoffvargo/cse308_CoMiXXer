package com.neonyellow.comixxr.service.interfaces;

import com.neonyellow.comixxr.model.Comic;

import java.util.List;

public interface IComicService {
    List<Comic> findAllComicsByUserId(Object userId);

}