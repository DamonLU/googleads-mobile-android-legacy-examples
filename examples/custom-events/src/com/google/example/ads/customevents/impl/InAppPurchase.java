// Copyright 2013 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.example.ads.customevents.impl;

import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventBanner;
import com.google.ads.mediation.customevent.CustomEventBannerListener;
import com.google.example.ads.customevents.IabActivity;
import com.google.example.ads.customevents.Utils;
import com.google.example.ads.customevents.impl.iab.IabHelper;
import com.google.example.ads.customevents.impl.iab.IabResult;
import com.google.example.ads.customevents.impl.iab.Inventory;
import com.google.example.ads.customevents.impl.iab.Purchase;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Custom event that invokes an in-app purchase request if clicked. If the in-app purchase is made,
 * the custom event will no longer serve the in-app purchase request, unless the user clears the
 * purchase.
 *
 * NOTE: This custom event is only supported for activities that extend from {@link IabActivity}.
 *
 */
public class InAppPurchase implements CustomEventBanner {
  /** Logtag used for logging statements. */
  private static final String LOGTAG = "InAppPurchase";

  /**
   * Your application key used for making in-app billing requests. You can use a dummy key for
   * test requests.
   */
  private static final String BASE_64_ENCODED_PUBLIC_KEY =
      "MY_APPLICATION_PUBLIC_KEY_USED_FOR_REAL_IAB_REQUESTS";

  /** Test SKU for our a successful in app purchase. */
  private static final String SKU_TEST_SUCCESSFUL_PURCHASE = "android.test.purchased";

  /** Arbitrary request code for the purchase flow. */
  private static final int RC_REQUEST = 10001;

  /** Key used for shared preferences to store whether or not the test purchase has been made. */
  public static final String SHARED_PREFERENCES_NAME_TEST_PURCHASE = "testPurchase";

  /** Helper used to interface with the Google In-app Billing API. */
  private IabHelper iabHelper;

  /** The {@link IabActivity} for which a banner is being requested. */
  private IabActivity iabActivity;

  /** The Custom event listener used to notify AdMob Mediation of ad events. */
  private CustomEventBannerListener bannerListener;

