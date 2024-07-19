package com.example.pizza_appproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Sign_Up_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Button button_signup;
    private EditText editText_firstName;
    private EditText editText_lastName;
    private EditText editText_email;
    private EditText editText_password;
    private EditText editText_confirmPassword;
    private EditText editText_phoneNumber;
    private Spinner genderSpinner;
    private boolean firstGender = true;
    public String permission = "User";
    private String[] genders = {"Gender", "Male", "Female"};
    private void initializeLayout() {
        button_signup= (Button) findViewById(R.id.buttonSignUp);
        editText_firstName = (EditText) findViewById(R.id.editTextFirstName);
        editText_lastName = (EditText) findViewById(R.id.editTextLastName);
        editText_email = (EditText) findViewById(R.id.editTextEmail);
        editText_password = (EditText) findViewById(R.id.editTextPassword);
        editText_confirmPassword = (EditText)findViewById(R.id.editTextConfirmPassword);
        editText_phoneNumber = (EditText) findViewById(R.id.editTextPhone);
        genderSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> objGenderArr = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);
        genderSpinner.setAdapter(objGenderArr);
        genderSpinner.setOnItemSelectedListener(this);
    }
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

    }

    protected void onResume() {
        super.onResume();
        initializeLayout();
        DataBaseHelper dataBaseHelper =new DataBaseHelper(Sign_Up_Activity.this,"UserDB", null,1);
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = editText_firstName.getText().toString();
                String lastName = editText_lastName.getText().toString();
                String email = editText_email.getText().toString().toLowerCase();
                String password = editText_password.getText().toString();
                String confirmPassword = editText_confirmPassword.getText().toString();
                String phoneNumber = editText_phoneNumber.getText().toString();
                String gender = genderSpinner.getSelectedItem().toString();
                boolean isValid = isUserDataValid(firstName, lastName, email, password, confirmPassword, phoneNumber);
                if (!isValid){
                    return;
                }
                else{
                    String hashedPassword = User.hashPassword(password);
                    firstName = firstName.substring(0,1).toUpperCase() + firstName.substring(1).toLowerCase();
                    lastName = lastName.substring(0,1).toUpperCase() + lastName.substring(1).toLowerCase();
                    User user = new User(email,firstName ,lastName,phoneNumber,gender, password,hashedPassword,permission,null);
                    boolean isInserted = dataBaseHelper.insertUser(user);
                    if (isInserted){
                        if (permission.equals("User")){
                            Toast.makeText(getApplicationContext(),"User Registered successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Sign_Up_Activity.this, LogIn_Activity.class);
                            startActivity(intent);
                            finish();
                        } else{
                            Toast.makeText(getApplicationContext(), "Admin Added successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Sign_Up_Activity.this, LogIn_Activity.class);
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        editText_email.setError("Email is already Exist");
                        Toast.makeText(getApplicationContext(), "Email is already Exist", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

    }
    private boolean isUserDataValid(String firstName, String lastName, String email,
                                    String password, String confirmPassword, String phoneNumber) {

        boolean isValid = true;
        // check if the user entered all the required data
        if (firstName.replaceAll(" ", "").isEmpty()) {
            editText_firstName.setError("Please enter your first name");
            isValid = false;
        }
        else if (firstName.length()<3) {
            editText_firstName.setError("Please enter a valid first name");
            isValid = false;
        }
        if (lastName.replaceAll(" ", "").isEmpty()) {
            editText_lastName.setError("Please enter your last name");
            isValid = false;
        }
        else if (lastName.length()<3) {
            editText_lastName.setError("Please enter a valid last name");
            isValid = false;
        }
        if (email.replaceAll(" ", "").isEmpty()) {
            editText_email.setError("Please enter your email");
            isValid = false;
        } // check if the email is valid
        else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            editText_email.setError("Please enter a valid email");
            isValid = false;
        }
        if (password.replaceAll(" ", "").isEmpty()) {
            editText_password.setError("Please enter your password");
            isValid = false;
        } else if (password.length() < 8) {
            editText_password.setError("Password must contain at least 8 characters");
            isValid = false;
        } else if (!password.matches(".*[a-zA-Z]+.*")) {
            editText_password.setError("Password must contain at least 1 character");
            isValid = false;
        } else if (!password.matches(".*[0-9]+.*")) {
            editText_password.setError("Password must contain at least 1 number");
            isValid = false;
        }
        if (confirmPassword.replaceAll(" ", "").isEmpty()) {
            editText_confirmPassword.setError("Please confirm your password");
            isValid = false;
        } else if (!confirmPassword.equals(password)) {
            editText_confirmPassword.setError("Passwords doesn't match");
            isValid = false;
        }

        if (phoneNumber.replaceAll(" ", "").isEmpty()) {
            editText_phoneNumber.setError("Please enter your phone number");
            isValid = false;
        } else if (phoneNumber.charAt(0) != '0' || phoneNumber.charAt(1) != '5' || phoneNumber.length() != 10 || !phoneNumber.matches("[0-9]+")) {
            editText_phoneNumber.setError("Please enter a valid phone number");
            isValid = false;
        }
        if (genderSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText( getApplicationContext(),"Please select the Gender", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        Log.d("PizzaApp", "Password: " + password+"vvvvvv");
        return isValid;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner) {
            String selectedGender = (String) parent.getItemAtPosition(position);
            if (firstGender) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.grey));
                for (int i = 1; i < parent.getChildCount(); i++) {
                    ((TextView) parent.getChildAt(i)).setTextColor(getResources().getColor(R.color.black));
                }
                firstGender = false;
            } else {
                // If "Gender" is selected again
                if (position == 0) {
                    // Show a toast message to select a gender
                    Toast.makeText(getApplicationContext(), "Please select a gender", Toast.LENGTH_SHORT).show();
                    // Automatically select "Male" if "Gender" is reselected
                    genderSpinner.setSelection(1);
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

}