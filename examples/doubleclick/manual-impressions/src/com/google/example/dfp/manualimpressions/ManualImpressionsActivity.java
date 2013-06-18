// Copyright 2013, Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.example.dfp.manualimpressions;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.doubleclick.DfpAdView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates how to manually record an impression for an ad in a {@link ViewPager}.
 *
 * @author api.eleichtenschl@gmail.com (Eric Leichtenschlag)
 */
public class ManualImpressionsActivity extends FragmentActivity {
  /** Whether or not the first ad has been received. */
  private boolean firstAdReceived;

  /** Whether or not the ad has been placed in the {@code ViewPager}. */
  private boolean adInViewPager;

  /** Whether or not a manual impression has already been fired on this ad. */
  private boolean manualImpressionAlreadyFired;

  /** Specifies which page should be shown in the {@code ViewPager}. */
  private MyPagerAdapter pagerAdapter;

  /** Listens for page changes and records ad impressions when an ad is on screen. */
  private MyOnPageChangeListener pageChangeListener;

  /** The {@code Fragment} that the banner lives in. */
  private DfpFragment dfpFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_manual_impressions);
    setFirstAdReceived(false);
    setAdInViewPager(false);
    setManualImpressionAlreadyFired(false);

    dfpFragment = DfpFragment.newInstance(createDfpAdView());
    pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
    pagerAdapter.addColor(Color.BLUE);
    pagerAdapter.addColor(Color.RED);
    pagerAdapter.addColor(Color.YELLOW);
    pagerAdapter.addColor(Color.BLUE);
    pagerAdapter.addColor(Color.GREEN);
    pagerAdapter.addColor(Color.RED);

    ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
    pager.setAdapter(pagerAdapter);
    pageChangeListener = new MyOnPageChangeListener();
    pager.setOnPageChangeListener(pageChangeListener);
  }

  /**
   * The ViewPager ends up in a weird state when changing orientation, so swallow this call
   * and the ViewPager will restart on an orientation change.
   */
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    // Swallow method call.
  }

  /**
   * Creates a {@link DfpAdView} capable of recording manual impressions.
   *
   * @return The created {@code DfpAdView}.
   */
  public DfpAdView createDfpAdView() {
    DfpAdView dfpAdView = new DfpAdView(this, AdSize.IAB_MRECT, Config.DFP_AD_UNIT_ID);
    // Enable this DfpAdView for recording manual impressions.
    dfpAdView.enableManualImpressions(true);
    dfpAdView.setAdListener(new MyAdListener());
    return dfpAdView;
  }

  /**
   * Records an impression on the {@code DfpAdView}.
   */
  public void recordImpression() {
    dfpFragment.getDfpAdView().recordImpression();
    setManualImpressionAlreadyFired(true);
    Log.d(Config.LOGTAG, "Invoked recordImpression()");
  }

  /**
   * Requests an ad. Invoked from the requestAd button defined in XML.
   *
   * @param view The {@code Button} that was clicked.
   */
  public void requestAd(View view) {
    dfpFragment.getDfpAdView().loadAd(new AdRequest());
  }

  /**
   * Sets the first ad received flag and updates the UI,
   *
   * @param firstAdReceived True if the first ad has been received.
   */
  public void setFirstAdReceived(boolean firstAdReceived) {
    this.firstAdReceived = firstAdReceived;
    TextView textView = (TextView) findViewById(R.id.firstAdReceivedText);
    textView.setText("First ad Received: " + firstAdReceived);
  }

  /**
   * Sets the ad in ViewPager flag and updates the UI,
   *
   * @param adInViewPager True if the ad has been loaded into the {@link ViewPager}.
   */
  public void setAdInViewPager(boolean adInViewPager) {
    this.adInViewPager = adInViewPager;
    TextView textView = (TextView) findViewById(R.id.adInViewPagerText);
    textView.setText("Ad in ViewPager: " + adInViewPager);
  }

  /**
   * Sets the manual impression already fired flag and updates the UI,
   *
   * @param manualImpressionAlreadyFired True if an impression has already been fired for the
   *     previous request.
   */
  public void setManualImpressionAlreadyFired(boolean manualImpressionAlreadyFired) {
    this.manualImpressionAlreadyFired = manualImpressionAlreadyFired;
    TextView textView = (TextView) findViewById(R.id.manualImpressionAlreadyFiredText);
    textView.setText("Manual impression already fired: " + manualImpressionAlreadyFired);
  }

  /**
   * A {@link FragmentStatePagerAdapter} that contains {@link ColorFragment}s with specified
   * background colors and a {@link DfpFragment}.
   */
  private class MyPagerAdapter extends FragmentStatePagerAdapter {
    /** The background colors for the fragments. */
    private List<Integer> colors;

    public MyPagerAdapter(FragmentManager fragmentManager) {
      super(fragmentManager);
      this.colors = new ArrayList<Integer>();
    }

    /**
     * Adds a color to the list of background colors.
     *
     * @param color The color to add.
     */
    public void addColor(int color) {
      colors.add(color);
    }

    /**
     * Find out when the ad will be placed into the {@code ViewPager}. Once it is added,
     * {@link #getItem(int)} will return the ad for the appropriate position.
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      if (firstAdReceived && position == Config.AD_INDEX) {
        setAdInViewPager(true);
      }
      return super.instantiateItem(container, position);
    }

    /** Return the ad if it's in the view pager and it matches the ad index. */
    @Override
    public Fragment getItem(int position) {
      if (adInViewPager && position == Config.AD_INDEX) {
        return dfpFragment;
      } else if (firstAdReceived && position > Config.AD_INDEX) {
        return ColorFragment.newInstance(this.colors.get(position - 1));
      } else {
        return ColorFragment.newInstance(this.colors.get(position));
      }
    }

    /** Add 1 to the number of elements once the ad comes back. */
    @Override
    public int getCount() {
      if (firstAdReceived) {
        return this.colors.size() + 1;
      } else {
        return this.colors.size();
      }
    }

    /**
     * Don't destroy the {@code DfpFragment}if the user moves too far away from it. We want to keep
     * the same instance of the ad.
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      if (!(object instanceof DfpFragment)) {
        super.destroyItem(container, position, object);
      }
    }
  }

  /** A page change listener that records an ad impression if the selected page is an ad. */
  private class MyOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
    /** The current page that the {@code ViewPager} is on. */
    private int currentPage;

    @Override
    public void onPageSelected(int position) {
      currentPage = position;
      // Record an impression on the DfpAdView if one hasn't been recorded already.
      if (adInViewPager
          && !manualImpressionAlreadyFired
          && pagerAdapter.getItem(position) == dfpFragment) {
        recordImpression();
      }
    }

    /**
     * Gets the current selected page.
     *
     * @return The currently selected page.
     */
    public final int getCurrentPage() {
      return currentPage;
    }
  }

  /** AdListener implementation. */
  private class MyAdListener implements AdListener {
    /**
     * Record a manual impression if the ad is present in the {@code ViewPager} when the ad is
     * received, or otherwise clears the {@code manualImpressionAlreadyFired} flag.
     */
    @Override
    public void onReceiveAd(Ad ad) {
      Log.d(Config.LOGTAG, "onReceiveAd()");
      setFirstAdReceived(true);
      if (pagerAdapter.getItem(pageChangeListener.getCurrentPage()) instanceof DfpFragment) {
        recordImpression();
      } else {
        setManualImpressionAlreadyFired(false);
      }
      pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailedToReceiveAd(Ad ad, ErrorCode errorCode) {
      Log.d(Config.LOGTAG, String.format("onFailedToReceiveAd(%s)", errorCode));
    }

    @Override
    public void onPresentScreen(Ad ad) {
      Log.d(Config.LOGTAG, "onPresentScreen()");
    }

    @Override
    public void onDismissScreen(Ad ad) {
      Log.d(Config.LOGTAG, "onDismissScreen()");
    }

    @Override
    public void onLeaveApplication(Ad ad) {
      Log.d(Config.LOGTAG, "onLeaveApplication()");
    }
  }
}
