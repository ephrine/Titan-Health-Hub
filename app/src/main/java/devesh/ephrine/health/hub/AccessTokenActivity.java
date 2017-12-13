package devesh.ephrine.health.hub;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Random;

public class AccessTokenActivity extends AppCompatActivity {
    public final static int WIDTH = 500;
    public static final String EXTRA_MESSAGE_CLIENT_TOKEN = "324";
    public static final String EXTRA_MESSAGE_CLIENT_USER_UID = "dfgf";
    public static final String EXTRA_MESSAGE_APP_USER_NAME = "deveshfdub";
    public static final String EXTRA_MESSAGE_APP_USER_PIC_URL = "devub";


    public static int white = 0xFFFFFFFF;
    public static int black = 0xFF000000;
    public String TAG = "Ephrine Health Hub :";
    public String UserUID;
    public String OldToken;
    public String OldToken1;
    public String UserToken;
    public String ClientToken;
    public String ClientUserUID;
    public int GMV = 0;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private SurfaceView cameraView;
    private TextView barcodeInfo;
    public String AppUserName;
    public String AppUserPicUrl;

    // Access Data Valuess
    public String AccessDataTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_token);
        // TODO: Remove the redundant calls to getSupportActionBar()
        //       and use variable actionBar instead
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();


        Intent intent = getIntent();
        UserToken = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_USER_TOKEN);
        if (UserToken.equals("0")) {
            TextView Tx = (TextView) findViewById(R.id.textView57);
            Tx.setText("---");
        } else {
            TextView Tx = (TextView) findViewById(R.id.textView57);
            Tx.setText(UserToken);
            GenerateQR();
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    UserUID = user.getUid();
                    DownloadToken();
                    AppUserName=user.getDisplayName().toString();
                    AppUserPicUrl=user.getPhotoUrl().toString();
                  //  MyConnections();

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };


    }

    public void DownloadToken() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Token = database.getReference("app/users/" + UserUID + "/access/tokens/token");
        Token.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                //Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    UserToken = value;
                    TextView Tx = (TextView) findViewById(R.id.textView57);
                    Tx.setText(UserToken);
                    GenerateQR();
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

    public void GTButton(View v){
        Context context=AccessTokenActivity.this;
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);

        builder.setTitle("Generate New Access Token")
                .setMessage("This will delete old access token & create new one. previous token will be inaccessible, Do you want to Continue ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        GT();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(R.drawable.warning_ico)
                .show();
    }
    public void GT() {
        View Loading = (View) findViewById(R.id.LoadingView);
        Loading.setVisibility(View.VISIBLE);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Token = database.getReference("app/users/" + UserUID + "/access/tokens/token");
        OldToken1 = UserToken;

        Token.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                OldToken = value;

                GenerateToken();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }

    public void GenerateQR() {


        ImageView QRimg = (ImageView) findViewById(R.id.imageViewQR);
        try {
            Bitmap bitmap = encodeAsBitmap(UserToken);
            QRimg.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        Bitmap bitmap = null;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);

            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = result.get(x, y) ? black : white;
                }
            }
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, 500, 0, 0, w, h);
        } catch (Exception iae) {
            iae.printStackTrace();
            return null;
        }
        return bitmap;
    }

    public void qrScan(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);
                Log.d(TAG, "No Permission");
            } else if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Yes Permission ");

              ReadQR();
            }
        }else{
ReadQR();

        }


