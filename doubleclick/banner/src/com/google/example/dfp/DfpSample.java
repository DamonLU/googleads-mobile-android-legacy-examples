// Copyright 2013 Google Inc.
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

/**
 * An enumeration of DFP samples that can be run in this project. Sample ad units that support each
 * feature are provided, but you can replace these ad units with your own to perform testing.
 *
 * @author api.eleichtenschl@gmail.com (Eric Leichtenschlag)
 */
public enum DfpSample {
  STANDARD_BANNER("/6253334/dfp_example_ad/banner"),
  MULTIPLE_AD_SIZES("/6253334/dfp_example_ad/multisize"),
  APP_EVENTS("/6253334/dfp_example_ad/appevents");

  public String adunitId;

  private DfpSample(String adUnitId) {
    this.adunitId = adUnitId;
  }
}
