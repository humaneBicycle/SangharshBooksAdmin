package com.sangharshAdmin.book.model;

import androidx.annotation.NonNull;

import com.sangharshAdmin.book.EditTestActivity;

public class ShortTest {
    String id;
    String title;
    int numQuestion;
    String time;
    String imageUrl;
    String description;

    public ShortTest() {
    }

    boolean isPaid;
    int price;
    public boolean isPaid(){
        return isPaid;
    }
    public int getPrice() {
        return price;
    }
    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumQuestion() {
        return numQuestion;
    }

    public void setNumQuestion(int numQuestion) {
        this.numQuestion = numQuestion;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
