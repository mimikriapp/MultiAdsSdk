package com.mimikridev.ad.sdk.format;


import static com.mimikridev.ad.sdk.util.Constant.AD_STATUS_ON;
import static com.mimikridev.ad.sdk.util.Constant.APPLOVIN_MAX;
import static com.mimikridev.ad.sdk.util.Constant.FACEBOOK;
import static com.mimikridev.ad.sdk.util.Constant.NONE;
import static com.mimikridev.ad.sdk.util.Constant.STARTAPP;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;


import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;

import com.mimikridev.ad.sdk.R;
import com.mimikridev.ad.sdk.util.Constant;


import java.util.ArrayList;
import java.util.List;

public class NativeAdViewPager {

    public static class Builder {

        private static final String TAG = "AdNetwork";
        private final Activity activity;

        View view;



        com.facebook.ads.NativeAd fanNativeAd;
        NativeAdLayout fanNativeAdLayout;

        View startappNativeAd;
        ImageView startappNativeImage;
        ImageView startappNativeIcon;
        TextView startappNativeTitle;
        TextView startappNativeDescription;
        Button startappNativeButton;
        LinearLayout startappNativeBackground;

        FrameLayout applovinNativeAd;

        ProgressBar progressBarAd;

        private String adStatus = "";
        private String adNetwork = "";
        private String backupAdNetwork = "";
        private String adMobNativeId = "";
        private String adManagerNativeId = "";
        private String fanNativeId = "";
        private String appLovinNativeId = "";
        private int placementStatus = 1;
        private boolean darkTheme = false;
        private boolean legacyGDPR = false;

        private int nativeBackgroundLight = R.color.color_native_background_light;
        private int nativeBackgroundDark = R.color.color_native_background_dark;

        public Builder(Activity activity, View view) {
            this.activity = activity;
            this.view = view;
        }

        public Builder build() {
            loadNativeAd();
            return this;
        }

        public Builder setAdStatus(String adStatus) {
            this.adStatus = adStatus;
            return this;
        }

        public Builder setAdNetwork(String adNetwork) {
            this.adNetwork = adNetwork;
            return this;
        }

        public Builder setBackupAdNetwork(String backupAdNetwork) {
            this.backupAdNetwork = backupAdNetwork;
            return this;
        }


        public Builder setFanNativeId(String fanNativeId) {
            this.fanNativeId = fanNativeId;
            return this;
        }

        public Builder setAppLovinNativeId(String appLovinNativeId) {
            this.appLovinNativeId = appLovinNativeId;
            return this;
        }

        public Builder setPlacementStatus(int placementStatus) {
            this.placementStatus = placementStatus;
            return this;
        }

        public Builder setDarkTheme(boolean darkTheme) {
            this.darkTheme = darkTheme;
            return this;
        }

        public Builder setLegacyGDPR(boolean legacyGDPR) {
            this.legacyGDPR = legacyGDPR;
            return this;
        }

        public Builder setNativeAdBackgroundColor(int colorLight, int colorDark) {
            this.nativeBackgroundLight = colorLight;
            this.nativeBackgroundDark = colorDark;
            return this;
        }

