package com.example.myapplication;

public class TopScore implements Comparable<TopScore> {
    private String playerName;
    private final String level;
    private int livesLeft;
    private String word;
    private String time ;

    @Override
    public String toString() {
        return "TopScore{" +
                "playerName='" + playerName + '\'' +
                ", level='" + level + '\'' +
                ", livesLeft=" + livesLeft +
                ", word='" + word + '\'' +
                ", timeRemaining='" + time + '\'' +
                '}';
    }

    public TopScore(String playerName, String level, int livesLeft, String word,String time) {
        this.playerName = playerName;
        this.level = level;
        this.livesLeft = livesLeft;
        this.word = word;
        this.time = time ;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getLevel() {
        return level;
    }

    public int getLivesLeft() {
        return livesLeft;
    }

    public String getWord() {
        return word;
    }

    public String getTime() {
        return time;
    }


    @Override
    public int compareTo(TopScore another) {
        return Integer.compare(this.livesLeft, another.livesLeft);
    }

    public void setPlayerName(String playerName) {
        this.playerName=playerName ;
    }

    public void setLivesLeft(int livesLeft) {
        this.livesLeft=livesLeft ;
    }

    public void setWord(String word) {
        this.word=word ;
    }

    public void setTime(String time) {
        this.time=time ;
    }
}
