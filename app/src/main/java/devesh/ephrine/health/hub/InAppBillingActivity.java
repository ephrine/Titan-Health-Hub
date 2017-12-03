package devesh.ephrine.health.hub;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import devesh.ephrine.health.hub.util.IabHelper;
import devesh.ephrine.health.hub.util.IabResult;
import devesh.ephrine.health.hub.util.Inventory;
import devesh.ephrine.health.hub.util.Purchase;



public class InAppBillingActivity extends AppCompatActivity {
    private static final String TAG = "Ephrine Health Hub";
    IabHelper mHelper;
    //static final String ITEM_SKU = "android.test.purchased";
    static final String ITEM_SKU = "1001";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_billing);
        setFonts();
    }


    @Override
    protected void onStart() {
        super.onStart();

        String base64EncodedPublicKey =
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgVhaeCsYFGJZAXP6qW7SLiCEn2PxP0R35naAq7LiNouHngz+wZ5xicmNA+9yqMh5FlcWyWc87lDQOLwMbFLv03nlh/b/bcTuz16I/ispF71u9o+b4tWuBLo21LNZBYMD0eXcbNBT/fN7KVkYm/ac/p02L3J1yQET/GWjSbqronnps0HnrA6C+DR4vQStM20/Oi7mIUCq0ktmxKUCQ9kwceWijIFVDGV6UwfAXT0edtGhokzrTA9eD9TpQWSCYvUjDNwKNBPEMeiAR6DuUoRzkw+Y2FKidFqJ4iFX4u8901SQKbeS9pcJ+b2FXOHlPvwBqtMy12+3YolKJdrIFVHHAQIDAQAB";

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new
                                   IabHelper.OnIabSetupFinishedListener() {
                                       public void onIabSetupFinished(IabResult result)
                                       {
                                           if (!result.isSuccess()) {
                                               Log.d(TAG, "In-app Billing setup failed:" +
                                                       result);
                                           } else {
                                               Log.d(TAG, "In-app Billing OK");
                                           }
                                       }
                                   });



    }
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                Log.d(TAG, "In-app Billing Error 1");

                // Handle error
                return;
            }
            else if (purchase.getSku().equals(ITEM_SKU)) {
                consumeItem();
                Log.d(TAG, "In-app Billing success OK");

            }

        }
    };
    public void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }
    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {


            if (result.isFailure()) {
                // Handle failure
                Log.d(TAG, "In-app Billing Error 2");

            } else {
                mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
                        mConsumeFinishedListener);
            }
        }
    };
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {
                        Log.d(TAG, "In-app Billing success 1");


                    } else {

                        // handle error
                        Log.d(TAG, "In-app Billing Error 3");

                    }
                }
            };

    public void pay(View v){
        mHelper.launchPurchaseFlow(this, ITEM_SKU, 1001,
                mPurchaseFinishedListener, "mypurchasetoken");
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    public void setFonts(){
        TextView tx = (TextView)findViewById(R.id.HeadingtextView77);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/RobotoBold.ttf");

        tx.setTypeface(custom_font);
    }
}
