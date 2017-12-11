package devesh.ephrine.health.hub;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class ClientActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private AdView mAdView;


    private Menu menu;

    public String ClientToken;
    public String ClientUserUID;
    public String TAG = "Ephrine Health Hub :";

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
    private long enqueue;
    private DownloadManager dm;
    public int downloading=0;
    public String AccessDataTotal;




    // Health Chart Values------------
    public String microS = "\u00B5";

    public int BMselected = 0;
    public int BPselected = 0;
    public int SPselected = 0;
    public int LPselected = 0;
    public int CBCselected = 0;
    public int RPselected = 0;
    public int TPselected = 0;
    public int LivPselected = 0;
    public int SEselected = 0;
    public int VPselected = 0;
    public int IPselected = 0;
    public int EPselected = 0;


    // Body Measurements
    public String ValWeight;
    public String ValHeight;
    public String ValBMI;

    // Blood Pressure
    public String ValSystolic;
    public String ValDiastolic;

    // Sugar Panel
    public String ValSuagarFasting;
    public String ValSuagarRandom;
    public String ValHbA1c;

    // Lipid Panel
    public String ValTriglyce;
    public String ValTotalChole;
    public String ValHDL;
    public String ValLDL;
    public String ValVLDL;

    // Complete Blood Count
    public String ValHaemoglobin;
    public String ValWBCs;
    public String ValNeutrophils;
    public String ValEosinophiles;
    public String ValBasophils;
    public String ValMonocytes;
    public String ValLymphocytes;
    public String ValPlatelets;
    public String ValESR;
    public String ValHct;
    public String ValMCH;
    public String ValMCHC;
    public String ValMCV;
    public String ValRBC;
    public String ValRDW;


    // Renal Panel
    public String ValBloodUreaN2;
    public String ValSerumCretine;
    public String ValSerumUricAcid;

    // Thyroid Profile
    public String ValTSH;
    public String ValTT4;
    public String ValTT3;
    public String ValFT4;
    public String ValFT3;
    public String ValTg;
    public String ValTgAbAntiTg;

    // Liver Panel
    public String ValBilirubin;
    public String ValSGOT;
    public String ValSGPT;
    public String ValAlkalinePhosphatase;
    public String ValGammaGlutamylTransminase;

    // Serum Electrolytes
    public String ValNA;
    public String ValK;
    public String ValCa;
    public String ValCl;
    public String ValP;

    // Vitamin Panel
    public String ValD3;
    public String ValB12;

    // Iron Panel
    public String ValSeriumFe;
    public String ValSerumFerritin;
    public String ValTIBC;
    public String ValUIBC;

    // Enzyme Panel
    public String ValSerumLipase;
    public String ValSerumAmylase;
    public String ValLeptin;



    public int BMLoadCount = 0;
    public int BPLoadCount = 0;
    public int SPLoadCount = 0;
    public int LPLoadCount = 0;
    public int CBCLoadCount = 0;
    public int RPLoadCount = 0;
    public int TPLoadCount = 0;
    public int LivPLoadCount = 0;
    public int SELoadCount = 0;
    public int VPLoadCount = 0;
    public int IPLoadCount = 0;
    public int EPLoadCount = 0;
    public String userSCR;

    // Medical History values -------------
    public String clk;
    public String TextGD;
    public String TextKC;
    public String TextFH;
    public String TextEA;
    public String TextDA;
    public String TextFA;
    public int Load;

    // Medical Report Values
    private static final int READ_REQUEST_CODE = 42;
    private static String LOG_TAG = "RecyclerViewActivity";
    public View uploadView;
   //  public FloatingActionButton uploadFAB;
  //  public UploadTask uploadTask;
    //public Uri uri = null;
   // public Uri fileuri = null;
  //  public String RTitle;
   // public String RDesc;
  //  public String DownloadURL;
  //  public String no;
  //  public String Durl;
  //  public String SlotNo;
  //  public String FileStorageVal;
   // private StorageReference mStorageRef;

// Prescription Values 

 public FloatingActionButton uploadFAB;
    public UploadTask uploadTask;
    public Uri uri = null;
    public Uri fileuri = null;
    public String UserUID;
    public String RTitle;
    public String RDesc;
    public String DownloadURL;
    public String no;
    public String Durl;
    public String SlotNo;
    public String FileStorageVal;
    public String tempDesc;

    public String AppUserName;
    public String AppUserPicUrl;


    public ProgressDialog LoadingDialog;

    private NavigationView navigationView;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        AdsLoad();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    UserUID=user.getUid();
                    MenuLoadTotal();


                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };



        LoadingDialog = ProgressDialog.show(ClientActivity.this, "",
                "Loading...", true);
//LoadingDialog.dismiss();
        Intent intent = getIntent();
        ClientToken = intent.getStringExtra(AccessTokenActivity.EXTRA_MESSAGE_CLIENT_TOKEN);
      //  ClientUserUID=intent.getStringExtra(AccessTokenActivity.EXTRA_MESSAGE_CLIENT_USER_UID);
     //   Log.d(TAG, "UID Value is: " + ClientUserUID);
AppUserName=intent.getStringExtra(AccessTokenActivity.EXTRA_MESSAGE_APP_USER_NAME);
        AppUserPicUrl=intent.getStringExtra(AccessTokenActivity.EXTRA_MESSAGE_APP_USER_PIC_URL);
ClientUserUID=intent.getStringExtra(AccessTokenActivity.EXTRA_MESSAGE_CLIENT_USER_UID);
        Log.d(TAG, "UID Value is: " + ClientUserUID);
        Log.d(TAG, "Intent Name Receive Value is: " + AppUserName);
        Log.d(TAG, "Intent Pic URL Receive Value is: " + AppUserPicUrl);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
     //   DatabaseReference ClientTokenData = database.getReference("app/TokenBucket/" +ClientToken+"/UID");
