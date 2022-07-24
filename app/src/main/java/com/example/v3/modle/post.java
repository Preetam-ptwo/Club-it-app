package com.example.v3.modle;

import android.widget.ProgressBar;

public class post {
    protected String postImg;
    protected String postId;
    protected String postedBy;
    protected String postDesc;
    protected long postdate;
    protected int postlike;
    protected int postcomment;

    public post(String postImg, String postId, String postedBy, String postDesc) {
        this.postImg = postImg;
        this.postId = postId;
        this.postedBy = postedBy;
        this.postDesc = postDesc;
    }

    public post(long postdate) {
        this.postdate = postdate;
    }

    public post() {
    }

    public String getPostImg() {
        return postImg;
    }

    public void setPostImg(String postImg) {
        this.postImg = postImg;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

    public long getPostdate() {
        return postdate;
    }

    public void setPostdate(long postdate) {
        this.postdate = postdate;
    }

    public int getPostlike() {
        return postlike;
    }

    public void setPostlike(int postlike) {
        this.postlike = postlike;
    }

    public int getPostcomment() {
        return postcomment;
    }

    public void setPostcomment(int postcomment) {
        this.postcomment = postcomment;
    }
}
