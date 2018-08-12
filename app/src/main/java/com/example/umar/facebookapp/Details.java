package com.example.umar.facebookapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Details extends AppCompatActivity {
    TextView fbUserName, fbUserEmail, fbUserFriend;
    ImageView fbUserImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        fbUserEmail = findViewById(R.id.fbUserEmail);
        fbUserName = findViewById(R.id.fbUserName);
        fbUserImage = findViewById(R.id.fbUserImage);
        fbUserFriend = findViewById(R.id.fbUserFriend);

        String email = null, photo = null, name = null,friends;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            email = bundle.getString("email");
            photo = bundle.getString("photo");
            name = bundle.getString("name");
            friends = bundle.getString("friends");
            Picasso.with(this).load(photo).into(fbUserImage);
            fbUserEmail.setText("email id: "+email);
            fbUserFriend.setText("No of friends: "+friends);
            fbUserName.setText("Full name: "+name);
        }

    }
}
