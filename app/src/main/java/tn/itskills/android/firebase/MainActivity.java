package tn.itskills.android.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tn.itskills.android.firebase.models.User;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mSignInButton;
    private Button mSignUpButton;

    //1.Add FirebaseDatabase(mDatabase), FirebaseAuth(mAuth) and FirebaseAuth.AuthStateListener(mAuthListener) objects
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initViews
        initViews();

        //2.Instanciate Database and Auth references
        initFirebase();


    }

    private void initViews() {
        // Views
        mEmailField = (EditText) findViewById(R.id.field_email);
        mPasswordField = (EditText) findViewById(R.id.field_password);
        mSignInButton = (Button) findViewById(R.id.button_sign_in);
        mSignUpButton = (Button) findViewById(R.id.button_sign_up);

        // Click listeners
        mSignInButton.setOnClickListener(this);
        mSignUpButton.setOnClickListener(this);


    }

    /*
    * 2. initFirebase
     */
    private void initFirebase() {

        //FirebaseDatabase mDatabase reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //FirebaseAuth Auth instance
        mAuth = FirebaseAuth.getInstance();


        //Create new FirebaseAuth.AuthStateListener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser != null) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
                }

            }
        };


    }

    /*
    * 3. Add OnStart & OnStop Methods - add and remove AuthStateListener
     */

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        // mAuth.addAuthStateListener onStart

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        // mAuth.removeAuthStateListener onStop if mAuthListener != null


    }

    /*
    * 4. Add SignIn Method
     */
    private void signIn() {

        //validateForm - check if Email and password ar not empty
        if (!validateForm()) {
            return;
        }

        //Show ProgressDialog
        showProgressDialog();

        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        // 4. create signInWithEmailAndPassword with mAuth - use addOnCompleteListener / hideProgressDialog
        // task.getResult().getUser() and call onAuthSuccess if task.isSuccessful()
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressDialog();
                if(task.isSuccessful()) {
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    onAuthSuccess(firebaseUser);
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "failed authentification", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    /*
    * 4. Add SignIn Method
    */
    private void signUp() {

        //validateForm - check if Email and password ar not empty
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        // 5. createUserWithEmailAndPassword with mAuth - use addOnCompleteListener / hideProgressDialog
        // // task.getResult().getUser() andcall onAuthSuccess if task.isSuccessful()

        mAuth.createUserWithEmailAndPassword(email, password).
                addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressDialog();
                if(task.isSuccessful()) {
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    onAuthSuccess(firebaseUser);
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "failed authentification", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void onAuthSuccess(FirebaseUser firebaseUser) {
        String username = usernameFromEmail(firebaseUser.getEmail());

        //Instanciate new User
        User user = new User(username);

        // Write new user into child users/Uid - setValue new User
        mDatabase.child("users").child(getUid()).setValue(user);

        // Go to MainActivity
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
        finish();
    }

    /*
    *
    *
    */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sign_in:
                signIn();
                break;
            case R.id.button_sign_up:
                signUp();
                break;
        }
    }

    /*
    *
    *
    */

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
            mEmailField.setError("Required");
            result = false;
        } else {
            mEmailField.setError(null);
        }

        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
            mPasswordField.setError("Required");
            result = false;
        } else {
            mPasswordField.setError(null);
        }

        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
