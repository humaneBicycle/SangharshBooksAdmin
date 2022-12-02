package com.sangharshAdmin.book.model;

public class FileModel {
    String name;
    String pointingDirId;

    public FileModel(){}

    public FileModel(String name, String pointingDirId) {
        this.name = name;
        this.pointingDirId = pointingDirId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPointingDirId() {
        return pointingDirId;
    }

    public void setPointingDirId(String pointingDirId) {
        this.pointingDirId = pointingDirId;
    }
}
