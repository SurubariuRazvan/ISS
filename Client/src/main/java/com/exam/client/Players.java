package com.exam.client;

public class Players {
    private Integer p1Score;
    private Integer p2Score;
    private Integer p3Score;

    public Players(Integer p1Score, Integer p2Score, Integer p3Score) {
        this.p1Score = p1Score;
        this.p2Score = p2Score;
        this.p3Score = p3Score;
    }

    public Integer getP1Score() {
        return p1Score;
    }

    public void setP1Score(Integer p1Score) {
        this.p1Score = p1Score;
    }

    public Integer getP2Score() {
        return p2Score;
    }

    public void setP2Score(Integer p2Score) {
        this.p2Score = p2Score;
    }

    public Integer getP3Score() {
        return p3Score;
    }

    public void setP3Score(Integer p3Score) {
        this.p3Score = p3Score;
    }
}
