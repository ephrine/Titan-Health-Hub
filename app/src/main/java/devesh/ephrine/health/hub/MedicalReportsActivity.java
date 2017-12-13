package devesh.ephrine.health.hub;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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

import java.util.Calendar;

public class MedicalReportsActivity extends AppCompatActivity {
    private static final int READ_REQUEST_CODE = 42;
    private static String LOG_TAG = "RecyclerViewActivity";
    public View uploadView;
    public FloatingActionButton uploadFAB;
    public UploadTask uploadTask;
    public Uri uri = null;
    public Uri fileuri = null;
    public String TAG = "Ephrine Health Hub :";
    public String UserUID;
    public String RTitle;
    public String RDesc;
    public String DownloadURL;
    public String no;
    public String Durl;
    public String SlotNo;
    public String FileStorageVal;
public int TotalFilStorageVal;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private StorageReference mStorageRef;

    private long enqueue;
    private DownloadManager dm;

    public String PurchaseStatus;

    public String ExpStrDate;
    public String ExpStrMonth;
    public String ExpStrYear;

    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_reports);

        String FVal=getString(R.string.File_Storage_Default);

        TotalFilStorageVal=Integer.parseInt(FVal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AdsLoad();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    UserUID = user.getUid();

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            UserUID = user.getUid();


            // Write a message to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference GetTotal = database.getReference("app/users/" + UserUID + "/data/MedicalRecords/total");
            GetTotal.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "Total Value is: " + value);
                    no = value;

                    CreateView();


                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });

            DatabaseReference TStorageQuota = database.getReference("app/users/" + UserUID + "/settings/storage/files");
            TStorageQuota.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "Total files " + value);
                    if(value!=null){
                        FileStorageVal=value;
                        Log.i(TAG, "File storage "+FileStorageVal);

                    }
                    else {
                        FileStorageVal="0";

                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });


        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }


//Download Manager
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(enqueue);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c
                                .getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c
                                .getInt(columnIndex)) {
                            Intent i = new Intent();
                            i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
                            startActivity(i);
                            Toast.makeText(MedicalReportsActivity.this, "Download Complete",
                                    Toast.LENGTH_SHORT).show();

                        }
                        if(DownloadManager.STATUS_FAILED==16){
                            Toast.makeText(MedicalReportsActivity.this, "Download Failed... Check your Internet Connection",
                                    Toast.LENGTH_SHORT).show();

                        }
                        if(DownloadManager.STATUS_PAUSED==4){
                            Toast.makeText(MedicalReportsActivity.this, "Download Failed... Check your Internet Connection",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        UserUID=currentUser.getUid().toString();
        DatabaseReference TStorageQuota = database.getReference("app/users/" + UserUID + "/settings/storage/files");
        TStorageQuota.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Total files " + value);
                if(value!=null){
                    FileStorageVal=value;
                    Log.i(TAG, "File storage "+FileStorageVal);
                    FindExpiry();

                }
                else {
                    FileStorageVal="1";

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



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

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void CreateView() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Drawable DOCDrawable = getResources().getDrawable(R.drawable.medical512);
                Drawable DownloadDrawable = getResources().getDrawable(R.drawable.download);
                Drawable DeleteDrawable = getResources().getDrawable(R.drawable.delete);

                LinearLayout myLayout = (LinearLayout) findViewById(R.id.LLCardView);
                View.OnClickListener clicks = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String S = String.valueOf(v.getId());
                        SlotNo = S;
                        System.out.println(S);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();

                        DatabaseReference urldata = database.getReference("app/users/" + UserUID + "/data/MedicalRecords/" + S + "/url");
                        urldata.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                String value = dataSnapshot.getValue(String.class);
                                Log.d(TAG, "Download URL is: " + value);
                                if (value != null) {
                                    Durl = value;

                                    //  String TextID = "btn"+ S;
                                    //    int resID = getResources().getIdentifier(buttonID, "id", getPackageName());


                                    menuDownload();
                                    View LLMenu = (View) findViewById(R.id.mrLLMenu);
                                    LLMenu.setVisibility(View.VISIBLE);


                                    //   Intent intent = new Intent(Intent.ACTION_VIEW);
                                    // intent.setData(Uri.parse(value)); //Google play store
                                    // startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.w(TAG, "Failed to read value.", error.toException());
                            }
                        });


                    }
                };

                //Download Button


                int t = Integer.parseInt(no);
