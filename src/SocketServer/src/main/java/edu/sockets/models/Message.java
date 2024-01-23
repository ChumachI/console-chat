package edu.sockets.models;

import java.sql.Timestamp;
import java.util.Objects;

public class Message {
    private Long id;
    private String sender;
    private String text;
    private Timestamp localDateTime;
    private Long room;
    public Message() {
    }

    public Message(Long id, String sender, String text, Timestamp localDateTime, Long room) {
        this.id = id;
        this.sender = sender;
        this.text = text;
        this.localDateTime = localDateTime;
        this.room = room;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(Timestamp localDateTime) {
        this.localDateTime = localDateTime;
    }

    public Long getRoom() {
        return room;
    }

    public void setRoom(Long room) {
        this.room = room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id) && Objects.equals(sender, message.sender) && Objects.equals(text, message.text) && Objects.equals(localDateTime, message.localDateTime) && Objects.equals(room, message.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sender, text, localDateTime, room);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", sender='" + sender + '\'' +
                ", text='" + text + '\'' +
                ", localDateTime=" + localDateTime +
                ", room=" + room +
                '}';
    }
}
