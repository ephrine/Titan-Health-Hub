package devesh.ephrine.health.hub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MedicalHistoryActivity extends AppCompatActivity {
    public String TAG = "Ephrine Health Hub :";
    public String UserUID;
    public String clk;
    public String TextGD;
    public String TextKC;
    public String TextFH;
    public String TextEA;
    public String TextDA;
    public String TextFA;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public int Load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history);
        View Loading=(View)findViewById(R.id.loading);
        Loading.setVisibility(View.VISIBLE);

        clk = "0";
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
Load=0;

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    UserUID = user.getUid().toString();
                    GetHistoryData();

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };

    }

    public void ClickFA(View v) {
        clk = "FA";
        View Menu = (View) findViewById(R.id.ViewMenu);
        Menu.setVisibility(View.VISIBLE);

        EditText et = (EditText) findViewById(R.id.editText4);
        if(TextFA!=null){
            et.setText(TextFA);
        }

        TextView eg = (TextView) findViewById(R.id.textView51);
        eg.setText("Example: Peanuts, Gluten, Curd. etc");

        TextView Title = (TextView) findViewById(R.id.textView48Title);
        TextView SubTitle = (TextView) findViewById(R.id.textView48SubT);
        Title.setText("Food Allergies");
        SubTitle.setText("Add Your Food Allergies, Seprated by Comma:");


    }

    public void ClickDA(View v) {
        clk = "DA";
        View Menu = (View) findViewById(R.id.ViewMenu);
        Menu.setVisibility(View.VISIBLE);

        EditText et = (EditText) findViewById(R.id.editText4);
        if(TextDA!=null){
            et.setText(TextDA);

        }


        TextView eg = (TextView) findViewById(R.id.textView51);
        eg.setText("Example: Antibiotics, Penicillin, Psychiatric Drugs, etc");


        TextView Title = (TextView) findViewById(R.id.textView48Title);
        TextView SubTitle = (TextView) findViewById(R.id.textView48SubT);
        Title.setText("Drug Allergies");
        SubTitle.setText("Add Your Drug Allergies, Seprated by Comma:");


    }

    public void ClickEA(View v) {
        clk = "EA";
        View Menu = (View) findViewById(R.id.ViewMenu);
        Menu.setVisibility(View.VISIBLE);

        EditText et = (EditText) findViewById(R.id.editText4);
        if(TextEA!=null){
            et.setText(TextEA);
        }

        TextView eg = (TextView) findViewById(R.id.textView51);
        eg.setText("Example: Cosmetics, Dust, Latex, Insect sting, etc");

        TextView Title = (TextView) findViewById(R.id.textView48Title);
        TextView SubTitle = (TextView) findViewById(R.id.textView48SubT);
        Title.setText("Environmental Allergies");
        SubTitle.setText("Add Your Environmental Allergies, Seprated by Comma:");


    }

    public void ClickFH(View v) {
        clk = "FH";
        View Menu = (View) findViewById(R.id.ViewMenu);
        Menu.setVisibility(View.VISIBLE);

        EditText et = (EditText) findViewById(R.id.editText4);
        if(TextFH!=null) {
            et.setText(TextFH);
        }
        TextView eg = (TextView) findViewById(R.id.textView51);
        eg.setText("Example: Alzheimer's Disease, Asthma, Depression, Mental Health History, etc");

        TextView Title = (TextView) findViewById(R.id.textView48Title);
        TextView SubTitle = (TextView) findViewById(R.id.textView48SubT);
        Title.setText("Family History");
        SubTitle.setText("Add Your Family History, Seprated by Comma:");


    }

    public void ClickKC(View v) {
        clk = "KC";
        View Menu = (View) findViewById(R.id.ViewMenu);
        Menu.setVisibility(View.VISIBLE);

        EditText et = (EditText) findViewById(R.id.editText4);
        if(TextKC!=null){
            et.setText(TextKC);

        }

        TextView eg = (TextView) findViewById(R.id.textView51);
        eg.setText("Example: Asthma, Diabetes, Epilepsy, etc");


        TextView Title = (TextView) findViewById(R.id.textView48Title);
        TextView SubTitle = (TextView) findViewById(R.id.textView48SubT);
        Title.setText("Known Conditions");
        SubTitle.setText("Add Your Known Conditions, Seprated by Comma:");


    }

    public void ClickGD(View v) {
        clk = "GD";
        View Menu = (View) findViewById(R.id.ViewMenu);
        Menu.setVisibility(View.VISIBLE);
        EditText et = (EditText) findViewById(R.id.editText4);
        if(TextGD!=null){
            et.setText(TextGD);
        }

        TextView eg = (TextView) findViewById(R.id.textView51);
        eg.setText("Example: Color Blindness, Haemophilia, Sickle Cell Anaemia, etc");

        TextView Title = (TextView) findViewById(R.id.textView48Title);
        TextView SubTitle = (TextView) findViewById(R.id.textView48SubT);
        Title.setText("Genetic Disorders");
        SubTitle.setText("Add Your Genetic Disorders, Seprated by Comma:");


    }


    public void SaveData(View v) {
        EditText et = (EditText) findViewById(R.id.editText4);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (clk.equals("FA")) {
            DatabaseReference data = database.getReference("app/users/" + UserUID + "/data/MedicalHistory/FoodAllergies");
            data.setValue(et.getText().toString());
            finish();
            Intent ab = new Intent(this, MedicalHistoryActivity.class);
            startActivity(ab);

        } else if (clk.equals("DA")) {
            DatabaseReference data = database.getReference("app/users/" + UserUID + "/data/MedicalHistory/DrugAllergies");
            data.setValue(et.getText().toString());
            finish();
            Intent ab = new Intent(this, MedicalHistoryActivity.class);
            startActivity(ab);

        } else if (clk.equals("EA")) {
            DatabaseReference data = database.getReference("app/users/" + UserUID + "/data/MedicalHistory/EnvironmentalAllergies");
            data.setValue(et.getText().toString());
            finish();
            Intent ab = new Intent(this, MedicalHistoryActivity.class);
            startActivity(ab);

        } else if (clk.equals("FH")) {
            DatabaseReference data = database.getReference("app/users/" + UserUID + "/data/MedicalHistory/FamilyHistory");
            data.setValue(et.getText().toString());
            finish();
            Intent ab = new Intent(this, MedicalHistoryActivity.class);
            startActivity(ab);

        } else if (clk.equals("KC")) {
            DatabaseReference data = database.getReference("app/users/" + UserUID + "/data/MedicalHistory/KnownConditions");
            data.setValue(et.getText().toString());
            finish();
            Intent ab = new Intent(this, MedicalHistoryActivity.class);
            startActivity(ab);

        } else if (clk.equals("GD")) {
            DatabaseReference data = database.getReference("app/users/" + UserUID + "/data/MedicalHistory/GeneticDisorders");
            data.setValue(et.getText().toString());
            finish();
            Intent ab = new Intent(this, MedicalHistoryActivity.class);
            startActivity(ab);
        }


    }

    public void GetHistoryData() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataGD = database.getReference("app/users/" + UserUID + "/data/MedicalHistory/GeneticDisorders");
        DatabaseReference dataKC = database.getReference("app/users/" + UserUID + "/data/MedicalHistory/KnownConditions");
        DatabaseReference dataFH = database.getReference("app/users/" + UserUID + "/data/MedicalHistory/FamilyHistory");
        DatabaseReference dataEA = database.getReference("app/users/" + UserUID + "/data/MedicalHistory/EnvironmentalAllergies");
        DatabaseReference dataDA = database.getReference("app/users/" + UserUID + "/data/MedicalHistory/DrugAllergies");
        DatabaseReference dataFA = database.getReference("app/users/" + UserUID + "/data/MedicalHistory/FoodAllergies");

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

    public void CloseData(View v) {
        finish();
        Intent ab = new Intent(this, MedicalHistoryActivity.class);
        startActivity(ab);
    }

    public void LoadCount(){
        if(Load==6){
            View Loading = (View) findViewById(R.id.loading);
            Loading.setVisibility(View.GONE);
        }

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


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }


}
