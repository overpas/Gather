package by.overpass.gather.data.repo.message;

import androidx.annotation.Keep;

import java.util.Date;

@Keep
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
