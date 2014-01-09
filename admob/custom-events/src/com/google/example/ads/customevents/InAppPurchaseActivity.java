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

package com.google.example.ads.customevents;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.example.ads.customevents.impl.InAppPurchase;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

/**
 * Example showing how to invoke an in-app purchase from an ad using the Google Play In-app Billing
 * API.
 *
 * @author api.eleichtenschl@gmail.com (Eric Leichtenschlag)
 */
public class InAppPurchaseActivity extends IabActivity {
  /** The {@link AdView} used to make ad requests. */
  private AdView adView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_in_app_purchase);
  }

  /**
   * Invoked when the clear purchase button is clicked. This will clear the purchase from
   * {@link SharedPreferences} so you can test the in-app purchase flow again.
   * @param view The clear purchase button.
   */
  public void clearPurchaseClicked(View view) {
    SharedPreferences.Editor spe = getPreferences(Context.MODE_PRIVATE).edit();
    spe.remove(InAppPurchase.SHARED_PREFERENCES_NAME_TEST_PURCHASE);
    spe.commit();
    Utils.logAndToast(this, MainActivity.LOGTAG, "Cleared purchase.");
  }

  /**
   * Invoked when the request ad button is clicked.
   * @param view The request ad button.
   */
  public void requestAdClicked(View view) {
    EditText publisherIdInput = (EditText) findViewById(R.id.publisher_id);
    String publisherId = publisherIdInput.getText().toString();
    RelativeLayout layout = (RelativeLayout) findViewById(R.id.birthday_ads_layout);

    // Remove AdView if it previously existed.
    if (adView != null) {
      layout.removeView(adView);
      adView.destroy();
    }

    // Create a new AdView with the provided publisher ID and add it to the view hierarchy.
    adView = new AdView(this, AdSize.BANNER, publisherId);
    adView.setAdListener(new LoggingAdListener(this));
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    layout.addView(adView, params);

    // Load an ad into the AdView.
    adView.loadAd(new AdRequest());
  }
}
