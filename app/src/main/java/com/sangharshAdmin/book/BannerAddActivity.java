package com.sangharshAdmin.book;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.sangharshAdmin.book.model.Banner;
import com.sangharshAdmin.book.model.HomeDocument;
import com.sangharshAdmin.book.utils.Tools;

import java.io.File;

public class BannerAddActivity extends AppCompatActivity {

    private static final int PIC_VID_CODE = 101;
    EditText urlEt;
    EditText titleEt;

    HomeDocument homeDocument;
    int index = 0;
    int subCatIndex = 0;


    TextView detailsTxt;
    Button addBtn;
    Button picImgBtn;

    Uri imgUri;
    String imgName;
    int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_add);

        addBtn = findViewById(R.id.addBtn);
        addBannerCode();

        picImgBtn = findViewById(R.id.pickBannerBtn);
        imgCode();

        urlEt = findViewById(R.id.urlET);
        titleEt = findViewById(R.id.titleTxt);
        detailsTxt = findViewById(R.id.detailsTxt);

    }

    private void imgCode() {
        picImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Video"),PIC_VID_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PIC_VID_CODE:
                if (resultCode == RESULT_OK && data != null){
                    imgUri = data.getData();
                    File file = new File(String.valueOf(data.getData()));
                    detailsTxt.setText("Image Picked: " + file.getName()+ "\nFrom Location: " + data.getData().toString());
                    imgName= new Tools().getTimeStamp(file.getName());
                    addBtn.setEnabled(true);
                }
                break;
            default:
                break;
        }
    }


    private void addBannerCode() {
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgUri!=null) {
                    addBtn.setEnabled(false);
                    detailsTxt.setText("PLEASE WAIT");
                    picImgBtn.setEnabled(false);
                    titleEt.setEnabled(false);
                    urlEt.setEnabled(false);
                    FirebaseStorage.getInstance()
                            .getReference()
                            .child("content")
                            .child("banners")
                            .child(imgName + ".png")
                            .putFile(imgUri)
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                    detailsTxt.setText("Uploading Image: " +
                                            String.valueOf(snapshot.getBytesTransferred() * 100 / snapshot.getTotalByteCount())
                                            + "%");
                                }
                            })
                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        detailsTxt.setText("Video Uploaded Successfully! Now Updating details");
                                        task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                uplaodBanner(uri.toString());
                                            }
                                        });
                                    } else {
                                        detailsTxt.setText("Upload Failed: "
                                                + task.getException());
                                        addBtn.setEnabled(true);
                                        picImgBtn.setEnabled(true);
                                        titleEt.setEnabled(true);
                                        urlEt.setEnabled(true);
                                    }
                                }
                            });
                } else {
                    detailsTxt.setText("All Fields are Maindatory");
                }
            }
        });
    }

    private void uplaodBanner(String url) {
        final Banner banner = new Banner();
        banner.setImageUrl(url);
        if (urlEt.getText()!=null && !urlEt.getText().toString().isEmpty()){
            banner.setRedirectUrl(urlEt.getText().toString());
        }

        if (titleEt.getText()!=null && !titleEt.getText().toString().isEmpty()){
            banner.setText(titleEt.getText().toString());
        }

        banner.setId(imgName.substring(0, 3) + (int) Math.random()*1000);

        if (homeDocument != null){
            homeDocument.getBanners().add(banner);
        }

        FirebaseFirestore.getInstance()
                .collection("app")
                .document("Home")
                .update("banners", FieldValue.arrayUnion(banner))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            detailsTxt.setText("Banner Add Success");
                        } else {
                            detailsTxt.setText("Banner Add Failed" + task.getException());
                        }
                        picImgBtn.setEnabled(true);
                        urlEt.setEnabled(true);
                        titleEt.setEnabled(true);
                        imgUri = null;
                        urlEt.setText("");
                        titleEt.setText("");
                    }
                });
    }
}