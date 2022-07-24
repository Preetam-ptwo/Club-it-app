package com.example.v3.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.v3.R;
import com.example.v3.loginActivity;
import com.example.v3.user;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class profileFragment extends Fragment {

    ImageView i,m;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase DB;
    TextView c,n;


    public profileFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        DB=FirebaseDatabase.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        i=view.findViewById(R.id.profile_image);
        c=view.findViewById(R.id.Club);
        n=view.findViewById(R.id.pname);
        m=view.findViewById(R.id.menu);

        DB.getReference().child("user").child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    user u=snapshot.getValue(user.class);
                    Picasso.get()
                            .load(u.getProfile_pic())
                            .placeholder(R.drawable.bms_l)
                            .into(i);
                    c.setText(u.getClub());
                    n.setText(u.getName());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        i.setOnClickListener(this::onClick);
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getActivity(),v);
                popup.inflate(R.menu.menu_profile);
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.l1:
                                return true;
                            case R.id.l2:
                                startActivity(new Intent(getActivity(), loginActivity.class));
                                return true;
                            default:
                                return true;

                        }

                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData()!=null){
            Uri uri =data.getData();
            i.setImageURI(uri);

            final StorageReference reference=storage.getReference()
                    .child("cover_photo")
                    .child(FirebaseAuth.getInstance().getUid());

            reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Profile pic saved" , Toast.LENGTH_SHORT).show();
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            DB.getReference().child("user")
                                    .child(auth.getUid())
                                    .child("profile_pic").setValue(uri.toString());

                        }
                    });

                }
            });
        }
    }

    private void onClick(View v) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 11);
    }


}


