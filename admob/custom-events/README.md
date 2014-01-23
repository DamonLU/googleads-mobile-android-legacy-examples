Terms
=====

Copyright 2013, Google Inc. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

About
=====

The sample app demonstrates several use cases for AdMob custom events that can
be used in conjunction with AdMob Mediation:

  1. Percentage House Ads - For each request, decide if a house ad should be
     requested, or whether to move on to the next network.
  2. Birthday Image - If passed the user's birthday, this custom event can show
     a Happy Birthday image instead of an ad on his/her birthday.
  3. In App Purchase - Set up a custom view that triggers a test in-app purchase
     if the user hasn't already made the purchase. Otherwise move on to the
     next network. This example uses Google Play In-app Billing API Version 3
     (http://developer.android.com/google/play/billing/billing_integrate.html)
     and includes the helper classes provided in the Trivial Drive sample app
     provided with the billing API.

Additional Resources:
=====================

https://support.google.com/admob/answer/2413211

https://developers.google.com/mobile-ads-sdk/training/mediation/custom-events/banners

https://developers.google.com/mobile-ads-sdk/
