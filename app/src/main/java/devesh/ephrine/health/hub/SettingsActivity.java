package devesh.ephrine.health.hub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    public int TotalStorage;
    public String FileStorageVal;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public String PurchaseStatus;

    public String ExpStrDate;
    public String ExpStrMonth;
    public String ExpStrYear;
    public int LoadCount;
    public int TotalLoadCount=6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        LoadCount=0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
String FVal=getString(R.string.File_Storage_Default);
TotalStorage=Integer.parseInt(FVal);
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
                    LoadCount=LoadCount+1;
       PurchaseStatus=value;  // T or F

                }else {
                    LoadCount=LoadCount+1;
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
                    LoadCount=LoadCount+1;
                    TextView TxExp=(TextView)findViewById(R.id.textView78Exp);
                    TxExp.setText("Expiry Date: "+value);


                    TextView BuyButton=(TextView)findViewById(R.id.textView55);


  //  TxAcc.setText("Account Status: Premium Account");
    BuyButton.setVisibility(View.GONE);
   // PurchaseStatus="T";


                }else {
                    LoadCount=LoadCount+1;
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
                    LoadCount=LoadCount+1;
                    TextView TxDate=(TextView)findViewById(R.id.textView78BuyDate);
                    TxDate.setText("Purchase Date: "+value);


                }else {
                    LoadCount=LoadCount+1;
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
                    LoadCount=LoadCount+1;
ExpStrDate=value;
FindExpiry();
                }else {
                    LoadCount=LoadCount+1;
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
                    LoadCount=LoadCount+1;
                    ExpStrMonth=value;
                    FindExpiry();
                }else {
                    LoadCount=LoadCount+1;
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
                  //  LoadCount=LoadCount+1;
                }else {
                    //LoadCount=LoadCount+1;
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
                    LoadCount=LoadCount+1;
                    int Val = Integer.valueOf(value);
                    int f= (int)(((double)Val/(double)TotalStorage) * 100);
                    Log.d(TAG, "Progress " + f);

                    StorageBar.setProgress(f);

                    if (PurchaseStatus.equals("T")){
                        tx.setText(value+"/ Unlimited");
                    }else{
                        tx.setText(value + "/"+TotalStorage);

                    }

                }else {
                    LoadCount=LoadCount+1;
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
        deleteAppData();
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
        Log.d(TAG, "Total Loadcount: "+LoadCount);

        if(LoadCount==TotalLoadCount){
            Intent ab = new Intent(this, InAppBillingActivity.class);
            startActivity(ab);
        }else {

            Toast.makeText(SettingsActivity.this, "Connecting to Server... Please wait)",
                    Toast.LENGTH_SHORT).show();
        }

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



    public void policy(View v){
        View WebCard=(View)findViewById(R.id.webCard);
        WebCard.setVisibility(View.VISIBLE);
        WebView myWebView = (WebView) findViewById(R.id.WebView1);
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.loadUrl("https://ephrine.github.io/Titan-Health-Hub/getting-started/health-data-storage-privacy-policies");

    }
    public void GetStartedClose(View v){
        View WebCard=(View)findViewById(R.id.webCard);
        WebCard.setVisibility(View.GONE);
    }
    private class MyWebViewClient extends WebViewClient {
      /*  @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals("www.example.com")) {
                // This is my web site, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
*/    }

    private void deleteAppData() {
        try {
            // clearing app data
            String packageName = getApplicationContext().getPackageName();
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear "+packageName);
            Log.d(TAG, "App Data Cleared !!");

        } catch (Exception e) {
            e.printStackTrace();
        } }
}
