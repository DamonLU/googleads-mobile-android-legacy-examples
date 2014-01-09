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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Main activity for custom events sample app, showing different custom event implementations you
 * can use with AdMob Mediation.
 *
 * @author api.eleichtenschl@gmail.com (Eric Leichtenschlag)
 */
public class MainActivity extends Activity {
  /** Logtag used for logging statements. */
  public static final String LOGTAG = "CustomEventsSample";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  /**
   * Called when the percentage house ads button is clicked. Go to percentage house ads activity.
   * @param view The percentage house ads button.
   */
  public void percentageHouseAdsClicked(View view) {
    Intent intent = new Intent(MainActivity.this, PercentageHouseAdsActivity.class);
    startActivity(intent);
  }

  /**
   * Called when the birthday ads button is clicked. Go to birthday ads activity.
   * @param view The birthday ads button.
   */
  public void birthdayAdsClicked(View view) {
    Intent intent = new Intent(MainActivity.this, BirthdayAdsActivity.class);
    startActivity(intent);
  }

  /**
   * Called when the in-app purchase ads button is clicked. Go to in-app purchase ads activity.
   * @param view the in-app purchase ads button.
   */
  public void inAppPurchaseClicked(View view) {
    Intent intent = new Intent(MainActivity.this, InAppPurchaseActivity.class);
    startActivity(intent);
  }
}
