package com.example.matt.assignment2;

import java.util.Date;

/**
 * Simple class to represent a note.
 */
public class Note {

    private long id;
    private String title;
    private String body;
    private int category;
    private boolean hasReminder;
    private Date reminder;
    private Date created;

    public Note() {
        id = -1;
    }

    public Note(long id) {
        this.id = id;
    }

    public Note(String title, String body, int category, boolean hasReminder, Date reminder, Date created) {
        this.body = body;
        this.category = category;
        this.hasReminder = hasReminder;
        this.reminder = reminder;
        this.title = title;
        this.created = created;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public boolean isHasReminder() {
        return hasReminder;
    }

    public void setHasReminder(boolean hasReminder) {
        this.hasReminder = hasReminder;
    }

    public Date getReminder() {
        return reminder;
    }

    public void setReminder(Date reminder) {
        this.reminder = reminder;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", category=" + category +
                ", hasReminder=" + hasReminder +
                ", reminder=" + reminder +
                ", created=" + created +
                '}';
    }
}
