package com.github.overpass.gather.model.repo.message;

import java.util.Date;

public class Message extends BaseMessage {

    private String id;

    public Message() {
        super();
    }

    public Message(String id,
                   String text,
                   Date date,
                   String authorId,
                   String authorName,
                   String authorPhotoUrl) {
        super(text, date, authorId, authorName, authorPhotoUrl);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
