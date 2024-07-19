package com.example.pizza_appproject;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class LogIn_Activity extends AppCompatActivity {
    public static String userEmail;
    private EditText editText_email;
    private EditText editText_password;
    private CheckBox checkBox_rememberMe;
    private SharedPrefManager sharedPrefManager;
    public static List<Pizza> favoritePizza=new ArrayList<>();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Override
    public void onResume() {
        super.onResume();
        // check if the user checked the remember me checkbox
        String email = sharedPrefManager.readString("email", "noValue");

        if (!email.equals("noValue")){
            // if the user checked the remember me checkbox
            // set the email and password in the edit texts
            editText_email.setText(email);
            editText_password.setText("");
            checkBox_rememberMe.setChecked(true);
        } else {
            // if the user didn't check the remember me checkbox
            // set the email and password in the edit texts to empty strings
            editText_email.setText("");
            editText_password.setText("");
            checkBox_rememberMe.setChecked(false);
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Button button_signIn = (Button) findViewById(R.id.buttonLogin);
        editText_email = (EditText) findViewById(R.id.editTextEmail);
        editText_password = (EditText)findViewById(R.id.editTextPassword);
        checkBox_rememberMe = (CheckBox) findViewById(R.id.checkBoxRememberMe);
        sharedPrefManager = SharedPrefManager.getInstance(LogIn_Activity.this);
        button_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editText_email.getText().toString();
                String password = editText_password.getText().toString();

                DataBaseHelper dataBaseHelper =new DataBaseHelper(LogIn_Activity.this,"UserDB", null,1);
                Cursor cursor = dataBaseHelper.getUserByEmail(email);

                // check if the email is found in the database
                if (cursor.getCount() == 0){
                    editText_email.setError("Email is not found");
                    return;
                } else {
                    // check if the password is correct
                    cursor.moveToFirst();
                    String passwordFromDB = cursor.getString(6);
                    password=User.hashPassword(password);

                    if (User.checkPassword(password, passwordFromDB) == false){
                        editText_password.setError("Password is incorrect");
                        return;
                    }

                }

                User.currentUser = cursor;


                // check the remember me checkbox
                if (checkBox_rememberMe.isChecked()){
                    // if the user checked the remember me checkbox
                    // save the email and password in the shared preferences
                    sharedPrefManager.writeString("email", email);
                    sharedPrefManager.writeString("password", password);                }
                else {

                    checkBox_rememberMe.setChecked(false);
                    // if the user didn't check the remember me checkbox
                    // save the email and password in the shared preferences as empty strings
                    sharedPrefManager.writeString("email", "noValue");
                    sharedPrefManager.writeString("password", "noValue");
                }

                if (cursor.getString(7).equals("Admin")){
                    Toast.makeText(getApplicationContext(), "Admin Logged in Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LogIn_Activity.this, AdminHome_nav.class);
                    startActivity(intent);
                }
                else{
                    userEmail=email;
                    favoritePizza.clear();
                    List<String> favoritePizzaTypes = dataBaseHelper.getFavoritePizzaTypesByEmail(userEmail);
                    // Iterate through the favorite pizza types
                    for (String type : favoritePizzaTypes) {
                        for (Pizza pizza : MainActivity.allPizza) {
                            if (pizza.getName().equals(type)) {
                                favoritePizza.add(pizza);
                                }
                            }
                        }

                    Toast.makeText(getApplicationContext(), "customer", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LogIn_Activity.this, Customer_Home_nav.class);
                    startActivity(intent);
                }

            }
        });

    }
    public void login(View view){
        Intent intent = new Intent(LogIn_Activity.this, Sign_Up_Activity.class);
        startActivity(intent);
        finish();
    }

}
