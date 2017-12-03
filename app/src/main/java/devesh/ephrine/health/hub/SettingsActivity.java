package devesh.ephrine.health.hub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {
    public String TAG = "Ephrine Health Hub :";
    public String UserUID;
    public int TotalStorage = 500;
    public String FileStorageVal;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    UserUID = user.getUid();
                    Storage();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };

        ProgressBar StorageBar = (ProgressBar) findViewById(R.id.progressBar5);
        Intent intent = getIntent();
        String FileVal = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_FILE_STORAGE_VAL);
        if (FileVal.equals("0")) {
            FileStorageVal = "0";
        } else {
            FileStorageVal = FileVal;
            StorageBar.setProgress(Integer.valueOf(FileStorageVal));
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void Storage() {

        final TextView tx = (TextView) findViewById(R.id.textView47);
        final ProgressBar StorageBar = (ProgressBar) findViewById(R.id.progressBar5);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("app/users/" + UserUID + "/settings/storage/files");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    int Val = Integer.valueOf(value);
                    int f= (int)(((double)Val/(double)TotalStorage) * 100);
                    Log.d(TAG, "Progress " + f);

                    StorageBar.setProgress(f);
                    tx.setText(value + "/500");
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public void signout(View v) {

        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        this.finish();
      //  Intent intent = new Intent(SettingsActivity.this,LoginActivity.class);
        //startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }

    public void BuyMore(View v){
        Intent ab = new Intent(this, InAppBillingActivity.class);
        startActivity(ab);
    }

}
