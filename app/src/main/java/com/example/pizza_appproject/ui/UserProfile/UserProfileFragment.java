package com.example.pizza_appproject.ui.UserProfile;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pizza_appproject.AdminHome_nav;
import com.example.pizza_appproject.Customer_Home_nav;
import com.example.pizza_appproject.DataBaseHelper;
import com.example.pizza_appproject.R;
import com.example.pizza_appproject.User;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;

import kotlin.Unit;

public class UserProfileFragment extends Fragment {

    private UserProfileViewModel mViewModel;

    private ImageView changeProfileImage;
    private ImageView profileImage;
    private EditText editText_firstName;
    private EditText editText_lastName;
    private TextView Text_email;
    private EditText editText_password;
    private EditText editText_confirmPassword;
    private EditText editText_phoneNumber;
    private Button saveButton;
    private Cursor currentUser;
    private boolean changePass = false;
    private boolean isImageChanged = false;


    public String permission = "User";


    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitializeLayout(view);
        currentUser = User.currentUser;
        profileImage = view.findViewById(R.id.imageView_profileImage);

        assignInfoToInputFields(currentUser);
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext(), "UserDB", null, 1);
        // set the on click listener for the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the user info from the input fields
                String firstName = editText_firstName.getText().toString();
                String lastName = editText_lastName.getText().toString();
                String phoneNumber = editText_phoneNumber.getText().toString();
                String gender = currentUser.getString(4);
                String permission = currentUser.getString(7);
                String password = editText_password.getText().toString();
                String confirmPassword = editText_confirmPassword.getText().toString();
                String email = currentUser.getString(0);

                User user = new User(email, firstName, lastName,  phoneNumber,gender, password, confirmPassword, permission,null);

                // check if all input fields are filled and the password and confirm password are the same
                if (!isUserDataValid(firstName, lastName, email,
                        password, confirmPassword, phoneNumber)) {
                    changePass = false;
                    return;
                }

//                // Inside your onClickListener for the save button
                if (isImageChanged && profileImage.getDrawable() != null) {
                    BitmapDrawable drawable = (BitmapDrawable) profileImage.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
                    byte[] data = outputStream.toByteArray();
                    dataBaseHelper.updateUserProfilePicture(email, data);

                }

                if (changePass) {
                    dataBaseHelper.updateUserPassword(email,User.hashPassword(password));

                }

                // update the user info in the database ( First name, Last name, Phone)
                dataBaseHelper.updateUserInfo(user);

                // update the current user info
                currentUser = dataBaseHelper.getUserByEmail(email);
                currentUser.moveToFirst();
                User.currentUser = currentUser;

                // assign the user info to Text fields
                assignInfoToInputFields(currentUser);

                Toast.makeText(getActivity(), "Profile Updated successfully", Toast.LENGTH_SHORT).show();


                //clear the passwords fields
                editText_password.setText("");
                editText_confirmPassword.setText("");
                changePass = false;
                //update nav header with the new (first name and last name)
                currentUser = User.currentUser;

                if (currentUser.getString(7).equals("User")){
                    View headerView = Customer_Home_nav.navigationView.getHeaderView(0);
                    TextView navUsername= (TextView) headerView.findViewById(R.id.usernameid);
                    TextView navEmail= (TextView) headerView.findViewById(R.id.emailid);
                    ImageView profile=headerView.findViewById(R.id.profileid);
                    navUsername.setText(User.currentUser.getString(1) +" " +User.currentUser.getString(2));
                    navEmail.setText(User.currentUser.getString(0));
                    if (User.currentUser.getBlob(8) != null) {
                        byte[] blobImage = User.currentUser.getBlob(8);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(blobImage, 0, blobImage.length);
                        profile.setImageBitmap(bitmap);
                    }
                } else {
                    View headerView = AdminHome_nav.navigationView.getHeaderView(0);
                    TextView navUsername= (TextView) headerView.findViewById(R.id.usernameid);
                    TextView navEmail= (TextView) headerView.findViewById(R.id.emailid);
                    ImageView profile=headerView.findViewById(R.id.profileid);

                    navUsername.setText(currentUser.getString(1) +" " +currentUser.getString(2));
                    navEmail.setText(User.currentUser.getString(0));
                    if (User.currentUser.getBlob(8) != null) {
                        byte[] blobImage = User.currentUser.getBlob(8);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(blobImage, 0, blobImage.length);
                        profile.setImageBitmap(bitmap);
                    }
                }

            }
        });
//
//        // listener for the change profile image button
        changeProfileImage.setOnClickListener(e->{
            ImagePicker.with(this)
                    .crop(1f, 1f)  // Set aspect ratio to 1:1 for square crop
                    .cropSquare()  // Alternatively, some libraries provide a shortcut method for square crop
                    .compress(1024)         // Final image size will be less than 1 MB
                    .maxResultSize(1080, 1080)  // Final image resolution
                    .createIntent(a->{
                        startForProfileImageResult.launch(a);
                        return Unit.INSTANCE;
                    });
        });
    }

    public void assignInfoToInputFields(Cursor currentUser) {
        editText_firstName.setText(currentUser.getString(1));
        Text_email.setText(currentUser.getString(0));
        editText_firstName.setText(currentUser.getString(1));
        editText_lastName.setText(currentUser.getString(2));
        editText_phoneNumber.setText(currentUser.getString(3));


        if (currentUser.getString(4).equals("Male")) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.baseline_male_24);
            editText_firstName.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);

        } else {
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.baseline_female_24);
            editText_firstName.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }
        // assign the profile image
        if (currentUser.getBlob(8) != null) {
            byte[] blobImage = currentUser.getBlob(8);
            Bitmap bitmap = BitmapFactory.decodeByteArray(blobImage, 0, blobImage.length);
            profileImage.setImageBitmap(bitmap);
        }
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
        if (phoneNumber.replaceAll(" ", "").isEmpty()) {
            editText_phoneNumber.setError("Please enter your phone number");
            isValid = false;
        } else if (phoneNumber.charAt(0) != '0' || phoneNumber.charAt(1) != '5' || phoneNumber.length() != 10 || !phoneNumber.matches("[0-9]+")) {
            editText_phoneNumber.setError("Please enter a valid phone number");
            isValid = false;
        }
        if (!password.replaceAll(" ", "").isEmpty()) {

         if (password.length() < 8) {
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
            changePass = true;
        }

        return isValid;
    }
    private void InitializeLayout(View view) {
        saveButton = view.findViewById(R.id.button_saveEdit);
        Text_email=view.findViewById(R.id.textView_email);
        editText_firstName = view.findViewById(R.id.editText_firstName);
        editText_lastName = view.findViewById(R.id.editText_lastName);
        editText_password = view.findViewById(R.id.editText_password);
        editText_confirmPassword = view.findViewById(R.id.editText_confirmPassword);
        editText_phoneNumber = view.findViewById(R.id.editText_phoneNumber);
        profileImage = view.findViewById(R.id.imageView_profileImage);
        changeProfileImage = view.findViewById(R.id.imageView_changeProfileImage);
    }
    // method to get the result of the image picker
    private ActivityResultLauncher<Intent> startForProfileImageResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            Intent data = result.getData();
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                if (data != null && data.getData() != null) {

                                    Uri fileUri = data.getData();

                                    profileImage.setImageURI(fileUri); // Update  ImageView
                                    isImageChanged = true;
                                }
                            } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                                Toast.makeText(getActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}