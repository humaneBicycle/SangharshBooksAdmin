package com.sangharsh.books.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sangharsh.books.NotificationActivity;
import com.sangharsh.books.R;
import com.sangharsh.books.UIUpdateHomeFrag;
import com.sangharsh.books.model.Notification;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    Context context;
    ArrayList<Notification> notifications;
    UIUpdateHomeFrag callback;
    public NotificationAdapter(Context context, ArrayList<Notification> notifications, UIUpdateHomeFrag callback){
        this.context=context;
        this.notifications=notifications;
        this.callback = callback;
    }

    @NonNull
    @Override
    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item,new LinearLayout(context),false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.titleTV.setText(notifications.get(position).getTitle());
        holder.bodyTV.setText(notifications.get(position).getBody());
        holder.itemBody.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                NotificationLongPressOptions notificationLongPressOptions = new NotificationLongPressOptions(context,notifications.get(position),callback,NotificationAdapter.this,position,notifications);
                notificationLongPressOptions.show(((AppCompatActivity)context).getSupportFragmentManager(),"deleteNotification");
                return true;
            }
        });


    }


    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder{
        TextView bodyTV, titleTV;
        ConstraintLayout itemBody;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            bodyTV = itemView.findViewById(R.id.notification_item_body);
            titleTV = itemView.findViewById(R.id.notification_item_title);
            itemBody = itemView.findViewById(R.id.notification_item_background);
        }
    }
}
