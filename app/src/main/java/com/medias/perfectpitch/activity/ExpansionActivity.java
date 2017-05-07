package com.medias.perfectpitch.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.medias.perfectpitch.R;
import com.medias.perfectpitch.databinding.ActivityExpansionBinding;
import com.medias.perfectpitch.observable.Expansion;
import com.medias.perfectpitch.utils.Typefaces;
import com.medias.perfectpitch.utils.UiHelper;
import com.medias.perfectpitch.utils.iab.IabBroadcastReceiver;
import com.medias.perfectpitch.utils.iab.IabBroadcastReceiver.IabBroadcastListener;
import com.medias.perfectpitch.utils.iab.IabHelper;
import com.medias.perfectpitch.utils.iab.IabResult;
import com.medias.perfectpitch.utils.iab.Inventory;
import com.medias.perfectpitch.utils.iab.Purchase;

public class ExpansionActivity extends AppCompatActivity implements IabBroadcastListener {

    private static final String SKU_TWELVE_NOTES = "sku_twelve_notes";
    private static final String SKU_TONES = "sku_tones";
    private static final int RC_REQUEST = 10001;

    private ActivityExpansionBinding binding;
    private Expansion observable;

    private IabHelper mHelper;
    private IabBroadcastReceiver mBroadcastReceiver;
    private IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            if (mHelper == null) {
                return;
            }
            if (result.isFailure()) {
                return;
            }
            Purchase tonesPurchase = inventory.getPurchase(SKU_TONES);
            observable.setTonesPurchased(tonesPurchase != null && verifyDeveloperPayload(tonesPurchase));

            Purchase twelveNotesPurchase = inventory.getPurchase(SKU_TWELVE_NOTES);
            observable.setTwelveNotesPurchased(twelveNotesPurchase != null && verifyDeveloperPayload(twelveNotesPurchase));
        }
    };
    private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (mHelper == null) {
                return;
            }
            if (result.isFailure()) {
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                return;
            }
            try {
                if (purchase.getSku().equals(SKU_TONES)) {
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                } else if (purchase.getSku().equals(SKU_TWELVE_NOTES)) {
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                }
            } catch (IabHelper.IabAsyncInProgressException e) {
                return;
            }
        }
    };
    private IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            if (mHelper == null) return;
            if (result.isSuccess()) {
                if (purchase.getSku().equals(SKU_TONES))
                    observable.setTonesPurchased(true);
                else if (purchase.getSku().equals(SKU_TWELVE_NOTES)) {
                    observable.setTwelveNotesPurchased(true);
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trns_from_top, R.anim.trns_to_bottom);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_expansion);
        observable = new Expansion(this);
        binding.setObservable(observable);
        binding.setHandler(this);
        UiHelper.overrideTextFonts(binding.layoutMain, Typefaces.ptSans(this));
        //observable.setTonesPurchased(true); //DEBUG
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArQQnjR7ffREoG1R4BOKoBso37T6m/l3/SALJ/CyRfYF4yrkx5x8x/I/5TW9yXSD9hi6nnpXRAWkQIfsWwjBU7wSoXCVI6kzGLx5AXlAY9tSAoVsy9MsAW9hat62lBiYcYXc4rUt159rHO/ZPZmjQTw7Yqf/viO32OplgsKAVU9v4xg31q17MpqoofnTrlZe4UHauuRbtOFpQKrxbg++0kHiQ98vWUvHTdG+VRO+Si0UDa7KElgvkGNLdC2MzdASdzdtM37WCDjbD2xKlfniU4PHfsomUaiTiHfMAEApv3aH8oEwlmHNtkQTH/Yyg7R1mfe0DQV726Q2Fp4eMLyLmKwIDAQAB";
        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    return;
                }
                if (mHelper == null) {
                    return;
                }
                mBroadcastReceiver = new IabBroadcastReceiver(ExpansionActivity.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {

                }
                if (mHelper == null) {
                    return;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mHelper == null) return;
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void receivedBroadcast() {
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {

        }
    }

    public void onTonesPressed(View view) {
        String payload = observable.getEmail();
        try {
            mHelper.launchPurchaseFlow(this, SKU_TONES, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {

        }
    }

    public void onTwelveNotesPressed(View view) {
        String payload = observable.getEmail();
        try {
            mHelper.launchPurchaseFlow(this, SKU_TWELVE_NOTES, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {

        }
    }

    private boolean verifyDeveloperPayload(Purchase p) {
        return observable.getEmail() == p.getDeveloperPayload();
    }


}
