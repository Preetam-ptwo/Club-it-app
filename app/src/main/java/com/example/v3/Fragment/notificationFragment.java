package com.example.v3.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.v3.Adapter.notificationAdapter;
import com.example.v3.R;
import com.example.v3.modle.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class notificationFragment extends Fragment {

    RecyclerView rv;
    ArrayList<Notification> list;
    FirebaseAuth auth;
    FirebaseDatabase database;

    public notificationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_notification, container, false);
        rv=view.findViewById(R.id.notificationrv);

        list = new ArrayList<>();

        notificationAdapter adapter =new notificationAdapter(list,getContext());
        LinearLayoutManager lm=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);

        database.getReference()
                .child("notification")
                .child(auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        list.clear();
                        for (DataSnapshot datasnapshot:
                             snapshot.getChildren()) {
                            Notification notification=datasnapshot.getValue(Notification.class);
                            notification.setNotificationID(datasnapshot.getKey());
                            list.add(notification);

                        }
                        adapter.notifyDataSetChanged();
                        
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        return view;
    }
}