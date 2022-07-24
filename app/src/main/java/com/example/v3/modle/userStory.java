package com.example.v3.modle;

public class userStory {
    private String image;
    private long storyAt;

    public userStory(String image, long storyAt) {
        this.image = image;
        this.storyAt = storyAt;
    }

    public userStory() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getStoryAt() {
        return storyAt;
    }

    public void setStoryAt(long storyAt) {
        this.storyAt = storyAt;
    }
}