if(t==1){
    View Loading = (View) findViewById(R.id.mrloading);
    Loading.setVisibility(View.GONE);

}
                for (int j = 1; j < t; j++) {

                    final CardView mcard = new CardView(MedicalReportsActivity.this);
                    mcard.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    mcard.setCardElevation(10);
                    mcard.setUseCompatPadding(true);
                    mcard.setId(j + 0);
                    mcard.setOnClickListener(clicks);
                    //mcard.setText("Button"+(i+1));
                    // btn.setLayoutParams(lprams);


                    myLayout.addView(mcard);

                    LinearLayout LL = new LinearLayout(MedicalReportsActivity.this);
                    LL.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    LL.setOrientation(LinearLayout.HORIZONTAL);
                    LL.setPadding(30, 20, 30, 20);
                    mcard.addView(LL);

                    ImageView img = new ImageView(MedicalReportsActivity.this);
                    img.setImageDrawable(DOCDrawable);
                    // img.requestLayout();
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
                    img.setLayoutParams(layoutParams);
                    ///  img.getLayoutParams().height = 20;
                    //  img.getLayoutParams().width = 20;
                    LL.addView(img);

                    LinearLayout LL1 = new LinearLayout(MedicalReportsActivity.this);
                    LL1.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    LL1.setOrientation(LinearLayout.VERTICAL);
                    LL.addView(LL1);

                    final TextView tx = new TextView(MedicalReportsActivity.this);
                    tx.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));


                    tx.setTypeface(null, Typeface.BOLD);
                    tx.setTextSize(18);
                    tx.setText("Loading....");
                    LL1.addView(tx);


                    final TextView tx1 = new TextView(MedicalReportsActivity.this);
                    tx1.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    tx1.setText("Loading....");
                    LL1.addView(tx1);


                    // Write a message to the database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final int idj = j;
                    DatabaseReference title = database.getReference("app/users/" + UserUID + "/data/MedicalRecords/" + j + "/title");
                    title.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            String value = dataSnapshot.getValue(String.class);
                            Log.d(TAG, "Value is: " + value);
                            if (value != null) {
                                tx.setText(value);
                                View Loading = (View) findViewById(R.id.mrloading);
                                Loading.setVisibility(View.GONE);

                            } else {

                                CardView cv = (CardView) findViewById(idj);
                                cv.setVisibility(View.GONE);
                                View Loading = (View) findViewById(R.id.mrloading);
                                Loading.setVisibility(View.GONE);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });

                    DatabaseReference desc = database.getReference("app/users/" + UserUID + "/data/MedicalRecords/" + j + "/desc");
                    desc.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            String value = dataSnapshot.getValue(String.class);
                            Log.d(TAG, "Value is: " + value);
                            if (value != null) {
                                tx1.setText(value);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });


                }


            }
        });


    }

    public void LayoutDownload(View v) {

        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(Durl));
        enqueue = dm.enqueue(request);
        Toast.makeText(MedicalReportsActivity.this, "Download in Progress",
                Toast.LENGTH_SHORT).show();

        //   Intent intent = new Intent(Intent.ACTION_VIEW);
     //   intent.setData(Uri.parse(Durl));
      //  startActivity(intent);
    }


    public void LayoutDelete(View v) {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference del = database.getReference("app/users/" + UserUID + "/data/MedicalRecords/" + SlotNo);
        del.removeValue();


        mStorageRef = FirebaseStorage.getInstance().getReference();
        FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance().getReference().getStorage();;
        StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl(Durl);
        final Intent ab = new Intent(this, MedicalReportsActivity.class);
        final DatabaseReference StorageQuota = database.getReference("app/users/" + UserUID + "/settings/storage/files");

        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Log.d(TAG, "onSuccess: deleted file");

                int f=Integer.valueOf(FileStorageVal);
                f=f-1;
                StorageQuota.setValue(String.valueOf(f));

                ab.putExtra("devesh.ephrine.health.hub", FileStorageVal);

                finish();
                startActivity(ab);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.d(TAG, "onFailure: did not delete file");
            }
        });



    }

    public void menuDownload() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        final TextView t1 = (TextView) findViewById(R.id.textView16Title);
        final TextView t2 = (TextView) findViewById(R.id.textView17Desc);

        t1.setText("Loading...");
        t2.setText("");

        DatabaseReference title = database.getReference("app/users/" + UserUID + "/data/MedicalRecords/" + SlotNo + "/title");
        title.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    t1.setText(value);

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference desc = database.getReference("app/users/" + UserUID + "/data/MedicalRecords/" + SlotNo + "/desc");
        desc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    t2.setText(value);

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }

    public void closeRec(View v) {
        View LLMenu = (View) findViewById(R.id.mrLLMenu);
        LLMenu.setVisibility(View.GONE);
    }

    public void uploadFab(View v) {
        performFileSearch();


    }

    public void closeUpload(View v) {
        uploadView = (View) findViewById(R.id.UploadView);
        uploadView.setVisibility(View.GONE);
        uploadFAB = (FloatingActionButton) findViewById(R.id.MedUploadFABbutton);
        uploadFAB.setVisibility(View.VISIBLE);

    }

    public void nulll(View v) {
    }

    public void StartUpload(View v) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        EditText ETtitle = (EditText) findViewById(R.id.editTextTitle);
        EditText ETdesc = (EditText) findViewById(R.id.editTextDesc);

        RTitle = ETtitle.getText().toString();
        RDesc = ETdesc.getText().toString();

        LinearLayout LLView = (LinearLayout) findViewById(R.id.LLView);
        LinearLayout LLupload = (LinearLayout) findViewById(R.id.LLUploading);
        LLView.setVisibility(View.GONE);
        LLupload.setVisibility(View.VISIBLE);


        // Get a non-default Storage bucket
        //  FirebaseStorage storage = FirebaseStorage.getInstance("gs://my-custom-bucket");
