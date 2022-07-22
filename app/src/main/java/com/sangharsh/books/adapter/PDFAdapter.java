package com.sangharsh.books.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.sangharsh.books.R;
import com.sangharsh.books.model.PDFModel;

import java.io.File;
import java.util.ArrayList;

public class PDFAdapter extends RecyclerView.Adapter<PDFAdapter.MyViewHolder> {

    Context context;
    ArrayList<PDFModel> pdfModels;

    public PDFAdapter (Context context, ArrayList<PDFModel> pdfModels){
        this.context = context;
        this.pdfModels = pdfModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pdf_item,parent);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.pdfName.setText(pdfModels.get(position).getName());
        holder.basicViewTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mFilePath;
                if(pdfModels.get(position).isOfflineAvailable()) {
                    mFilePath = pdfModels.get(position).getOfflinePath();
                }else{
                    mFilePath = "";
                }
                File file=new File(mFilePath);
                Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);
            }
        });
        holder.advancedViewTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Not ready yet", Toast.LENGTH_SHORT).show();
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
