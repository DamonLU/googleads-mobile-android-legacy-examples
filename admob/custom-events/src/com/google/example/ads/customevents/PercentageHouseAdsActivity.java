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
import com.google.ads.mediation.customevent.CustomEventExtras;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * Example showing how you can request house ads X% of the time. This is done via a custom event
 * placed at the top of the mediation stack which takes an AdMob publisher ID trafficked to house
 * ads, and a percentage. If a random generated number is less than the specified percentage,
 * don't show the ad.
 *
 * @author api.eleichtenschl@gmail.com (Eric Leichtenschlag)
 */
public class PercentageHouseAdsActivity extends Activity {
  /** The label of the Custom Event set up in the AdMob front end. */
  private static final String CUSTOM_EVENT_LABEL = "Percentage House Ads";

  /** The {@link AdView} used to make ad requests. */
  private AdView adView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_percentage_house_ads);
  }

  /**
   * Invoked when the request ad button is clicked.
   * @param view The request ad button.
   */
  public void requestAdClicked(View view) {
    EditText publisherIdInput = (EditText) findViewById(R.id.publisher_id);
    EditText percentageInput = (EditText) findViewById(R.id.percentage);
    double percentage = 0;
    try {
      percentage = Double.parseDouble(percentageInput.getText().toString());
      if (percentage > 100 || percentage < 0) {
        Utils.logAndToast(this, MainActivity.LOGTAG,
            "Invalid percentage! Enter a value less than 100.");
        return;
      }
    } catch (NumberFormatException exception) {
      Utils.logAndToast(this, MainActivity.LOGTAG, "Enter a percentage.");
      return;
    }
    RelativeLayout layout = (RelativeLayout) findViewById(R.id.percentage_house_ads_layout);

    // Remove AdView if it previously existed.
    if (adView != null) {
      layout.removeView(adView);
      adView.destroy();
    }

    // Create the AdView and align it to the bottom of the screen.
    adView = new AdView(this, AdSize.BANNER, publisherIdInput.getText().toString());
    adView.setId(123);
    adView.setAdListener(new LoggingAdListener(this));
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    layout.addView(adView, params);

    // Set the ScrollView to sit above the adView.
    ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
    RelativeLayout.LayoutParams scrollViewParams = new RelativeLayout.LayoutParams(
        scrollView.getLayoutParams());
    scrollViewParams.addRule(RelativeLayout.ABOVE, adView.getId());
    scrollView.setLayoutParams(scrollViewParams);

    // Create an ad request, and pass in percentage into the extras object. The key for the extra
    // must match the label of the custom event you defined in the AdMob front end.
    AdRequest adRequest = new AdRequest();
    CustomEventExtras extras = new CustomEventExtras();
    extras.addExtra(CUSTOM_EVENT_LABEL, percentage);
    adRequest.setNetworkExtras(extras);
    adView.loadAd(adRequest);
  }
}
