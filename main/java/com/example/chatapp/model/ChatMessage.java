package com.example.chatapp.model;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ChatMessage {

    String senderId;
    String text;
    long time;
    boolean isMine;

    public ChatMessage(String senderId, String text, long time) {
        this.senderId = senderId;
        this.text = text;
        this.time = time;

    }

    public ChatMessage() {
        // empty constructor needed for firebase
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isMine() {

        // determining if the messeage is mine or not
        // if senderId == currentUserId ,then the msg is mine
        if(senderId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

            isMine = true;
        }
        else isMine = false;

        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    // converting the time into string format
    public String convertTime() {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        Date date = new Date(getTime());
        sdf.setTimeZone(TimeZone.getDefault());

        return sdf.format(date);
    }
}
