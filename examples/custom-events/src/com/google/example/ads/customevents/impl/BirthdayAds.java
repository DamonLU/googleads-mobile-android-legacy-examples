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
import com.google.example.ads.customevents.Utils;

import android.app.Activity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Custom event that shows a "Happy Birthday" image if it is your birthday. This custom event
 * expects the birthday to be passed into the extras object.
 *
 * @author api.eleichtenschl@gmail.com (Eric Leichtenschlag)
 */
public class BirthdayAds implements CustomEventBanner {
  /** Logtag used for logging statements. */
  private static final String LOGTAG = "BirthdayAds";

  /**
   * Requests a banner ad. This method is called by AdMob Mediation when it selects your custom
   * event network and asks for an ad.
   */
  @Override
  public void requestBannerAd(final CustomEventBannerListener listener,
                              final Activity activity,
                              String label,
                              String unusedServerParameter,
                              AdSize adSize,
                              MediationAdRequest mediationAdRequest,
                              Object customEventExtra) {
    // This custom event should only respond to banner requests.
    if (!adSize.isSizeAppropriate(320, 50)) {
      listener.onFailedToReceiveAd();
      return;
    }

    Calendar birthday = null;
    try {
      if (customEventExtra != null) {
        birthday = (Calendar) customEventExtra;
      } else {
        Log.w(LOGTAG, String.format("Couldn't find birthday in extras with key: %s", label));
        listener.onFailedToReceiveAd();
        return;
      }
    } catch (Exception exception) {
      Log.w(LOGTAG, exception.getMessage());
      listener.onFailedToReceiveAd();
      return;
    }

    Calendar now = new GregorianCalendar();
    if (birthday.get(Calendar.MONTH) == now.get(Calendar.MONTH) &&
        birthday.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH)) {
      Utils.logAndToast(activity, LOGTAG, "Happy Birthday!");
      // The image happens to be an animated gif - so load it in a WebView so it animates.
      WebView webView = new WebView(activity);
      webView.loadUrl("file:///android_res/drawable/birthday.gif");
      webView.setLayoutParams(new RelativeLayout.LayoutParams(
          adSize.getWidthInPixels(activity), adSize.getHeightInPixels(activity)));
      listener.onReceivedAd(webView);
    } else {
      Utils.logAndToast(activity, LOGTAG, "Not your birthday. Moving on to next network.");
      listener.onFailedToReceiveAd();
    }
  }

  /**
   * Destroy method called by AdMob Mediation when the custom event gets destroyed.
   */
  @Override
  public void destroy() {
    // Nothing needs to be done here.
  }
}
