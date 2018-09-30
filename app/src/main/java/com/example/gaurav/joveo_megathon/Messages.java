package com.example.gaurav.joveo_megathon;

public class Messages {


    private String message,from;

    public Messages()
    {

    }

    public Messages(String message, String type, String from, long time) {
        this.message = message;
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {

        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}