  /**
   * Requests a banner ad. This method is called by AdMob Mediation when it selects your custom
   * event network and asks for an ad.
   */
  @Override
  public void requestBannerAd(final CustomEventBannerListener bannerListener,
                              final Activity activity,
                              String label,
                              String unusedServerParameter,
                              AdSize adSize,
                              MediationAdRequest mediationAdRequest,
                              Object customEventExtra) {
    this.bannerListener = bannerListener;
    try {
      iabActivity = (IabActivity) activity;
    } catch (ClassCastException exception) {
      Log.e(LOGTAG, String.format("The in-app billing custom event only supports IabActivity."));
      bannerListener.onFailedToReceiveAd();
      return;
    }

    Utils.logAndToast(activity, LOGTAG, "Checking if purchase was already made...");
    if (madeTestPurchase()) {
      Utils.logAndToast(activity, LOGTAG, "Purchase made. No need to show the ad.");
      bannerListener.onFailedToReceiveAd();
    } else {
      // Start the in-app billing flow.
      iabHelper = new IabHelper(activity, BASE_64_ENCODED_PUBLIC_KEY);
      iabActivity.setIabHelper(iabHelper);
      iabHelper.enableDebugLogging(true);
      iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
        @Override
        public void onIabSetupFinished(IabResult result) {
          if (!result.isSuccess()) {
            Utils.logAndToast(activity, LOGTAG, "Problem setting up in-app billing: " + result);
            bannerListener.onFailedToReceiveAd();
            return;
          }
          iabHelper.queryInventoryAsync(mGotInventoryListener);
        }
      });
    }
  }

  /**
   * Creates the in app purchase image with an onClick action to launch the in-app purchase flow.
   * @return An {@link ImageView} to be passed back to AdMob Mediation as the ad.
   */
  private View createInAppPurchaseAd() {
    RelativeLayout layout = new RelativeLayout(iabActivity);
    TextView textView = new TextView(iabActivity);
    textView.setText("Want to remove ads?");
    textView.setTextSize(16);
    RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(
        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    textViewParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
    textViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
    layout.addView(textView, textViewParams);
    Button purchaseButton = new Button(iabActivity);
    purchaseButton.setText("Upgrade!");
    RelativeLayout.LayoutParams purchaseButtonParams = new RelativeLayout.LayoutParams(
        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    purchaseButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    layout.addView(purchaseButton, purchaseButtonParams);
    purchaseButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        bannerListener.onClick();
        if (madeTestPurchase()) {
          Utils.logAndToast(iabActivity, LOGTAG, "Purchase already made.");
        } else {
          // TODO: For security, generate your payload here for verification. See the comments on
          // verifyDeveloperPayload() for more info. Since this is a sample, we just use an empty
          // string, but on a production app you should carefully generate this.
          String payload = "";
          iabHelper.launchPurchaseFlow(iabActivity, SKU_TEST_SUCCESSFUL_PURCHASE, RC_REQUEST,
              mPurchaseFinishedListener, payload);
        }
      }
    });
    return layout;
  }

  // Called when we finish querying the items and subscriptions we own.
  IabHelper.QueryInventoryFinishedListener mGotInventoryListener =
      new IabHelper.QueryInventoryFinishedListener() {
    @Override
    public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
      Log.d(LOGTAG, "Query inventory finished.");
      if (result.isFailure()) {
        Log.e(LOGTAG, "Failed to query inventory: " + result);
        bannerListener.onFailedToReceiveAd();
        return;
      }
      // Did we already make the purchase?
      Purchase testPurchase = inventory.getPurchase(SKU_TEST_SUCCESSFUL_PURCHASE);
      boolean hasTestPurchaseInventory =
          (testPurchase != null && verifyDeveloperPayload(testPurchase));
      if (hasTestPurchaseInventory) {
        Utils.logAndToast(iabActivity, LOGTAG,
            "Purchase made, but wasn't previously consumed. Consume and move on to next network.");
        iabHelper.consumeAsync(testPurchase, mConsumeFinishedListener);
      } else {
        Utils.logAndToast(iabActivity, LOGTAG, "Has not made purchase. Set up in-app purchase ad.");
        bannerListener.onReceivedAd(createInAppPurchaseAd());
      }
    }
  };

  // Called when a purchase is finished.
  IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener =
      new IabHelper.OnIabPurchaseFinishedListener() {
    @Override
    public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
      Log.d(LOGTAG, "Purchase finished: " + result + ", purchase: " + purchase);
      if (result.isFailure()) {
        Log.e(LOGTAG, "Error purchasing: " + result);
        return;
      }
      if (!verifyDeveloperPayload(purchase)) {
        Log.e(LOGTAG, "Error purchasing. Authenticity verification failed.");
        return;
      }

      Log.d(LOGTAG, "Purchase successful.");

      if (purchase.getSku().equals(SKU_TEST_SUCCESSFUL_PURCHASE)) {
        // Bought test purchase
        saveTestPurchase();
        iabHelper.consumeAsync(purchase, mConsumeFinishedListener);
        Log.d(LOGTAG, "Thank you for making the test purchase!");
      }
    }
  };

  // Called when consumption is complete
  IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
      new IabHelper.OnConsumeFinishedListener() {
    @Override
    public void onConsumeFinished(Purchase purchase, IabResult result) {
        if (result.isSuccess()) {
            Log.d(LOGTAG, "Consumption successful. Provisioning.");
            saveTestPurchase();
            bannerListener.onFailedToReceiveAd();
        }
        else {
          Log.e(LOGTAG, "Error while consuming: " + result);
        }
    }
  };

  /**
   * Verifies that the {@link Purchase} made is valid.
   * @param purchase The {@link Purchase} payload.
   * @return True if the payload is valid.
   */
  private boolean verifyDeveloperPayload(Purchase purchase) {
    // TODO: In a real application, you should verify that the developer payload of the purchase is
    // correct. It will be the same one that you sent when initiating the purchase. Using your own
    // server to store and verify developer payloads across app installations is recommended.
    return true;
  }

  /**
   * Gets the test purchase flag from {@link SharedPreferences}.
   * @return The value of the purchase flag, or false if it was not found.
   */
  private boolean madeTestPurchase() {
    SharedPreferences preferences = iabActivity.getPreferences(Context.MODE_PRIVATE);
    return preferences.getBoolean(SHARED_PREFERENCES_NAME_TEST_PURCHASE, false);
  }

  /**
   * Saves the test purchase flag to {@link SharedPreferences}.
   */
  private void saveTestPurchase() {
    // On a real application, we recommend you save data in a secure way to prevent tampering.
    // For simplicity in this sample, we simply store the data using SharedPreferences.
    if (iabActivity == null) {
      Log.w(LOGTAG, "Activity null. Can't save purchase.");
      return;
    }
    SharedPreferences.Editor spe = iabActivity.getPreferences(Context.MODE_PRIVATE).edit();
    spe.putBoolean(SHARED_PREFERENCES_NAME_TEST_PURCHASE, true);
    spe.commit();
    Utils.logAndToast(iabActivity, LOGTAG, "Purchase stored successfully.");
  }

  /**
   * Destroy method called by AdMob Mediation when the custom event gets destroyed.
   */
  @Override
  public void destroy() {
    iabActivity = null;
  }
}
