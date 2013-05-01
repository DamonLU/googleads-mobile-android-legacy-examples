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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Example showing how you can show a birthday ad when it's someone's birthday. This is done via a
 * custom event that takes in a user's birthday from the app, and checks the current date to see
 * if it is their birthday. If it is, it shows a happy birthday image instead of an ad.
 *
 * @author api.eleichtenschl@gmail.com (Eric Leichtenschlag)
 */
public class BirthdayAdsActivity extends Activity {
  /** The label of the Custom Event set up in the AdMob front end. */
  private static final String CUSTOM_EVENT_LABEL = "Birthday Image";

  /** The {@link AdView} used to make ad requests. */
  private AdView adView;

  /** The {@link DatePicker} input that represents your birthday. */
  private DatePicker birthdayInput;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_birthday_ads);

    birthdayInput = (DatePicker) findViewById(R.id.birthday_input);
    final Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    // Set the birthday input to the current day.
    birthdayInput.init(year, month, day, null);
  }

  /**
   * Invoked when the request ad button is clicked.
   * @param view The request ad button.
   */
  public void requestAdClicked(View view) {
    EditText publisherIdInput = (EditText) findViewById(R.id.publisher_id);
    DatePicker birthdayInput = (DatePicker) findViewById(R.id.birthday_input);
    RelativeLayout layout = (RelativeLayout) findViewById(R.id.birthday_ads_layout);

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

    // Create an ad request, and pass in the birthday into the extras object. The key for the extra
    // must match the label of the custom event you defined in the AdMob front end.
    AdRequest adRequest = new AdRequest();
    CustomEventExtras extras = new CustomEventExtras();
    GregorianCalendar birthday = new GregorianCalendar(
        birthdayInput.getYear(), birthdayInput.getMonth(), birthdayInput.getDayOfMonth());
    extras.addExtra(CUSTOM_EVENT_LABEL, birthday);
    adRequest.setNetworkExtras(extras);
    adView.loadAd(adRequest);
  }
}
