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

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Utilities class for the sample project.
 *
 * @author api.eleichtenschl@gmail.com (Eric Leichtenschlag)
 */
public class Utils {
  private Utils() {
    // Don't instantiate.
  }

  /**
   * Logs a message and toasts an update to the screen.
   * @param context An activity context on which to make the toast.
   * @param logtag The tag used for the logging statement.
   * @param message The message.
   */
  public static void logAndToast(Context context, String logtag, String message) {
    Log.i(logtag, message);
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
  }
}
