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

/**
 * Configuration for running this sample project. You can change these values to be more
 * suitable for your use case.
 */
public class Config {
  /** The log tag used for logging statements. */
  public static final String LOGTAG = "DFPManualImpressionsSample";

  /**
   * The index to place the ad in the {@link android.support.v4.view.ViewPager}.
   * Make sure this index is within the bounds of the list to avoid an exception.
   */
  public static final int AD_INDEX = 2;

  /** Your DFP ad unit ID. */
  public static final String DFP_AD_UNIT_ID = "/6253334/dfp_example_ad/manual_impressions";
}
