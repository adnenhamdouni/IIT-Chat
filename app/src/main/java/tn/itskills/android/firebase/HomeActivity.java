package tn.itskills.android.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tn.itskills.android.firebase.models.Post;

//1. implements ValueEventListener - this will add OnDataChanged and onCanceled methods
public class HomeActivity extends BaseActivity implements ValueEventListener {

    private TextView mPostMessage;

    private Post mPost;
    private ArrayList<Post> mPosts;

    //2.Add FirebaseDatabase mDatabase object
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, NewPostActivity.class));
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mPosts = new ArrayList<>();

        // initViews
        initViews();

        //2.initFirebase - Instanciate Database reference
        initFirebase();


    }

    private void initViews() {
        mPostMessage = (TextView) findViewById(R.id.post_message);
    }

    /*
    * 3. initFirebase
     */
    private void initFirebase() {

        //FirebaseDatabase mDatabase reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Create new mDatabase.addValueEventListener
        mDatabase.addValueEventListener(this);


    }

    /*
    * implement onDataChange/onCancelled using ValueEventListener
     */

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        // Get Post object and use the values to update the UI
        mPosts.clear();
        mPostMessage.setText("");


        // 4. foreach DataSnapshot keys into dataSnapshot.child("posts").getChildren()
        //here we need to getValue from keys and Serializable data into mPost object using Post.class
        //then add it to mPosts list, Finally, append mPost.author and mPost.title into mPostMessage. "\n"

        for (DataSnapshot keys : dataSnapshot.child("posts").getChildren()) {
            mPost = keys.getValue(Post.class);
            mPosts.add(mPost);
            mPostMessage.append(mPost.author + ": "+mPost.title+ "\n");


        }




    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        // Getting Post failed, log a message into mPostMessage

    }

    /*
    * SignOut
     */

    private void signOut() {

        // signOut using FirebaseAuth instance
        FirebaseAuth.getInstance().signOut();


        // Back to MainActivity
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /*
    *
    *
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
