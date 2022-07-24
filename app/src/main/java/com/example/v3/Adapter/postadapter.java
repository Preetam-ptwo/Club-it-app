package com.example.v3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.v3.R;
import com.example.v3.databinding.ActivityMainBinding;
import com.example.v3.databinding.DashboardBinding;
import com.example.v3.modle.Notification;
import com.example.v3.modle.post;
import com.example.v3.user;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;


public class postadapter extends  RecyclerView.Adapter<postadapter.viewholder>{

    ArrayList<post> list;
    Context context;

    public postadapter(ArrayList<post> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.dashboard,parent,false);

        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        post modle=list.get(position);
        Picasso.get()
                .load(modle.getPostImg())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.postimg);
        holder.binding.textView11.setText(modle.getPostDesc());
        FirebaseDatabase.getInstance().getReference()
                .child("user")
                .child(modle.getPostedBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user user=snapshot.getValue(user.class);
                Picasso.get()
                        .load(user.getProfile_pic())
                        .placeholder(R.drawable.bms_l)
                        .into(holder.binding.profileImage);
                holder.binding.like.setText(modle.getPostlike()+"");
                holder.binding.comment.setText(modle.getPostcomment()+"");
                holder.binding.user.setText(user.getClub());
                holder.binding.club.setText(user.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //like
        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(modle.getPostId())
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_red,0,0,0);
                }
                else {
                    holder.binding.like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("posts")
                                    .child(modle.getPostId())
                                    .child("likes")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .setValue(true)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    FirebaseDatabase.getInstance().getReference().child("posts")
                                            .child(modle.getPostId())
                                            .child("postlike")
                                            .setValue(modle.getPostlike()+1)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_red,0,0,0);


                                            //notification

                                            Notification notification=new Notification();
                                            notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                            notification.setNotificationAy(new Date().getTime());
                                            notification.setPostID(modle.getPostId());
                                            notification.setPostedBy(modle.getPostedBy());
                                            notification.setType("like");

                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("notification")
                                                    .child(modle.getPostedBy())
                                                    .push()
                                                    .setValue(notification);




                                        }
                                    });
                                }
                            });

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //comment
        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(modle.getPostId())
                .child("comments")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){
                            holder.binding.comment.setCompoundDrawablesWithIntrinsicBounds(R.drawable.comment_red,0,0,0);
                        }
                        else {
                            holder.binding.comment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("posts")
                                            .child(modle.getPostId())
                                            .child("comments")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            FirebaseDatabase.getInstance().getReference().child("posts")
                                                    .child(modle.getPostId())
                                                    .child("postcomment")
                                                    .setValue(modle.getPostcomment()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    holder.binding.comment.setCompoundDrawablesWithIntrinsicBounds(R.drawable.comment_red,0,0,0);


                                                    //notification

                                                    Notification notification=new Notification();
                                                    notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                    notification.setNotificationAy(new Date().getTime());
                                                    notification.setPostID(modle.getPostId());
                                                    notification.setPostedBy(modle.getPostedBy());
                                                    notification.setType("comment");

                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("notification")
                                                            .child(modle.getPostedBy())
                                                            .push()
                                                            .setValue(notification);

                                                }
                                            });
                                        }
                                    });

                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //save
        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(modle.getPostId())
                .child("saved")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            holder.binding.save.setImageResource(R.drawable.savedred);
                        }
                        else {
                            holder.binding.save.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("posts")
                                            .child(modle.getPostId())
                                            .child("saved")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            holder.binding.save.setImageResource(R.drawable.savedred);

                                        }
                                    });

                                }
                            });
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

    public class viewholder extends RecyclerView.ViewHolder{

        DashboardBinding binding;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            binding= DashboardBinding.bind(itemView);



        }
    }
}
