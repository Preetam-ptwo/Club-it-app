package com.example.v3.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.v3.R;
import com.example.v3.databinding.Notification2SampleBinding;
import com.example.v3.modle.Notification;
import com.example.v3.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class notificationAdapter extends RecyclerView.Adapter<notificationAdapter.viewHolder> {

    ArrayList<Notification> list;
    Context context;

    public notificationAdapter(ArrayList<Notification> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.notification2_sample,parent,false);
        return new viewHolder( view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Notification notification=list.get(position);
        String type=notification.getType();

        FirebaseDatabase.getInstance().getReference()
                .child("user")
                .child(notification.getNotificationBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        user user=snapshot.getValue(user.class);
                        Picasso.get()
                                .load(user.getProfile_pic())
                                .placeholder(R.drawable.bms_l)
                                .into(holder.binding.profileImage);

                        if (type.equals("like")){
                            holder.binding.notificationt.setText(Html.fromHtml("<b>"+user.getName()+"</b>"+" liked your post"));
                        }
                        else {
                            holder.binding.notificationt.setText(Html.fromHtml("<b>"+user.getName()+"</b>"+" commented your post"));
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class viewHolder extends RecyclerView.ViewHolder{

        Notification2SampleBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
           binding=Notification2SampleBinding.bind(itemView);
        }
    }
}