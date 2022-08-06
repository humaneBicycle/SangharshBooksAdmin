package com.sangharshAdmin.book.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sangharshAdmin.book.R;
import com.sangharshAdmin.book.model.Banner;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.MyViewHolder> {

    private ArrayList<Banner> banners;
    private Listener listener;

    public interface Listener {
        void delete(Banner banner);
    }

    public BannerAdapter(ArrayList<Banner> banners, Listener listener) {
        this.banners = banners;
        this.listener = listener;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_banner_layout, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int pos) {
        final int position = pos;
        Picasso.get()
                .load(banners.get(position).getImageUrl())
                .into(holder.imageView);

        if (banners.get(position).getText() != null && !banners.get(position).getText().isEmpty()){
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText(banners.get(position).getText());
        }

        if (banners.get(position).getRedirectUrl() != null && !banners.get(position).getRedirectUrl().isEmpty()){
            holder.url.setVisibility(View.VISIBLE);
            holder.url.setText(banners.get(position).getRedirectUrl());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.delete(banners.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return banners.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title;
        TextView url;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            title =  itemView.findViewById(R.id.title);
            url =  itemView.findViewById(R.id.url);
        }


    }
}