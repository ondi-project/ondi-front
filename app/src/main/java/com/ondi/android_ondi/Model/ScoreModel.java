package com.ondi.android_ondi.Model;

public class ScoreModel {
    int id;
    int from_user;
    int to_user;
    int score;
    String comment;

    public ScoreModel(int id, int from_user, int to_user, int score, String comment) {
        this.id = id;
        this.from_user = from_user;
        this.to_user = to_user;
        this.score = score;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrom_user() {
        return from_user;
    }

    public void setFrom_user(int from_user) {
        this.from_user = from_user;
    }

    public int getTo_user() {
        return to_user;
    }

    public void setTo_user(int to_user) {
        this.to_user = to_user;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
