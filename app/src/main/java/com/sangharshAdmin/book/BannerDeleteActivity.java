package com.sangharshAdmin.book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sangharshAdmin.book.adapter.BannerAdapter;
import com.sangharshAdmin.book.model.Banner;
import com.sangharshAdmin.book.model.HomeDocument;

public class BannerDeleteActivity extends AppCompatActivity implements BannerAdapter.Listener {

    HomeDocument document;
    TextView textView;
    RecyclerView recyclerView;
    BannerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_delete);

        recyclerView = findViewById(R.id.recyclerView);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        textView = findViewById(R.id.textView);
        textView.setText("Please Wait");

        FirebaseFirestore.getInstance()
                .collection("app")
                .document("Home")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        textView.setText("Select Any One to Delete");
                        if (task.isSuccessful()){
                            document = task.getResult().toObject(HomeDocument.class);
                            adapter = new BannerAdapter(document.getBanners(), BannerDeleteActivity.this);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(manager);
                        } else {
                            textView.setText("Some error occured" + task.getException());
                        }
                    }
                });
    }


    @Override
    public void delete(final Banner banner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Banner")
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        permaDelete(banner);
                    }
                })
                .setMessage("This action cannot be UnDone")
                .setCancelable(true)
                .show();
    }

    private void permaDelete(final Banner banner) {
        document.getBanners().remove(banner);
        textView.setText("Please Wait");
        FirebaseFirestore.getInstance()
                .collection("app")
                .document("Home")
                .set(document)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            textView.setText("Successful");
                            adapter.notifyDataSetChanged();
                        } else {
                            textView.setText("Failed" + task.getException());
                            document.getBanners().add(banner);
                        }
                    }
                });
    }
}