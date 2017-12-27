package devesh.ephrine.health.hub;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;


public class MainActivity extends Activity {
    public static final String EXTRA_MESSAGE_USER_TOKEN = "devesh.ephrine.health.hub";
    public static final String EXTRA_MESSAGE_USER_UID = "devesh.ephrine.health.hub";
    public static final String EXTRA_MESSAGE_FILE_STORAGE_VAL = "devesh.ephrine.health.hub";

    public String TAG = "Ephrine Health Hub :";
    public String UserUID;
    public String UserToken; // Access Token
    public String FileStorageVal;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public int loadcount=0;
    public int TotalLoadCount=4;
    public String UserName;
    private AdView mAdView;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);

        AdsLoad();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                Log.d(TAG, "No Permission");
            } else if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Yes Permission ");
             //   AppStart();
            }
        }



        Context context = MainActivity.this;
        String locale = context.getResources().getConfiguration().locale.getCountry();
        Log.d(TAG, "Country is: " + locale);
        if (locale.equals("IN")) {
            Drawable DOCDrawable = getResources().getDrawable(R.drawable.financialin);
            //Drawable DOCDrawable = getResources().getDrawable(R.mipmap.bill);
            ImageView img = (ImageView) findViewById(R.id.imageView4);
            img.setImageDrawable(DOCDrawable);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            //     w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        }

        final ImageView BGimg = (ImageView) findViewById(R.id.imageViewBG);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //   FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        DatabaseReference BGload = database.getReference("bg/img");
// Read from the database
        BGload.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null && value != "0") {
                    Picasso.with(MainActivity.this).load(value).into(BGimg);

                    //Glide.with(MainActivity.this).load(value).into(BGimg);
                    loadcount=loadcount+1;
                    ConnectingView();
                }else{
                    loadcount=loadcount+1;
                    ConnectingView();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        BGload.keepSynced(true);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    UserUID = user.getUid();
                    GetUserProfile();
                    DownloadToken();
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    finish();
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UserUID = user.getUid();

            DatabaseReference StorageQuota = database.getReference("app/users/" + UserUID + "/settings/storage/files");
            StorageQuota.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "Value is: " + value);
                    if (value != null) {
                        FileStorageVal = value;
                        loadcount=loadcount+1;
                        ConnectingView();

                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });

        }

      //  final GestureDetector gestureDetector = new GestureDetector(this, new SingleTapConfirm());

    /*    CardView ProfileCard=(CardView)findViewById(R.id.ProfileCardView);

        ProfileCard.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                Log.d(TAG, "onTouch");
                CardView ProfileCard1=(CardView)findViewById(R.id.ProfileCardView);

              //  Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_button_touch);
              //  ProfileCard1.startAnimation(hyperspaceJumpAnimation);
                if (gestureDetector.onTouchEvent(arg1)) {
                    // single tap
                    return true;
                } else {
                    // your code for move and drag
                }


                return false;
            }
        }); */

/*
        Intent intent = getIntent();
        String FirstStart = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE_FIRST_START);
        if (FirstStart.equals("1") || FirstStart == "1") {
            View Note = (View) findViewById(R.id.ViewNote);
            Note.setVisibility(View.VISIBLE);
        } else {
            View Note = (View) findViewById(R.id.ViewNote);
            Note.setVisibility(View.GONE);
        }*/





    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //    Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.help:
//               setContentView(R.layout.edit_profile);
                break;


            default:
                break;
        }

        return true;
    }

*/

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
      //  deleteCache(MainActivity.this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // This ensures that if the user denies the permissions then uses Settings to re-enable
        // them, the app will start working.
        //buildFitnessClient();
    }




    public void mdata(View v) {

        Intent ab = new Intent(this, MedicalReportsActivity.class);
        if (FileStorageVal != null) {
            ab.putExtra(EXTRA_MESSAGE_FILE_STORAGE_VAL, FileStorageVal);
        } else {
            ab.putExtra(EXTRA_MESSAGE_FILE_STORAGE_VAL, "0");
        }
        startActivity(ab);

    }

    public void presc(View v) {
        Intent ab = new Intent(this, PrescriptionActivity.class);
        if (FileStorageVal != null) {
            ab.putExtra(EXTRA_MESSAGE_FILE_STORAGE_VAL, FileStorageVal);
        } else {
            ab.putExtra(EXTRA_MESSAGE_FILE_STORAGE_VAL, "0");
        }
        startActivity(ab);
    }

    public void mchart(View v) {
        Intent ab = new Intent(this, HealthChartActivity.class);
        startActivity(ab);
    }

    public void mbills(View v) {

        Intent ab = new Intent(this, MedicalBillsActivity.class);
        if (FileStorageVal != null) {
            ab.putExtra(EXTRA_MESSAGE_FILE_STORAGE_VAL, FileStorageVal);
        } else {
            ab.putExtra(EXTRA_MESSAGE_FILE_STORAGE_VAL, "0");
        }
        startActivity(ab);

    }

    public void Mhistory(View v) {
        Intent ab = new Intent(this, MedicalHistoryActivity.class);
        startActivity(ab);
    }

    public void profile(View v) {
        Intent ab = new Intent(this, ProfileActivity.class);
        startActivity(ab);
    }

    public void settings(View v) {
        Intent ab = new Intent(this, SettingsActivity.class);
        if (FileStorageVal != null) {
            ab.putExtra(EXTRA_MESSAGE_FILE_STORAGE_VAL, FileStorageVal);
        } else {
            ab.putExtra(EXTRA_MESSAGE_FILE_STORAGE_VAL, "0");
        }
        startActivity(ab);
    }

    public void token(View v) {
        String token;
        Intent ab = new Intent(this, AccessTokenActivity.class);

        if (UserToken != null) {
            token = UserToken;
            ab.putExtra(EXTRA_MESSAGE_USER_TOKEN, token);
        } else {
            token = "0";
            ab.putExtra(EXTRA_MESSAGE_USER_TOKEN, token);
        }
        startActivity(ab);
    }
