package com.sangharsh.books.model;

import java.util.ArrayList;

public class Directory {
    int depth;//used to fetch first screen list
    ArrayList<FileModel> files;
    ArrayList<PDFModel> pdfModels;

    public Directory(int depth, ArrayList<FileModel> files, ArrayList<PDFModel> pdfModels) {
        this.depth = depth;
        this.files = files;
        this.pdfModels = pdfModels;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public ArrayList<FileModel> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<FileModel> files) {
        this.files = files;
    }

    public ArrayList<PDFModel> getPdfModels() {
        return pdfModels;
    }

    public void setPdfModels(ArrayList<PDFModel> pdfModels) {
        this.pdfModels = pdfModels;
    }
}
