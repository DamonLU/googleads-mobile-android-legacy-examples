// Copyright 2012 Google Inc.
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

package com.google.example.dfp;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.AppEventListener;
import com.google.ads.doubleclick.DfpAdView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * This class shows how to insert a DFP Banner into your application.
 *
 * There are three examples included:
 *   1. Standard DFP banner
 *   2. Multiple Ad Sizes
 *   3. App Events
 *
 * Change the SAMPLE_TO_RUN value to run a specific sample. See the {@link DfpSample} enumeration
 * for possible samples to run.
 *
 * @author api.eleichtenschl@gmail.com (Eric Leichtenschlag)
 */
public class BannerSamples extends Activity
    implements AdListener, AppEventListener, OnClickListener {
  /** The Sample to run. Change this value to run a different sample. */
  private static final DfpSample SAMPLE_TO_RUN = DfpSample.STANDARD_BANNER;

  /** The log tag. */
  private static final String LOG_TAG = "BannerSample";

  /** The view to show the ad. */
  private DfpAdView adView;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // The DfpAdView initialization depends on the type of example you wish to run. Change the
    // value of SAMPLE_TO_RUN to run a different sample.
    switch (SAMPLE_TO_RUN) {
      case STANDARD_BANNER:
        // Example 1. Using sample DFP banner.
        setContentView(R.layout.main);
        adView = new DfpAdView(this, AdSize.BANNER, DfpSample.STANDARD_BANNER.adunitId);
        break;
      case MULTIPLE_AD_SIZES:
        // Example 2. Using multiple ad sizes.
        // This ad unit supports the following sizes: (320x250, 300x250, 120x20)
        setContentView(R.layout.refresh);
        AdSize[] adSizes = { AdSize.BANNER, AdSize.IAB_MRECT, new AdSize(120, 20)};
        adView = new DfpAdView(this, adSizes, DfpSample.MULTIPLE_AD_SIZES.adunitId);
        break;
      case APP_EVENTS:
        // Example 3. Using app events.
        setContentView(R.layout.refresh);
        adView = new DfpAdView(this, AdSize.BANNER, DfpSample.APP_EVENTS.adunitId);
        adView.setAppEventListener(this);
        break;
    }

    /** The implementation below is the same for all examples. */
    // Set the AdListener to listen for standard ad events.
    adView.setAdListener(this);

    // Lookup your LinearLayout assuming it’s been given the attribute
    // android:id="@+id/mainLayout".
    LinearLayout layout = (LinearLayout) findViewById(R.id.mainLayout);

    // Add the adView to it.
    layout.addView(adView);

    // Initiate an request to load the AdView with an ad.
    adView.loadAd(new AdRequest());
  }

  /** Called when the refresh button is clicked. */
  @Override
  public void onClick(View view) {
    if (adView != null) {
      adView.loadAd(new AdRequest());
    }
  }

  /**
   * Called when a DFP creative invokes an app event.
   *
   * The app event creative is set up to send color=red when the ad is loaded, color=green when the
   * ad is clicked, and color=blue after 5 seconds. This example will listen for these events to
   * change the app's background color.
   */
  @Override
  public void onAppEvent(Ad ad, String name, String info) {
    String message = String.format("Received app event (%s, %s)", name, info);
    Log.d(LOG_TAG, message);
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    if ("color".equals(name)) {
      LinearLayout layout = (LinearLayout) findViewById(R.id.mainLayout);
      if ("red".equals(info)) {
        layout.setBackgroundColor(Color.RED);
      } else if ("green".equals(info)) {
        layout.setBackgroundColor(Color.GREEN);
      } else if ("blue".equals(info)) {
        layout.setBackgroundColor(Color.BLUE);
      }
    }
  }

  /** Called when an ad is received. */
  @Override
  public void onReceiveAd(Ad ad) {
    Log.d(LOG_TAG, "onReceiveAd");
  }

  /** Called when an ad was not received. */
  @Override
  public void onFailedToReceiveAd(Ad ad, ErrorCode error) {
    String message = String.format("onFailedToReceiveAd (%s)", error);
    Log.d(LOG_TAG, message);
  }

  /**
   * Called when an Activity is created in front of the app (e.g. an interstitial is shown, or an
   * ad is clicked and launches a new Activity).
   */
  @Override
  public void onPresentScreen(Ad ad) {
    Log.d(LOG_TAG, "onPresentScreen");
  }

  /** Called when an ad is exited and about to return to the application. */
  @Override
  public void onDismissScreen(Ad ad) {
    Log.d(LOG_TAG, "onDismissScreen");
  }

  /**
   * Called when an ad is clicked and going to start a new Activity that will leave the application
   * (e.g. breaking out to the Browser or Maps application).
   */
  @Override
  public void onLeaveApplication(Ad ad) {
    Log.d(LOG_TAG, "onLeaveApplication");
  }
}
