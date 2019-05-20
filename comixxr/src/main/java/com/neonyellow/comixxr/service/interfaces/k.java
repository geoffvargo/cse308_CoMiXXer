package com.neonyellow.comixxr.service.interfaces;

import com.neonyellow.comixxr.model.Comment;

import org.bson.types.ObjectId;

import java.util.ArrayList;

public interface k {
    Comment findCommentBy_id(ObjectId id);
}
