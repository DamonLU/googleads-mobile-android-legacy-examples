package com.google.example.dfp;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.doubleclick.DfpInterstitialAd;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * An {@link Activity} that requests and displays a DfpInterstitialAd.
 */
public class InterstitialSample extends Activity implements AdListener, OnClickListener {
  /** The log tag. */
  private static final String LOG_TAG = "InterstitialSample";

  /** Interstitial ad unit constant. */
  private static final String AD_UNIT_SAMPLE = "/6253334/dfp_example_ad/interstitial";

  /** The interstitial ad. */
  private DfpInterstitialAd interstitialAd;

  /** The button to load the interstitial. */
  private Button loadButton;

  /** The button to show the interstitial. */
  private Button showButton;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    // Create an ad.
    interstitialAd = new DfpInterstitialAd(this, AD_UNIT_SAMPLE);

    // Set the AdListener.
    interstitialAd.setAdListener(this);

    // Get a reference to the reload button. This button will load an
    // interstitial ad.
    loadButton = (Button) findViewById(R.id.loadButton);

    // Get a reference to the show button. The button will start disabled, but
    // will be enabled when the interstitial ad is loaded. After the interstitial
    // is shown, the button will be disabled again.
    showButton = (Button) findViewById(R.id.showButton);
    showButton.setText("Interstitial Not Ready");
    showButton.setEnabled(false);

    // Set the onClickListener for the buttons.
    loadButton.setOnClickListener(this);
    showButton.setOnClickListener(this);
  }

  /** Called when a view has been clicked. */
  @Override
  public void onClick(View view) {
    // If the load button was clicked - load an interstitial.
    // If the show button was clicked - show the interstitial.
    if (view == loadButton) {
      // Disable the show button until the new ad is loaded.
      showButton.setText("Loading Interstitial...");
      showButton.setEnabled(false);

      // Load the interstitial ad.
      interstitialAd.loadAd(new AdRequest());
    } else if (view == showButton) {
      // Show the interstitial if it's loaded.
      if (interstitialAd.isReady()) {
        interstitialAd.show();
      } else {
        Log.d(LOG_TAG, "Interstitial ad was not ready to be shown.");
      }

      // Disable the show button until another interstitial is loaded.
      showButton.setText("Interstitial Not Ready");
      showButton.setEnabled(false);
    }
  }

  /** Called when an ad is received. */
  @Override
  public void onReceiveAd(Ad ad) {
    Log.d(LOG_TAG, "onReceiveAd");
    Toast.makeText(this, "onReceiveAd", Toast.LENGTH_SHORT).show();

    // Change the button text and enable the show button.
    if (ad == interstitialAd) {
      showButton.setText("Show Interstitial");
      showButton.setEnabled(true);
    }
  }

  /** Called when an ad was not received. */
  @Override
  public void onFailedToReceiveAd(Ad ad, AdRequest.ErrorCode error) {
    String message = "onFailedToReceiveAd (" + error + ")";
    Log.d(LOG_TAG, message);
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    // Change the button text and disable the show button.
    if (ad == interstitialAd) {
      showButton.setText("Failed to Receive Ad");
      showButton.setEnabled(false);
    }
  }

  /**
   * Called when an Activity is created in front of the app (e.g. an
   * interstitial is shown, or an ad is clicked and launches a new Activity).
   */
  @Override
  public void onPresentScreen(Ad ad) {
    Log.d(LOG_TAG, "onPresentScreen");
    Toast.makeText(this, "onPresentScreen", Toast.LENGTH_SHORT).show();
  }

  /** Called when an ad is clicked and about to return to the application. */
  @Override
  public void onDismissScreen(Ad ad) {
    Log.d(LOG_TAG, "onDismissScreen");
    Toast.makeText(this, "onDismissScreen", Toast.LENGTH_SHORT).show();
  }

  /**
   * Called when an ad is clicked and going to start a new Activity that will
   * leave the application (e.g. breaking out to the Browser or Maps
   * application).
   */
  @Override
  public void onLeaveApplication(Ad ad) {
    Log.d(LOG_TAG, "onLeaveApplication");
    Toast.makeText(this, "onLeaveApplication", Toast.LENGTH_SHORT).show();
  }
}
