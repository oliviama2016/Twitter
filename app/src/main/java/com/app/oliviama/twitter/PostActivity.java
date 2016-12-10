package com.app.oliviama.twitter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostActivity extends Activity implements View.OnClickListener {

    private Button buttonSubmit;
    private EditText editTextTitle;
    private EditText editTextContent;
    private EditText editTextTime;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        buttonSubmit=(Button)findViewById(R.id.buttonSubmit);
        editTextContent=(EditText)findViewById(R.id.editTextContent);
        editTextTitle=(EditText)findViewById(R.id.editTextTitle);
        editTextTime=(EditText)findViewById(R.id.editTextTime);

        buttonSubmit.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(PostActivity.this, "User signed in: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PostActivity.this, "Nobody Logged In", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PostActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    @Override
    public void onClick(View view) {

        String title = editTextTitle.getText().toString();
        String content = editTextContent.getText().toString();
        String time = editTextTime.getText().toString();

        Tweet tweet = new Tweet(title, content, time);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("tweets");
        DatabaseReference Reference = reference.push();
        Reference.setValue(tweet);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intentRead = new Intent(PostActivity.this, ReadActivity.class);
        //Intent intentPurchase = new Intent(MonitorActivity.this, SalesActivity.class);
        if (mAuth.getCurrentUser() != null ) {
            if (item.getItemId() == R.id.menuLogout) {
                mAuth.signOut();

            } else if (item.getItemId() == R.id.menuRead) {
                startActivity(intentRead);

            } else if (item.getItemId() == R.id.menuPost) {

                Toast.makeText(this, "You are already in new Tweet Posting page", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(this, "Nobody Logged In", Toast.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}