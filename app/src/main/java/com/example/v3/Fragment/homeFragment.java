package com.example.v3.Fragment;

import android.app.Instrumentation;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.v3.Adapter.StoryAdapter;
import com.example.v3.Adapter.postadapter;
import com.example.v3.R;
import com.example.v3.modle.post;
import com.example.v3.modle.story;
import com.example.v3.modle.userStory;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Date;


public class homeFragment extends Fragment {

    RecyclerView storyRv,DB;
    ArrayList<story> list;
    ArrayList<post> postlist;
    FirebaseDatabase db;
    FirebaseAuth auth;
    FirebaseStorage storage;
    RoundedImageView addStoryImage;
    ActivityResultLauncher<String> gallery;
    ProgressDialog dialog;


    public homeFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        dialog=new ProgressDialog(getContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


            View  view= inflater.inflate(R.layout.fragment_home, container, false);
            storyRv=view.findViewById(R.id.storyRV);
            list=new ArrayList<>();

        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Post Uploading");
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);


        StoryAdapter adapter=new StoryAdapter(list,getContext());
        LinearLayoutManager llm=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        storyRv.setLayoutManager(llm);
        storyRv.setNestedScrollingEnabled(false);
        storyRv.setAdapter(adapter);

        //post image
        DB=view.findViewById(R.id.DBRV);
        postlist=new ArrayList<>();

        postadapter posta=new postadapter(postlist,getContext());
        LinearLayoutManager ll=new LinearLayoutManager(getContext());
        DB.setLayoutManager(ll);
        DB.setNestedScrollingEnabled(false);
        DB.setAdapter(posta);

        db.getReference()
                .child("stories")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    list.clear();
                    for(DataSnapshot storysnapshot:snapshot.getChildren()){
                        story story=new story();
                        story.setStoryBy(storysnapshot.getKey());
                        story.setStoryAt(storysnapshot.child("postedby").getValue(long.class));

                        ArrayList<userStory>stories=new ArrayList<>();
                        for(DataSnapshot snapshot1:storysnapshot.child("userstory").getChildren()){
                            userStory userStories=snapshot1.getValue(userStory.class);
                            stories.add(userStories);
                        }
                        story.setStories(stories);
                        list.add(story);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        db.getReference()
                .child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postlist.clear();
                for(DataSnapshot dataSnapshot:
                        snapshot.getChildren()){
                    post post=dataSnapshot.getValue(post.class);
                    post.setPostId(dataSnapshot.getKey());
                    postlist.add(post);
                }
                posta.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addStoryImage=view.findViewById(R.id.postimg);
        addStoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallery.launch("image/*");

            }
        });
        
        gallery=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                addStoryImage.setImageURI(result);

                dialog.show();

                final StorageReference ref=storage.getReference()
                        .child("stories")
                        .child(auth.getUid())
                        .child(new Date().getTime()+"");
                ref.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                story story=new story();
                                story.setStoryAt(new Date().getTime());
                                db.getReference()
                                        .child("stories")
                                        .child(auth.getUid())
                                        .child("postedby")
                                        .setValue(story.getStoryAt())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        userStory us=new userStory(uri.toString(),story.getStoryAt());
                                        db.getReference()
                                                .child("stories")
                                                .child(auth.getUid())
                                                .child("userstory")
                                                .push()
                                                .setValue(us);

                                    }
                                });
                            }
                        });
                    }
                });

                dialog.dismiss();
                Toast.makeText(getContext(), "Story Posted", Toast.LENGTH_SHORT).show();
            }
        });

        return view;



    }
}