        public void loadNativeAd() {

            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {


                fanNativeAdLayout = view.findViewById(R.id.fan_native_ad_container);

                startappNativeAd = view.findViewById(R.id.startapp_native_ad_container);
                startappNativeImage = view.findViewById(R.id.startapp_native_image);
                startappNativeIcon = activity.findViewById(R.id.startapp_native_icon);
                startappNativeTitle = view.findViewById(R.id.startapp_native_title);
                startappNativeDescription = view.findViewById(R.id.startapp_native_description);
                startappNativeButton = view.findViewById(R.id.startapp_native_button);
                startappNativeButton.setOnClickListener(v1 -> startappNativeAd.performClick());
                startappNativeBackground = view.findViewById(R.id.startapp_native_background);
                applovinNativeAd = view.findViewById(R.id.applovin_native_ad_container);
                progressBarAd = view.findViewById(R.id.progress_bar_ad);
                progressBarAd.setVisibility(View.VISIBLE);

                switch (adNetwork) {
                    case FACEBOOK:
                        if (fanNativeAdLayout.getVisibility() != View.VISIBLE) {
                            fanNativeAd = new com.facebook.ads.NativeAd(activity, fanNativeId);
                            NativeAdListener nativeAdListener = new NativeAdListener() {
                                @Override
                                public void onMediaDownloaded(com.facebook.ads.Ad ad) {

                                }

                                @Override
                                public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                    loadBackupNativeAd();
                                }

                                @Override
                                public void onAdLoaded(com.facebook.ads.Ad ad) {
                                    // Race condition, load() called again before last ad was displayed
                                    fanNativeAdLayout.setVisibility(View.VISIBLE);
                                    progressBarAd.setVisibility(View.GONE);
                                    if (fanNativeAd != ad) {
                                        return;
                                    }
                                    // Inflate Native Ad into Container
                                    //inflateAd(nativeAd);
                                    fanNativeAd.unregisterView();
                                    // Add the Ad view into the ad container.
                                    LayoutInflater inflater = LayoutInflater.from(activity);
                                    // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                                    LinearLayout nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_large_template_view, fanNativeAdLayout, false);
                                    fanNativeAdLayout.addView(nativeAdView);

                                    // Add the AdOptionsView
                                    LinearLayout adChoicesContainer = nativeAdView.findViewById(R.id.ad_choices_container);
                                    AdOptionsView adOptionsView = new AdOptionsView(activity, fanNativeAd, fanNativeAdLayout);
                                    adChoicesContainer.removeAllViews();
                                    adChoicesContainer.addView(adOptionsView, 0);

                                    // Create native UI using the ad metadata.
                                    TextView nativeAdTitle = nativeAdView.findViewById(R.id.native_ad_title);
                                    com.facebook.ads.MediaView nativeAdMedia = nativeAdView.findViewById(R.id.native_ad_media);
                                    com.facebook.ads.MediaView nativeAdIcon = nativeAdView.findViewById(R.id.native_ad_icon);
                                    TextView nativeAdSocialContext = nativeAdView.findViewById(R.id.native_ad_social_context);
                                    TextView nativeAdBody = nativeAdView.findViewById(R.id.native_ad_body);
                                    TextView sponsoredLabel = nativeAdView.findViewById(R.id.native_ad_sponsored_label);
                                    Button nativeAdCallToAction = nativeAdView.findViewById(R.id.native_ad_call_to_action);
                                    LinearLayout fanNativeBackground = nativeAdView.findViewById(R.id.ad_unit);

                                    if (darkTheme) {
                                        nativeAdTitle.setTextColor(ContextCompat.getColor(activity, R.color.applovin_dark_primary_text_color));
                                        nativeAdSocialContext.setTextColor(ContextCompat.getColor(activity, R.color.applovin_dark_primary_text_color));
                                        sponsoredLabel.setTextColor(ContextCompat.getColor(activity, R.color.applovin_dark_secondary_text_color));
                                        nativeAdBody.setTextColor(ContextCompat.getColor(activity, R.color.applovin_dark_secondary_text_color));
                                        fanNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                    } else {
                                        fanNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                    }

                                    // Set the Text.
                                    nativeAdTitle.setText(fanNativeAd.getAdvertiserName());
                                    nativeAdBody.setText(fanNativeAd.getAdBodyText());
                                    nativeAdSocialContext.setText(fanNativeAd.getAdSocialContext());
                                    nativeAdCallToAction.setVisibility(fanNativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
                                    nativeAdCallToAction.setText(fanNativeAd.getAdCallToAction());
                                    sponsoredLabel.setText(fanNativeAd.getSponsoredTranslation());

                                    // Create a list of clickable views
                                    List<View> clickableViews = new ArrayList<>();
                                    clickableViews.add(nativeAdTitle);
                                    clickableViews.add(sponsoredLabel);
                                    clickableViews.add(nativeAdIcon);
                                    clickableViews.add(nativeAdMedia);
                                    clickableViews.add(nativeAdBody);
                                    clickableViews.add(nativeAdSocialContext);
                                    clickableViews.add(nativeAdCallToAction);

                                    // Register the Title and CTA button to listen for clicks.
                                    fanNativeAd.registerViewForInteraction(nativeAdView, nativeAdIcon, nativeAdMedia, clickableViews);

                                }

                                @Override
                                public void onAdClicked(com.facebook.ads.Ad ad) {

                                }

                                @Override
                                public void onLoggingImpression(com.facebook.ads.Ad ad) {

                                }
                            };

                            com.facebook.ads.NativeAd.NativeLoadAdConfig loadAdConfig = fanNativeAd.buildLoadAdConfig().withAdListener(nativeAdListener).build();
                            fanNativeAd.loadAd(loadAdConfig);
                        } else {
                            Log.d(TAG, "FAN Native Ad has been loaded");
                            progressBarAd.setVisibility(View.GONE);
                        }
                        break;





                }

            }

        }

