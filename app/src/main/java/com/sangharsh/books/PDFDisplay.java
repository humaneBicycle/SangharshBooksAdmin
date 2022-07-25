package com.sangharsh.books;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.pdfview.PDFView;

import java.io.File;

public class PDFDisplay extends AppCompatActivity {
    //PDFView pdfView;
    SangharshBooks sangharshBooks;
    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfdisplay);
        sangharshBooks = (SangharshBooks)getApplication();
        //pdfView = findViewById(R.id.pdfView);
        pdfView =  findViewById(R.id.pdf_viewer);

        //String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/abhay.pdf";
        String dirPath = getFilesDir().getAbsolutePath()+"/"+sangharshBooks.getActivePdfModel().getPointingDir()+".pdf";
        File file = new File(dirPath);
        Log.d("sba pdf display", "onCreate: pdf -> "+dirPath);
        //pdfView.fromFile(file).load();
        pdfView.fromFile(file).show();
        //Log.d(TAG, "onCreate: "+String.valueOf(pdfView.getPageCount()));
        //pdfView.getPageCount();

    }

    @Override
    public void onBackPressed() {
        sangharshBooks.clearRecentPDFModel();
        super.onBackPressed();
    }
}