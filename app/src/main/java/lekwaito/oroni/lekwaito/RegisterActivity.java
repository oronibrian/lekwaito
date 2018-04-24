package lekwaito.oroni.lekwaito;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import lekwaito.oroni.lekwaito.Users.User;

public class RegisterActivity extends AppCompatActivity {

    EditText Email, Password, Name ;
    Button Register;
    String NameHolder, EmailHolder, PasswordHolder;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    SQLiteHelper sqLiteHelper;
    String F_Result = "Not_Found";
    private static String TAG = RegisterActivity.class.getSimpleName();
    User User;
    TextView tvconnected;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Register = (Button)findViewById(R.id.buttonRegister);

        Email = (EditText)findViewById(R.id.editEmail);
        Password = (EditText)findViewById(R.id.editPassword);
        Name = (EditText)findViewById(R.id.editName);
        tvconnected=findViewById(R.id.tvconnected);
//        sqLiteHelper = new SQLiteHelper(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        //if the user is already logged in we will directly start the profile activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, Home.class));
            return;
        }


        // check if you are connected or not
        if(isConnected()){
            tvconnected.setBackgroundColor(0xFF00CC00);
            tvconnected.setText("You are conncted");
        }
        else{
            tvconnected.setText("You are NOT conncted");
        }

        // Adding click listener to register button.
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Checking EditText is empty or Not.
//                CheckEditTextStatus();
//
//                // Method to check Email is already exists or not.
//                CheckingEmailAlreadyExistsOrNot();
//
//                // Empty EditText After done inserting process.
//                EmptyEditTextAfterDataInsert();

                //if user pressed on button register
                //here we will register the user to server
                registerUser();
            }
        });

    }


//    // Insert data into SQLite database method.
//    public void InsertDataIntoSQLiteDatabase(){
//        // If editText is not empty then this block will executed.
//        if(EditTextEmptyHolder == true)
//        {
//            sqLiteHelper.insertUser(NameHolder,PasswordHolder,EmailHolder);
//
//            // Printing toast message after done inserting.
//            Toast.makeText(RegisterActivity.this,"User Registered Successfully",
//                    Toast.LENGTH_LONG).show();
//        }
//        // This block will execute if any of the registration EditText is empty.
//        else {
//            // Printing toast message if any of EditText is empty.
//            Toast.makeText(RegisterActivity.this,"Please Fill All The Required Fields.",
//                    Toast.LENGTH_LONG).show();
//        }
//    }
//
//
//    // Empty edittext after done inserting process method.
//    public void EmptyEditTextAfterDataInsert(){
//
//        Name.getText().clear();
//
//        Email.getText().clear();
//
//        Password.getText().clear();
//
//    }
//
//    // Method to check EditText is empty or Not.
//    public void CheckEditTextStatus(){
//
//        // Getting value from All EditText and storing into String Variables.
//        NameHolder = Name.getText().toString() ;
//        EmailHolder = Email.getText().toString();
//        PasswordHolder = Password.getText().toString();
//
//        if(TextUtils.isEmpty(NameHolder) || TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder)){
//            EditTextEmptyHolder = false ;
//        }
//        else {
//            EditTextEmptyHolder = true ;
//        }
//    }
//
//    // Checking Email is already exists or not.
//    public void CheckingEmailAlreadyExistsOrNot(){
//
//        F_Result = sqLiteHelper.getDataForUser(EmailHolder);
//
//        // Opening SQLite database write permission.
//        sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();
//
//        Log.d(TAG, " CheckingEmailAlreadyExistsOrNot:  "+F_Result);
//
//        // Calling method to check final result and insert data into SQLite database.
//        CheckFinalResult();
//
//    }
//
//
//    // Checking result
//    public void CheckFinalResult(){
//        // Checking whether email is already exists or not.
//        if(!F_Result.equalsIgnoreCase("Not Found"))
//        {
//            // If email is exists then toast msg will display.
//            Toast.makeText(RegisterActivity.this,"Email Already Exists",
//                    Toast.LENGTH_LONG).show();
//        }
//        else {
//            // If email already dose n't exists then User registration
//            // details will entered to SQLite database.
//            InsertDataIntoSQLiteDatabase();
//        }
//        F_Result = "Not Found" ;
//    }



    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private void registerUser() {
        final String username = Name.getText().toString().trim();
        final String email = Email.getText().toString().trim();
        final String password = Password.getText().toString().trim();


        //first we will do the validations

        if (TextUtils.isEmpty(username)) {
            Name.setError("Please enter username");
            Name.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Email.setError("Please enter your email");
            Email.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError("Enter a valid email");
            Email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Password.setError("Enter a password");
            Password.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");

                                //creating a new user object
                                User user = new User(
                                        userJson.getString("username"),
                                        userJson.getString("email"),
                                        userJson.getString("password")
                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                //starting the profile activity
                                finish();
                                startActivity(new Intent(getApplicationContext(), Login.class));
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password1", email);
                params.put("firstname", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

}
