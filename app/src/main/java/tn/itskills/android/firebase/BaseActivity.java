package tn.itskills.android.firebase;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by adnenhamdouni on 25/11/2016.
 */

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /*
    * getUid from firebaseAuth instance
     */
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    /*
    * getCurrentUser from firebaseAuth instance
    */
    public FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

}