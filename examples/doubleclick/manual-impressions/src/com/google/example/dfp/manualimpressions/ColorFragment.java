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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A {@link Fragment} that contains an empty layout with a background color.
 *
 * @author api.eleichtenschl@gmail.com (Eric Leichtenschlag)
 */
public class ColorFragment extends Fragment {
  /** The color of the background view. */
  private int color;

  /**
   * Creates a new instance of {@code ColorFragment}.
   *
   * @param color An {@link android.graphics.Color} constant.
   * @return An instance of {@code ColorFragment}.
   */
  public static ColorFragment newInstance(int color) {
    ColorFragment fragment = new ColorFragment();
    fragment.color = color;
    return fragment;
  }

  /** Create a basic view and set its background color. */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = new View(this.getActivity());
    view.setBackgroundColor(color);
    return view;
  }
}
