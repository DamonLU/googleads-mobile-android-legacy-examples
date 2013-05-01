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

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventBanner;
import com.google.ads.mediation.customevent.CustomEventBannerListener;
import com.google.example.ads.customevents.Utils;

import android.app.Activity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Custom event to request house ads a percentage of the time. Expects a server parameter in JSON
 * of the form:
 * {
 *   "publisherId": "a1516441cce65b8",
 *   "percentage": 20
 * }
 *
 * This will request an ad for publisher ID "a1516441cce65b8" 20% of the time. The publisher ID
 * should be fully allocated to house ads.
 *
 * @author api.eleichtenschl@gmail.com (Eric Leichtenschlag)
 */
public class PercentageHouseAds implements CustomEventBanner, AdListener {
  /** Logtag used for logging statements. */
  public static final String LOGTAG = "PercentageHouseAds";

  /** The Custom event listener used to notify AdMob Mediation of ad events. */
  private CustomEventBannerListener bannerListener;

  /** The AdMob {@link AdView} used to request a house ad. */
  private AdView adView;

  /**
   * Requests a banner ad. This method is called by AdMob Mediation when it selects your custom
   * event network and asks for an ad.
   */
  @Override
  public void requestBannerAd(final CustomEventBannerListener bannerListener,
                              final Activity activity,
                              String label,
                              String serverParameter,
                              AdSize adSize,
                              MediationAdRequest mediationAdRequest,
                              Object customEventExtra) {
    this.bannerListener = bannerListener;
    String publisherId = "";
    double percentage = 0;
    try {
      JSONObject jsonObject = new JSONObject(serverParameter);
      publisherId = jsonObject.getString("publisherId");
      percentage = jsonObject.getDouble("percentage");
    } catch (JSONException exception) {
      Log.w(LOGTAG, exception.getMessage());
      bannerListener.onFailedToReceiveAd();
      return;
    }

    // For this demo, we'll override the percentage from the server, and use the percentage
    // specified in the app. A practical implementation of this custom event puts the percentage
    // as part of the JSON in the server parameter, so that all inputs are not hardcoded into the
    // app.
    try {
      if (customEventExtra != null) {
        percentage = (Double) customEventExtra;
      } else {
        Log.w(LOGTAG, String.format("Couldn't find percentage in extras with key: %s", label));
        bannerListener.onFailedToReceiveAd();
      }
    } catch (Exception exception) {
      Log.w(LOGTAG, exception.getMessage());
      bannerListener.onFailedToReceiveAd();
      return;
    }

    // Construct a random number. If the random number is higher than the percentage, don't return
    // a house ad.
    double random = Math.random() * 100;
    Log.d(LOGTAG, String.format("Random value is %s", random));
    if (random > percentage) {
      Utils.logAndToast(
          activity, LOGTAG, String.format("%s > %s, skipping house ad", random, percentage));
      bannerListener.onFailedToReceiveAd();
      return;
    } else {
      Utils.logAndToast(
          activity, LOGTAG, String.format("%s < %s, requesting house ad", random, percentage));
      adView = new AdView(activity, AdSize.BANNER, publisherId);
      adView.setAdListener(this);
      adView.loadAd(new AdRequest());
    }
  }

  /** AdListener implementation. */
  @Override
  public void onReceiveAd(Ad ad) {
    bannerListener.onReceivedAd(adView);
  }

  @Override
  public void onFailedToReceiveAd(Ad ad, ErrorCode errorCode) {
    Log.d(LOGTAG, "Failed to receive the house ad: " + errorCode);
    bannerListener.onFailedToReceiveAd();
  }

  @Override
  public void onPresentScreen(Ad ad) {
    bannerListener.onClick();
    bannerListener.onPresentScreen();
  }

  @Override
  public void onDismissScreen(Ad ad) {
    bannerListener.onDismissScreen();
  }

  @Override
  public void onLeaveApplication(Ad ad) {
    bannerListener.onLeaveApplication();
  }

  @Override
  public void destroy() {
    // Clean up the ad view.
    if (adView != null) {
      adView.destroy();
    }
  }
}