        public void loadBackupNativeAd() {

            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {


                fanNativeAdLayout = view.findViewById(R.id.fan_native_ad_container);

                startappNativeAd = view.findViewById(R.id.startapp_native_ad_container);
                startappNativeImage = view.findViewById(R.id.startapp_native_image);
                startappNativeIcon = activity.findViewById(R.id.startapp_native_icon);
                startappNativeTitle = view.findViewById(R.id.startapp_native_title);
                startappNativeDescription = view.findViewById(R.id.startapp_native_description);
                startappNativeButton = view.findViewById(R.id.startapp_native_button);
                startappNativeButton.setOnClickListener(v1 -> startappNativeAd.performClick());
                startappNativeBackground = view.findViewById(R.id.startapp_native_background);
                applovinNativeAd = view.findViewById(R.id.applovin_native_ad_container);
                progressBarAd = view.findViewById(R.id.progress_bar_ad);
                progressBarAd.setVisibility(View.VISIBLE);

                switch (backupAdNetwork) {

                    case FACEBOOK:
                        if (fanNativeAdLayout.getVisibility() != View.VISIBLE) {
                            fanNativeAd = new com.facebook.ads.NativeAd(activity, fanNativeId);
                            NativeAdListener nativeAdListener = new NativeAdListener() {
                                @Override
                                public void onMediaDownloaded(com.facebook.ads.Ad ad) {

                                }

                                @Override
                                public void onError(com.facebook.ads.Ad ad, AdError adError) {

                                }

                                @Override
                                public void onAdLoaded(com.facebook.ads.Ad ad) {
                                    // Race condition, load() called again before last ad was displayed
                                    fanNativeAdLayout.setVisibility(View.VISIBLE);
                                    progressBarAd.setVisibility(View.GONE);
                                    if (fanNativeAd != ad) {
                                        return;
                                    }
                                    // Inflate Native Ad into Container
                                    //inflateAd(nativeAd);
                                    fanNativeAd.unregisterView();
                                    // Add the Ad view into the ad container.
                                    LayoutInflater inflater = LayoutInflater.from(activity);
                                    // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                                    LinearLayout nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_large_template_view, fanNativeAdLayout, false);
                                    fanNativeAdLayout.addView(nativeAdView);

                                    // Add the AdOptionsView
                                    LinearLayout adChoicesContainer = nativeAdView.findViewById(R.id.ad_choices_container);
                                    AdOptionsView adOptionsView = new AdOptionsView(activity, fanNativeAd, fanNativeAdLayout);
                                    adChoicesContainer.removeAllViews();
                                    adChoicesContainer.addView(adOptionsView, 0);

                                    // Create native UI using the ad metadata.
                                    TextView nativeAdTitle = nativeAdView.findViewById(R.id.native_ad_title);
                                    com.facebook.ads.MediaView nativeAdMedia = nativeAdView.findViewById(R.id.native_ad_media);
                                    com.facebook.ads.MediaView nativeAdIcon = nativeAdView.findViewById(R.id.native_ad_icon);
                                    TextView nativeAdSocialContext = nativeAdView.findViewById(R.id.native_ad_social_context);
                                    TextView nativeAdBody = nativeAdView.findViewById(R.id.native_ad_body);
                                    TextView sponsoredLabel = nativeAdView.findViewById(R.id.native_ad_sponsored_label);
                                    Button nativeAdCallToAction = nativeAdView.findViewById(R.id.native_ad_call_to_action);
                                    LinearLayout fanNativeBackground = nativeAdView.findViewById(R.id.ad_unit);

                                    if (darkTheme) {
                                        nativeAdTitle.setTextColor(ContextCompat.getColor(activity, R.color.applovin_dark_primary_text_color));
                                        nativeAdSocialContext.setTextColor(ContextCompat.getColor(activity, R.color.applovin_dark_primary_text_color));
                                        sponsoredLabel.setTextColor(ContextCompat.getColor(activity, R.color.applovin_dark_secondary_text_color));
                                        nativeAdBody.setTextColor(ContextCompat.getColor(activity, R.color.applovin_dark_secondary_text_color));
                                        fanNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                    } else {
                                        fanNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                    }

                                    // Set the Text.
                                    nativeAdTitle.setText(fanNativeAd.getAdvertiserName());
                                    nativeAdBody.setText(fanNativeAd.getAdBodyText());
                                    nativeAdSocialContext.setText(fanNativeAd.getAdSocialContext());
                                    nativeAdCallToAction.setVisibility(fanNativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
                                    nativeAdCallToAction.setText(fanNativeAd.getAdCallToAction());
                                    sponsoredLabel.setText(fanNativeAd.getSponsoredTranslation());

                                    // Create a list of clickable views
                                    List<View> clickableViews = new ArrayList<>();
                                    clickableViews.add(nativeAdTitle);
                                    clickableViews.add(sponsoredLabel);
                                    clickableViews.add(nativeAdIcon);
                                    clickableViews.add(nativeAdMedia);
                                    clickableViews.add(nativeAdBody);
                                    clickableViews.add(nativeAdSocialContext);
                                    clickableViews.add(nativeAdCallToAction);

                                    // Register the Title and CTA button to listen for clicks.
                                    fanNativeAd.registerViewForInteraction(nativeAdView, nativeAdIcon, nativeAdMedia, clickableViews);

                                }

                                @Override
                                public void onAdClicked(com.facebook.ads.Ad ad) {

                                }

                                @Override
                                public void onLoggingImpression(com.facebook.ads.Ad ad) {

                                }
                            };

                            com.facebook.ads.NativeAd.NativeLoadAdConfig loadAdConfig = fanNativeAd.buildLoadAdConfig().withAdListener(nativeAdListener).build();
                            fanNativeAd.loadAd(loadAdConfig);
                        } else {
                            Log.d(TAG, "FAN Native Ad has been loaded");
                            progressBarAd.setVisibility(View.GONE);
                        }
                        break;

                   case NONE:
                        //do nothing
                        break;

                }

            }

        }



    }

}
