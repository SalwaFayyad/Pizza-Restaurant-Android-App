package com.example.pizza_appproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class secondHome extends AppCompatActivity {
    Button login_Button;
    Button signup_Button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_home);

        signup_Button = (Button) findViewById(R.id.signup);
        signup_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(secondHome.this, Sign_Up_Activity.class);
                startActivity(intent);
            }
        });
        login_Button = (Button) findViewById(R.id.login);
        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(secondHome.this, LogIn_Activity.class);
                startActivity(intent);
            }
        });


    }
}