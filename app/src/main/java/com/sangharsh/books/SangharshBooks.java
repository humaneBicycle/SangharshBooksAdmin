package com.sangharsh.books;

import android.app.Application;

import java.util.ArrayList;

public class SangharshBooks extends Application {
    String path;//by names of files
    ArrayList<String> stackOfDocumentIds ;

    public void addStack(String s){
        if(stackOfDocumentIds==null){
            stackOfDocumentIds = new ArrayList<>();

        }
        stackOfDocumentIds.add(s);
    }

    public void removeRecentStack(){
        if(stackOfDocumentIds!=null){
            stackOfDocumentIds.remove(stackOfDocumentIds.size()-1);
        }
    }

//    public void addToPath(String s){
//        path = path + "\\"+s;
//    }
//
//    public void removeRecentDirectoryFromPath(){
//        for(int i = path.length()-1;i<=0;i--){
//            if("\\".equals(path.charAt(i))){
//                path = path.substring(0,i+1);
//            }
//        }
//    }

}
