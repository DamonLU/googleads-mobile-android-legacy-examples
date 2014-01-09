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

import com.google.example.ads.customevents.impl.iab.IabHelper;

import android.app.Activity;
import android.content.Intent;

/**
 * An activity that is capable of handling the in-app purchase custom event.
 *
 * @author api.eleichtenschl@gmail.com (Eric Leichtenschlag)
 */
public class IabActivity extends Activity {
  protected IabHelper iabHelper;

  public IabHelper getIabHelper() {
    return iabHelper;
  }

  public void setIabHelper(IabHelper iabHelper) {
    this.iabHelper = iabHelper;
  }

  /**
   * If the activity result is intended for the in-app billing helper, let the in-app billing
   * helper handle it.
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // Pass on the activity result to the helper for handling.
    if (iabHelper == null || !iabHelper.handleActivityResult(requestCode, resultCode, data)) {
      // Activity result not handled by in app billing, so handle it ourselves.
      super.onActivityResult(requestCode, resultCode, data);
    }
  }
}
