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

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest.ErrorCode;

import android.content.Context;

/**
 * This class implements {@link AdListener} and just logs and Toasts the ad responses.
 *
 * @author api.eleichtenschl@gmail.com (Eric Leichtenschlag)
 */
public class LoggingAdListener implements AdListener {
  private Context context;

  public LoggingAdListener(Context context) {
    this.context = context;
  }

  @Override
  public void onReceiveAd(Ad ad) {
    Utils.logAndToast(context, MainActivity.LOGTAG, "onReceiveAd()");
  }

  @Override
  public void onFailedToReceiveAd(Ad ad, ErrorCode error) {
    Utils.logAndToast(context, MainActivity.LOGTAG,
        String.format("onFailedToReceiveAd(%s)", error));
  }

  @Override
  public void onPresentScreen(Ad ad) {
    Utils.logAndToast(context, MainActivity.LOGTAG, "onPresentScreen()");
  }

  @Override
  public void onDismissScreen(Ad ad) {
    Utils.logAndToast(context, MainActivity.LOGTAG, "onDismissScreen()");
  }

  @Override
  public void onLeaveApplication(Ad ad) {
    Utils.logAndToast(context, MainActivity.LOGTAG, "onLeaveApplication()");
  }
}
