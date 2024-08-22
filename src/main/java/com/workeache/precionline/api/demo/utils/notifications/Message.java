package com.workeache.precionline.api.demo.utils.notifications;

public class Message {
    private String message;
    private String title;
    private int priority;

    public Message(String title, String message, int priority) {
        this.message = message;
        this.priority = priority;
        this.title = title;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