/*    private void printKeyHash(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "devesh.ephrine.health.hub",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                 Log.d("--------------------", "----------");
                 Log.d("KeyHash1:-----------", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {
             Log.d("KeyHash2:", e.toString());
        } catch (NoSuchAlgorithmException e) {
             Log.d("KeyHash3:", e.toString());
        }
    } */

    public void GetUserProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            UserName=name;
            //  String email = user.getEmail();
            UserUID = user.getUid();
          //   CircularImageView ProfilePic = (CircularImageView) findViewById(R.id.imageViewProfilePic);
            //ImageView ProfilePic=(ImageView) findViewById(R.id.imageView34);
            //ImageView ProfilePic = (ImageView) findViewById(R.id.imageViewProfilePic);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            //    FirebaseDatabase.getInstance().setPersistenceEnabled(true);

            //    DatabaseReference DataPIC= database.getReference("app/users/" + UserUID + "/bio/photo");
            /*DataPIC.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "Value is: " + value);
                    if(value!=null){
                        Glide.with(MainActivity.this).load(value).into(ProfilePic);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });*/
            //  DataPIC.keepSynced(true);
         //   Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            //String uid = user.getUid();
           // String picURL = photoUrl.toString();
           // Log.d(TAG, picURL);
           // Glide.with(MainActivity.this).load(picURL).into(ProfilePic);
           // Picasso.with(MainActivity.this).load(picURL).into(ProfilePic);

                    Log.i(TAG,"User is signed in with Facebook");
                    String ProfileID= Profile.getCurrentProfile().getId().toString();
                    String ProfilePicUrl="http://graph.facebook.com/"+ProfileID+"+/picture?type=small";
                    // Picasso.with(this).load(ProfilePicUrl).into(ProfilePic);
                    ProfilePictureView profilePictureView;
                    profilePictureView = (ProfilePictureView) findViewById(R.id.friendProfilePicture);
                    profilePictureView.setProfileId(ProfileID);





              TextView UserNameTx = (TextView) findViewById(R.id.textView2UserName);
            UserNameTx.setText(UserName);

            loadcount=loadcount+1;
            ConnectingView();
            SaveData();
        }
    }


    public void nulll(View v) {
    }

    // Download Data
    public void DownloadToken() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Token = database.getReference("app/users/" + UserUID + "/access/tokens/token");
        Token.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Token Downloaded");
                if (value != null) {
                    UserToken = value;
                    loadcount=loadcount+1;
                    ConnectingView();
                    SaveData();
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }


    public void ok(View v) {
        View Note = (View) findViewById(R.id.ViewNote);
        Note.setVisibility(View.GONE);
    }

    public void ConnectingView(){
if(loadcount==TotalLoadCount){
    LinearLayout ViewConnecting=(LinearLayout)findViewById(R.id.connectingView);
    ViewConnecting.setVisibility(View.INVISIBLE);
}


    }

    @Override
    public void onBackPressed() {

        if(mInterstitialAd.isLoaded()){

            mInterstitialAd.show();

        }else{
            Log.d("TAG", "The interstitial wasn't loaded yet.");
            super.onBackPressed();

            MainActivity.this.finish();

        }



         //  super.onBackPressed();

    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 //   AppStart();                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    finish();
                    Toast.makeText(MainActivity.this, "Please Grant The Permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }



    public void GetStarted(View v){
        View WebCard=(View)findViewById(R.id.webCard);
        WebCard.setVisibility(View.VISIBLE);
        WebView myWebView = (WebView) findViewById(R.id.WebView1);
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.loadUrl("https://ephrine.github.io/Titan-Health-Hub/getting-started/");

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

    public void SaveData(){
        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("UserData","Offline Data");
        if(UserName!=null){
        editor.putString("Username",UserName);
        }
        if (UserToken!=null){
        editor.putString("Token",UserToken);
        }
        editor.commit();

    }

    public void AdsLoad(){
        MobileAds.initialize(this,
                getString(R.string.ad_id));


        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Int Ads
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.int_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {

                MainActivity.this.finish();

                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {

                MainActivity.this.finish();

                // Code to be executed when when the interstitial ad is closed.
            }
        });

    }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

}
