package com.sangharshAdmin.book.model;

import java.util.ArrayList;

public class Test {
    String testTitle;
    String testDescription;
    String id;
    int timeAllowed;
    int noOfQuestion;
    String testBannerUrl;

    public String getTestBannerUrl() {
        return testBannerUrl;
    }

    public void setTestBannerUrl(String testBannerUrl) {
        this.testBannerUrl = testBannerUrl;


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

    public int getNoOfQuestion() {
        return noOfQuestion;
    }

    public void setNoOfQuestion(int noOfQuestion) {
        this.noOfQuestion = noOfQuestion;
    }


    public int getTimeAllowed() {
        return timeAllowed;
    }

    public void setTimeAllowed(int timeAllowed) {
        this.timeAllowed = timeAllowed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    ArrayList<Question> questions;

    public Test() {
    }

    public Test(String testTitle, String testDescription, ArrayList<Question> questions) {
        this.testTitle = testTitle;
        this.testDescription = testDescription;
        this.questions = questions;
    }

    public String getTestTitle() {
        return testTitle;
    }

    public void setTestTitle(String testTitle) {
        this.testTitle = testTitle;
    }

    public String getTestDescription() {
        return testDescription;
    }

    public void setTestDescription(String testDescription) {
        this.testDescription = testDescription;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

}
