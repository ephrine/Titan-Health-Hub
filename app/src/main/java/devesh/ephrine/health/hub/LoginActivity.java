package devesh.ephrine.health.hub;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {
    public String TAG = "Ephrine Health Hub";
    public CallbackManager mCallbackManager;
    public String UserUID;
    public String UserToken;

    public String Fname;
    public String Lname;
    public String EmailID;
    public String Gender;
    //public Uri profilePIC;
    public String LoginCheck="0";

    String Login = "1";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public static final String EXTRA_MESSAGE_FIRST_START= "devesh.ephrine.health.hub";
    public String FirstStart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
//printKeyHash();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && Login.equals("1")) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    FirstStart="0";

                    finish();
                    MainAct();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Login = "0";

                    View Splash=(View)findViewById(R.id.ViewSplash);
                    Splash.setVisibility(View.GONE);

                }
                // ...
            }
        };
        setContentView(R.layout.activity_login);


        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                View Loading = (View) findViewById(R.id.loading);
                Loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                View Loading = (View) findViewById(R.id.loading);
                Loading.setVisibility(View.GONE);
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                View Loading = (View) findViewById(R.id.loading);
                Loading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);
        mAuth.addAuthStateListener(mAuthListener);

       // FirebaseUser user = firebaseAuth.getCurrentUser();
        if (currentUser != null && Login.equals("1")) {
            // User is signed in
            Log.d(TAG, "Current User:" + currentUser.getUid());
            FirstStart="0";

            finish();
            MainAct();
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
            Login = "0";

            View Splash=(View)findViewById(R.id.ViewSplash);
            Splash.setVisibility(View.GONE);

        }


    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            UserUID = user.getUid();
                            Log.d(TAG, "FB Login:" + user.getDisplayName());
                            //MainAct();
                            AccCheck();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void MainAct() {
/*
        if(FirstStart=="1" || FirstStart.equals("1")){

            Intent ab = new Intent(this, MainActivity.class);
           // ab.putExtra(EXTRA_MESSAGE_FIRST_START, "1");
            startActivity(ab);
            finish();

        }else{
            Intent ab = new Intent(this, MainActivity.class);
            //ab.putExtra(EXTRA_MESSAGE_FIRST_START, "0");
            startActivity(ab);
            finish();

        } */

if(LoginCheck.equals("0")) {
    Intent ab = new Intent(this, MainActivity.class);
    //  ab.putExtra(EXTRA_MESSAGE_FIRST_START, "0");
    finish();
    startActivity(ab);
    LoginCheck = "1";
}else{
    LoginActivity.this.finish();
}

    }


    public void AccCheck() {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference NewUserData = database.getReference("app/users/" + UserUID + "/new");
// Read from the database
        NewUserData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "new user Value is: " + value);
                if (value != null) {
                    FirstStart="1";
                    MainAct();
                } else {
                    FirstStart="1";
                    CreateAccount();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        //myRef.setValue("Hello, World!");

    }

    public void CreateAccount() {

        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("LoginActivity", response.toString());
                        try {

                            String email = object.getString("email");
                            String UserGender = object.getString("gender");
                            String FBlink = object.getString("link");
                            String FName = object.getString("first_name");
                            String LName = object.getString("last_name");
                            String ProfilePic = object.getString("picture");
                            // String FBDOB = object.getString("birthday"); // 01/31/1980 format

                            Log.d("FB Email", email);
                            Log.d("FB Gender", UserGender);
                            Log.d("FB Profile Pic URL", ProfilePic);

                            //     Log.d("FB link", FBlink);


                            EmailID = email;
                            Gender = UserGender;
                            Fname = FName;
                            Lname = LName;
                            String ProfileID= Profile.getCurrentProfile().getId().toString();
                            String ProfilePicUrl="http://graph.facebook.com/"+ProfileID+"/picture?type=large&width=720&height=720";



                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                            DatabaseReference NewUserData = database.getReference("app/users/" + UserUID + "/new");
                            NewUserData.setValue("0");

                            // User Name
                            DatabaseReference UserNameData = database.getReference("app/users/" + UserUID + "/name");
                            UserNameData.setValue(Fname + " " + Lname);

                            // First Name
                            DatabaseReference FNameData = database.getReference("app/users/" + UserUID + "/fname");
                            FNameData.setValue(Fname);

                            // Last Name
                            DatabaseReference LNameData = database.getReference("app/users/" + UserUID + "/lname");
                            LNameData.setValue(Lname);

                            // Gender
                            DatabaseReference GenderData = database.getReference("app/users/" + UserUID + "/gender");
                            GenderData.setValue(Gender);

                            // Email ID
                            DatabaseReference EmailData = database.getReference("app/users/" + UserUID + "/emailID");
                            EmailData.setValue(EmailID);

                            //Data Total
                            DatabaseReference TotalMR = database.getReference("app/users/" + UserUID + "/data/MedicalRecords/total");
                            TotalMR.setValue("1");

                            DatabaseReference TotalPs = database.getReference("app/users/" + UserUID + "/data/Prescriptions/total");
                            TotalPs.setValue("1");

                            DatabaseReference TotalBills = database.getReference("app/users/" + UserUID + "/data/MedicalBills/total");
                            TotalBills.setValue("1");

                            DatabaseReference StorageQuota = database.getReference("app/users/" + UserUID + "/settings/storage/files");
                            StorageQuota.setValue("0");

                            DatabaseReference FBPic = database.getReference("app/users/" + UserUID + "/pic");
                            FBPic.setValue(ProfilePicUrl);



                            GenerateToken();


                        } catch (JSONException e) {
                            Log.e(TAG, "unexpected JSON exception", e);
                            // Do something to recover ... or kill the app.
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name,last_name,email,gender,link,picture");
        request.setParameters(parameters);
        request.executeAsync();

        Profile profile = Profile.getCurrentProfile();
        System.out.println(profile.getFirstName());
        //System.out.println(profile.getId());
        Log.d("FB id", profile.getId());


    }

    public void GenerateToken() {

        //Create Access ID
        Random rand = new Random();
        int n1 = rand.nextInt(9);
        String N1 = String.valueOf(n1);
        int n2 = rand.nextInt(9);
        String N2 = String.valueOf(n2);
        int n3 = rand.nextInt(9);
        String N3 = String.valueOf(n3);
        int n4 = rand.nextInt(9);
        String N4 = String.valueOf(n4);
        int n5 = rand.nextInt(9);
        String N5 = String.valueOf(n5);
        int n6 = rand.nextInt(9);
        String N6 = String.valueOf(n6);
        int n7 = rand.nextInt(9);
        String N7 = String.valueOf(n7);
        int n8 = rand.nextInt(9);
        String N8 = String.valueOf(n8);
        String no = String.valueOf(N1 + N2 + N3 + N4 + N5 + N6 + N7 + N8);
        UserToken = no;
        Log.d(TAG, "New Token Generated: " + UserToken);
        TokenValidation();


    }

    public void TokenValidation() {

        // Write a message to the database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference Tcheck = database.getReference("app/TokenBucket/" + UserToken + "/UID");
        // Read from the database
        Tcheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                //Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    //Token already exhist, recreate it
                    GenerateToken();
                } else {

                    DatabaseReference Token = database.getReference("app/users/" + UserUID + "/access/tokens/token");
                    DatabaseReference TokenBucket = database.getReference("app/TokenBucket/" + UserToken + "/UID");
                    Token.setValue(UserToken);
                    TokenBucket.setValue(UserUID);
                    MainAct();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }

   private void printKeyHash(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "devesh.ephrine.health.hub",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("--------------------", "----------");
                Log.d("KeyHash FB 1:---", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Log.d("--------------------", "----------");

            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash FB 2:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash FB 3:", e.toString());
        }
    }



    public void nulll(View v) {
    }

    public  void AppPolicy(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://ephrine.blogspot.com/p/privacy-policy.html"));
        startActivity(intent);
    }

}