// Create a storage reference from our app
        //   StorageReference storageRef = storage.getReference();
        //   StorageReference spaceRef = storageRef.child("app/users/"+UserUID+"/MedicalRecords/");

        StorageReference storageRef = storage.getReferenceFromUrl("gs://healthhub-fb351.appspot.com/app/users/" + UserUID + "/data/MedicalRecords");


        StorageReference riversRef = storageRef.child("/" + uri.getLastPathSegment());
        uploadTask = riversRef.putFile(fileuri);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                //  dialog.hide();
                //finish();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.i(TAG, "Success for upload: " + downloadUrl);
                DownloadURL = downloadUrl.toString();
                AddDataBase();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                int i = (int) progress;
                // String pr = String.valueOf(i);
                System.out.println(TAG + "Upload is " + progress + "% done");
                ProgressBar progressbar = (ProgressBar) findViewById(R.id.progressBar);
                progressbar.setProgress(i);
                //dialog.show();
            }
        });
    }


    public void CancelUpload(View v) {
        // Cancel the upload
        uploadTask.cancel();
        finish();
        Intent ab = new Intent(this, MedicalReportsActivity.class);
        startActivity(ab);
    }

    public void UploadResume(View v) {
        // Cancel the upload
        uploadTask.resume();
        Button pauseBT = (Button) findViewById(R.id.buttonPause);
        Button resumeBT = (Button) findViewById(R.id.buttonResume);
        pauseBT.setVisibility(View.VISIBLE);
        resumeBT.setVisibility(View.GONE);

    }

    public void PauseUpload(View v) {
        // Cancel the upload
        uploadTask.pause();
        Button pauseBT = (Button) findViewById(R.id.buttonPause);
        Button resumeBT = (Button) findViewById(R.id.buttonResume);
        pauseBT.setVisibility(View.GONE);
        resumeBT.setVisibility(View.VISIBLE);
    }

    public void AddDataBase() {
        int Tno = Integer.parseInt(no);
        Tno = Tno + 1;

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Title
        DatabaseReference titleData = database.getReference("app/users/" + UserUID + "/data/MedicalRecords/" + no + "/title");
        titleData.setValue(RTitle);

        // desc
        DatabaseReference descData = database.getReference("app/users/" + UserUID + "/data/MedicalRecords/" + no + "/desc");
        if (RDesc != null) {
            descData.setValue(RDesc);
        } else {
            descData.setValue("null");
        }

        //Download URL
        DatabaseReference downloadURLData = database.getReference("app/users/" + UserUID + "/data/MedicalRecords/" + no + "/url");
        downloadURLData.setValue(DownloadURL);

        // Add total
        DatabaseReference Total = database.getReference("app/users/" + UserUID + "/data/MedicalRecords/total");
        Total.setValue(String.valueOf(Tno));

        int t=Integer.valueOf(FileStorageVal);
        t=t+1;
        FileStorageVal=String.valueOf(t);
        Log.i(TAG, "File storage "+FileStorageVal);

        DatabaseReference StorageQuota = database.getReference("app/users/" + UserUID + "/settings/storage/files");
        StorageQuota.setValue(FileStorageVal);


        finish();
        Intent ab = new Intent(this, MedicalReportsActivity.class);
        ab.putExtra("devesh.ephrine.health.hub", FileStorageVal);

        startActivity(ab);


    }


    /**
     * Fires an intent to spin up the "file chooser" UI and select an image.
     */
    public void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        //Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

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
        Drawable PDFDrawable = getResources().getDrawable(R.drawable.pdf1);
        Drawable DOCDrawable = getResources().getDrawable(R.drawable.pdf1);
        ImageView FileTypeImg=(ImageView)findViewById(R.id.imageViewFileType);
        //Drawable PPTDrawable = getResources().getDrawable(R.drawable.pdf1);

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
              //  fileuri = uri;
                String extension = url.substring(url.lastIndexOf("."));
                Log.i(TAG, "file Format: " + extension);

            //    uploadView = (View) findViewById(R.id.UploadView);
            //    uploadView.setVisibility(View.VISIBLE);
                uploadFAB = (FloatingActionButton) findViewById(R.id.MedUploadFABbutton);
                uploadFAB.setVisibility(View.INVISIBLE);




                if (extension.equals(".pdf")) {
                    Log.i(TAG, "Accepted " + extension);
                    fileuri = uri;
                    uploadView = (View) findViewById(R.id.UploadView);
                    uploadView.setVisibility(View.VISIBLE);
                    uploadFAB = (FloatingActionButton) findViewById(R.id.MedUploadFABbutton);
                    uploadFAB.setVisibility(View.INVISIBLE);



                    Uri returnUri = fileuri;
                    Cursor returnCursor =
                            getContentResolver().query(returnUri, null, null, null, null);
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                    returnCursor.moveToFirst();
                    TextView nameView = (TextView) findViewById(R.id.textViewFileName);
                    TextView sizeView = (TextView) findViewById(R.id.textViewFileSize);
                    nameView.setText(returnCursor.getString(nameIndex));
                    sizeView.setText(Long.toString(returnCursor.getLong(sizeIndex)));
                    FileTypeImg.setImageDrawable(PDFDrawable);

                    EditText ETtitle=(EditText)findViewById(R.id.editTextTitle);
                    ETtitle.setText(returnCursor.getString(nameIndex));




                }
                //PowerPoint
          /*      else if (extension.equals(".pptx")) {
                    Log.i(TAG, "Accepted " + extension);
                    fileuri = uri;
                    uploadView = (View) findViewById(R.id.UploadView);
                    uploadView.setVisibility(View.VISIBLE);
                    uploadFAB = (FloatingActionButton) findViewById(R.id.MedUploadFABbutton);
                    uploadFAB.setVisibility(View.INVISIBLE);

                } else if (extension.equals(".ppt")) {
                    Log.i(TAG, "Accepted " + extension);

                    fileuri = uri;
                    uploadView = (View) findViewById(R.id.UploadView);
                    uploadView.setVisibility(View.VISIBLE);
                    uploadFAB = (FloatingActionButton) findViewById(R.id.MedUploadFABbutton);
                    uploadFAB.setVisibility(View.INVISIBLE);


                } else if (extension.equals(".ppsx")) {
                    Log.i(TAG, "Accepted " + extension);

                    fileuri = uri;
                    uploadView = (View) findViewById(R.id.UploadView);
                    uploadView.setVisibility(View.VISIBLE);
                    uploadFAB = (FloatingActionButton) findViewById(R.id.MedUploadFABbutton);
                    uploadFAB.setVisibility(View.INVISIBLE);


                } else if (extension.equals(".odp")) {
                    Log.i(TAG, "Accepted " + extension);
                    fileuri = uri;
                    uploadView = (View) findViewById(R.id.UploadView);
                    uploadView.setVisibility(View.VISIBLE);
                    uploadFAB = (FloatingActionButton) findViewById(R.id.MedUploadFABbutton);
                    uploadFAB.setVisibility(View.INVISIBLE);


                } else if (extension.equals(".pps")) {
                    Log.i(TAG, "Accepted " + extension);
                    fileuri = uri;

                    uploadView = (View) findViewById(R.id.UploadView);
                    uploadView.setVisibility(View.VISIBLE);
                    uploadFAB = (FloatingActionButton) findViewById(R.id.MedUploadFABbutton);
                    uploadFAB.setVisibility(View.INVISIBLE);

                } */
                // Word
                else if (extension.equals(".docx")) {
                    Log.i(TAG, "Accepted " + extension);
                    fileuri = uri;
                    uploadView = (View) findViewById(R.id.UploadView);
                    uploadView.setVisibility(View.VISIBLE);
                    uploadFAB = (FloatingActionButton) findViewById(R.id.MedUploadFABbutton);
                    uploadFAB.setVisibility(View.INVISIBLE);

                    Uri returnUri = fileuri;
                    Cursor returnCursor =
                            getContentResolver().query(returnUri, null, null, null, null);
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                    returnCursor.moveToFirst();
                    TextView nameView = (TextView) findViewById(R.id.textViewFileName);
                    TextView sizeView = (TextView) findViewById(R.id.textViewFileSize);
                    nameView.setText(returnCursor.getString(nameIndex));
                    sizeView.setText(Long.toString(returnCursor.getLong(sizeIndex)));
                    FileTypeImg.setImageDrawable(DOCDrawable);
                    EditText ETtitle=(EditText)findViewById(R.id.editTextTitle);
                    ETtitle.setText(returnCursor.getString(nameIndex));



                } else if (extension.equals(".doc")) {
                    Log.i(TAG, "Accepted " + extension);
                    fileuri = uri;

                    uploadView = (View) findViewById(R.id.UploadView);
                    uploadView.setVisibility(View.VISIBLE);
                    uploadFAB = (FloatingActionButton) findViewById(R.id.MedUploadFABbutton);
                    uploadFAB.setVisibility(View.INVISIBLE);

                    Uri returnUri = fileuri;
                    Cursor returnCursor =
                            getContentResolver().query(returnUri, null, null, null, null);
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                    returnCursor.moveToFirst();
                    TextView nameView = (TextView) findViewById(R.id.textViewFileName);
                    TextView sizeView = (TextView) findViewById(R.id.textViewFileSize);
                    nameView.setText(returnCursor.getString(nameIndex));
                    sizeView.setText(Long.toString(returnCursor.getLong(sizeIndex)));
                    FileTypeImg.setImageDrawable(DOCDrawable);
                    EditText ETtitle=(EditText)findViewById(R.id.editTextTitle);
                    ETtitle.setText(returnCursor.getString(nameIndex));


                } else if (extension.equals(".odt")) {
                    Log.i(TAG, "Accepted " + extension);
                    fileuri = uri;
                    uploadView = (View) findViewById(R.id.UploadView);
                    uploadView.setVisibility(View.VISIBLE);
                    uploadFAB = (FloatingActionButton) findViewById(R.id.MedUploadFABbutton);
                    uploadFAB.setVisibility(View.INVISIBLE);

                    Uri returnUri = fileuri;
                    Cursor returnCursor =
                            getContentResolver().query(returnUri, null, null, null, null);
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                    returnCursor.moveToFirst();
                    TextView nameView = (TextView) findViewById(R.id.textViewFileName);
                    TextView sizeView = (TextView) findViewById(R.id.textViewFileSize);
                    nameView.setText(returnCursor.getString(nameIndex));
                    sizeView.setText(Long.toString(returnCursor.getLong(sizeIndex)));
                    FileTypeImg.setImageDrawable(DOCDrawable);
                    EditText ETtitle=(EditText)findViewById(R.id.editTextTitle);
                    ETtitle.setText(returnCursor.getString(nameIndex));



                } else if (extension.equals(".docm")) {
                    Log.i(TAG, "Accepted " + extension);
                    fileuri = uri;
                    uploadView = (View) findViewById(R.id.UploadView);
                    uploadView.setVisibility(View.VISIBLE);
                    uploadFAB = (FloatingActionButton) findViewById(R.id.MedUploadFABbutton);
                    uploadFAB.setVisibility(View.INVISIBLE);

                    Uri returnUri = fileuri;
                    Cursor returnCursor =
                            getContentResolver().query(returnUri, null, null, null, null);
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                    returnCursor.moveToFirst();
                    TextView nameView = (TextView) findViewById(R.id.textViewFileName);
                    TextView sizeView = (TextView) findViewById(R.id.textViewFileSize);
                    nameView.setText(returnCursor.getString(nameIndex));
                    sizeView.setText(Long.toString(returnCursor.getLong(sizeIndex)));
                    FileTypeImg.setImageDrawable(DOCDrawable);
                    EditText ETtitle=(EditText)findViewById(R.id.editTextTitle);
                    ETtitle.setText(returnCursor.getString(nameIndex));



                } else {
                    Log.i(TAG, "not Accepted " + extension);
                    Toast.makeText(MedicalReportsActivity.this, "Oops!! Only PDF and Word File Formats are allowed",
                            Toast.LENGTH_LONG).show();
                }


            }
        }
    }

    public void pickFile(View v) {
        performFileSearch();
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }
    public void StorageQuotaCheck(){
        if(FileStorageVal!=null){

            FloatingActionButton UploadFab=(FloatingActionButton)findViewById(R.id.MedUploadFABbutton);
            LinearLayout LLErro=(LinearLayout)findViewById(R.id.LLViewStorageSpaceError1);

            int FilVal=Integer.parseInt(FileStorageVal);
            if(FilVal>=TotalFilStorageVal){

                if(PurchaseStatus.equals("F")){
                    //Quota full+ Acc Expired
                    Log.i(TAG, "Quota full + Acc Expired");

                    UploadFab.setVisibility(View.GONE);
                    LLErro.setVisibility(View.VISIBLE);

                }else if(PurchaseStatus.equals("T")) {
                    //Quota full+ Premium Acc
                    Log.i(TAG, "Quota full+ Premium Acc");
                    UploadFab.setVisibility(View.VISIBLE);
                    LLErro.setVisibility(View.GONE);

                }else {
                    UploadFab.setVisibility(View.GONE);
                    LLErro.setVisibility(View.VISIBLE);
                    //Qouta full+ free acc !!
                    Log.i(TAG, "Quota full+ free acc !!");

                }


            }else{
// Quota not Full
                UploadFab.setVisibility(View.VISIBLE);
                LLErro.setVisibility(View.GONE);

            }




        }
    }

    public void FindExpiry(){

        Calendar c = Calendar.getInstance();
        // Expiry Date
        int Tdt=c.get(Calendar.DATE);
        int Tmonth=c.get(Calendar.MONTH);
        Tmonth=Tmonth+1;
        int Tyear=c.get(Calendar.YEAR);
        if(ExpStrDate!=null && ExpStrMonth!=null && ExpStrYear!=null){

            int ExpDate=Integer.parseInt(ExpStrDate);
            int ExpMonth=Integer.parseInt(ExpStrMonth);
            int ExpYear=Integer.parseInt(ExpStrYear);
            if(Tyear>=ExpYear){
                if(Tmonth>=ExpMonth){
                    if(Tdt>=ExpDate){
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference BuyData = database.getReference("app/users/" + UserUID + "/settings/purchase/buy");
                        BuyData.setValue("F");
                        PurchaseStatus="F";
                        StorageQuotaCheck();

                    }
                }
            }else {
                PurchaseStatus="T";
                StorageQuotaCheck();

            }

        }else{
            PurchaseStatus="F";
            StorageQuotaCheck();
        }

    }

    public void AdsLoad(){
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.pdf:

                boolean isAppInstalled = appInstalledOrNot("com.adobe.scan.android");
                boolean isAppInstalledCamScanner = appInstalledOrNot("com.intsig.camscanner");
                boolean isAppInstalledOfficeLens = appInstalledOrNot("com.microsoft.office.officelens");
                boolean isAppInstalledAdobeReader = appInstalledOrNot("com.adobe.reader");


                if(isAppInstalled) {
                    //This intent will help you to launch if the package is already installed
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.adobe.scan.android");
                    if (launchIntent != null) {
                        startActivity(launchIntent);//null pointer check in case package name was not found
                    }

                }
                else if(isAppInstalledCamScanner) {
                    //This intent will help you to launch if the package is already installed
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.intsig.camscanner");
                    if (launchIntent != null) {
                        startActivity(launchIntent);//null pointer check in case package name was not found
                    }

                }
                else if(isAppInstalledOfficeLens) {
                    //This intent will help you to launch if the package is already installed
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.microsoft.office.officelens");
                    if (launchIntent != null) {
                        startActivity(launchIntent);//null pointer check in case package name was not found
                    }

                } else if(isAppInstalledAdobeReader) {
                    //This intent will help you to launch if the package is already installed
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.adobe.reader");
                    if (launchIntent != null) {
                        startActivity(launchIntent);//null pointer check in case package name was not found
                    }

                }
                else {
                    // Do whatever we want to do if application not installed
                    // For example, Redirect to play store
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=com.adobe.scan.android")); //Google play store
                    startActivity(intent);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }



}

