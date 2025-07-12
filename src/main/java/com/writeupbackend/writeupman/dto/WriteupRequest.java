package com.writeupbackend.writeupman.dto;

public class WriteupRequest {
    public String subject;
    public String title;
    public String type;
    public boolean table;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getTable() {
        return table;
    }

    public void setTable(boolean table) {
        this.table = table;
    }
}
