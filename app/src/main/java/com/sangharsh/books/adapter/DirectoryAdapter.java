package com.sangharsh.books.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sangharsh.books.FileActivity;
import com.sangharsh.books.R;
import com.sangharsh.books.SangharshBooks;
import com.sangharsh.books.model.Directory;

public class DirectoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder  > {

    Context context;
    Directory directory;
    SangharshBooks sangharshBooks;

    final int TYPE_FILE = 0;
    final int TYPE_PDF = 1;

    public DirectoryAdapter(Context c, Directory directory, SangharshBooks sangharshBooks){
        this.context=c;
        this.directory=directory;
        this.sangharshBooks = sangharshBooks;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_FILE){
            View view = LayoutInflater.from(context).inflate(R.layout.file_item,parent);
            return new MyViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.pdf_item,parent);
            return new PDFVIewHolder(view);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //0 to f files, then p pdfs. total = p+f
        //if the type is file, open intent and pass the document id it stores. if not then open pdf
        if(holder instanceof MyViewHolder){
            //it is a file
            ((MyViewHolder) holder).fileNameTextView.setText(directory.getFiles().get(position).getName());
            ((MyViewHolder) holder).fileBg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, FileActivity.class);
                    intent.putExtra("id",directory.getFiles().get(position).getPointingDirId());
                    //sangharshBooks.addToPath(directory.getFiles().get(position).getPointingDirId());
                    sangharshBooks.addStack(directory.getFiles().get(position).getPointingDirId());
                    context.startActivity(intent);
                }
            });

        }
        if(holder instanceof PDFVIewHolder){
            //it is a pdf
            int index = position-directory.getFiles().size();
            ((PDFVIewHolder) holder).pdfNamepdfItem.setText(directory.getPdfModels().get(index).getName());
            //TODO set on click listeners. if file not available offline, download it.

        }

    }

    @Override
    public int getItemCount() {
        return directory.getFiles().size()+directory.getPdfModels().size();
    }

    @Override
    public int getItemViewType(int position){
        if(position < directory.getPdfModels().size()){
            return TYPE_PDF;
        }

        if(position - directory.getPdfModels().size() < directory.getFiles().size()){
            return TYPE_FILE;
        }

        return -1;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{//file view holder
        TextView fileNameTextView;
        CardView fileBg;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameTextView = itemView.findViewById(R.id.book_item_name);
            fileBg=itemView.findViewById(R.id.background_file_item);
        }
    }
    public class PDFVIewHolder extends RecyclerView.ViewHolder{
        TextView pdfNamepdfItem;
        TextView advancedPDFViewer, basicPDFViewer;

        public PDFVIewHolder(View itemView){
            super(itemView);
            pdfNamepdfItem = itemView.findViewById(R.id.pdf_name_pdf_item);
            advancedPDFViewer = itemView.findViewById(R.id.pdf_view_advance);
            basicPDFViewer = itemView.findViewById(R.id.pdf_view_basic);
        }
    }

}
