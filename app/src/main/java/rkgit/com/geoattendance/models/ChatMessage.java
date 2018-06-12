package rkgit.com.geoattendance.models;

/**
 * Created by Ayush Kulshrestha
 * on 23-03-2018.
 */

public class ChatMessage {

    public ChatMessage(String messageText, String messageUser, String messageTime, String messageDate) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageTime = messageTime;
        this.messageDate = messageDate;
    }

    public String messageText;
    public String messageUser;
    public String messageTime;
    public String messageDate;

}
