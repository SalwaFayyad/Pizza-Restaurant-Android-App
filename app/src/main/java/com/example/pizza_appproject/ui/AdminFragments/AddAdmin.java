package com.example.pizza_appproject.ui.AdminFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pizza_appproject.DataBaseHelper;
import com.example.pizza_appproject.R;
import com.example.pizza_appproject.SharedPrefManager;
import com.example.pizza_appproject.User;

public class AddAdmin extends Fragment implements AdapterView.OnItemSelectedListener {

    private Button buttonAddAdmin;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private EditText editTextPhoneNumber;
    private Spinner genderSpinner;
    private boolean firstGender = true;
    private String permission = "Admin";
    private String[] genders = {"Gender", "Male", "Female"};
    private SharedPrefManager sharedPrefManager;

    public AddAdmin() {
        // Required empty public constructor
    }

    public static AddAdmin newInstance(String param1, String param2) {
        AddAdmin fragment = new AddAdmin();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString("param1");
            String mParam2 = getArguments().getString("param2");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeLayout(view);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity(), "UserDB", null, 1);

        buttonAddAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = editTextFirstName.getText().toString();
                String lastName = editTextLastName.getText().toString();
                String email = editTextEmail.getText().toString().toLowerCase();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();
                String phoneNumber = editTextPhoneNumber.getText().toString();
                String gender = genderSpinner.getSelectedItem().toString();
                boolean isValid = isUserDataValid(firstName, lastName, email, password, confirmPassword, phoneNumber);
                if (!isValid) {
                    return;
                } else {
                    String hashedPassword = User.hashPassword(password);
                    firstName = capitalize(firstName);
                    lastName = capitalize(lastName);
                    User user = new User(email, firstName, lastName, phoneNumber, gender, password, hashedPassword, permission,null);
                    boolean isInserted = dataBaseHelper.insertUser(user);
                    if (isInserted) {
                        Toast.makeText(getContext(), "Admin Added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        editTextEmail.setError("Email already exists");
                        Toast.makeText(getContext(), "Email already exists", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void initializeLayout(View view) {
        buttonAddAdmin = view.findViewById(R.id.buttonaddAdmin);
        editTextFirstName = view.findViewById(R.id.editTextFirstName);
        editTextLastName = view.findViewById(R.id.editTextLastName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextConfirmPassword = view.findViewById(R.id.editTextConfirmPassword);
        editTextPhoneNumber = view.findViewById(R.id.editTextPhone);
        genderSpinner = view.findViewById(R.id.spinner);

        ArrayAdapter<String> objGenderArr = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, genders);
        objGenderArr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(objGenderArr);

        // Set the OnItemSelectedListener
        genderSpinner.setOnItemSelectedListener(this);
    }


    private boolean isUserDataValid(String firstName, String lastName, String email,
                                    String password, String confirmPassword, String phoneNumber) {
        boolean isValid = true;

        if (firstName.trim().isEmpty() || firstName.length() < 3) {
            editTextFirstName.setError("Please enter a valid first name");
            isValid = false;
        }
        if (lastName.trim().isEmpty() || lastName.length() < 3) {
            editTextLastName.setError("Please enter a valid last name");
            isValid = false;
        }
        if (email.trim().isEmpty() || !email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            editTextEmail.setError("Please enter a valid email");
            isValid = false;
        }
        if (password.trim().isEmpty() || password.length() < 8 || !password.matches(".*[a-zA-Z]+.*") || !password.matches(".*[0-9]+.*")) {
            editTextPassword.setError("Password must contain at least 8 characters, including a number and a letter");
            isValid = false;
        }
        if (confirmPassword.trim().isEmpty() || !confirmPassword.equals(password)) {
            editTextConfirmPassword.setError("Passwords don't match");
            isValid = false;
        }
        if (phoneNumber.trim().isEmpty() || phoneNumber.charAt(0) != '0' || phoneNumber.charAt(1) != '5' || phoneNumber.length() != 10 || !phoneNumber.matches("[0-9]+")) {
            editTextPhoneNumber.setError("Please enter a valid phone number");
            isValid = false;
        }
        if (genderSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Please select a gender", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner) {
            if (firstGender) {
                // Check if the view is non-null and an instance of TextView
                if (view instanceof TextView) {
                    ((TextView) view).setTextColor(getResources().getColor(R.color.grey));
                }
                firstGender = false;
            } else if (position == 0) {
                Toast.makeText(getContext(), "Please select a gender", Toast.LENGTH_SHORT).show();
                genderSpinner.setSelection(1);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
