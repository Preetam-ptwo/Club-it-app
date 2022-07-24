package com.example.v3.Fragment;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.v3.MainActivity;
import com.example.v3.loginActivity;
import com.example.v3.modle.post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Date;

import com.example.v3.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;
import static com.example.v3.R.*;

public class addFragment extends Fragment {

    FirebaseDatabase db;
    FirebaseAuth auth;
    FirebaseStorage storage;
    ImageView i;
    Button post, upload;
    EditText desc;
    Uri uri;
    ProgressDialog dialog;

    public addFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        db = FirebaseDatabase.getInstance();
        dialog=new ProgressDialog(getContext());

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout.fragment_add, container, false);
        i = view.findViewById(id.post_image);
        post = view.findViewById(id.post);
        upload = view.findViewById(id.upload);
        desc = view.findViewById(id.desc);

        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Post Uploading");
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 11);
            }
        });

        desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String description = desc.getText().toString();
                if (description.isEmpty()) {
                    post.setVisibility(View.GONE);
                    post.setEnabled(false);
                } else {
                    post.setBackgroundColor(getContext().getResources().getColor(color.maincolor));
                    post.setVisibility(View.VISIBLE);
                    post.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();
                final StorageReference ref=storage.getReference()
                        .child("post")
                        .child(auth.getUid())
                        .child(new Date().getTime()+"");
                ref.putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                post p=new post();
                                p.setPostImg(uri.toString());
                                p.setPostedBy(auth.getUid());
                                p.setPostDesc(desc.getText().toString());
                                p.setPostdate(new Date().getTime());

                                db.getReference()
                                        .child("posts")
                                        .push()
                                        .setValue(p)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialog.dismiss();
                                        Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getActivity(), MainActivity.class));

                                    }
                                });
                            }
                        });
                    }
                });

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null) {
            uri = data.getData();
            i.setImageURI(uri);
            post.setVisibility(View.VISIBLE);
            post.setEnabled(true);
        }
    }
}