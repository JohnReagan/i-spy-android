package edu.virginia.cs.cs4720.ispy;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.Locale;

/**
 * Created by Cole on 12/3/2015.
 */
public class SignUp extends Activity implements View.OnClickListener {

    private EditText userNameEdit;
    private EditText passEdit;
    private EditText confirmPassEdit;
    private Button createAccount;

    private String username;
    private String password;
    private String confirmpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userNameEdit = (EditText) findViewById(R.id.editUsername);
        passEdit = (EditText) findViewById(R.id.editPass);
        confirmPassEdit = (EditText) findViewById(R.id.editConfirm);

        createAccount = (Button) findViewById(R.id.accountBtn);
        createAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accountBtn:
                createAccount();
                break;
            default:
                break;
        }
    }

    private void createAccount(){
        boolean cancel = false;
        View focusView = null;

        username = userNameEdit.getText().toString();
        password = passEdit.getText().toString();
        confirmpassword = confirmPassEdit.getText().toString();

        //check valid confirmation
        if (TextUtils.isEmpty(confirmpassword)) {
            confirmPassEdit.setError(getString(R.string.error_field_req));
            focusView = confirmPassEdit;
            cancel = true;
        } else if (password != null && !confirmpassword.equals(password)) {
            passEdit.setError(getString(R.string.error_invalid));
            focusView = passEdit;
            cancel = true;
        }

        //check valid pass
        if (TextUtils.isEmpty(password)) {
            passEdit.setError(getString(R.string.error_field_req));
            focusView = passEdit;
            cancel = true;
        }


        if (cancel) {
            //error, focus first form field with error
            focusView.requestFocus();
        } else {
            signUp(username.toLowerCase(Locale.getDefault()), password);
        }
    }

    private void signUp(final String username, String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "Account Created Successfully", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(in);
                } else {
                    Toast.makeText(getApplicationContext(), "Error, account taken", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