// Read from the database

  /*      ClientTokenData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "UID Value is: " + value);
                if(value!=null){
                    ClientUserUID=value;
                    GetBio();
                    AddAccessRecord1();
                }else{
                    Toast.makeText(ClientActivity.this, "Access Token Invalid or incorrect",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

     //   NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView= (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



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
                            Toast.makeText(ClientActivity.this, "Download Complete",
                                    Toast.LENGTH_SHORT).show();
downloading=0;
                        }
                    }
                }
            }
        };

        if(downloading==1) {
            registerReceiver(receiver, new IntentFilter(
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }else{

        }

        GetBio();
        AddAccessRecord1();

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
    @Override
    public void onPause(){
        LoadingDialog.dismiss();
        super.onPause();
    }

    @Override
    public void onDestroy(){
        LoadingDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //ClientOut();
            finish();
          //  Intent ab = new Intent(ClientActivity.this, MainActivity.class);
          //  startActivity(ab);
            super.onBackPressed();
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.client, menu);
        //menu.getItem(R.id.action_settings).setTitle("Devesh");
        MenuItem favMenu=menu.findItem(R.id.fav);
        Drawable DOCDrawable = getResources().getDrawable(R.drawable.prescription512);
        if(isFavourite.equals("1")){
    //favMenu.setIcon();
}


        return true;
    }

   */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.fav) {
AddFav();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public int FavTotal;
    public String isFavourite="0";
    public String FavSlotNo;
    public int tempFavSlotNo;
    public void MenuLoadTotal(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference favTotal = database.getReference("app/users/" + UserUID + "/fav/total");
        favTotal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Fav Total: " + value);
                if(value!=null){
                    FavTotal=Integer.parseInt(value);
MenuLoadCheck();
                }else{
                    FavTotal=1;
                    isFavourite="0";
                    MenuLoadCheck();

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }


    public void MenuLoadCheck(){
        Log.d(TAG, "Load Menu Check");
        new Thread(new Runnable() {
            public void run() {
                Log.d(TAG, "In the Thread");

                for (int j = 0; j < FavTotal; j++) {
                    Log.d(TAG, "In the For Loop Thread");
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    tempFavSlotNo=j;

                    DatabaseReference favCheck = database.getReference("app/users/" + UserUID + "/fav/"+j+"/uid");
                    favCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            String value = dataSnapshot.getValue(String.class);
                            Log.d(TAG, "Value is: " + value);
                            if(value!=null){
                                if (value.equals(ClientUserUID)) {

                                    isFavourite = "1";
                                    Log.d(TAG, "IS FAVOURITE: True");

FavSlotNo=String.valueOf(tempFavSlotNo);
                                    MenuItem favMenu=menu.findItem(R.id.fav);
                                   // Drawable DOCDrawable = getResources().getDrawable(R.drawable.prescription512);
favMenu.setTitle("Remove from Favourite");

                                }else{
                                    isFavourite="0";
                                    Log.d(TAG, "IS FAVOURITE 1: False");

                                }
                            }else{
                                isFavourite="0";
                                Log.d(TAG, "IS FAVOURITE 2 : False");

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
        }).start();

    }

    public void AddFav(){
       //if(FavTotal>=1){
if(isFavourite.equals("0")) {
    String favtotal = String.valueOf(FavTotal);
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference favAdd = database.getReference("app/users/" + UserUID + "/fav/" + favtotal + "/token");
    favAdd.setValue(ClientToken);

    DatabaseReference favAddName = database.getReference("app/users/" + UserUID + "/fav/" + favtotal + "/name");
    favAddName.setValue(""+FName+" "+LName+"");

    int FavT = FavTotal + 1;

    DatabaseReference favTotal = database.getReference("app/users/" + UserUID + "/fav/total");
    favTotal.setValue(String.valueOf(FavT));
    MenuLoadTotal();

    Toast.makeText(ClientActivity.this, "Added to My Connections",
            Toast.LENGTH_SHORT).show();
}
else{
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference favDel = database.getReference("app/users/" + UserUID + "/fav/" + FavSlotNo);
    favDel.removeValue();
    MenuLoadTotal();

    Toast.makeText(ClientActivity.this, "Removed from My Connection",
            Toast.LENGTH_SHORT).show();
   }


    }

  public void AddAccessRecord1(){
      FirebaseDatabase database = FirebaseDatabase.getInstance();
      DatabaseReference GetRecordTotal = database.getReference("app/users/" + ClientUserUID + "/accessdata/total");
      GetRecordTotal.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              // This method is called once with the initial value and again
              // whenever data at this location is updated.
              String value = dataSnapshot.getValue(String.class);
              Log.d(TAG, "Add Record Total Value is: " + value);
              if(value!=null){
                  AccessDataTotal=value;
AddAccessRecord2();
              }else {
                  AccessDataTotal="1";
                  AddAccessRecord2();
              }
          }

          @Override
          public void onCancelled(DatabaseError error) {
              // Failed to read value
              Log.w(TAG, "Failed to read value.", error.toException());
          }
      });

  }


    public void AddAccessRecord2(){
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int min=c.get(Calendar.MINUTE);
        int dt=c.get(Calendar.DATE);
        int month=c.get(Calendar.MONTH);
        int year=c.get(Calendar.YEAR);

        int tot=Integer.valueOf(AccessDataTotal);
        String Total=String.valueOf(tot+1);
        String Hr=String.valueOf(hour);
        String Min=String.valueOf(min);
        String Dt=String.valueOf(dt);
        String Month=String.valueOf(month+1);
        String Year=String.valueOf(year);

        String Time=Hr+":"+Min;
        String Date=Dt+"-"+Month+"-"+Year;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference AddRecordName = database.getReference("app/users/" + ClientUserUID + "/accessdata/"+AccessDataTotal+"/name");
        DatabaseReference AddRecordTime = database.getReference("app/users/" + ClientUserUID + "/accessdata/"+AccessDataTotal+"/time");
        DatabaseReference AddRecordDate = database.getReference("app/users/" + ClientUserUID + "/accessdata/"+AccessDataTotal+"/date");
        DatabaseReference RecordTotal = database.getReference("app/users/" + ClientUserUID + "/accessdata/total");

        AddRecordName.setValue(AppUserName);
        AddRecordTime.setValue(Time);
        AddRecordDate.setValue(Date);
RecordTotal.setValue(Total);




    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_health_chart) {
            View chome=(View)findViewById(R.id.viewClientHome);
            chome.setVisibility(View.INVISIBLE);
            View cHealthChart=(View)findViewById(R.id.viewClientHealthCharts);
            cHealthChart.setVisibility(View.VISIBLE);
            View cMedicalHistory=(View)findViewById(R.id.viewMedicalHistory);
            cMedicalHistory.setVisibility(View.INVISIBLE);
            View cMedicalReports=(View)findViewById(R.id.viewMedicalReports);
            cMedicalReports.setVisibility(View.INVISIBLE);
            View cPrescription=(View)findViewById(R.id.viewPrescription);
            cPrescription.setVisibility(View.INVISIBLE);

            AdsLoad();
        } else if (id == R.id.menu_bio) {
            View chome=(View)findViewById(R.id.viewClientHome);
            chome.setVisibility(View.VISIBLE);
            View cHealthChart=(View)findViewById(R.id.viewClientHealthCharts);
            cHealthChart.setVisibility(View.INVISIBLE);
            View cMedicalHistory=(View)findViewById(R.id.viewMedicalHistory);
            cMedicalHistory.setVisibility(View.INVISIBLE);
            View cMedicalReports=(View)findViewById(R.id.viewMedicalReports);
            cMedicalReports.setVisibility(View.INVISIBLE);
            View cPrescription=(View)findViewById(R.id.viewPrescription);
            cPrescription.setVisibility(View.INVISIBLE);

            AdsLoad();
        } else if (id == R.id.menu_medical_history) {
            View chome=(View)findViewById(R.id.viewClientHome);
            chome.setVisibility(View.INVISIBLE);
            View cHealthChart=(View)findViewById(R.id.viewClientHealthCharts);
            cHealthChart.setVisibility(View.INVISIBLE);
            View cMedicalHistory=(View)findViewById(R.id.viewMedicalHistory);
            cMedicalHistory.setVisibility(View.VISIBLE);
            View cMedicalReports=(View)findViewById(R.id.viewMedicalReports);
            cMedicalReports.setVisibility(View.INVISIBLE);
            View cPrescription=(View)findViewById(R.id.viewPrescription);
            cPrescription.setVisibility(View.INVISIBLE);
            GetHistoryData();

            AdsLoad();
        } else if (id == R.id.menu_medical_report) {

            View chome=(View)findViewById(R.id.viewClientHome);
            chome.setVisibility(View.INVISIBLE);
            View cHealthChart=(View)findViewById(R.id.viewClientHealthCharts);
            cHealthChart.setVisibility(View.INVISIBLE);
            View cMedicalHistory=(View)findViewById(R.id.viewMedicalHistory);
            cMedicalHistory.setVisibility(View.INVISIBLE);
            View cMedicalReports=(View)findViewById(R.id.viewMedicalReports);
            cMedicalReports.setVisibility(View.VISIBLE);
            View cPrescription=(View)findViewById(R.id.viewPrescription);
            cPrescription.setVisibility(View.INVISIBLE);
            MedicalReport();

            AdsLoad();
        } else if (id == R.id.menu_prescription) {
			  View chome=(View)findViewById(R.id.viewClientHome);
            chome.setVisibility(View.INVISIBLE);
            View cHealthChart=(View)findViewById(R.id.viewClientHealthCharts);
            cHealthChart.setVisibility(View.INVISIBLE);
            View cMedicalHistory=(View)findViewById(R.id.viewMedicalHistory);
            cMedicalHistory.setVisibility(View.INVISIBLE);
            View cMedicalReports=(View)findViewById(R.id.viewMedicalReports);
            cMedicalReports.setVisibility(View.INVISIBLE);
            View cPrescription=(View)findViewById(R.id.viewPrescription);
            cPrescription.setVisibility(View.VISIBLE);
			GetPrescription();
            AdsLoad();
			

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void ClientOut(){
        Context context=ClientActivity.this;
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);

        builder.setTitle("Generate New Access Token")
                .setMessage("This will delete old access token & create new one. previous token will be inaccessible, Do you want to Continue ?")
                .setPositiveButton("Sign out", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // continue with delete
                        finish();
                        Intent ab = new Intent(ClientActivity.this, MainActivity.class);
                        startActivity(ab);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(R.drawable.warning_ico)
                .show();


    }

    public void GetBio(){
        navigationView= (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        final TextView TxEmail = (TextView) findViewById(R.id.textView16EmailID);
        final TextView TxUsername = (TextView) findViewById(R.id.textView16UserName);
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
        final TextView ClientNameTx = (TextView) headerView.findViewById(R.id.clientName);
        final TextView ModeTx = (TextView) headerView.findViewById(R.id.textView76);

       final ImageView PicIMG = (ImageView) headerView.findViewById(R.id.imageView15ProfilePic);
        final ImageView PicIMG1 = (ImageView) findViewById(R.id.imageViewProfilePic);

        ModeTx.setText("Permission: Read Only");
    //    TxEmail.setText(EmailID);
      //  TxUsername.setText(UserName);
        //Glide.with(this).load(PicURI).into(PicIMG);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference DataAge = database.getReference("app/users/" + ClientUserUID + "/bio/age");
        DatabaseReference DataDOB = database.getReference("app/users/" + ClientUserUID + "/bio/dob");
        DatabaseReference DataWeight = database.getReference("app/users/" + ClientUserUID + "/bio/weight");
        DatabaseReference DataHeight = database.getReference("app/users/" + ClientUserUID + "/bio/height");
        DatabaseReference DataBlood = database.getReference("app/users/" + ClientUserUID + "/bio/blood");
        DatabaseReference DataPhno = database.getReference("app/users/" + ClientUserUID + "/bio/phno");
        DatabaseReference DataLandline = database.getReference("app/users/" + ClientUserUID + "/bio/landlineno");
        DatabaseReference DataAddress = database.getReference("app/users/" + ClientUserUID + "/bio/address");
        DatabaseReference DataFname = database.getReference("app/users/" + ClientUserUID + "/fname");
        DatabaseReference DataLname = database.getReference("app/users/" + ClientUserUID + "/lname");
        DatabaseReference DataEmail = database.getReference("app/users/" + ClientUserUID + "/emailID");
        DatabaseReference DataProfilePic = database.getReference("app/users/" + ClientUserUID + "/bio/photo");


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
TxUsername.setText(FName+" "+LName);
                    ClientNameTx.setText(FName+" "+LName);




                } else {
                    TxFname.setText("---");
                    ClientNameTx.setText("Loading...");

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
                    TxUsername.setText(FName+" "+LName);
                    ClientNameTx.setText(FName+" "+LName);


                } else {
                    TxLname.setText("---");
                    ClientNameTx.setText("Loading...");


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        DataLname.keepSynced(true);

        DataEmail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                load = load + 1;
                loadCount();
                if (value != null) {
                    EmailID = value;
                    TxEmail.setText(EmailID);

                } else {
                    TxEmail.setText("---");

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        DataEmail.keepSynced(true);

        DataProfilePic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Profili pic url Value is: " + value);
                load = load + 1;
                loadCount();
                if (value != null) {
                    Glide.with(ClientActivity.this).load(Uri.parse(value)).into(PicIMG);
                    //Glide.with(ClientActivity.this).load(Uri.parse(value)).into(PicIMG1);
                    Picasso.with(ClientActivity.this).load(Uri.parse(value)).into(PicIMG1);
LoadingDialog.dismiss();
                } else {


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        DataProfilePic.keepSynced(true);




    }

    public void loadCount() {
        if (load == 10) {
            LoadingDialog.dismiss();

            // View loading = (View) findViewById(R.id.loading);
           // loading.setVisibility(View.GONE);
        } else {

        }
    }



    public void nulll(View v){}


    // Health Charts---------------
    // Body Measurements====================
    public void BMclick(View v) {

        if (BMselected == 0) {

            BMselected = 1;
            TextView sub = (TextView) findViewById(R.id.subBM);
            sub.setVisibility(View.INVISIBLE);

            Drawable img = getResources().getDrawable(R.drawable.arrorrdown);
            ImageView A1 = (ImageView) findViewById(R.id.imageViewA1);
            ImageView editIMG = (ImageView) findViewById(R.id.imageView22Edit);
            editIMG.setVisibility(View.VISIBLE);
            View LLedit = (View) findViewById(R.id.LLeditBM);
            View LLview = (View) findViewById(R.id.LLViewBM);
            LLedit.setVisibility(View.INVISIBLE);
            LLview.setVisibility(View.VISIBLE);
            A1.setImageDrawable(img);
            BMdownload();
            userSCR = "BM";


        } else if (BMselected == 1) {

            BMselected = 0;

            TextView sub = (TextView) findViewById(R.id.subBM);
            sub.setVisibility(View.VISIBLE);
            Drawable img = getResources().getDrawable(R.drawable.arrorr);
            ImageView A1 = (ImageView) findViewById(R.id.imageViewA1);
            View LLedit = (View) findViewById(R.id.LLeditBM);
            View LLview = (View) findViewById(R.id.LLViewBM);
            LLedit.setVisibility(View.GONE);
            LLview.setVisibility(View.GONE);
            A1.setImageDrawable(img);
            ImageView editIMG = (ImageView) findViewById(R.id.imageView22Edit);
            editIMG.setVisibility(View.GONE);
        }


    }

    public void BMdownload() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference weightData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/BodyMeasurements/weight");
        weightData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValWeight = value;
                    TextView tx = (TextView) findViewById(R.id.textViewWeight44);
                    tx.setText(ValWeight + " Kg");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewWeight44);
                    tx.setText("-- Kg");
                }
                BMLoadCount = BMLoadCount + 1;
                BMProgressBar();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference heightData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/BodyMeasurements/height");
        heightData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValHeight = value;

                    TextView tx = (TextView) findViewById(R.id.textViewHeight47);
                    tx.setText(ValHeight + " inch");

                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewHeight47);
                    tx.setText("-- inch");
                }
                BMLoadCount = BMLoadCount + 1;
                BMProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference bmiData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/BodyMeasurements/bmi");
        bmiData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValBMI = value;
                    TextView tx = (TextView) findViewById(R.id.textViewBMI49);
                    tx.setText(ValBMI);
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewBMI49);
                    tx.setText("--");
                }
                BMLoadCount = BMLoadCount + 1;
                BMProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public void BMProgressBar() {
        if (BMLoadCount == 3) {
            ProgressBar p = (ProgressBar) findViewById(R.id.progressBar4);
            p.setVisibility(View.GONE);
            BMLoadCount = 0;
        }
    }

    public void BMedit(View v) {

        View AddMenu = (View) findViewById(R.id.LLAddMenu);
        AddMenu.setVisibility(View.VISIBLE);

        LinearLayout L1 = (LinearLayout) findViewById(R.id.LL1);
        L1.setVisibility(View.VISIBLE);

        LinearLayout L2 = (LinearLayout) findViewById(R.id.LL2);
        L2.setVisibility(View.VISIBLE);

        LinearLayout L3 = (LinearLayout) findViewById(R.id.LL3);
        L3.setVisibility(View.VISIBLE);

        TextView tx1 = (TextView) findViewById(R.id.txview1);
        EditText et1 = (EditText) findViewById(R.id.etview1);

        tx1.setText("Height(inch): ");
        if (ValHeight != null) {
            et1.setText(ValHeight);
        }

        TextView tx2 = (TextView) findViewById(R.id.txview2);
        EditText et2 = (EditText) findViewById(R.id.etview2);
        tx2.setText("Weight(Kg): ");
        if (ValHeight != null) {
            et2.setText(ValWeight);
        }

        TextView tx3 = (TextView) findViewById(R.id.txview3);
        EditText et3 = (EditText) findViewById(R.id.etview3);
        tx3.setText("BMI: ");
        if (ValHeight != null) {
            et3.setText(ValBMI);
        }


    }

    public void BMUpdate() {
        EditText etHeight = (EditText) findViewById(R.id.etview1);
        EditText etWeight = (EditText) findViewById(R.id.etview2);
        EditText etBMI = (EditText) findViewById(R.id.etview3);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference weightData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/BodyMeasurements/weight");
        DatabaseReference bmiData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/BodyMeasurements/bmi");
        DatabaseReference heightData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/BodyMeasurements/height");

        weightData.setValue(etWeight.getText().toString());
        bmiData.setValue(etBMI.getText().toString());
        heightData.setValue(etHeight.getText().toString());

        finish();
        Intent ab = new Intent(this, HealthChartActivity.class);
        startActivity(ab);


    }

    // Blood Pressure=========================
    public void BPclick(View v) {

        if (BPselected == 0) {

            userSCR = "BP";
            BPselected = 1;
            TextView sub = (TextView) findViewById(R.id.subtxBP);
            sub.setVisibility(View.INVISIBLE);

            Drawable img = getResources().getDrawable(R.drawable.arrorrdown);
            ImageView A1 = (ImageView) findViewById(R.id.imageViewA2);
            ImageView editIMG = (ImageView) findViewById(R.id.imageViewBPEdit22);
            editIMG.setVisibility(View.INVISIBLE);

            //  View LLedit = (View) findViewById(R.id.LLeditBM); // Edit button
            View LLview = (View) findViewById(R.id.LLViewBP); // Main View
            //  LLedit.setVisibility(View.VISIBLE);
            LLview.setVisibility(View.VISIBLE);
            A1.setImageDrawable(img);
            BPdownload();


        } else if (BPselected == 1) {

            BPselected = 0;
            Drawable img = getResources().getDrawable(R.drawable.arrorr);
            TextView sub = (TextView) findViewById(R.id.subtxBP);
            sub.setVisibility(View.VISIBLE);

            ImageView A1 = (ImageView) findViewById(R.id.imageViewA2);
            ImageView editIMG = (ImageView) findViewById(R.id.imageViewBPEdit22);
            editIMG.setVisibility(View.GONE);

            //  View LLedit = (View) findViewById(R.id.LLeditBM); // Edit button
            View LLview = (View) findViewById(R.id.LLViewBP); // Main View
            //  LLedit.setVisibility(View.VISIBLE);
            LLview.setVisibility(View.GONE);
            A1.setImageDrawable(img);
        }


    }

    public void BPdownload() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference systolicData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/BloodPressure/systolic");
        systolicData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValSystolic = value;
                    TextView tx = (TextView) findViewById(R.id.textViewSystolic47);
                    tx.setText(ValSystolic + " mmHg");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewSystolic47);
                    tx.setText("-- mmHg");
                }
                BPLoadCount = BPLoadCount + 1;
                BPProgressBar();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference diastolicData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/BloodPressure/diastolic");
        diastolicData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValDiastolic = value;

                    TextView tx = (TextView) findViewById(R.id.textViewDiastolic44);
                    tx.setText(ValDiastolic + " mmHg");

                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewDiastolic44);
                    tx.setText("-- mmHg");
                }
                BPLoadCount = BPLoadCount + 1;
                BPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }

    public void BPProgressBar() {
        if (BPLoadCount == 2) {
            ProgressBar p = (ProgressBar) findViewById(R.id.progressBarBP1);
            p.setVisibility(View.GONE);
            BMLoadCount = 0;
        }
    }

    public void BPedit(View v) {

        View AddMenu = (View) findViewById(R.id.LLAddMenu);
        AddMenu.setVisibility(View.VISIBLE);

        LinearLayout L1 = (LinearLayout) findViewById(R.id.LL1);
        L1.setVisibility(View.VISIBLE);

        LinearLayout L2 = (LinearLayout) findViewById(R.id.LL2);
        L2.setVisibility(View.VISIBLE);

        TextView tx1 = (TextView) findViewById(R.id.txview1);
        EditText et1 = (EditText) findViewById(R.id.etview1);

        tx1.setText("Systolic (mmHg): ");
        if (ValSystolic != null) {
            et1.setText(ValSystolic);
        }

        TextView tx2 = (TextView) findViewById(R.id.txview2);
        EditText et2 = (EditText) findViewById(R.id.etview2);
        tx2.setText("Diastolic (mmHg): ");
        if (ValDiastolic != null) {
            et2.setText(ValDiastolic);
        }


    }

    public void BPUpdate() {
        EditText etSys = (EditText) findViewById(R.id.etview1);
        EditText etDyst = (EditText) findViewById(R.id.etview2);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference systolicData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/BloodPressure/systolic");
        DatabaseReference diastolicData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/BloodPressure/diastolic");

        systolicData.setValue(etSys.getText().toString());
        diastolicData.setValue(etDyst.getText().toString());


        finish();
        Intent ab = new Intent(this, HealthChartActivity.class);
        startActivity(ab);


    }

    // Sugar Panel ====================
    public void SPclick(View v) {

        if (SPselected == 0) {

            SPselected = 1;

            TextView sub = (TextView) findViewById(R.id.subTxSP);
            sub.setVisibility(View.INVISIBLE);

            Drawable img = getResources().getDrawable(R.drawable.arrorrdown);
            ImageView A3 = (ImageView) findViewById(R.id.imageViewA3);
            ImageView editIMG = (ImageView) findViewById(R.id.imageViewSPedit);
            editIMG.setVisibility(View.VISIBLE);
            View LLedit = (View) findViewById(R.id.LLeditSP);
            View LLview = (View) findViewById(R.id.LLViewSP);
            LLedit.setVisibility(View.INVISIBLE);
            LLview.setVisibility(View.VISIBLE);
            A3.setImageDrawable(img);
            SPdownload();
            userSCR = "SP";


        } else if (SPselected == 1) {

            SPselected = 0;

            TextView sub = (TextView) findViewById(R.id.subTxSP);
            sub.setVisibility(View.VISIBLE);
            Drawable img = getResources().getDrawable(R.drawable.arrorr);
            ImageView A1 = (ImageView) findViewById(R.id.imageViewA3);
            View LLedit = (View) findViewById(R.id.LLeditSP);
            View LLview = (View) findViewById(R.id.LLViewSP);
            LLedit.setVisibility(View.GONE);
            LLview.setVisibility(View.GONE);
            A1.setImageDrawable(img);
            ImageView editIMG = (ImageView) findViewById(R.id.imageViewSPedit);
            editIMG.setVisibility(View.GONE);
        }


    }

    public void SPdownload() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference SfastingData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/SugarPanel/SugarFasting");
        SfastingData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValSuagarFasting = value;
                    TextView tx = (TextView) findViewById(R.id.textViewFasting);
                    tx.setText(ValSuagarFasting + " mg/dl");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewFasting);
                    tx.setText("-- mg/dl");
                }
                SPLoadCount = SPLoadCount + 1;
                SPProgressBar();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference SrandomData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/SugarPanel/SugarRandom");
        SrandomData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValSuagarRandom = value;

                    TextView tx = (TextView) findViewById(R.id.textViewRandom);
                    tx.setText(ValSuagarRandom + " mm/dl");

                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewRandom);
                    tx.setText("-- mm/dl");
                }
                SPLoadCount = SPLoadCount + 1;
                SPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference Hb1Data = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/SugarPanel/HbA1c");
        Hb1Data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValHbA1c = value;
                    TextView tx = (TextView) findViewById(R.id.textViewHbA1c);
                    tx.setText(ValHbA1c + " %");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewHbA1c);
                    tx.setText("-- %");
                }
                SPLoadCount = SPLoadCount + 1;
                SPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public void SPProgressBar() {
        if (SPLoadCount == 3) {
            ProgressBar p = (ProgressBar) findViewById(R.id.progressBarSP);
            p.setVisibility(View.GONE);
            SPLoadCount = 0;
        }
    }

    public void SPedit(View v) {

        View AddMenu = (View) findViewById(R.id.LLAddMenu);
        AddMenu.setVisibility(View.VISIBLE);

        LinearLayout L1 = (LinearLayout) findViewById(R.id.LL1);
        L1.setVisibility(View.VISIBLE);

        LinearLayout L2 = (LinearLayout) findViewById(R.id.LL2);
        L2.setVisibility(View.VISIBLE);

        LinearLayout L3 = (LinearLayout) findViewById(R.id.LL3);
        L3.setVisibility(View.VISIBLE);

        TextView tx1 = (TextView) findViewById(R.id.txview1);
        EditText et1 = (EditText) findViewById(R.id.etview1);

        tx1.setText("Blood Sugar (Fasting)(mg/dl): ");
        if (ValSuagarFasting != null) {
            et1.setText(ValSuagarFasting);
        }

        TextView tx2 = (TextView) findViewById(R.id.txview2);
        EditText et2 = (EditText) findViewById(R.id.etview2);
        tx2.setText("Blood Sugar (Random)(mg/dl): ");
        if (ValSuagarRandom != null) {
            et2.setText(ValSuagarRandom);
        }

        TextView tx3 = (TextView) findViewById(R.id.txview3);
        EditText et3 = (EditText) findViewById(R.id.etview3);
        tx3.setText("HbA1c (%): ");
        if (ValHbA1c != null) {
            et3.setText(ValHbA1c);
        }


    }

    public void SPUpdate() {
        EditText etSugarF = (EditText) findViewById(R.id.etview1);
        EditText etSugarR = (EditText) findViewById(R.id.etview2);
        EditText etHba1c = (EditText) findViewById(R.id.etview3);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference SfastingData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/SugarPanel/SugarFasting");
        DatabaseReference SrandomData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/SugarPanel/SugarRandom");
        DatabaseReference Hb1Data = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/SugarPanel/HbA1c");

        SfastingData.setValue(etSugarF.getText().toString());
        SrandomData.setValue(etSugarR.getText().toString());
        Hb1Data.setValue(etHba1c.getText().toString());

        finish();
        Intent ab = new Intent(this, HealthChartActivity.class);
        startActivity(ab);


    }


    // Lipid Panel ====================
    public void LPclick(View v) {

        if (LPselected == 0) {

            LPselected = 1;

            TextView sub = (TextView) findViewById(R.id.SubTxLP);
            sub.setVisibility(View.INVISIBLE);

            Drawable img = getResources().getDrawable(R.drawable.arrorrdown);
            ImageView A4 = (ImageView) findViewById(R.id.imageViewA4);
            ImageView editIMG = (ImageView) findViewById(R.id.imageViewLPEdit22);
            editIMG.setVisibility(View.VISIBLE);
            View LLedit = (View) findViewById(R.id.LLeditLP);
            View LLview = (View) findViewById(R.id.LLViewLP);
            LLedit.setVisibility(View.INVISIBLE);
            LLview.setVisibility(View.VISIBLE);
            A4.setImageDrawable(img);
            LPdownload();
            userSCR = "LP";


        } else if (LPselected == 1) {

            LPselected = 0;

            TextView sub = (TextView) findViewById(R.id.SubTxLP);
            sub.setVisibility(View.VISIBLE);
            Drawable img = getResources().getDrawable(R.drawable.arrorr);
            ImageView A4 = (ImageView) findViewById(R.id.imageViewA4);
            View LLedit = (View) findViewById(R.id.LLeditLP);
            View LLview = (View) findViewById(R.id.LLViewLP);
            LLedit.setVisibility(View.GONE);
            LLview.setVisibility(View.GONE);
            A4.setImageDrawable(img);
            ImageView editIMG = (ImageView) findViewById(R.id.imageViewLPEdit22);
            editIMG.setVisibility(View.GONE);
        }


    }

    public void LPdownload() {// -------

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference TriglycData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/LipidPanel/Triglycerides");
        TriglycData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValTriglyce = value;
                    TextView tx = (TextView) findViewById(R.id.textViewTetragly);
                    tx.setText(ValTriglyce + " mg/dl");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewTetragly);
                    tx.setText("-- mg/dl");
                }
                LPLoadCount = LPLoadCount + 1;
                LPProgressBar();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference TotalCholeData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/LipidPanel/TotalCholesterol");
        TotalCholeData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValTotalChole = value;

                    TextView tx = (TextView) findViewById(R.id.textViewTotalChol);
                    tx.setText(ValTotalChole + " mg/dl");

                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewTotalChol);
                    tx.setText("-- mg/dl");
                }
                LPLoadCount = LPLoadCount + 1;
                LPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference HDLData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/LipidPanel/HDL");
        HDLData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValHDL = value;
                    TextView tx = (TextView) findViewById(R.id.textViewHDL);
                    tx.setText(ValHDL + " mg/dl");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewHDL);
                    tx.setText("-- mg/dl");
                }
                LPLoadCount = LPLoadCount + 1;
                LPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference LDLData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/LipidPanel/LDL");
        LDLData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValLDL = value;
                    TextView tx = (TextView) findViewById(R.id.textViewLDL);
                    tx.setText(ValLDL + " mg/dl");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewLDL);
                    tx.setText("-- mg/dl");
                }
                LPLoadCount = LPLoadCount + 1;
                LPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference VLDLData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/LipidPanel/VLDL");
        VLDLData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValVLDL = value;
                    TextView tx = (TextView) findViewById(R.id.textViewVLDL);
                    tx.setText(ValVLDL + " mg/dl");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewVLDL);
                    tx.setText("-- mg/dl");
                }
                LPLoadCount = LPLoadCount + 1;
                LPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public void LPProgressBar() {
        if (LPLoadCount == 5) {
            ProgressBar p = (ProgressBar) findViewById(R.id.progressBarLP);
            p.setVisibility(View.GONE);
            LPLoadCount = 0;
        }
    }

    public void LPedit(View v) {

        View AddMenu = (View) findViewById(R.id.LLAddMenu);
        AddMenu.setVisibility(View.VISIBLE);

        LinearLayout L1 = (LinearLayout) findViewById(R.id.LL1);
        L1.setVisibility(View.VISIBLE);

        LinearLayout L2 = (LinearLayout) findViewById(R.id.LL2);
        L2.setVisibility(View.VISIBLE);

        LinearLayout L3 = (LinearLayout) findViewById(R.id.LL3);
        L3.setVisibility(View.VISIBLE);

        LinearLayout L4 = (LinearLayout) findViewById(R.id.LL4);
        L4.setVisibility(View.VISIBLE);

        LinearLayout L5 = (LinearLayout) findViewById(R.id.LL5);
        L5.setVisibility(View.VISIBLE);

        TextView tx1 = (TextView) findViewById(R.id.txview1);
        EditText et1 = (EditText) findViewById(R.id.etview1);

        tx1.setText("Triglycerides(mg/dl): ");
        if (ValTriglyce != null) {
            et1.setText(ValTriglyce);
        }

        TextView tx2 = (TextView) findViewById(R.id.txview2);
        EditText et2 = (EditText) findViewById(R.id.etview2);
        tx2.setText("Total Cholesterol(mg/dl): ");
        if (ValTotalChole != null) {
            et2.setText(ValTotalChole);
        }

        TextView tx3 = (TextView) findViewById(R.id.txview3);
        EditText et3 = (EditText) findViewById(R.id.etview3);
        tx3.setText("High Density Lipoprotein(HDL)(mg/dl) ");
        if (ValHDL != null) {
            et3.setText(ValHDL);
        }


        TextView tx4 = (TextView) findViewById(R.id.txview4);
        EditText et4 = (EditText) findViewById(R.id.etview4);
        tx4.setText("Low Density Lipoprotein(LDL)(mg/dl) ");
        if (ValLDL != null) {
            et4.setText(ValLDL);
        }

        TextView tx5 = (TextView) findViewById(R.id.txview5);
        EditText et5 = (EditText) findViewById(R.id.etview5);
        tx5.setText("Very Low Density Lipoprotein(VLDL)(mg/dl) ");
        if (ValVLDL != null) {
            et5.setText(ValVLDL);
        }


    }

    public void LPUpdate() {
        EditText etTriglycer = (EditText) findViewById(R.id.etview1);
        EditText etTotalChol = (EditText) findViewById(R.id.etview2);
        EditText etHDL = (EditText) findViewById(R.id.etview3);
        EditText etLDL = (EditText) findViewById(R.id.etview4);
        EditText etVLDL = (EditText) findViewById(R.id.etview5);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference TriglycData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/LipidPanel/Triglycerides");
        DatabaseReference TotalCholeData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/LipidPanel/TotalCholesterol");
        DatabaseReference HDLData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/LipidPanel/HDL");
        DatabaseReference LDLData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/LipidPanel/LDL");
        DatabaseReference VLDLData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/LipidPanel/VLDL");


        TriglycData.setValue(etTriglycer.getText().toString());
        TotalCholeData.setValue(etTotalChol.getText().toString());
        HDLData.setValue(etHDL.getText().toString());
        LDLData.setValue(etLDL.getText().toString());
        VLDLData.setValue(etVLDL.getText().toString());

        finish();
        Intent ab = new Intent(this, HealthChartActivity.class);
        startActivity(ab);


    }


    // Complete Blood Count ====================
    public void CBCclick(View v) {

        if (CBCselected == 0) {

            CBCselected = 1;

            TextView sub = (TextView) findViewById(R.id.SubTxCBC);
            sub.setVisibility(View.INVISIBLE);

            Drawable img = getResources().getDrawable(R.drawable.arrorrdown);
            ImageView A4 = (ImageView) findViewById(R.id.imageViewA5);
            ImageView editIMG = (ImageView) findViewById(R.id.imageViewCBCEdit22);
            editIMG.setVisibility(View.INVISIBLE);
            // View LLedit = (View) findViewById(R.id.LLeditCBC);
            View LLview = (View) findViewById(R.id.LLViewCBC);
            //  LLedit.setVisibility(View.VISIBLE);
            LLview.setVisibility(View.VISIBLE);
            A4.setImageDrawable(img);
            CBCdownload();
            userSCR = "CBC";


        } else if (CBCselected == 1) {

            CBCselected = 0;

            TextView sub = (TextView) findViewById(R.id.SubTxCBC);
            sub.setVisibility(View.VISIBLE);
            Drawable img = getResources().getDrawable(R.drawable.arrorr);
            ImageView A4 = (ImageView) findViewById(R.id.imageViewA5);
            ImageView editIMG = (ImageView) findViewById(R.id.imageViewCBCEdit22);
            View LLview = (View) findViewById(R.id.LLViewCBC);
            LLview.setVisibility(View.GONE);
            A4.setImageDrawable(img);
            editIMG.setVisibility(View.GONE);
        }


    }

    public void CBCdownload() {// -------

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference HaemoData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/Haemoglobin");
        HaemoData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValHaemoglobin = value;
                    TextView tx = (TextView) findViewById(R.id.textViewHaeHb);
                    tx.setText(ValHaemoglobin + " g/dl");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewTetragly);
                    tx.setText("-- g/dl");
                }
                CBCLoadCount = CBCLoadCount + 1;
                CBCProgressBar();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference WBCsData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/WBC");
        WBCsData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValWBCs = value;

                    TextView tx = (TextView) findViewById(R.id.textViewWBCs);
                    tx.setText(ValWBCs + " Count/cu mm");

                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewWBCs);
                    tx.setText("-- Count/cu mm");
                }
                CBCLoadCount = CBCLoadCount + 1;
                CBCProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference NeutrData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/Neutrophils");
        NeutrData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValNeutrophils = value;
                    TextView tx = (TextView) findViewById(R.id.textViewNeutrophils);
                    tx.setText(ValNeutrophils + " %");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewNeutrophils);
                    tx.setText("-- %");
                }
                CBCLoadCount = CBCLoadCount + 1;
                CBCProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference EosinoData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/Eosinophils");
        EosinoData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValEosinophiles = value;
                    TextView tx = (TextView) findViewById(R.id.textViewEosinophils);
                    tx.setText(ValEosinophiles + " %");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewEosinophils);
                    tx.setText("-- %");
                }
                CBCLoadCount = CBCLoadCount + 1;
                CBCProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference BasoData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/Basophils");
        BasoData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValBasophils = value;
                    TextView tx = (TextView) findViewById(R.id.textViewBasophils);
                    tx.setText(ValBasophils + " %");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewBasophils);
                    tx.setText("-- %");
                }
                CBCLoadCount = CBCLoadCount + 1;
                CBCProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference monocData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/Monocytes");
        monocData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValMonocytes = value;
                    TextView tx = (TextView) findViewById(R.id.textViewMonocytes);
                    tx.setText(ValMonocytes + " %");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewMonocytes);
                    tx.setText("-- %");
                }
                CBCLoadCount = CBCLoadCount + 1;
                CBCProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference lympData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/Lymphocytes");
        lympData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValLymphocytes = value;
                    TextView tx = (TextView) findViewById(R.id.textViewLymphocytes);
                    tx.setText(ValLymphocytes + " %");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewLymphocytes);
                    tx.setText("-- %");
                }
                CBCLoadCount = CBCLoadCount + 1;
                CBCProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference plateData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/Platelets");
        plateData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValPlatelets = value;
                    TextView tx = (TextView) findViewById(R.id.textViewPlatelets);
                    tx.setText(ValPlatelets + " 10^9/L");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewPlatelets);
                    tx.setText("-- 10^9/L");
                }
                CBCLoadCount = CBCLoadCount + 1;
                CBCProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference ESRData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/ESR");
        ESRData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValESR = value;
                    TextView tx = (TextView) findViewById(R.id.textViewESR);
                    tx.setText(ValESR);
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewESR);
                    tx.setText("--");
                }
                CBCLoadCount = CBCLoadCount + 1;
                CBCProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference HctData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/Haematocrit");
        HctData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValHct = value;
                    TextView tx = (TextView) findViewById(R.id.textViewHaematocrit);
                    tx.setText(ValHct + " %");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewHaematocrit);
                    tx.setText("-- %");
                }
                CBCLoadCount = CBCLoadCount + 1;
                CBCProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference MCHData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/MCH");
        MCHData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValMCH = value;
                    TextView tx = (TextView) findViewById(R.id.textViewMeanCorpuscularHaemoglobin);
                    tx.setText(ValMCH + " g/dl");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewMeanCorpuscularHaemoglobin);
                    tx.setText("-- g/dl");
                }
                CBCLoadCount = CBCLoadCount + 1;
                CBCProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference MCHCData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/MCHC");
        MCHCData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValMCHC = value;
                    TextView tx = (TextView) findViewById(R.id.textViewMCHC);
                    tx.setText(ValMCHC + " %");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewMCHC);
                    tx.setText("-- %");
                }
                CBCLoadCount = CBCLoadCount + 1;
                CBCProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference MCVData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/MCV");
        MCVData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValMCV = value;
                    TextView tx = (TextView) findViewById(R.id.textViewMCV);
                    tx.setText(ValMCV + " fL");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewMCV);
                    tx.setText("-- fL");
                }
                CBCLoadCount = CBCLoadCount + 1;
                CBCProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference RBCData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/RBC");
        RBCData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValRBC = value;
                    TextView tx = (TextView) findViewById(R.id.textViewRBC);
                    tx.setText(ValRBC + " 10^12/L");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewRBC);
                    tx.setText("-- 10^12/L");
                }
                CBCLoadCount = CBCLoadCount + 1;
                CBCProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference RDWData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/RDW");
        RDWData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValRDW = value;
                    TextView tx = (TextView) findViewById(R.id.textViewRDW);
                    tx.setText(ValRDW + " %");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewRDW);
                    tx.setText("-- %");
                }
                CBCLoadCount = CBCLoadCount + 1;
                CBCProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }

    public void CBCProgressBar() {
        if (CBCLoadCount == 15) {
            ProgressBar p = (ProgressBar) findViewById(R.id.progressBarCBC);
            p.setVisibility(View.GONE);
            CBCLoadCount = 0;
        }
    }

    public void CBCedit(View v) {

        View AddMenu = (View) findViewById(R.id.LLAddMenu);
        AddMenu.setVisibility(View.VISIBLE);

        LinearLayout L1 = (LinearLayout) findViewById(R.id.LL1);
        L1.setVisibility(View.VISIBLE);

        LinearLayout L2 = (LinearLayout) findViewById(R.id.LL2);
        L2.setVisibility(View.VISIBLE);

        LinearLayout L3 = (LinearLayout) findViewById(R.id.LL3);
        L3.setVisibility(View.VISIBLE);

        LinearLayout L4 = (LinearLayout) findViewById(R.id.LL4);
        L4.setVisibility(View.VISIBLE);

        LinearLayout L5 = (LinearLayout) findViewById(R.id.LL5);
        L5.setVisibility(View.VISIBLE);

        LinearLayout L6 = (LinearLayout) findViewById(R.id.LL6);
        L6.setVisibility(View.VISIBLE);

        LinearLayout L7 = (LinearLayout) findViewById(R.id.LL7);
        L7.setVisibility(View.VISIBLE);

        LinearLayout L8 = (LinearLayout) findViewById(R.id.LL8);
        L8.setVisibility(View.VISIBLE);

        LinearLayout L9 = (LinearLayout) findViewById(R.id.LL9);
        L9.setVisibility(View.VISIBLE);

        LinearLayout L10 = (LinearLayout) findViewById(R.id.LL10);
        L10.setVisibility(View.VISIBLE);

        LinearLayout L11 = (LinearLayout) findViewById(R.id.LL11);
        L11.setVisibility(View.VISIBLE);

        LinearLayout L12 = (LinearLayout) findViewById(R.id.LL12);
        L12.setVisibility(View.VISIBLE);

        LinearLayout L13 = (LinearLayout) findViewById(R.id.LL13);
        L13.setVisibility(View.VISIBLE);

        LinearLayout L14 = (LinearLayout) findViewById(R.id.LL14);
        L14.setVisibility(View.VISIBLE);

        LinearLayout L15 = (LinearLayout) findViewById(R.id.LL15);
        L15.setVisibility(View.VISIBLE);


        TextView tx1 = (TextView) findViewById(R.id.txview1);
        EditText et1 = (EditText) findViewById(R.id.etview1);

        tx1.setText("Haemoglobin(Hb)(g/dl): ");
        if (ValHaemoglobin != null) {
            et1.setText(ValHaemoglobin);
        }

        TextView tx2 = (TextView) findViewById(R.id.txview2);
        EditText et2 = (EditText) findViewById(R.id.etview2);
        tx2.setText("White Blood Cells(WBCs)(Count/cu mm): ");
        if (ValWBCs != null) {
            et2.setText(ValWBCs);
        }

        TextView tx3 = (TextView) findViewById(R.id.txview3);
        EditText et3 = (EditText) findViewById(R.id.etview3);
        tx3.setText("Neutrophils (%) ");
        if (ValNeutrophils != null) {
            et3.setText(ValNeutrophils);
        }


        TextView tx4 = (TextView) findViewById(R.id.txview4);
        EditText et4 = (EditText) findViewById(R.id.etview4);
        tx4.setText("Eosinophils (%) ");
        if (ValEosinophiles != null) {
            et4.setText(ValEosinophiles);
        }

        TextView tx5 = (TextView) findViewById(R.id.txview5);
        EditText et5 = (EditText) findViewById(R.id.etview5);
        tx5.setText("Basophils (%) ");
        if (ValBasophils != null) {
            et5.setText(ValBasophils);
        }

        TextView tx6 = (TextView) findViewById(R.id.txview6);
        EditText et6 = (EditText) findViewById(R.id.etview6);
        tx6.setText("Monocytes (%)");
        if (ValMonocytes != null) {
            et6.setText(ValMonocytes);
        }


        TextView tx7 = (TextView) findViewById(R.id.txview7);
        EditText et7 = (EditText) findViewById(R.id.etview7);
        tx7.setText("Lymphocytes (%)");
        if (ValLymphocytes != null) {
            et7.setText(ValLymphocytes);
        }


        TextView tx8 = (TextView) findViewById(R.id.txview8);
        EditText et8 = (EditText) findViewById(R.id.etview8);
        tx8.setText("Platelets (10^9/L)");
        if (ValPlatelets != null) {
            et8.setText(ValPlatelets);
        }

        TextView tx9 = (TextView) findViewById(R.id.txview9);
        EditText et9 = (EditText) findViewById(R.id.etview9);
        tx9.setText("ESR");
        if (ValESR != null) {
            et9.setText(ValESR);
        }


        TextView tx10 = (TextView) findViewById(R.id.txview10);
        EditText et10 = (EditText) findViewById(R.id.etview10);
        tx10.setText("Haematocrit(Hct)");
        if (ValHct != null) {
            et10.setText(ValHct);
        }

        TextView tx11 = (TextView) findViewById(R.id.txview11);
        EditText et11 = (EditText) findViewById(R.id.etview11);
        tx11.setText("Mean Corpuscular Haemoglobin(MCH)(g/dl):");
        if (ValMCH != null) {
            et11.setText(ValMCH);
        }

        TextView tx12 = (TextView) findViewById(R.id.txview12);
        EditText et12 = (EditText) findViewById(R.id.etview12);
        tx12.setText("Mean Corpuscular Haemoglobin Concentration(MCHC)(%):");
        if (ValMCHC != null) {
            et12.setText(ValMCHC);
        }

        TextView tx13 = (TextView) findViewById(R.id.txview13);
        EditText et13 = (EditText) findViewById(R.id.etview13);
        tx13.setText("Mean Corpuscular Volume(MCV)(fL):");
        if (ValMCV != null) {
            et13.setText(ValMCV);
        }

        TextView tx14 = (TextView) findViewById(R.id.txview14);
        EditText et14 = (EditText) findViewById(R.id.etview14);
        tx14.setText("Red Blood Cell Count(RBC)(10^212/L):");
        if (ValRBC != null) {
            et14.setText(ValRBC);
        }


        TextView tx15 = (TextView) findViewById(R.id.txview15);
        EditText et15 = (EditText) findViewById(R.id.etview15);
        tx15.setText("Red Cell Distribution Width(RDW)(%):");
        if (ValRDW != null) {
            et15.setText(ValRDW);
        }

    }

    public void CBCUpdate() {

        EditText etHaemo = (EditText) findViewById(R.id.etview1);
        EditText etWBC = (EditText) findViewById(R.id.etview2);
        EditText etNeutr = (EditText) findViewById(R.id.etview3);
        EditText etEosino = (EditText) findViewById(R.id.etview4);
        EditText etBaso = (EditText) findViewById(R.id.etview5);
        EditText etMono = (EditText) findViewById(R.id.etview6);
        EditText etLymp = (EditText) findViewById(R.id.etview7);
        EditText etPlate = (EditText) findViewById(R.id.etview8);
        EditText etESR = (EditText) findViewById(R.id.etview9);
        EditText etHct = (EditText) findViewById(R.id.etview10);
        EditText etMCH = (EditText) findViewById(R.id.etview11);
        EditText etMCHC = (EditText) findViewById(R.id.etview12);
        EditText etMCV = (EditText) findViewById(R.id.etview13);
        EditText etRBC = (EditText) findViewById(R.id.etview14);
        EditText etRDW = (EditText) findViewById(R.id.etview15);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference HaemoData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/Haemoglobin");
        DatabaseReference WBCsData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/WBC");
        DatabaseReference NeutrData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/Neutrophils");
        DatabaseReference EosinoData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/Eosinophils");
        DatabaseReference BasoData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/Basophils");
        DatabaseReference monocData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/Monocytes");
        DatabaseReference lympData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/Lymphocytes");
        DatabaseReference plateData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/Platelets");
        DatabaseReference ESRData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/ESR");
        DatabaseReference HctData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/Haematocrit");
        DatabaseReference MCHData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/MCH");
        DatabaseReference MCHCData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/MCHC");
        DatabaseReference MCVData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/MCV");
        DatabaseReference RBCData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/RBC");
        DatabaseReference RDWData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/CompleteBloodCount/RDW");


        HaemoData.setValue(etHaemo.getText().toString());
        WBCsData.setValue(etWBC.getText().toString());
        NeutrData.setValue(etNeutr.getText().toString());
        EosinoData.setValue(etEosino.getText().toString());
        BasoData.setValue(etBaso.getText().toString());
        monocData.setValue(etMono.getText().toString());
        lympData.setValue(etLymp.getText().toString());
        plateData.setValue(etPlate.getText().toString());
        ESRData.setValue(etESR.getText().toString());
        HctData.setValue(etHct.getText().toString());
        MCHData.setValue(etMCH.getText().toString());
        MCHCData.setValue(etMCHC.getText().toString());
        MCVData.setValue(etMCV.getText().toString());
        RBCData.setValue(etRBC.getText().toString());
        RDWData.setValue(etRDW.getText().toString());

        finish();
        Intent ab = new Intent(this, HealthChartActivity.class);
        startActivity(ab);


    }


    // Renal Panel ==============================
    public void RPclick(View v) {

        if (RPselected == 0) {

            RPselected = 1;

            TextView sub = (TextView) findViewById(R.id.SubTxRP);
            sub.setVisibility(View.INVISIBLE);

            Drawable img = getResources().getDrawable(R.drawable.arrorrdown);
            ImageView A6 = (ImageView) findViewById(R.id.imageViewA6);
            ImageView editIMG = (ImageView) findViewById(R.id.imageViewRPedit);
            editIMG.setVisibility(View.INVISIBLE);
            // View LLedit = (View) findViewById(R.id.LLeditCBC);
            View LLview = (View) findViewById(R.id.LLViewRP);
            //  LLedit.setVisibility(View.VISIBLE);
            LLview.setVisibility(View.VISIBLE);
            A6.setImageDrawable(img);
            RPdownload();
            userSCR = "RP";


        } else if (RPselected == 1) {

            RPselected = 0;

            TextView sub = (TextView) findViewById(R.id.SubTxRP);
            sub.setVisibility(View.VISIBLE);
            Drawable img = getResources().getDrawable(R.drawable.arrorr);
            ImageView A6 = (ImageView) findViewById(R.id.imageViewA6);
            ImageView editIMG = (ImageView) findViewById(R.id.imageViewRPedit);
            View LLview = (View) findViewById(R.id.LLViewRP);
            LLview.setVisibility(View.GONE);
            A6.setImageDrawable(img);
            editIMG.setVisibility(View.GONE);
        }


    }

    public void RPdownload() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference BloodUN2 = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/RenalPanel/BloodUreaNitrogen");
        BloodUN2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValBloodUreaN2 = value;
                    TextView tx = (TextView) findViewById(R.id.textViewBloodUreaN2);
                    tx.setText(ValBloodUreaN2 + " mg/dl");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewBloodUreaN2);
                    tx.setText("-- mg/dl");
                }
                RPLoadCount = RPLoadCount + 1;
                RPProgressBar();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference SerumCData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/RenalPanel/SerumCreatinine");
        SerumCData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValSerumCretine = value;

                    TextView tx = (TextView) findViewById(R.id.textViewSerumCreatinine);
                    tx.setText(ValSerumCretine + " mg/dl");

                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewSerumCreatinine);
                    tx.setText("-- mg/dl");
                }
                RPLoadCount = RPLoadCount + 1;
                RPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference SerumUricA = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/RenalPanel/SerumUricAcid");
        SerumUricA.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValSerumUricAcid = value;
                    TextView tx = (TextView) findViewById(R.id.textViewSerumUricAcid);
                    tx.setText(ValSerumUricAcid + " mg/dl");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewSerumUricAcid);
                    tx.setText("-- mg/dl");
                }
                RPLoadCount = RPLoadCount + 1;
                RPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void RPProgressBar() {
        if (RPLoadCount == 3) {
            ProgressBar p = (ProgressBar) findViewById(R.id.progressBarRP);
            p.setVisibility(View.GONE);
            RPLoadCount = 0;
        }
    }

    public void RPedit(View v) {

        View AddMenu = (View) findViewById(R.id.LLAddMenu);
        AddMenu.setVisibility(View.VISIBLE);

        LinearLayout L1 = (LinearLayout) findViewById(R.id.LL1);
        L1.setVisibility(View.VISIBLE);

        LinearLayout L2 = (LinearLayout) findViewById(R.id.LL2);
        L2.setVisibility(View.VISIBLE);

        LinearLayout L3 = (LinearLayout) findViewById(R.id.LL3);
        L3.setVisibility(View.VISIBLE);


        TextView tx1 = (TextView) findViewById(R.id.txview1);
        EditText et1 = (EditText) findViewById(R.id.etview1);

        tx1.setText("Blood Urea Nitrogen (mg/dl): ");
        if (ValBloodUreaN2 != null) {
            et1.setText(ValBloodUreaN2);
        }

        TextView tx2 = (TextView) findViewById(R.id.txview2);
        EditText et2 = (EditText) findViewById(R.id.etview2);
        tx2.setText("Serum Creatinine(mg/dl): ");
        if (ValSerumCretine != null) {
            et2.setText(ValSerumCretine);
        }

        TextView tx3 = (TextView) findViewById(R.id.txview3);
        EditText et3 = (EditText) findViewById(R.id.etview3);
        tx3.setText("Serun Uric Acid(mg/dl)");
        if (ValSerumUricAcid != null) {
            et3.setText(ValSerumUricAcid);
        }


    }

    public void RPUpdate() {

        EditText etBloodUreaN2 = (EditText) findViewById(R.id.etview1);
        EditText etSerumCreat = (EditText) findViewById(R.id.etview2);
        EditText etSerumUricAcid = (EditText) findViewById(R.id.etview3);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference BloodUN2 = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/RenalPanel/BloodUreaNitrogen");
        DatabaseReference SerumCData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/RenalPanel/SerumCreatinine");
        DatabaseReference SerumUricA = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/RenalPanel/SerumUricAcid");

        BloodUN2.setValue(etBloodUreaN2.getText().toString());
        SerumCData.setValue(etSerumCreat.getText().toString());
        SerumUricA.setValue(etSerumUricAcid.getText().toString());

        finish();
        Intent ab = new Intent(this, HealthChartActivity.class);
        startActivity(ab);


    }


    // Thyroid Profile ========================
    public void TPclick(View v) {

        if (TPselected == 0) {

            TPselected = 1;

            TextView sub = (TextView) findViewById(R.id.SubTxTP);
            sub.setVisibility(View.INVISIBLE);

            Drawable img = getResources().getDrawable(R.drawable.arrorrdown);
            ImageView A6 = (ImageView) findViewById(R.id.imageViewA7);
            ImageView editIMG = (ImageView) findViewById(R.id.imageViewTPedit);
            editIMG.setVisibility(View.INVISIBLE);
            // View LLedit = (View) findViewById(R.id.LLeditCBC);
            View LLview = (View) findViewById(R.id.LLViewTP);
            //  LLedit.setVisibility(View.VISIBLE);
            LLview.setVisibility(View.VISIBLE);
            A6.setImageDrawable(img);
            TPdownload();
            userSCR = "TP";


        } else if (TPselected == 1) {

            TPselected = 0;

            TextView sub = (TextView) findViewById(R.id.SubTxTP);
            sub.setVisibility(View.VISIBLE);
            Drawable img = getResources().getDrawable(R.drawable.arrorr);
            ImageView A6 = (ImageView) findViewById(R.id.imageViewA7);
            ImageView editIMG = (ImageView) findViewById(R.id.imageViewTPedit);
            View LLview = (View) findViewById(R.id.LLViewTP);
            LLview.setVisibility(View.GONE);
            A6.setImageDrawable(img);
            editIMG.setVisibility(View.GONE);
        }


    }

    public void TPdownload() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference TSHData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/ThyroidProfile/ThyroidStimulatingHormone");
        TSHData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValTSH = value;
                    TextView tx = (TextView) findViewById(R.id.textViewTSH);
                    tx.setText(ValTSH + " " + microS + "IU/ml");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewTSH);
                    tx.setText("-- " + microS + "IU/ml");
                }
                TPLoadCount = TPLoadCount + 1;
                TPProgressBar();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference TT4Data = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/ThyroidProfile/TotalT4");
        TT4Data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValTT4 = value;

                    TextView tx = (TextView) findViewById(R.id.textViewTT4);
                    tx.setText(ValTT4 + " " + microS + "g/dl");

                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewTT4);
                    tx.setText("-- " + microS + "g/dl");
                }
                TPLoadCount = TPLoadCount + 1;
                TPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference TT3Data = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/ThyroidProfile/TotalT3");
        TT3Data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValTT3 = value;
                    TextView tx = (TextView) findViewById(R.id.textViewTT3);
                    tx.setText(ValTT3 + "" + microS + "g/dl");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewTT3);
                    tx.setText("-- " + microS + "g/dl");
                }
                TPLoadCount = TPLoadCount + 1;
                TPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference FT4Data = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/ThyroidProfile/FreeT4");
        FT4Data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValFT4 = value;
                    TextView tx = (TextView) findViewById(R.id.textViewFT4);
                    tx.setText(ValFT4 + "ng/dl");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewFT4);
                    tx.setText("-- ng/dl");
                }
                TPLoadCount = TPLoadCount + 1;
                TPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference FT3Data = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/ThyroidProfile/FreeT3");
        FT3Data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValFT3 = value;
                    TextView tx = (TextView) findViewById(R.id.textViewFT3);
                    tx.setText(ValFT3 + "pg/dl");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewFT3);
                    tx.setText("-- pg/dl");
                }
                TPLoadCount = TPLoadCount + 1;
                TPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference TgData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/ThyroidProfile/Thyroglobuline");
        TgData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValTg = value;
                    TextView tx = (TextView) findViewById(R.id.textViewThyroTG);
                    tx.setText(ValTg + "ng/dl");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewThyroTG);
                    tx.setText("-- ng/dl");
                }
                TPLoadCount = TPLoadCount + 1;
                TPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference TgAbData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/ThyroidProfile/ThyroglobulineAntiBody");
        TgAbData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValTgAbAntiTg = value;
                    TextView tx = (TextView) findViewById(R.id.textViewTgAb);
                    tx.setText(ValTgAbAntiTg + "Present/Absent");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewTgAb);
                    tx.setText("-- Present/Absent");
                }
                TPLoadCount = TPLoadCount + 1;
                TPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public void TPProgressBar() {
        if (TPLoadCount == 7) {
            ProgressBar p = (ProgressBar) findViewById(R.id.progressBarTP);
            p.setVisibility(View.GONE);
            TPLoadCount = 0;
        }
    }

    public void TPedit(View v) {

        View AddMenu = (View) findViewById(R.id.LLAddMenu);
        AddMenu.setVisibility(View.VISIBLE);

        LinearLayout L1 = (LinearLayout) findViewById(R.id.LL1);
        L1.setVisibility(View.VISIBLE);

        LinearLayout L2 = (LinearLayout) findViewById(R.id.LL2);
        L2.setVisibility(View.VISIBLE);

        LinearLayout L3 = (LinearLayout) findViewById(R.id.LL3);
        L3.setVisibility(View.VISIBLE);

        LinearLayout L4 = (LinearLayout) findViewById(R.id.LL4);
        L4.setVisibility(View.VISIBLE);

        LinearLayout L5 = (LinearLayout) findViewById(R.id.LL5);
        L5.setVisibility(View.VISIBLE);

        LinearLayout L6 = (LinearLayout) findViewById(R.id.LL6);
        L6.setVisibility(View.VISIBLE);

        LinearLayout L7 = (LinearLayout) findViewById(R.id.LL7);
        L7.setVisibility(View.VISIBLE);


        TextView tx1 = (TextView) findViewById(R.id.txview1);
        EditText et1 = (EditText) findViewById(R.id.etview1);

        tx1.setText("Thyroid Stimulting Hormone(TSH)(" + microS + "IU/ml):");
        if (ValTSH != null) {
            et1.setText(ValTSH);
        }

        TextView tx2 = (TextView) findViewById(R.id.txview2);
        EditText et2 = (EditText) findViewById(R.id.etview2);
        tx2.setText("Total T4(" + microS + "g/dl):");
        if (ValTT4 != null) {
            et2.setText(ValTT4);
        }

        TextView tx3 = (TextView) findViewById(R.id.txview3);
        EditText et3 = (EditText) findViewById(R.id.etview3);
        tx3.setText("Total T3(" + microS + "g/dl):");
        if (ValTT3 != null) {
            et3.setText(ValTT3);
        }

        TextView tx4 = (TextView) findViewById(R.id.txview4);
        EditText et4 = (EditText) findViewById(R.id.etview4);
        tx4.setText("Free T4(ng/dl):");
        if (ValFT4 != null) {
            et4.setText(ValFT4);
        }

        TextView tx5 = (TextView) findViewById(R.id.txview5);
        EditText et5 = (EditText) findViewById(R.id.etview5);
        tx5.setText("Free T3(pg/ml):");
        if (ValFT3 != null) {
            et5.setText(ValFT3);
        }

        TextView tx6 = (TextView) findViewById(R.id.txview6);
        EditText et6 = (EditText) findViewById(R.id.etview6);
        tx6.setText("Thyroglobuline(Tg)(ng/ml):");
        if (ValTg != null) {
            et6.setText(ValTg);
        }


        TextView tx7 = (TextView) findViewById(R.id.txview7);
        EditText et7 = (EditText) findViewById(R.id.etview7);
        tx7.setText("Thyroglobulin Antibody(TgAb,Anti-TG): ");
        if (ValTgAbAntiTg != null) {
            et7.setText(ValTgAbAntiTg);
        }


    }

    public void TPUpdate() {

        EditText etTSH = (EditText) findViewById(R.id.etview1);
        EditText etTT4 = (EditText) findViewById(R.id.etview2);
        EditText etTT3 = (EditText) findViewById(R.id.etview3);
        EditText etFT4 = (EditText) findViewById(R.id.etview4);
        EditText etFT3 = (EditText) findViewById(R.id.etview5);
        EditText etTg = (EditText) findViewById(R.id.etview6);
        EditText etTgAb = (EditText) findViewById(R.id.etview7);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference TSHData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/ThyroidProfile/ThyroidStimulatingHormone");
        DatabaseReference TT4Data = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/ThyroidProfile/TotalT4");
        DatabaseReference TT3Data = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/ThyroidProfile/TotalT3");
        DatabaseReference FT4Data = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/ThyroidProfile/FreeT4");
        DatabaseReference FT3Data = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/ThyroidProfile/FreeT3");
        DatabaseReference TgData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/ThyroidProfile/Thyroglobuline");
        DatabaseReference TgAbData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/ThyroidProfile/ThyroglobulineAntiBody");


        TSHData.setValue(etTSH.getText().toString());
        TT4Data.setValue(etTT4.getText().toString());
        TT3Data.setValue(etTT3.getText().toString());
        FT4Data.setValue(etFT4.getText().toString());
        FT3Data.setValue(etFT3.getText().toString());
        TgData.setValue(etTg.getText().toString());
        TgAbData.setValue(etTgAb.getText().toString());


        finish();
        Intent ab = new Intent(this, HealthChartActivity.class);
        startActivity(ab);


    }


    // Liver Panel ==========================
    public void LivPclick(View v) {

        if (LivPselected == 0) {

            LivPselected = 1;

            TextView sub = (TextView) findViewById(R.id.SubTxLivP);
            sub.setVisibility(View.INVISIBLE);

            Drawable img = getResources().getDrawable(R.drawable.arrorrdown);
            ImageView A6 = (ImageView) findViewById(R.id.imageViewA8);
            ImageView editIMG = (ImageView) findViewById(R.id.imageViewLivPedit);
            editIMG.setVisibility(View.INVISIBLE);
            // View LLedit = (View) findViewById(R.id.LLeditCBC);
            View LLview = (View) findViewById(R.id.LLViewLivP);
            //  LLedit.setVisibility(View.VISIBLE);
            LLview.setVisibility(View.VISIBLE);
            A6.setImageDrawable(img);
            LivPdownload();
            userSCR = "LivP";


        } else if (LivPselected == 1) {

            LivPselected = 0;

            TextView sub = (TextView) findViewById(R.id.SubTxLivP);
            sub.setVisibility(View.VISIBLE);
            Drawable img = getResources().getDrawable(R.drawable.arrorr);
            ImageView A6 = (ImageView) findViewById(R.id.imageViewA8);
            ImageView editIMG = (ImageView) findViewById(R.id.imageViewLivPedit);
            View LLview = (View) findViewById(R.id.LLViewLivP);
            LLview.setVisibility(View.GONE);
            A6.setImageDrawable(img);
            editIMG.setVisibility(View.GONE);
        }


    }

    public void LivPdownload() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference BilirubinData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/LiverPanel/Bilirubin");
        BilirubinData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValBilirubin = value;
                    TextView tx = (TextView) findViewById(R.id.textViewBilirubin);
                    tx.setText(ValBilirubin + " mg/dl");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewBilirubin);
                    tx.setText("-- mg/dl");
                }
                LivPLoadCount = LivPLoadCount + 1;
                LivPProgressBar();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference SGOTData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/LiverPanel/SGOTAST");
        SGOTData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValSGOT = value;

                    TextView tx = (TextView) findViewById(R.id.textViewSGOT);
                    tx.setText(ValSGOT + " Units/L");

                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewSGOT);
                    tx.setText("-- Units/L");
                }
                LivPLoadCount = LivPLoadCount + 1;
                LivPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference SGPTData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/LiverPanel/SGPTALT");
        SGPTData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValSGPT = value;
                    TextView tx = (TextView) findViewById(R.id.textViewSGPT);
                    tx.setText(ValSGPT + "Unit/L");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewSGPT);
                    tx.setText("-- Unit/L");
                }
                LivPLoadCount = LivPLoadCount + 1;
                LivPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference AlkalinePhosphateData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/LiverPanel/AlkalinePhosphate");
        AlkalinePhosphateData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValAlkalinePhosphatase = value;
                    TextView tx = (TextView) findViewById(R.id.textViewAlkalinePhosphatase);
                    tx.setText(ValAlkalinePhosphatase + "Unit/L");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewAlkalinePhosphatase);
                    tx.setText("-- Unit/L");
                }
                LivPLoadCount = LivPLoadCount + 1;
                LivPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference GammaGlutData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/ThyroidProfile/GammaGlutamylTransaminase");
        GammaGlutData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValGammaGlutamylTransminase = value;
                    TextView tx = (TextView) findViewById(R.id.textViewGammaGlutamylTransaminase);
                    tx.setText(ValGammaGlutamylTransminase + " Unit/L");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewGammaGlutamylTransaminase);
                    tx.setText("-- Unit/L");
                }
                LivPLoadCount = LivPLoadCount + 1;
                LivPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public void LivPProgressBar() {
        if (LivPLoadCount == 5) {
            ProgressBar p = (ProgressBar) findViewById(R.id.progressBarLivP);
            p.setVisibility(View.GONE);
            LivPLoadCount = 0;
        }
    }

    public void LivPedit(View v) {

        View AddMenu = (View) findViewById(R.id.LLAddMenu);
        AddMenu.setVisibility(View.VISIBLE);

        LinearLayout L1 = (LinearLayout) findViewById(R.id.LL1);
        L1.setVisibility(View.VISIBLE);

        LinearLayout L2 = (LinearLayout) findViewById(R.id.LL2);
        L2.setVisibility(View.VISIBLE);

        LinearLayout L3 = (LinearLayout) findViewById(R.id.LL3);
        L3.setVisibility(View.VISIBLE);

        LinearLayout L4 = (LinearLayout) findViewById(R.id.LL4);
        L4.setVisibility(View.VISIBLE);

        LinearLayout L5 = (LinearLayout) findViewById(R.id.LL5);
        L5.setVisibility(View.VISIBLE);


        TextView tx1 = (TextView) findViewById(R.id.txview1);
        EditText et1 = (EditText) findViewById(R.id.etview1);

        tx1.setText("Bilirubin(mg/dl):");
        if (ValBilirubin != null) {
            et1.setText(ValBilirubin);
        }

        TextView tx2 = (TextView) findViewById(R.id.txview2);
        EditText et2 = (EditText) findViewById(R.id.etview2);
        tx2.setText("Serum Glutamic Oxaloacetic Transaminase(SGOT)/Aspartate Aminotransferase(AST)(Unit/L):");
        if (ValSGOT != null) {
            et2.setText(ValSGOT);
        }


        TextView tx3 = (TextView) findViewById(R.id.txview3);
        EditText et3 = (EditText) findViewById(R.id.etview3);
        tx3.setText("Serum Glutamic Pyruvic Transaminase(SGPT)/Alanine Aminotransferase(ALT)(Units/L):");
        if (ValSGPT != null) {
            et3.setText(ValSGPT);
        }

        TextView tx4 = (TextView) findViewById(R.id.txview4);
        EditText et4 = (EditText) findViewById(R.id.etview4);
        tx4.setText("Alkaline Phosphatase(Units/L)");
        if (ValAlkalinePhosphatase != null) {
            et4.setText(ValAlkalinePhosphatase);
        }

        TextView tx5 = (TextView) findViewById(R.id.txview5);
        EditText et5 = (EditText) findViewById(R.id.etview5);
        tx5.setText("Gamma Glutamyl Transaminase(Unit/L):");
        if (ValGammaGlutamylTransminase != null) {
            et5.setText(ValGammaGlutamylTransminase);
        }


    }

    public void LivPUpdate() {

        EditText etBili = (EditText) findViewById(R.id.etview1);
        EditText etSGOT = (EditText) findViewById(R.id.etview2);
        EditText etSGPT = (EditText) findViewById(R.id.etview3);
        EditText etAlkalineP = (EditText) findViewById(R.id.etview4);
        EditText etGammaGlu = (EditText) findViewById(R.id.etview5);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference BilirubinData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/LiverPanel/Bilirubin");
        DatabaseReference SGOTData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/LiverPanel/SGOTAST");
        DatabaseReference SGPTData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/LiverPanel/SGPTALT");
        DatabaseReference AlkalinePhosphateData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/LiverPanel/AlkalinePhosphate");
        DatabaseReference GammaGlutData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/ThyroidProfile/GammaGlutamylTransaminase");


        BilirubinData.setValue(etBili.getText().toString());
        SGOTData.setValue(etSGOT.getText().toString());
        SGPTData.setValue(etSGPT.getText().toString());
        AlkalinePhosphateData.setValue(etAlkalineP.getText().toString());
        GammaGlutData.setValue(etGammaGlu.getText().toString());


        finish();
        Intent ab = new Intent(this, HealthChartActivity.class);
        startActivity(ab);


    }


    // Serum Electrolytes ====================
    public void SEclick(View v) {

        if (SEselected == 0) {

            SEselected = 1;

            TextView sub = (TextView) findViewById(R.id.SubTxSE);
            sub.setVisibility(View.INVISIBLE);

            Drawable img = getResources().getDrawable(R.drawable.arrorrdown);
            ImageView A6 = (ImageView) findViewById(R.id.imageViewA9);
            ImageView editIMG = (ImageView) findViewById(R.id.imageViewSEedit);
            editIMG.setVisibility(View.INVISIBLE);
            // View LLedit = (View) findViewById(R.id.LLeditCBC);
            View LLview = (View) findViewById(R.id.LLViewSE);
            //  LLedit.setVisibility(View.VISIBLE);
            LLview.setVisibility(View.VISIBLE);
            A6.setImageDrawable(img);
            SEdownload();
            userSCR = "SE";


        } else if (SEselected == 1) {

            SEselected = 0;

            TextView sub = (TextView) findViewById(R.id.SubTxSE);
            sub.setVisibility(View.VISIBLE);
            Drawable img = getResources().getDrawable(R.drawable.arrorr);
            ImageView A6 = (ImageView) findViewById(R.id.imageViewA9);
            ImageView editIMG = (ImageView) findViewById(R.id.imageViewSEedit);
            View LLview = (View) findViewById(R.id.LLViewSE);
            LLview.setVisibility(View.GONE);
            A6.setImageDrawable(img);
            editIMG.setVisibility(View.GONE);
        }


    }

    public void SEdownload() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference NaData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/SerumElectrolytes/Sodium");
        NaData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValNA = value;
                    TextView tx = (TextView) findViewById(R.id.textViewNa);
                    tx.setText(ValNA + " mEq/L");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewNa);
                    tx.setText("-- mEq/L");
                }
                SELoadCount = SELoadCount + 1;
                SEProgressBar();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference KData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/SerumElectrolytes/Potassium");
        KData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValK = value;

                    TextView tx = (TextView) findViewById(R.id.textViewK);
                    tx.setText(ValK + " mEq/L");

                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewK);
                    tx.setText("-- mEq/L");
                }
                SELoadCount = SELoadCount + 1;
                SEProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference CaData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/SerumElectrolytes/Calcium");
        CaData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValCa = value;
                    TextView tx = (TextView) findViewById(R.id.textViewCa);
                    tx.setText(ValCa + " mEq/L");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewCa);
                    tx.setText("-- mEq/L");
                }
                SELoadCount = SELoadCount + 1;
                SEProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference ClData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/SerumElectrolytes/Chloride");
        ClData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValCl = value;
                    TextView tx = (TextView) findViewById(R.id.textViewCl);
                    tx.setText(ValCl + " mEq/L");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewCl);
                    tx.setText("-- mEq/L");
                }
                SELoadCount = SELoadCount + 1;
                SEProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference PData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/SerumElectrolytes/Phosphorous");
        PData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValP = value;
                    TextView tx = (TextView) findViewById(R.id.textViewP);
                    tx.setText(ValP + " mEq/L");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewP);
                    tx.setText("-- mEq/L");
                }
                SELoadCount = SELoadCount + 1;
                SEProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public void SEProgressBar() {
        if (SELoadCount == 5) {
            ProgressBar p = (ProgressBar) findViewById(R.id.progressBarSE);
            p.setVisibility(View.GONE);
            SELoadCount = 0;
        }
    }

    public void SEedit(View v) {

        View AddMenu = (View) findViewById(R.id.LLAddMenu);
        AddMenu.setVisibility(View.VISIBLE);

        LinearLayout L1 = (LinearLayout) findViewById(R.id.LL1);
        L1.setVisibility(View.VISIBLE);

        LinearLayout L2 = (LinearLayout) findViewById(R.id.LL2);
        L2.setVisibility(View.VISIBLE);

        LinearLayout L3 = (LinearLayout) findViewById(R.id.LL3);
        L3.setVisibility(View.VISIBLE);

        LinearLayout L4 = (LinearLayout) findViewById(R.id.LL4);
        L4.setVisibility(View.VISIBLE);

        LinearLayout L5 = (LinearLayout) findViewById(R.id.LL5);
        L5.setVisibility(View.VISIBLE);


        TextView tx1 = (TextView) findViewById(R.id.txview1);
        EditText et1 = (EditText) findViewById(R.id.etview1);

        tx1.setText("Sodium (Na+)(mEq/L):");
        if (ValNA != null) {
            et1.setText(ValNA);
        }

        TextView tx2 = (TextView) findViewById(R.id.txview2);
        EditText et2 = (EditText) findViewById(R.id.etview2);
        tx2.setText("Potassium(K+)(mEq/L)");
        if (ValK != null) {
            et2.setText(ValK);
        }


        TextView tx3 = (TextView) findViewById(R.id.txview3);
        EditText et3 = (EditText) findViewById(R.id.etview3);
        tx3.setText("Calcium(Ca+)(mEq/L)");
        if (ValCa != null) {
            et3.setText(ValCa);
        }

        TextView tx4 = (TextView) findViewById(R.id.txview4);
        EditText et4 = (EditText) findViewById(R.id.etview4);
        tx4.setText("Chloride(Cl-)(mEq/L)");
        if (ValCl != null) {
            et4.setText(ValCl);
        }

        TextView tx5 = (TextView) findViewById(R.id.txview5);
        EditText et5 = (EditText) findViewById(R.id.etview5);
        tx5.setText("Phosphorous(P-)(mEq/L):");
        if (ValP != null) {
            et5.setText(ValP);
        }


    }

    public void SEUpdate() {

        EditText etNa = (EditText) findViewById(R.id.etview1);
        EditText etK = (EditText) findViewById(R.id.etview2);
        EditText etCa = (EditText) findViewById(R.id.etview3);
        EditText etCl = (EditText) findViewById(R.id.etview4);
        EditText etP = (EditText) findViewById(R.id.etview5);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference NaData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/SerumElectrolytes/Sodium");
        DatabaseReference KData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/SerumElectrolytes/Potassium");
        DatabaseReference CaData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/SerumElectrolytes/Calcium");
        DatabaseReference ClData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/SerumElectrolytes/Chloride");
        DatabaseReference PData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/SerumElectrolytes/Phosphorous");


        NaData.setValue(etNa.getText().toString());
        KData.setValue(etK.getText().toString());
        CaData.setValue(etCa.getText().toString());
        ClData.setValue(etCl.getText().toString());
        PData.setValue(etP.getText().toString());


        finish();
        Intent ab = new Intent(this, HealthChartActivity.class);
        startActivity(ab);


    }


    // Vitamin Panel ======================
    public void VPclick(View v) {

        if (VPselected == 0) {

            VPselected = 1;

            TextView sub = (TextView) findViewById(R.id.SubTxVP);
            sub.setVisibility(View.INVISIBLE);

            Drawable img = getResources().getDrawable(R.drawable.arrorrdown);
            ImageView A6 = (ImageView) findViewById(R.id.imageViewA10);
            ImageView editIMG = (ImageView) findViewById(R.id.imageViewVPedit);
            editIMG.setVisibility(View.INVISIBLE);
            // View LLedit = (View) findViewById(R.id.LLeditCBC);
            View LLview = (View) findViewById(R.id.LLViewVP);
            //  LLedit.setVisibility(View.VISIBLE);
            LLview.setVisibility(View.VISIBLE);
            A6.setImageDrawable(img);
            VPdownload();
            userSCR = "VP";


        } else if (VPselected == 1) {

            VPselected = 0;

            TextView sub = (TextView) findViewById(R.id.SubTxVP);
            sub.setVisibility(View.VISIBLE);
            Drawable img = getResources().getDrawable(R.drawable.arrorr);
            ImageView A6 = (ImageView) findViewById(R.id.imageViewA10);
            ImageView editIMG = (ImageView) findViewById(R.id.imageViewVPedit);
            View LLview = (View) findViewById(R.id.LLViewVP);
            LLview.setVisibility(View.GONE);
            A6.setImageDrawable(img);
            editIMG.setVisibility(View.GONE);
        }


    }

    public void VPdownload() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference VitD3Data = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/VitaminPanel/VitaminD3");
        VitD3Data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValD3 = value;
                    TextView tx = (TextView) findViewById(R.id.textViewVD3);
                    tx.setText(ValD3 + " ng/ml");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewVD3);
                    tx.setText("-- ng/ml");
                }
                VPLoadCount = VPLoadCount + 1;
                VPProgressBar();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference VitB12Data = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/VitaminPanel/VitaminB12");
        VitB12Data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValB12 = value;

                    TextView tx = (TextView) findViewById(R.id.textViewVB12);
                    tx.setText(ValB12 + " pg/ml");

                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewVB12);
                    tx.setText("-- pg/ml");
                }
                VPLoadCount = VPLoadCount + 1;
                VPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }

    public void VPProgressBar() {
        if (VPLoadCount == 2) {
            ProgressBar p = (ProgressBar) findViewById(R.id.progressBarVP);
            p.setVisibility(View.GONE);
            VPLoadCount = 0;
        }
    }

    public void VPedit(View v) {

        View AddMenu = (View) findViewById(R.id.LLAddMenu);
        AddMenu.setVisibility(View.VISIBLE);

        LinearLayout L1 = (LinearLayout) findViewById(R.id.LL1);
        L1.setVisibility(View.VISIBLE);

        LinearLayout L2 = (LinearLayout) findViewById(R.id.LL2);
        L2.setVisibility(View.VISIBLE);


        TextView tx1 = (TextView) findViewById(R.id.txview1);
        EditText et1 = (EditText) findViewById(R.id.etview1);

        tx1.setText("Vitamin D3 (ng/ml):");
        if (ValD3 != null) {
            et1.setText(ValD3);
        }

        TextView tx2 = (TextView) findViewById(R.id.txview2);
        EditText et2 = (EditText) findViewById(R.id.etview2);
        tx2.setText("Vitamin D12 (pg/ml):");
        if (ValB12 != null) {
            et2.setText(ValB12);
        }


    }

    public void VPUpdate() {

        EditText etD3 = (EditText) findViewById(R.id.etview1);
        EditText etB12 = (EditText) findViewById(R.id.etview2);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference VitD3Data = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/VitaminPanel/VitaminD3");
        DatabaseReference VitB12Data = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/VitaminPanel/VitaminB12");

        VitD3Data.setValue(etD3.getText().toString());
        VitB12Data.setValue(etB12.getText().toString());


        finish();
        Intent ab = new Intent(this, HealthChartActivity.class);
        startActivity(ab);


    }


    // Iron Panel ========================
    public void IPclick(View v) {

        if (IPselected == 0) {

            IPselected = 1;

            TextView sub = (TextView) findViewById(R.id.SubTxIP);
            sub.setVisibility(View.INVISIBLE);

            Drawable img = getResources().getDrawable(R.drawable.arrorrdown);
            ImageView A6 = (ImageView) findViewById(R.id.imageViewA11);
            ImageView editIMG = (ImageView) findViewById(R.id.imageViewIPedit);
            editIMG.setVisibility(View.INVISIBLE);
            // View LLedit = (View) findViewById(R.id.LLeditCBC);
            View LLview = (View) findViewById(R.id.LLViewIP);
            //  LLedit.setVisibility(View.VISIBLE);
            LLview.setVisibility(View.VISIBLE);
            A6.setImageDrawable(img);
            IPdownload();
            userSCR = "IP";


        } else if (IPselected == 1) {

            IPselected = 0;

            TextView sub = (TextView) findViewById(R.id.SubTxIP);
            sub.setVisibility(View.VISIBLE);
            Drawable img = getResources().getDrawable(R.drawable.arrorr);
            ImageView A6 = (ImageView) findViewById(R.id.imageViewA11);
            ImageView editIMG = (ImageView) findViewById(R.id.imageViewIPedit);
            View LLview = (View) findViewById(R.id.LLViewIP);
            LLview.setVisibility(View.GONE);
            A6.setImageDrawable(img);
            editIMG.setVisibility(View.GONE);
        }


    }

    public void IPdownload() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference SerumIronData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/IronPanel/SerumIron");
        SerumIronData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValSeriumFe = value;
                    TextView tx = (TextView) findViewById(R.id.textViewSeriumFe);
                    tx.setText(ValSeriumFe + " " + microS + "g/dl");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewSeriumFe);
                    tx.setText("-- " + microS + "g/dl");
                }
                IPLoadCount = IPLoadCount + 1;
                IPProgressBar();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference SerumFerritinData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/IronPanel/SerumFerritin");
        SerumFerritinData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValSerumFerritin = value;

                    TextView tx = (TextView) findViewById(R.id.textViewSeriumFerritin);
                    tx.setText(ValSerumFerritin + " ng/ml");

                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewSeriumFerritin);
                    tx.setText("-- ng/ml");
                }
                IPLoadCount = IPLoadCount + 1;
                IPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference TIBCData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/IronPanel/TotalIronBindingCapacity");
        TIBCData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValTIBC = value;

                    TextView tx = (TextView) findViewById(R.id.textViewTotalIronBindingCapacity);
                    tx.setText(ValTIBC + " " + microS + "g/dl");

                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewTotalIronBindingCapacity);
                    tx.setText("-- " + microS + "g/dl");
                }
                IPLoadCount = IPLoadCount + 1;
                IPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference UIBCData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/IronPanel/UnsaturatedIronBindingCapacity");
        UIBCData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValUIBC = value;

                    TextView tx = (TextView) findViewById(R.id.textViewUnsaturatedIronBindingCapacity);
                    tx.setText(ValUIBC + " " + microS + "g/dl");

                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewUnsaturatedIronBindingCapacity);
                    tx.setText("-- " + microS + "g/dl");
                }
                IPLoadCount = IPLoadCount + 1;
                IPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public void IPProgressBar() {
        if (IPLoadCount == 4) {
            ProgressBar p = (ProgressBar) findViewById(R.id.progressBarIP);
            p.setVisibility(View.GONE);
            IPLoadCount = 0;
        }
    }

    public void IPedit(View v) {

        View AddMenu = (View) findViewById(R.id.LLAddMenu);
        AddMenu.setVisibility(View.VISIBLE);

        LinearLayout L1 = (LinearLayout) findViewById(R.id.LL1);
        L1.setVisibility(View.VISIBLE);

        LinearLayout L2 = (LinearLayout) findViewById(R.id.LL2);
        L2.setVisibility(View.VISIBLE);

        LinearLayout L3 = (LinearLayout) findViewById(R.id.LL3);
        L3.setVisibility(View.VISIBLE);

        LinearLayout L4 = (LinearLayout) findViewById(R.id.LL4);
        L4.setVisibility(View.VISIBLE);


        TextView tx1 = (TextView) findViewById(R.id.txview1);
        EditText et1 = (EditText) findViewById(R.id.etview1);

        tx1.setText("Serum Iron (" + microS + "g/dl):");
        if (ValSeriumFe != null) {
            et1.setText(ValSeriumFe);
        }

        TextView tx2 = (TextView) findViewById(R.id.txview2);
        EditText et2 = (EditText) findViewById(R.id.etview2);
        tx2.setText("Serum Ferritin (ng/dl):");
        if (ValSerumFerritin != null) {
            et2.setText(ValSerumFerritin);
        }

        TextView tx3 = (TextView) findViewById(R.id.txview3);
        EditText et3 = (EditText) findViewById(R.id.etview3);

        tx3.setText("Total Iron Binding Capacity (" + microS + "g/dl):");
        if (ValTIBC != null) {
            et3.setText(ValTIBC);
        }

        TextView tx4 = (TextView) findViewById(R.id.txview4);
        EditText et4 = (EditText) findViewById(R.id.etview4);
        tx4.setText("Unsaturated Iron Binding Capacity (" + microS + "g/dl):");
        if (ValUIBC != null) {
            et4.setText(ValUIBC);
        }

    }

    public void IPUpdate() {

        EditText etSerumFe = (EditText) findViewById(R.id.etview1);
        EditText etSerumFerratin = (EditText) findViewById(R.id.etview2);
        EditText etTIBC = (EditText) findViewById(R.id.etview3);
        EditText etUIBC = (EditText) findViewById(R.id.etview4);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference SerumIronData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/IronPanel/SerumIron");
        DatabaseReference SerumFerritinData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/IronPanel/SerumFerritin");
        DatabaseReference TIBCData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/IronPanel/TotalIronBindingCapacity");
        DatabaseReference UIBCData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/IronPanel/UnsaturatedIronBindingCapacity");


        SerumIronData.setValue(etSerumFe.getText().toString());
        SerumFerritinData.setValue(etSerumFerratin.getText().toString());
        TIBCData.setValue(etTIBC.getText().toString());
        UIBCData.setValue(etUIBC.getText().toString());

        finish();
        Intent ab = new Intent(this, HealthChartActivity.class);
        startActivity(ab);


    }

    // Enzyme Panel ========================
    public void EPclick(View v) {

        if (EPselected == 0) {

            EPselected = 1;

            TextView sub = (TextView) findViewById(R.id.SubTxEP);
            sub.setVisibility(View.INVISIBLE);

            Drawable img = getResources().getDrawable(R.drawable.arrorrdown);
            ImageView A6 = (ImageView) findViewById(R.id.imageViewA12);
            ImageView editIMG = (ImageView) findViewById(R.id.imageViewEPedit);
            editIMG.setVisibility(View.INVISIBLE);
            // View LLedit = (View) findViewById(R.id.LLeditCBC);
            View LLview = (View) findViewById(R.id.LLViewEP);
            //  LLedit.setVisibility(View.VISIBLE);
            LLview.setVisibility(View.VISIBLE);
            A6.setImageDrawable(img);
            EPdownload();
            userSCR = "EP";


        } else if (EPselected == 1) {

            EPselected = 0;

            TextView sub = (TextView) findViewById(R.id.SubTxEP);
            sub.setVisibility(View.VISIBLE);
            Drawable img = getResources().getDrawable(R.drawable.arrorr);
            ImageView A6 = (ImageView) findViewById(R.id.imageViewA12);
            ImageView editIMG = (ImageView) findViewById(R.id.imageViewEPedit);
            View LLview = (View) findViewById(R.id.LLViewEP);
            LLview.setVisibility(View.GONE);
            A6.setImageDrawable(img);
            editIMG.setVisibility(View.GONE);
        }


    }

    public void EPdownload() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference SerumLipaseData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/EnzymePanel/SerumLipase");
        SerumLipaseData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValSerumLipase = value;
                    TextView tx = (TextView) findViewById(R.id.textViewSeriumLipase);
                    tx.setText(ValSerumLipase + " U/L");
                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewSeriumLipase);
                    tx.setText("-- U/L");
                }
                EPLoadCount = EPLoadCount + 1;
                EPProgressBar();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference SerumAmylaseData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/EnzymePanel/SerumAmylase");
        SerumAmylaseData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValSerumAmylase = value;

                    TextView tx = (TextView) findViewById(R.id.textViewSeriumAmylase);
                    tx.setText(ValSerumAmylase + " U/L");

                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewSeriumAmylase);
                    tx.setText("-- U/L");
                }
                EPLoadCount = EPLoadCount + 1;
                EPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference LeptinData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/EnzymePanel/Leptin");
        LeptinData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    ValLeptin = value;

                    TextView tx = (TextView) findViewById(R.id.textViewLeptin);
                    tx.setText(ValLeptin + " U/L");

                } else {
                    TextView tx = (TextView) findViewById(R.id.textViewLeptin);
                    tx.setText("-- U/L");
                }
                EPLoadCount = EPLoadCount + 1;
                EPProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }

    public void EPProgressBar() {
        if (EPLoadCount == 3) {
            ProgressBar p = (ProgressBar) findViewById(R.id.progressBarEP);
            p.setVisibility(View.GONE);
            EPLoadCount = 0;
        }
    }

    public void EPedit(View v) {

        View AddMenu = (View) findViewById(R.id.LLAddMenu);
        AddMenu.setVisibility(View.VISIBLE);

        LinearLayout L1 = (LinearLayout) findViewById(R.id.LL1);
        L1.setVisibility(View.VISIBLE);

        LinearLayout L2 = (LinearLayout) findViewById(R.id.LL2);
        L2.setVisibility(View.VISIBLE);

        LinearLayout L3 = (LinearLayout) findViewById(R.id.LL3);
        L3.setVisibility(View.VISIBLE);


        TextView tx1 = (TextView) findViewById(R.id.txview1);
        EditText et1 = (EditText) findViewById(R.id.etview1);

        tx1.setText("Serum Lipase (U/L):");
        if (ValSerumLipase != null) {
            et1.setText(ValSerumLipase);
        }

        TextView tx2 = (TextView) findViewById(R.id.txview2);
        EditText et2 = (EditText) findViewById(R.id.etview2);
        tx2.setText("Serum Amylase (U/L):");
        if (ValSerumAmylase != null) {
            et2.setText(ValSerumAmylase);
        }

        TextView tx3 = (TextView) findViewById(R.id.txview3);
        EditText et3 = (EditText) findViewById(R.id.etview3);

        tx3.setText("Leptin (U/L):");
        if (ValLeptin != null) {
            et3.setText(ValLeptin);
        }


    }

    public void EPUpdate() {

        EditText etSerumLi = (EditText) findViewById(R.id.etview1);
        EditText etSerumAmy = (EditText) findViewById(R.id.etview2);
        EditText etLip = (EditText) findViewById(R.id.etview3);
        EditText etUIBC = (EditText) findViewById(R.id.etview4);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference SerumLipaseData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/EnzymePanel/SerumLipase");
        DatabaseReference SerumAmylaseData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/EnzymePanel/SerumAmylase");
        DatabaseReference LeptinData = database.getReference("app/users/" + ClientUserUID + "/data/HealthCharts/EnzymePanel/Leptin");


        SerumLipaseData.setValue(etSerumLi.getText().toString());
        SerumAmylaseData.setValue(etSerumAmy.getText().toString());
        LeptinData.setValue(etLip.getText().toString());

        finish();
        Intent ab = new Intent(this, HealthChartActivity.class);
        startActivity(ab);


    }


    public void save(View v) {

        if (userSCR.equals("BM")) {
            BMUpdate();
        } else if (userSCR.equals("BP")) {
            BPUpdate();
        } else if (userSCR.equals("SP")) {
            SPUpdate();
        } else if (userSCR.equals("LP")) {
            LPUpdate();
        } else if (userSCR.equals("CBC")) {
            CBCUpdate();
        } else if (userSCR.equals("RP")) {
            RPUpdate();
        } else if (userSCR.equals("TP")) {
            TPUpdate();
        } else if (userSCR.equals("LivP")) {
            LivPUpdate();
        } else if (userSCR.equals("SE")) {
            SEUpdate();
        } else if (userSCR.equals("VP")) {
            VPUpdate();
        } else if (userSCR.equals("IP")) {
            IPUpdate();
        } else if (userSCR.equals("EP")) {
            EPUpdate();
        }


    }

    public void close(View v) {
        finish();
        Intent ab = new Intent(this, HealthChartActivity.class);
        startActivity(ab);
    }


    // Medical History =========================
    public void GetHistoryData() {

        ImageView IMGadd1=(ImageView)findViewById(R.id.IMGadd1);
        ImageView IMGadd2=(ImageView)findViewById(R.id.IMGadd2);
        ImageView IMGadd3=(ImageView)findViewById(R.id.IMGadd3);
        ImageView IMGadd4=(ImageView)findViewById(R.id.IMGadd4);
        ImageView IMGadd5=(ImageView)findViewById(R.id.IMGadd5);
        ImageView IMGadd6=(ImageView)findViewById(R.id.IMGadd6);

        IMGadd1.setVisibility(View.GONE);
        IMGadd2.setVisibility(View.GONE);
        IMGadd3.setVisibility(View.GONE);
        IMGadd4.setVisibility(View.GONE);
        IMGadd5.setVisibility(View.GONE);
        IMGadd6.setVisibility(View.GONE);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataGD = database.getReference("app/users/" + ClientUserUID + "/data/MedicalHistory/GeneticDisorders");
        DatabaseReference dataKC = database.getReference("app/users/" + ClientUserUID + "/data/MedicalHistory/KnownConditions");
        DatabaseReference dataFH = database.getReference("app/users/" + ClientUserUID + "/data/MedicalHistory/FamilyHistory");
        DatabaseReference dataEA = database.getReference("app/users/" + ClientUserUID + "/data/MedicalHistory/EnvironmentalAllergies");
        DatabaseReference dataDA = database.getReference("app/users/" + ClientUserUID + "/data/MedicalHistory/DrugAllergies");
        DatabaseReference dataFA = database.getReference("app/users/" + ClientUserUID + "/data/MedicalHistory/FoodAllergies");

        final TextView TxDA = (TextView) findViewById(R.id.subText2);
        final TextView TxFA = (TextView) findViewById(R.id.subText1);
        final TextView TxGD = (TextView) findViewById(R.id.subText6);
        final TextView TxKC = (TextView) findViewById(R.id.subText5);
        final TextView TxFH = (TextView) findViewById(R.id.subText4);
        final TextView TxEA = (TextView) findViewById(R.id.subText3);

        dataDA.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                Load=Load+1;
                LoadCount();
                if (value != null) {
                    TextDA = value;
                    TxDA.setText(TextDA);

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        dataFA.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                Load=Load+1;
                LoadCount();
                if (value != null) {
                    TextFA = value;
                    TxFA.setText(TextFA);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        dataGD.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                Load=Load+1;
                LoadCount();
                if (value != null) {
                    TextGD = value;
                    TxGD.setText(TextGD);

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        dataKC.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                Load=Load+1;
                LoadCount();
                if (value != null) {
                    TextKC = value;
                    TxKC.setText(TextKC);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        dataFH.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                Load=Load+1;
                LoadCount();
                if (value != null) {
                    TextFH = value;
                    TxFH.setText(TextFH);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        dataEA.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                Load=Load+1;
                LoadCount();
                if (value != null) {
                    TextEA = value;
                    TxEA.setText(TextEA);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }
    public void LoadCount(){
        if(Load==6){
            View Loading = (View) findViewById(R.id.loading);
            Loading.setVisibility(View.GONE);
        }

    }



    // Medical Report===============

    public void MedicalReport(){

        FloatingActionButton UploadFab1=(FloatingActionButton)findViewById(R.id.MedUploadFABbutton);
        UploadFab1.setVisibility(View.GONE);


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference GetTotal = database.getReference("app/users/" + ClientUserUID + "/data/MedicalRecords/total");
        GetTotal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Total Value is: " + value);
                no = value;
if(no!=null){
    CreateView();
    View Loading = (View) findViewById(R.id.mrloading);
    //Loading.setVisibility(View.GONE);
                }



            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


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

                        DatabaseReference urldata = database.getReference("app/users/" + ClientUserUID + "/data/MedicalRecords/" + S + "/url");
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
                                    View LLMenu = (View) findViewById(R.id.LLMenu);
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

                    final CardView mcard = new CardView(ClientActivity.this);
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

                    LinearLayout LL = new LinearLayout(ClientActivity.this);
                    LL.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    LL.setOrientation(LinearLayout.HORIZONTAL);
                    LL.setPadding(30, 20, 30, 20);
                    mcard.addView(LL);

                    ImageView img = new ImageView(ClientActivity.this);
                    img.setImageDrawable(DOCDrawable);
                    // img.requestLayout();
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
                    img.setLayoutParams(layoutParams);
                    ///  img.getLayoutParams().height = 20;
                    //  img.getLayoutParams().width = 20;
                    LL.addView(img);

                    LinearLayout LL1 = new LinearLayout(ClientActivity.this);
                    LL1.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    LL1.setOrientation(LinearLayout.VERTICAL);
                    LL.addView(LL1);

                    final TextView tx = new TextView(ClientActivity.this);
                    tx.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));


                    tx.setTypeface(null, Typeface.BOLD);
                    tx.setTextSize(18);
                    tx.setText("Loading....");
                    LL1.addView(tx);


                    final TextView tx1 = new TextView(ClientActivity.this);
                    tx1.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    tx1.setText("Loading....");
                    LL1.addView(tx1);


                    // Write a message to the database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final int idj = j;
                    DatabaseReference title = database.getReference("app/users/" + ClientUserUID + "/data/MedicalRecords/" + j + "/title");
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

                    DatabaseReference desc = database.getReference("app/users/" + ClientUserUID+ "/data/MedicalRecords/" + j + "/desc");
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
downloading=1;
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(Durl));
        enqueue = dm.enqueue(request);
        Toast.makeText(ClientActivity.this, "Download in Progress",
                Toast.LENGTH_SHORT).show();


        //   Intent intent = new Intent(Intent.ACTION_VIEW);
     //   intent.setData(Uri.parse(Durl));
     //   startActivity(intent);
    }

    public void menuDownload() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        final TextView t1 = (TextView) findViewById(R.id.textView16TitleClient);
        final TextView t2 = (TextView) findViewById(R.id.textView17DescClient);

        t1.setText("Loading...");
        t2.setText("");

        DatabaseReference title = database.getReference("app/users/" + ClientUserUID + "/data/MedicalRecords/" + SlotNo + "/title");
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

        DatabaseReference desc = database.getReference("app/users/" + ClientUserUID + "/data/MedicalRecords/" + SlotNo + "/desc");
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
        View LLMenu = (View) findViewById(R.id.LLMenu);
        LLMenu.setVisibility(View.GONE);
    }

// Prescription ===============================

	public void GetPrescription(){
        FloatingActionButton UploadFab2=(FloatingActionButton)findViewById(R.id.PrescUploadFABbutton);
        UploadFab2.setVisibility(View.GONE);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference GetTotal = database.getReference("app/users/" + ClientUserUID + "/data/Prescriptions/total");
            GetTotal.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "Total Value is: " + value);
                    no = value;
if(no!=null){
	 PrescriptionCreateView();
}
                   


                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
          
		
	}
	
	  public void PrescriptionCreateView() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Drawable DOCDrawable = getResources().getDrawable(R.drawable.prescription512);

                LinearLayout myLayout = (LinearLayout) findViewById(R.id.PrescLLCardView);
                View.OnClickListener clicks = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String S = String.valueOf(v.getId());
                        SlotNo = S;
                        System.out.println(S);


                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference urldata = database.getReference("app/users/" + ClientUserUID + "/data/Prescriptions/" + S + "/url");
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


                                    View LLMenu = (View) findViewById(R.id.LLMenu);
                                    LLMenu.setVisibility(View.VISIBLE);
                                    ImageView im = (ImageView) findViewById(R.id.imageView);
                                    TextView tt = (TextView) findViewById(R.id.textView20client);
                                    tt.setText("Prescription");
                                    PrescmenuDownload();

                                    im.setImageDrawable(DOCDrawable);

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
                if (t == 1) {
                    View Loading = (View) findViewById(R.id.prescLoading);
                    Loading.setVisibility(View.GONE);

                }
                for (int j = 1; j < t; j++) {

                    final CardView mcard = new CardView(ClientActivity.this);
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

                    LinearLayout LL = new LinearLayout(ClientActivity.this);
                    LL.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    LL.setOrientation(LinearLayout.HORIZONTAL);
                    LL.setPadding(30, 20, 30, 20);
                    mcard.addView(LL);

                    ImageView img = new ImageView(ClientActivity.this);
                    img.setImageDrawable(DOCDrawable);
                    // img.requestLayout();
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
                    img.setLayoutParams(layoutParams);
                    ///  img.getLayoutParams().height = 20;
                    //  img.getLayoutParams().width = 20;
                    LL.addView(img);

                    LinearLayout LL1 = new LinearLayout(ClientActivity.this);
                    LL1.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    LL1.setOrientation(LinearLayout.VERTICAL);
                    LL.addView(LL1);

                    final TextView tx = new TextView(ClientActivity.this);
                    tx.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));


                    tx.setTypeface(null, Typeface.BOLD);
                    tx.setTextSize(18);
                    tx.setText("Loading....");
                    LL1.addView(tx);


                    final TextView tx1 = new TextView(ClientActivity.this);
                    tx1.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    tx1.setText("Loading....");
                    LL1.addView(tx1);


                    // Write a message to the database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final int idj = j;
                    DatabaseReference title = database.getReference("app/users/" + ClientUserUID + "/data/Prescriptions/" + j + "/title");
                    title.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            String value = dataSnapshot.getValue(String.class);
                            Log.d(TAG, "Value is: " + value);
                            if (value != null) {
                                tx.setText(value);
                                View Loading = (View) findViewById(R.id.prescLoading);
                                Loading.setVisibility(View.GONE);

                            } else {

                                CardView cv = (CardView) findViewById(idj);
                                cv.setVisibility(View.GONE);

                                View Loading = (View) findViewById(R.id.prescLoading);
                                Loading.setVisibility(View.GONE);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });

                    DatabaseReference desc = database.getReference("app/users/" + ClientUserUID + "/data/Prescriptions/" + j + "/desc");
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

  /*  public void LayoutDownload(View v) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Durl));
        startActivity(intent);
    }*/

   
    public void PrescmenuDownload() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final Drawable DOCDrawable = getResources().getDrawable(R.drawable.prescription512);
        ImageView im = (ImageView) findViewById(R.id.imageViewClient);
        im.setImageDrawable(DOCDrawable);


        final TextView t1 = (TextView) findViewById(R.id.textView16TitleClient);
        final TextView t2 = (TextView) findViewById(R.id.textView17DescClient);

        t1.setText("Loading..");
        t2.setText("");

        DatabaseReference title = database.getReference("app/users/" + ClientUserUID + "/data/Prescriptions/" + SlotNo + "/title");
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

        DatabaseReference desc = database.getReference("app/users/" + ClientUserUID + "/data/Prescriptions/" + SlotNo + "/desc");
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


    public void AdsLoad(){

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

   
	
}
