package com.example.v3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.v3.R;
import com.example.v3.databinding.DashboardBinding;
import com.example.v3.databinding.StoryDegineBinding;
import com.example.v3.modle.story;
import com.example.v3.modle.userStory;
import com.example.v3.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.viewHolder> {

    ArrayList<story> list;
    Context context;

    public StoryAdapter(ArrayList<story> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.story_degine, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        story story = list.get(position);

        if (story.getStories().size() > 0) {
            userStory lastStory = story.getStories().get(story.getStories().size() - 1);
            Picasso.get()
                    .load(lastStory.getImage())
                    .into(holder.binding.postimg);

            holder.binding.circlecount.setPortionsCount(story.getStories().size());

            FirebaseDatabase.getInstance().getReference()
                    .child("user")
                    .child(story.getStoryBy())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user user = snapshot.getValue(user.class);
                    Picasso.get()
                            .load(user.getProfile_pic())
                            .placeholder(R.drawable.bms_l)
                            .into(holder.binding.profileImage);
                    holder.binding.name.setText(user.getName());

                    holder.binding.postimg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ArrayList<MyStory> myStories = new ArrayList<>();

                            for (userStory stories : story.getStories()) {
                                myStories.add(new MyStory(
                                        stories.getImage()

                                ));
                            }
                            new StoryView.Builder(((AppCompatActivity) context).getSupportFragmentManager())
                                    .setStoriesList(myStories)
                                    .setStoryDuration(5000)
                                    .setTitleText(user.getClub())
                                    .setSubtitleText(user.getName())
                                    .setTitleLogoUrl(user.getProfile_pic())
                                    .setStoryClickListeners(new StoryClickListeners() {
                                        @Override
                                        public void onDescriptionClickListener(int position) {

                                        }

                                        @Override
                                        public void onTitleIconClickListener(int position) {

                                        }
                                    })
                                    .build()
                                    .show();

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        StoryDegineBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = StoryDegineBinding.bind(itemView);

        }
    }
}
