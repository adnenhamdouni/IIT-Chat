package tn.itskills.android.isgc_firebase;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import tn.itskills.android.isgc_firebase.models.Post;

public class NewPostActivity extends BaseActivity {

    private EditText mTitleField;
    private EditText mBodyField;

    //1.Add FirebaseDatabase mDatabase object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPost();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // initViews
        initViews();

        //2.initFirebase - Instanciate Database reference
        initFirebase();
    }

    private void initViews() {
        mTitleField = (EditText) findViewById(R.id.field_title);
        mBodyField = (EditText) findViewById(R.id.field_body);
    }

    /*
    * 3. initFirebase
     */
    private void initFirebase() {

        //FirebaseDatabase mDatabase reference



    }

    private void submitPost() {

        //get title
        final String title = mTitleField.getText().toString();

        //get body
        final String body = mBodyField.getText().toString();

        // Title is required
        if (TextUtils.isEmpty(title)) {
            mTitleField.setError("required");
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(body)) {
            mBodyField.setError("required");
            return;
        }

        //getUid from Auth
        final String userId = getUid();

        //implement listener addListenerForSingleValueEvent from mDatabase.child("users").child(userId)
        //this listener onDataChange add method that create dataSnapshot
        //here we need to getValue from dataSnapshot and Serializable data into new User object using User.class
        //then test if User != null, we call writeNewPostMethod (writeNewPost(userId, user.username, title, body);)
        //Else we show error message.
        //finally, call finish method to close NewPostActivity and back to HomeActivity


    }

    private void writeNewPost(String userId, String username, String title, String body) {

        //getKey from mDatabase.child("posts") after use push();

        //Create new post
        Post post = new Post(userId, username, title, body);

        //Create new postValues with Map<String, Object> using post.toMap()
        Map<String, Object> postValues = post.toMap();

        //Create new Childs Map
        Map<String, Object> childUpdates = new HashMap<>();
        // First child key ==> "/posts/" + key with postValues;
        // First child key ==> "/user-posts/" + userId + "/" + key with postValues;


        // update mDatabaseChildren

    }


}
