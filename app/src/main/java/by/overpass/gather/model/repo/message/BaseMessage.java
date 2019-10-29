package by.overpass.gather.model.repo.message;

import java.util.Date;

public class BaseMessage {

    private String text;
    private Date date;
    private String authorId;
    private String authorName;
    private String authorPhotoUrl;

    public BaseMessage() {
    }

    public BaseMessage(String text,
                   Date date,
                   String authorId,
                   String authorName,
                   String authorPhotoUrl) {
        this.text = text;
        this.date = date;
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorPhotoUrl = authorPhotoUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorPhotoUrl() {
        return authorPhotoUrl;
    }

    public void setAuthorPhotoUrl(String authorPhotoUrl) {
        this.authorPhotoUrl = authorPhotoUrl;
    }
}
