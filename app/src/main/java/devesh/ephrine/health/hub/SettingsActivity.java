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

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {
    public String TAG = "Ephrine Health Hub :";
    public String UserUID;
    public int TotalStorage = 500;
    public String FileStorageVal;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public String PurchaseStatus;

    public String ExpStrDate;
    public String ExpStrMonth;
    public String ExpStrYear;

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
        PurchaseStatus="F";
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        UserUID=currentUser.getUid().toString();

        DatabaseReference BuyData = database.getReference("app/users/" + UserUID + "/settings/purchase/buy");
        BuyData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Buy T/F Value is: " + value);
                if(value!=null){

       PurchaseStatus=value;  // T or F

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference ExpDateData = database.getReference("app/users/" + UserUID + "/settings/purchase/ExpDate/Exp");
        ExpDateData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if(value!=null){
                    TextView TxExp=(TextView)findViewById(R.id.textView78Exp);
                    TxExp.setText("Expiry Date: "+value);


                    TextView BuyButton=(TextView)findViewById(R.id.textView55);


  //  TxAcc.setText("Account Status: Premium Account");
    BuyButton.setVisibility(View.GONE);
   // PurchaseStatus="T";


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference PurchasedDateData = database.getReference("app/users/" + UserUID + "/settings/purchase/BuyDate");
       PurchasedDateData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if(value!=null){

                    TextView TxDate=(TextView)findViewById(R.id.textView78BuyDate);
                    TxDate.setText("Purchase Date: "+value);


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



        //Find Expiry
        DatabaseReference ExpDateData1 = database.getReference("app/users/" + UserUID + "/settings/purchase/ExpDate/date");
        DatabaseReference ExpMonthData1 = database.getReference("app/users/" + UserUID + "/settings/purchase/ExpDate/Month");
        DatabaseReference ExpYearData1 = database.getReference("app/users/" + UserUID + "/settings/purchase/ExpDate/Year");
        ExpDateData1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if(value!=null){
ExpStrDate=value;
FindExpiry();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        ExpMonthData1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if(value!=null){
                    ExpStrMonth=value;
                    FindExpiry();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        ExpYearData1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if(value!=null){
                    ExpStrYear=value;
                    FindExpiry();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

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

                    if (PurchaseStatus.equals("T")){
                        tx.setText(value+"/ Unlimited");
                    }else{
                        tx.setText(value + "/500");

                    }

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

    public void FindExpiry(){

        Calendar c = Calendar.getInstance();
        // Expiry Date
        int Tdt=c.get(Calendar.DATE);
        int Tmonth=c.get(Calendar.MONTH);
        Tmonth=Tmonth+1;
        int Tyear=c.get(Calendar.YEAR);
        TextView BuyNow=(TextView)findViewById(R.id.textView55);
        TextView TxAcc=(TextView)findViewById(R.id.textView77);

if(ExpStrDate!=null && ExpStrMonth!=null && ExpStrYear!=null){

    int ExpDate=Integer.parseInt(ExpStrDate);
    int ExpMonth=Integer.parseInt(ExpStrMonth);
    int ExpYear=Integer.parseInt(ExpStrYear);

    if(Tyear>=ExpYear){
        if(Tmonth>=ExpMonth){
            if(Tdt>=ExpDate){
                BuyNow.setVisibility(View.VISIBLE);
TxAcc.setText("Account Status: Free Account (Premium Account has been Expired)");

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference BuyData = database.getReference("app/users/" + UserUID + "/settings/purchase/buy");
                BuyData.setValue("F");
                PurchaseStatus="F";

            }
        }
    }else {

        TxAcc.setText("Account Status: Premium Account");
        BuyNow.setVisibility(View.INVISIBLE);
        PurchaseStatus="T";
    }

}

    }

}