//ClientToken="48351038";
      //  Intent ab = new Intent(this, ClientActivity.class);
       // ab.putExtra(EXTRA_MESSAGE_CLIENT_TOKEN, ClientToken);
        //finish();
      //  startActivity(ab);

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
        Tcheck.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    DatabaseReference OldTokenBucket = database.getReference("app/TokenBucket/" + OldToken+"/UID");
                    OldTokenBucket.setValue("zero");

                    DatabaseReference OldTokenBucket1 = database.getReference("app/TokenBucket/" + OldToken1+"/UID");
                    OldTokenBucket1.setValue("zero");

                    DatabaseReference Token = database.getReference("app/users/" + UserUID + "/access/tokens/token");
                    DatabaseReference TokenBucket = database.getReference("app/TokenBucket/" + UserToken + "/UID");
                    Token.setValue(UserToken);
                    TokenBucket.setValue(UserUID);
                    TextView Tx = (TextView) findViewById(R.id.textView57);
                    Tx.setText(UserToken);
                    GenerateQR();
                    View Loading = (View) findViewById(R.id.LoadingView);
                    Loading.setVisibility(View.GONE);


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }

    public void nulll(View v) {
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

    public void ReadQR() {

        GMV = 1;
        View CameraV = (View) findViewById(R.id.QRCAM);
        CameraV.setVisibility(View.VISIBLE);
        EditText qredittext=(EditText)findViewById(R.id.qredittext);
    //   qredittext.setText("22511814");

        //setContentView(R.layout.qr_read);

        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        barcodeInfo = (TextView) findViewById(R.id.code_info);

        barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();

        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1024, 764)
                .setAutoFocusEnabled(true)
                .build();


        // cameraFocus(cameraSource, Camera.Parameters.FOCUS_MODE_AUTO);


        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(AccessTokenActivity.this, new String[]{android.Manifest.permission.CAMERA}, 1);
                            Log.d(TAG, "No Permission");
                        } else if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            Log.d(TAG, "Yes Permission ");
                            cameraSource.start(cameraView.getHolder());



                        }
                    }else{
                        cameraSource.start(cameraView.getHolder());

                    }


                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    barcodeInfo.post(new Runnable() {    // Use the post method of the TextView
                        public void run() {
                            barcodeInfo.setText(    // Update the TextView
                                    barcodes.valueAt(0).displayValue
                            );
                            ClientToken = barcodes.valueAt(0).displayValue;
                            EditText qredittext=(EditText)findViewById(R.id.qredittext);
                            qredittext.setText(ClientToken);
// StartClient1();

                            Toast.makeText(AccessTokenActivity.this, "Found it !! :)",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (GMV == 1) {
            cameraSource.release();
            barcodeDetector.release();
        }
    }

    public void StartClient(View v) {

        final ProgressDialog dialog = ProgressDialog.show(AccessTokenActivity.this, "",
                "Searching Database...Please wait ", true);
        EditText qredittext=(EditText)findViewById(R.id.qredittext);
String et=qredittext.getText().toString();
if(ClientToken==null){
    ClientToken=et;
}

if(ClientToken!=null){


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ClientTokenData = database.getReference("app/TokenBucket/" + ClientToken+"/UID");
// Read from the database

    ClientTokenData.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            String value = dataSnapshot.getValue(String.class);
            Log.d(TAG, "Client UID Value is: " + value);
            Log.d(TAG, "Intent Name Send Value is: " + AppUserName);
            Log.d(TAG, "Intent Pic URL Send Value is: " + AppUserPicUrl);
            if(value!=null){
                ClientUserUID=value;
                Intent ab = new Intent(AccessTokenActivity.this, ClientActivity.class);
                ab.putExtra(EXTRA_MESSAGE_CLIENT_TOKEN, ClientToken);
                ab.putExtra(EXTRA_MESSAGE_APP_USER_NAME, AppUserName);
                ab.putExtra(EXTRA_MESSAGE_APP_USER_PIC_URL, AppUserPicUrl);
               ab.putExtra(EXTRA_MESSAGE_CLIENT_USER_UID, ClientUserUID);
                dialog.dismiss();
                finish();
                startActivity(ab);

            }else{
                dialog.dismiss();
                Toast.makeText(AccessTokenActivity.this, "Access Token Invalid or incorrect",
                        Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        public void onCancelled(DatabaseError error) {
            // Failed to read value
            Log.w(TAG, "Failed to read value.", error.toException());
        }
    });

}

     /*   if(ClientToken!=null && et!=null){

            // Write a message to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ClientTokenData = database.getReference("app/TokenBucket/" + ClientToken+"/UID");
// Read from the database
            final Intent ab = new Intent(this, ClientActivity.class);

            ClientTokenData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "UID Value is: " + value);
                    if(value!=null){
                        ClientUserUID=value;
                        ab.putExtra(EXTRA_MESSAGE_CLIENT_TOKEN, ClientToken);
                        ab.putExtra(EXTRA_MESSAGE_CLIENT_USER_UID, ClientUserUID);
                        startActivity(ab);
                    }
                    else{
                        Toast.makeText(AccessTokenActivity.this, "Access Token Invalid or incorrect",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });



        } */


    }





    public void access(){
//ClientToken="48351038";
  Intent ab = new Intent(this, ClientActivity.class);
 ab.putExtra(EXTRA_MESSAGE_CLIENT_TOKEN, ClientToken);
        //finish();
startActivity(ab);

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
                    Toast.makeText(AccessTokenActivity.this, "Please Grant The Permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void MyConnections(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference GetTotalConnectionsData = database.getReference("app/users/" + UserUID + "/fav/total");
        GetTotalConnectionsData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if(value!=null){
AccessDataTotal=value;
                    CreateViewMyConnections();


                }else{

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public String ConnectionUserUid;
    public int ThumbImgID;
    public int TxID;

    public void CreateViewMyConnections() {

        LinearLayout LLAccessData = (LinearLayout) findViewById(R.id.LLAccessData);
        int t = Integer.valueOf(AccessDataTotal);

        //LinearLayout myLayout = (LinearLayout) findViewById(R.id.LLCardView);
        View.OnClickListener clicks = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String S = String.valueOf(v.getId());
                System.out.println(S);
int ID=1000+Integer.parseInt(S);
                TextView  Tx=(TextView)findViewById(ID);
                ClientToken=Tx.getText().toString();

                final ProgressDialog dialog = ProgressDialog.show(AccessTokenActivity.this, "",
                        "Getting Data...Please wait ", true);

// Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
/*                DatabaseReference ClientTokenData = database.getReference("app/users/" + UserUID + "/fav/" + S + "/token");
// Read from the database

                ClientTokenData.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        String value = dataSnapshot.getValue(String.class);

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });

*/


                DatabaseReference CTokenData = database.getReference("app/TokenBucket/" + ClientToken+"/UID");
// Read from the database

                CTokenData.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        String value = dataSnapshot.getValue(String.class);
                        Log.d(TAG, "Client UID Value is: " + value);
                        Log.d(TAG, "Intent Name Send Value is: " + AppUserName);
                        Log.d(TAG, "Intent Pic URL Send Value is: " + AppUserPicUrl);
                        if(value!=null){
                            ClientUserUID=value;
                            Intent ab = new Intent(AccessTokenActivity.this, ClientActivity.class);
                            ab.putExtra(EXTRA_MESSAGE_CLIENT_TOKEN, ClientToken);
                            ab.putExtra(EXTRA_MESSAGE_APP_USER_NAME, AppUserName);
                            ab.putExtra(EXTRA_MESSAGE_APP_USER_PIC_URL, AppUserPicUrl);
                            ab.putExtra(EXTRA_MESSAGE_CLIENT_USER_UID, ClientUserUID);
                            dialog.dismiss();
                            finish();
                            startActivity(ab);

                        }else{
                            dialog.dismiss();
                            Toast.makeText(AccessTokenActivity.this, "Access Token Invalid or incorrect",
                                    Toast.LENGTH_SHORT).show();

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


        for (int j = 1; j < t; j++) {
            Drawable SampleDrawable = getResources().getDrawable(R.drawable.account);

            LinearLayout LL = new LinearLayout(AccessTokenActivity.this);
            LL.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            LL.setOrientation(LinearLayout.HORIZONTAL);
            LL.setPadding(30, 20, 30, 20);
            LL.setId(j);
            LL.setOnClickListener(clicks);
            LLAccessData.addView(LL);

            ImageView img = new ImageView(AccessTokenActivity.this);
            img.setImageDrawable(SampleDrawable);  // Profile Pic
            // img.requestLayout();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
            img.setLayoutParams(layoutParams);
            ///  img.getLayoutParams().height = 20;
            //  img.getLayoutParams().width = 20;
            ThumbImgID=10000+j;
            img.setId(ThumbImgID);
            LL.addView(img);

            LinearLayout LL1 = new LinearLayout(AccessTokenActivity.this);
            LL1.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            LL1.setOrientation(LinearLayout.VERTICAL);
            LL.addView(LL1);

            final TextView tx = new TextView(AccessTokenActivity.this);
            tx.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));


            tx.setTypeface(null, Typeface.BOLD);
            tx.setTextSize(18);
            tx.setText("Loading....");  // Name
            LL1.addView(tx);


            final TextView tx1 = new TextView(AccessTokenActivity.this);
            tx1.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            TxID=10000+j;
tx1.setId(TxID);
            tx1.setText("Loading....");   // Date
             LL1.addView(tx1);

  /*          final TextView tx2 = new TextView(AccessTokenActivity.this);
            tx2.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            tx2.setText("Loading....");   // Time
            // LL1.addView(tx2);
*/

            // Write a message to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final int idj = j;
            DatabaseReference name = database.getReference("app/users/" + UserUID + "/fav/" + j + "/name");
            name.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "Value is: " + value);
                    if (value != null) {
                        tx.setText(value);
                        // View Loading = (View) findViewById(R.id.mrloading);
                        //Loading.setVisibility(View.GONE);

                    } else {

                        LinearLayout cv = (LinearLayout) findViewById(idj);
                        cv.setVisibility(View.GONE);
                        //                    View Loading = (View) findViewById(R.id.mrloading);
                        //Loading.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });

            DatabaseReference TokenData = database.getReference("app/users/" + UserUID + "/fav/" + j + "/token");
            TokenData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "Connection UID Value is: " + value);
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


   /*        DatabaseReference time = database.getReference("app/users/" + UserUID + "/accessdata/"+j+"/time");
           time.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   // This method is called once with the initial value and again
                   // whenever data at this location is updated.
                   String value = dataSnapshot.getValue(String.class);
                   Log.d(TAG, "Value is: " + value);
                   if (value != null) {
                       tx2.setText(value);

                   }
               }

               @Override
               public void onCancelled(DatabaseError error) {
                   // Failed to read value
                   Log.w(TAG, "Failed to read value.", error.toException());
               }
           });
*/
  /*         DatabaseReference ProfileIMG = database.getReference("app/users/" + UserUID + "/accessdata/"+j+"/time");
           ProfileIMG.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   // This method is called once with the initial value and again
                   // whenever data at this location is updated.
                   String value = dataSnapshot.getValue(String.class);
                   Log.d(TAG, "Value is: " + value);
                   if (value != null) {
                       tx2.setText(value);

                   }
               }

               @Override
               public void onCancelled(DatabaseError error) {
                   // Failed to read value
                   Log.w(TAG, "Failed to read value.", error.toException());
               }
           });
*/

        }
    }

    //Load Connection Thumb Image
public void ConnectionThumbPic(){
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference Picdate = database.getReference("app/users/" + ConnectionUserUid + "/pic");
    Picdate.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            String value = dataSnapshot.getValue(String.class);
            Log.d(TAG, " Thumb Pic Value is: " + value);
            if (value != null) {
ImageView img=(ImageView)findViewById(ThumbImgID);
                Picasso.with(AccessTokenActivity.this).load(value).into(img);


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



