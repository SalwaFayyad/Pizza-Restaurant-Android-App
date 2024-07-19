package com.example.pizza_appproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button button;
    public static List<Pizza> allPizza = new ArrayList<>();
    Activity activity;
    public static List<Pizza> pizza_menu = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setProgress(false);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHelper dbHelper = new DataBaseHelper( MainActivity.this, "UserDB", null, 1);

                User user1=new User("adminSalwa@gmail.com","Salwa","Fayyad","0593200783","female","1200430S",User.hashPassword("1200430S"),"Admin",null);
                boolean check=dbHelper.insertUser(user1);
                User user2=new User("adminKatya@gmail.com","Katya","Kobari","0599999999","female","1201478K",User.hashPassword("1201478K"),"Admin",null);
                boolean check2=dbHelper.insertUser(user2);
                User user3=new User("salwa@gmail.com","Salwa","Fayyad","0593200783","female","1200430S",User.hashPassword("1200430S"),"User",null);
                boolean check3=dbHelper.insertUser(user3);
                ConnectionAsyncTask connectionAsyncTask = new
                        ConnectionAsyncTask(MainActivity.this);

                connectionAsyncTask.execute("https://18fbea62d74a40eab49f72e12163fe6c.api.mockbin.io/");
            }
        });
    }
    public void setButtonText(String text) {
        button.setText(text);
    }
    public void setProgress(boolean progress) {
        ProgressBar progressBar = (ProgressBar)
                findViewById(R.id.progressBar);
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
    public void Second_home() {
        Intent intent = new Intent(MainActivity.this, secondHome.class);
        startActivity(intent);
    }

    public void toastmessage() {
        Toast toast =Toast.makeText(MainActivity.this,
                "Error Uploading Data !!!",Toast.LENGTH_SHORT);
        toast.show();
    }

}
