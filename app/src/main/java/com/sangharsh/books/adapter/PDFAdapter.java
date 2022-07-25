package com.sangharsh.books.adapter;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.sangharsh.books.BuildConfig;
import com.sangharsh.books.PDFDisplay;
import com.sangharsh.books.R;
import com.sangharsh.books.SangharshBooks;
import com.sangharsh.books.model.PDFModel;

import java.io.File;
import java.util.ArrayList;

public class PDFAdapter extends RecyclerView.Adapter<PDFAdapter.MyViewHolder> {

    Context context;
    ArrayList<PDFModel> pdfModels;
    SangharshBooks sangharshBooks;

    public PDFAdapter (Application application, Context context, ArrayList<PDFModel> pdfModels){
        this.context = context;
        this.pdfModels = pdfModels;
        this.sangharshBooks = (SangharshBooks) application;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pdf_item,new LinearLayout(context),false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.pdfName.setText(pdfModels.get(position).getName());
        holder.basicViewTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dirPath = context.getFilesDir().getAbsolutePath()+"/"+pdfModels.get(position).getPointingDir()+".pdf";
                //String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/"+directory.getPdfModels().get(index).getName()+".pdf";
                File file = new File(dirPath);
                //Log.d("sba adapter", dirPath);
//                        if(!file.exists()){
//                            file.mkdir();
//                        }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",file);
                intent.setDataAndType(uri, "application/pdf");
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);
            }
        });
        holder.advancedViewTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("sba", "basic pdf view onClick at: "+directory.getPdfModels().get(index).getName());
                sangharshBooks.setActivePdfModel(pdfModels.get(position));
                context.startActivity(new Intent(context, PDFDisplay.class));
            }
        });
        
    }

    @Override
    public int getItemCount() {
        return pdfModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView pdfName;
        TextView advancedViewTV;
        TextView basicViewTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            pdfName = itemView.findViewById(R.id.pdf_name_pdf_item);
            advancedViewTV = itemView.findViewById(R.id.pdf_view_advance);
            basicViewTV = itemView.findViewById(R.id.pdf_view_basic);
        }
    }
}
