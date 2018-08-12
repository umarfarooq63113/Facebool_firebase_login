package com.example.umar.facebookapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    TextView fbUserName, fbUserEmail, fbUserFriend;
    ImageView fbUserImage;
    Button myButton;
    String email,photo,name,friends;
    ProgressDialog progressDialog;

    // for fire base login and signup starts
    EditText etUserName, etUserPass;
    private FirebaseAuth firebaseAuth;
    Button btnLogin, btnReg;
    // for fire base login and signup ends
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// for fire base login and signup starts
        etUserName = findViewById(R.id.etUserName);
        etUserPass = findViewById(R.id.etUserPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnReg = findViewById(R.id.btnReg);
        firebaseAuth = FirebaseAuth.getInstance();


        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent (MainActivity.this, RegistrationActivity.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnUserLogin_Click();
            }
        });



        // for fire base login and signup ends




        callbackManager = CallbackManager.Factory.create();
        final LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email", "user_birthday", "user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Retrieving data......");
                progressDialog.show();

                String accessToken = loginResult.getAccessToken().getToken();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        progressDialog.dismiss();
                        Log.d("response", response.toString());
                        getData(object);
                        Intent intent= new Intent(MainActivity.this,Details.class);
                        intent.putExtra("email",email);
                        intent.putExtra("photo",photo);
                        intent.putExtra("name",name);
                        intent.putExtra("friends",friends);
                        startActivity(intent);
                    }
                });
                //Request Graph Api
                Bundle bundle=new Bundle();
                bundle.putString("fields","id,first_name, last_name,email,birthday,friends");
                request.setParameters(bundle);
                request.executeAsync();


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    // if already login
        if(AccessToken.getCurrentAccessToken() != null)

        {
            //fbUserEmail.setText(AccessToken.getCurrentAccessToken().getUserId());
        }
    }


    public void btnUserLogin_Click() {
        final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "Please wait...", "Proccessing...", true);

        (firebaseAuth.signInWithEmailAndPassword(etUserName.getText().toString(), etUserPass.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(MainActivity.this, Main.class);
                            i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                            startActivity(i);
                        } else {

                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }



    private void getData(JSONObject object) {
        try{
            URI profile_picture=new URI("https://graph.facebook.com/"+object.getString("id")+"/picture?width=250&height=250");
             email=object.getString("email");
             photo=profile_picture.toString();
             name= object.getString("first_name")+" "+ object.getString("last_name");
            friends=object.getJSONObject("friends").getJSONObject("summary").getString("total_count");


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


}
