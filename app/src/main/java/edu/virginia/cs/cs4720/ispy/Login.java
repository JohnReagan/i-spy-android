package edu.virginia.cs.cs4720.ispy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.Locale;

/**
 * Created by Cole on 12/3/2015.
 */
public class Login extends Activity {
    Button loginBtn = null;
    Button signUpBtn = null;
    private EditText usernameEdit;
    private EditText passEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        usernameEdit = (EditText) findViewById(R.id.username);
        passEdit = (EditText) findViewById(R.id.password);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Login.this, SignUp.class);
                startActivity(in);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_forgot_password:
                forgotPassword();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void forgotPassword() {

    }

    public void attemptLogin() {
        usernameEdit.setError(null);
        passEdit.setError(null);

        String username = usernameEdit.getText().toString();
        String password = passEdit.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            passEdit.setError(getString(R.string.error_field_req));
            focusView = passEdit;
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            usernameEdit.setError(getString(R.string.error_field_req));
            focusView = usernameEdit;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            login(username.toLowerCase(Locale.getDefault()), password);
        }
    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    loginSuccess();
                } else {
                    loginUnsuccess();
                }
            }
        });
    }

    protected void loginSuccess() {
        Intent in = new Intent(Login.this, MainActivity.class);
        startActivity(in);
    }

    protected void loginUnsuccess() {
        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
        showAlertDialog(Login.this, "Login", "Username or password is invalid.", false);
    }

    @SuppressWarnings("deprecation")
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }

}
