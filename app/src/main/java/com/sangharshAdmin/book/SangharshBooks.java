package com.sangharshAdmin.book;

import android.app.Application;

import com.sangharshAdmin.book.model.Directory;
import com.sangharshAdmin.book.model.PDFModel;

import java.util.ArrayList;

public class SangharshBooks extends Application {
    static boolean darkMode;
    static PDFModel pdfModel;

    public static String path="\\home";//by names of files
    static ArrayList<String> stackOfDocumentIds ;
    static ArrayList<Directory> directories;

    public void addStack(String s){
        if(stackOfDocumentIds==null){
            stackOfDocumentIds = new ArrayList<>();

        }
        stackOfDocumentIds.add(s);
    }
    public void resetAddress(){
        path="\\home";
    }
    public String getPath(){
        return path;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        SangharshBooks.darkMode = darkMode;
    }

    public String getLatestDir(){
        String s="";
        for(int i = path.length()-1;i>=0;i--){
            if('\\'==path.charAt(i)){
                s=path.substring(i+1,path.length());
                break;
            }
        }
        return s;
    }

    //    public void removeRecentStack(){
//        if(stackOfDocumentIds!=null){
//            stackOfDocumentIds.remove(stackOfDocumentIds.size()-1);
//        }
//    }
//    public void removeRecentDirectoryFromStack(){
//        if(directories!=null){
//            directories.remove(directories.size()-1);
//        }
//    }

    public void addDirectoryToStack(Directory d){
        if(directories==null){
            directories = new ArrayList<>();

        }
        directories.add(d);
    }
    public Directory getLastActiveDirectory(){
        if(directories!=null && directories.size()>0) {
            return directories.get(directories.size() - 1);
        }else{
            //first created. inflate empty dir

            return null;
        }
    }



    public void addToPath(String s){
        path = path + "\\"+s;
    }

    public void removeRecentDirectoryFromPath(){
        for(int i = path.length()-1;i>=0;i--){
            if('\\'==path.charAt(i)){
                path = path.substring(0,i);
                break;
            }
        }
    }


    public void clearRecentPDFModel(){
        pdfModel =null;
    }
    public PDFModel getActivePdfModel(){
        return pdfModel;
    }

    public void setActivePdfModel(PDFModel pdf){
        pdfModel = pdf;
    }

}
