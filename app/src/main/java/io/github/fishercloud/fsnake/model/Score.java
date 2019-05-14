package io.github.fishercloud.fsnake.model;

import java.util.Date;

public class Score {
    public Integer score = 0;
    public String date;

    public Score() {
    }

    public Score(Integer score, String date) {
        this.score = score;
        this.date = date;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Score{" +
                "score=" + score +
                ", date='" + date + '\'' +
                '}';
    }
}
