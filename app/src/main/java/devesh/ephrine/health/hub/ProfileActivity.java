package devesh.ephrine.health.hub;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;


public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int READ_REQUEST_CODE = 42;
    public Uri uri = null;
    public Uri fileuri = null;
    public UploadTask uploadTask;

    public String TAG = "Ephrine Health Hub :";
    public String UserUID;
    public String EmailID="0";
    public String UserName="0";
    public String FName="0";
    public String LName="0";
    public String Age="0";
    public String DOB="0";
    public String Weight="0";
    public String Height="0";
    public String BloodGroup="0";
    public String Phno="0";
    public String Landlineno="0";
    public String Address="0";
    public Uri PicURI;
    public int load = 0;
    public String PicDownloadURL;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public String scr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);
scr="h";
        View loading = (View) findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && scr.equals("h")) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    UserUID = user.getUid();
                    UserName = user.getDisplayName();
                    EmailID = user.getEmail();
                    PicURI = user.getPhotoUrl();
                    GetUSerData();

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


    }


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

    public void GetUSerData() {

        TextView TxEmail = (TextView) findViewById(R.id.textView16EmailID);
        TextView TxUsername = (TextView) findViewById(R.id.textView16UserName);
        final TextView TxAge = (TextView) findViewById(R.id.textView23Age);
        final TextView TxDOB = (TextView) findViewById(R.id.textView22DOB);
        final TextView TxWeight = (TextView) findViewById(R.id.textView25Weight);
        final TextView TxHeight = (TextView) findViewById(R.id.textView24Height);
        final TextView TxBlodgroup = (TextView) findViewById(R.id.textView26BloodGroup);
        final TextView TxPhno = (TextView) findViewById(R.id.textView29Phoneno);
        final TextView TxLandline = (TextView) findViewById(R.id.textView30Landline);
        final TextView TxAddress = (TextView) findViewById(R.id.textView28Address);
        final TextView TxFname = (TextView) findViewById(R.id.textView16Fname);
        final TextView TxLname = (TextView) findViewById(R.id.textView22Lname);

        ImageView PicIMG = (ImageView) findViewById(R.id.imageView15ProfilePic);

        TxEmail.setText(EmailID);
        TxUsername.setText(UserName);
        Glide.with(this).load(PicURI).into(PicIMG);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference DataAge = database.getReference("app/users/" + UserUID + "/bio/age");
        DatabaseReference DataDOB = database.getReference("app/users/" + UserUID + "/bio/dob");
        DatabaseReference DataWeight = database.getReference("app/users/" + UserUID + "/bio/weight");
        DatabaseReference DataHeight = database.getReference("app/users/" + UserUID + "/bio/height");
        DatabaseReference DataBlood = database.getReference("app/users/" + UserUID + "/bio/blood");
        DatabaseReference DataPhno = database.getReference("app/users/" + UserUID + "/bio/phno");
        DatabaseReference DataLandline = database.getReference("app/users/" + UserUID + "/bio/landlineno");
        DatabaseReference DataAddress = database.getReference("app/users/" + UserUID + "/bio/address");
        DatabaseReference DataFname = database.getReference("app/users/" + UserUID + "/fname");
        DatabaseReference DataLname = database.getReference("app/users/" + UserUID + "/lname");


        // Read from the database
        DataAge.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                load = load + 1;
                loadCount();
                if (value != null) {
                    Age = value;
                    TxAge.setText(Age);


                } else {

                    TxAge.setText("---");

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
DataAge.keepSynced(true);

        DataDOB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                load = load + 1;
                loadCount();

                if (value != null) {
                    DOB = value;
                    TxDOB.setText(DOB);

                } else {
                    TxDOB.setText("---");

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
DataDOB.keepSynced(true);

        DataWeight.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                load = load + 1;
                loadCount();
                if (value != null) {
                    Weight = value;
                    TxWeight.setText(Weight + " Kg");

                } else {
                    TxWeight.setText("---");

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
DataWeight.keepSynced(true);

        DataHeight.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                load = load + 1;
                loadCount();
                if (value != null) {
                    Height = value;
                    TxHeight.setText(Height + " inch");

                } else {
                    TxHeight.setText("---");

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
DataHeight.keepSynced(true);

        DataBlood.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                load = load + 1;
                loadCount();
                if (value != null) {
                    BloodGroup = value;
                    TxBlodgroup.setText(BloodGroup);

                } else {
                    TxBlodgroup.setText("---");

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
DataBlood.keepSynced(true);

        DataPhno.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                load = load + 1;
                loadCount();
                if (value != null) {
                    Phno = value;
                    TxPhno.setText(Phno);

                } else {
                    TxPhno.setText("---");

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
DataPhno.keepSynced(true);

        DataLandline.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                load = load + 1;
                loadCount();
                if (value != null) {
                    Landlineno = value;
                    TxLandline.setText(Landlineno);


                } else {
                    TxLandline.setText("---");

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
DataLandline.keepSynced(true);

        DataAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                load = load + 1;
                loadCount();
                if (value != null) {
                    Address = value;
                    TxAddress.setText(Address);

                } else {
                    TxAddress.setText("---");

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
DataAddress.keepSynced(true);

        DataFname.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                load = load + 1;
                loadCount();
                if (value != null) {
                    FName = value;
                    TxFname.setText(FName);

                } else {
                    TxFname.setText("---");

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
DataFname.keepSynced(true);

        DataLname.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                load = load + 1;
                loadCount();
                if (value != null) {
                    LName = value;
                    TxLname.setText(LName);

                } else {
                    TxLname.setText("---");

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
DataLname.keepSynced(true);


        LoadProfilePic();


    }

    public void signout(View v) {

        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        finish();
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);

    }

    public void loadCount() {
        if (load == 10) {
            View loading = (View) findViewById(R.id.loading);
            loading.setVisibility(View.GONE);
        } else {

        }
    }

    public void nulll(View v) {

    }

    public void editprof(View v) {
        setContentView(R.layout.edit_profile);
        scr="e";
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("-----");
        categories.add("A +ve");
        categories.add("A -ve");
        categories.add("B +ve");
        categories.add("B -ve");
        categories.add("AB +ve");
        categories.add("AB -ve");
        categories.add("O +ve");
        categories.add("O -ve");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        TextView TxEmail = (TextView) findViewById(R.id.textView16EmailID);
        TxEmail.setText("Email ID: " + EmailID);
        EditText ETFName = (EditText) findViewById(R.id.editText4FName);
        EditText ETLName = (EditText) findViewById(R.id.editText5Lname);
        EditText ETAge = (EditText) findViewById(R.id.editText5Age);
        EditText ETDOB = (EditText) findViewById(R.id.editText6DOB);
        EditText ETWeight = (EditText) findViewById(R.id.editText6Weight);
        EditText ETHeight = (EditText) findViewById(R.id.editText7Height);
        //  EditText ETBloodGroup = (EditText) findViewById(R.id.editText5Lname);
        EditText ETPhno = (EditText) findViewById(R.id.editText9Phno);
        EditText ETLandline = (EditText) findViewById(R.id.editText10Landline);
        EditText ETAddress = (EditText) findViewById(R.id.editText4Address);
LoadProfilePic();
        if (FName != null) {
            ETFName.setText(FName);
        }

        if (LName != null) {
            ETLName.setText(LName);
        }

        if (Age != null) {
            ETAge.setText(Age);
        }

        if (DOB != null) {
            ETDOB.setText(DOB);

        }
        if (Weight != null) {
            ETWeight.setText(Weight);
        }
        if (Height != null) {
            ETHeight.setText(Height);

        }
        if (Phno != null) {
            ETPhno.setText(Phno);

        }

        if (Landlineno != null) {
            ETLandline.setText(Landlineno);

        }
        if (Address != null) {
            ETAddress.setText(Address);

        }
        if (BloodGroup != null) {
            // ETBloodGroup.setText(BloodGroup);
        }
        //ETBloodGroup.setText(FName);

    }

    public void SaveProfile(View v) {
        EditText ETFName = (EditText) findViewById(R.id.editText4FName);
        EditText ETLName = (EditText) findViewById(R.id.editText5Lname);
        EditText ETAge = (EditText) findViewById(R.id.editText5Age);
        EditText ETDOB = (EditText) findViewById(R.id.editText6DOB);
        EditText ETWeight = (EditText) findViewById(R.id.editText6Weight);
        EditText ETHeight = (EditText) findViewById(R.id.editText7Height);
        EditText ETPhno = (EditText) findViewById(R.id.editText9Phno);
        EditText ETLandline = (EditText) findViewById(R.id.editText10Landline);
        EditText ETAddress = (EditText) findViewById(R.id.editText4Address);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference DataAge = database.getReference("app/users/" + UserUID + "/bio/age");
        DatabaseReference DataDOB = database.getReference("app/users/" + UserUID + "/bio/dob");
        DatabaseReference DataWeight = database.getReference("app/users/" + UserUID + "/bio/weight");
        DatabaseReference DataHeight = database.getReference("app/users/" + UserUID + "/bio/height");
        DatabaseReference DataBlood = database.getReference("app/users/" + UserUID + "/bio/blood");
        DatabaseReference DataPhno = database.getReference("app/users/" + UserUID + "/bio/phno");
        DatabaseReference DataLandline = database.getReference("app/users/" + UserUID + "/bio/landlineno");
        DatabaseReference DataAddress = database.getReference("app/users/" + UserUID + "/bio/address");
        DatabaseReference DataFname = database.getReference("app/users/" + UserUID + "/fname");
        DatabaseReference DataLname = database.getReference("app/users/" + UserUID + "/lname");

        DataFname.setValue(ETFName.getText().toString());
        DataLname.setValue(ETLName.getText().toString());
        DataAge.setValue(ETAge.getText().toString());
        DataDOB.setValue(ETDOB.getText().toString());
        DataWeight.setValue(ETWeight.getText().toString());
        DataHeight.setValue(ETHeight.getText().toString());
        DataPhno.setValue(ETPhno.getText().toString());
        DataLandline.setValue(ETLandline.getText().toString());
        DataAddress.setValue(ETAddress.getText().toString());
        DataBlood.setValue(BloodGroup);

        finish();
        Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
        startActivity(intent);
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public void LoadProfilePic(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
      //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        final ImageView ProfilePic = (ImageView) findViewById(R.id.imageViewProfilePic);

        DatabaseReference DataPIC= database.getReference("app/users/" + UserUID + "/bio/photo");
        DataPIC.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if(value!=null){
                    Glide.with(ProfileActivity.this).load(value).into(ProfilePic);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        DataPIC.keepSynced(true);
    }

    public void Pclose(View v) {
        finish();
        Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
        startActivity(intent);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void ChangeProfilePic(View v) {
        performFileSearch();

    }

    public void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("*/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().

            if (resultData != null) {
                uri = resultData.getData();
                Log.i(TAG, "Uri: " + uri.toString());

                String url = uri.toString();
                fileuri = uri;
                String extension = url.substring(url.lastIndexOf("."));
                Log.i(TAG, "file Format: " + extension);

                FirebaseStorage storage = FirebaseStorage.getInstance();
                // Get a non-default Storage bucket
                //  FirebaseStorage storage = FirebaseStorage.getInstance("gs://my-custom-bucket");
                // Create a storage reference from our app
                //   StorageReference storageRef = storage.getReference();
                //   StorageReference spaceRef = storageRef.child("app/users/"+UserUID+"/MedicalRecords/");

                StorageReference storageRef = storage.getReferenceFromUrl("gs://healthhub-fb351.appspot.com/app/users/" + UserUID + "/data/bio/Photo");


                StorageReference riversRef = storageRef.child("/" + uri.getLastPathSegment());
                uploadTask = riversRef.putFile(fileuri);
                Toast.makeText(ProfileActivity.this, "Uploading Bio Photo.... Please Wait ", Toast.LENGTH_LONG).show();

// Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        //  dialog.hide();
                        //finish();
                        Toast.makeText(ProfileActivity.this, "Failed to Upload Bio Photo... Try Again ", Toast.LENGTH_LONG).show();
                        ProgressBar progressbar = (ProgressBar) findViewById(R.id.progressBar3);
                        progressbar.setVisibility(View.GONE);
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Log.i(TAG, "Success for upload: " + downloadUrl);
                        PicDownloadURL = downloadUrl.toString();
                        // Write a message to the database
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference DataURL = database.getReference("app/users/" + UserUID + "/bio/photo");
                        DataURL.setValue(PicDownloadURL);
                        Toast.makeText(ProfileActivity.this, "Bio Photo Changed Successfully ", Toast.LENGTH_LONG).show();
                        ProgressBar progressbar = (ProgressBar) findViewById(R.id.progressBar3);
                        progressbar.setVisibility(View.GONE);
                        LoadProfilePic();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests") double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        int i = (int) progress;
                        // String pr = String.valueOf(i);
                        System.out.println(TAG + "Upload is " + progress + "% done");

                            ProgressBar progressbar = (ProgressBar) findViewById(R.id.progressBar3);
                        progressbar.setVisibility(View.VISIBLE);
                          progressbar.setProgress(i);
                        //dialog.show();
                    }
                });


            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        if (item.equals("A +ve")) {
            BloodGroup = "A +ve";
        }
        if (item.equals("A -ve")) {
            BloodGroup = "A -ve";
        }
        if (item.equals("B +ve")) {
            BloodGroup = "B +ve";
        }
        if (item.equals("B -ve")) {
            BloodGroup = "B -ve";
        }
        if (item.equals("AB +ve")) {
            BloodGroup = "AB +ve";
        }
        if (item.equals("AB -ve")) {
            BloodGroup = "AB -ve";
        }
        if (item.equals("O +ve")) {
            BloodGroup = "O +ve";
        }
        if (item.equals("O -ve")) {
            BloodGroup = "O -ve";
        }


        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();


    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }

}
