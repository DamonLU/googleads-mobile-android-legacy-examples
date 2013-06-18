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

import com.google.ads.doubleclick.DfpAdView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A {@link Fragment} that contains an {@link DfpAdView}.
 *
 * @author api.eleichtenschl@gmail.com (Eric Leichtenschlag)
 */
public class DfpFragment extends Fragment {
  /** The banner to display. */
  private DfpAdView dfpAdView;

  /**
   * Creates a new instance of {@code DFPFragment}.
   *
   * @param adView A {@link DfpAdView} to place in the fragment.
   * @return an instance of {@code DFPFragment}.
   */
  public static DfpFragment newInstance(DfpAdView adView) {
    DfpFragment fragment = new DfpFragment();
    fragment.dfpAdView = adView;
    return fragment;
  }

  /**
   * Gets the {@code DfpAdView}.
   *
   * @return The {@code DfpAdView}.
   */
  public DfpAdView getDfpAdView() {
    return dfpAdView;
  }

  /** Return the already created {@code DfpAdView}. */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return dfpAdView;
  }
}
