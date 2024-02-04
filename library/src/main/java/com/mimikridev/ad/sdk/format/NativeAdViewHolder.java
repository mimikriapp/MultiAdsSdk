package com.mimikridev.ad.sdk.format;


import static com.mimikridev.ad.sdk.util.Constant.AD_STATUS_ON;
import static com.mimikridev.ad.sdk.util.Constant.APPLOVIN_DISCOVERY;
import static com.mimikridev.ad.sdk.util.Constant.APPLOVIN_MAX;
import static com.mimikridev.ad.sdk.util.Constant.FACEBOOK;
import static com.mimikridev.ad.sdk.util.Constant.NONE;
import static com.mimikridev.ad.sdk.util.Constant.STARTAPP;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.mimikridev.ad.sdk.R;
import com.mimikridev.ad.sdk.util.Constant;



import java.util.ArrayList;
import java.util.List;

public class NativeAdViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "AdNetwork";
    LinearLayout nativeAdViewContainer;



    //FAN
    com.facebook.ads.NativeAd fanNativeAd;
    NativeAdLayout fanNativeAdLayout;

    //StartApp
    View startappNativeAd;
    ImageView startappNativeImage;
    ImageView startappNativeIcon;
    TextView startappNativeTitle;
    TextView startappNativeDescription;
    Button startappNativeButton;
    LinearLayout startappNativeBackground;

    //AppLovin
    FrameLayout applovinNativeAd;
    LinearLayout appLovinDiscoveryMrecAd;


    public NativeAdViewHolder(View view) {
        super(view);

        nativeAdViewContainer = view.findViewById(R.id.native_ad_view_container);


        //FAN
        fanNativeAdLayout = view.findViewById(R.id.fan_native_ad_container);

        //StartApp
        startappNativeAd = view.findViewById(R.id.startapp_native_ad_container);
        startappNativeImage = view.findViewById(R.id.startapp_native_image);
        startappNativeIcon = view.findViewById(R.id.startapp_native_icon);
        startappNativeTitle = view.findViewById(R.id.startapp_native_title);
        startappNativeDescription = view.findViewById(R.id.startapp_native_description);
        startappNativeButton = view.findViewById(R.id.startapp_native_button);
        startappNativeButton.setOnClickListener(v1 -> itemView.performClick());
        startappNativeBackground = view.findViewById(R.id.startapp_native_background);

        //AppLovin
        applovinNativeAd = view.findViewById(R.id.applovin_native_ad_container);
        appLovinDiscoveryMrecAd = view.findViewById(R.id.applovin_discovery_mrec_ad_container);



    }

    public void loadNativeAd(Context context, String adStatus, int placementStatus, String adNetwork, String backupAdNetwork,String fanNativeId, String appLovinNativeId, String appLovinDiscMrecZoneId, String PangleNativeId, boolean darkTheme, boolean legacyGDPR, String nativeAdStyle, int nativeBackgroundLight, int nativeBackgroundDark) {
        if (adStatus.equals(AD_STATUS_ON)) {
            if (placementStatus != 0) {
                switch (adNetwork) {

                    case FACEBOOK:
                        if (fanNativeAdLayout.getVisibility() != View.VISIBLE) {
                            fanNativeAd = new com.facebook.ads.NativeAd(context, fanNativeId);
                            NativeAdListener nativeAdListener = new NativeAdListener() {
                                @Override
                                public void onMediaDownloaded(com.facebook.ads.Ad ad) {

                                }

                                @Override
                                public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                    loadBackupNativeAd(context, adStatus, placementStatus, backupAdNetwork,fanNativeId, appLovinNativeId, appLovinDiscMrecZoneId, PangleNativeId, darkTheme, legacyGDPR, nativeAdStyle, nativeBackgroundLight, nativeBackgroundDark);
                                }

                                @Override
                                public void onAdLoaded(com.facebook.ads.Ad ad) {
                                    // Race condition, load() called again before last ad was displayed
                                    fanNativeAdLayout.setVisibility(View.VISIBLE);
                                    nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    if (fanNativeAd != ad) {
                                        return;
                                    }
                                    // Inflate Native Ad into Container
                                    //inflateAd(nativeAd);
                                    fanNativeAd.unregisterView();
                                    // Add the Ad view into the ad container.
                                    LayoutInflater inflater = LayoutInflater.from(context);
                                    // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                                    LinearLayout nativeAdView;

                                    switch (nativeAdStyle) {
                                        case Constant.STYLE_NEWS:
                                        case Constant.STYLE_MEDIUM:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_news_template_view, fanNativeAdLayout, false);
                                            break;
                                        case Constant.STYLE_VIDEO_SMALL:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_video_small_template_view, fanNativeAdLayout, false);
                                            break;
                                        case Constant.STYLE_VIDEO_LARGE:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_video_large_template_view, fanNativeAdLayout, false);
                                            break;
                                        case Constant.STYLE_RADIO:
                                        case Constant.STYLE_SMALL:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_radio_template_view, fanNativeAdLayout, false);
                                            break;
                                        default:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_medium_template_view, fanNativeAdLayout, false);
                                            break;
                                    }
                                    fanNativeAdLayout.addView(nativeAdView);

                                    // Add the AdOptionsView
                                    LinearLayout adChoicesContainer = nativeAdView.findViewById(R.id.ad_choices_container);
                                    AdOptionsView adOptionsView = new AdOptionsView(context, fanNativeAd, fanNativeAdLayout);
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
                                        nativeAdTitle.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_primary_text_color));
                                        nativeAdSocialContext.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_primary_text_color));
                                        sponsoredLabel.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_secondary_text_color));
                                        nativeAdBody.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_secondary_text_color));
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
                        }
                        break;




                }
            }
        }
    }

    public void loadBackupNativeAd(Context context, String adStatus, int placementStatus, String backupAdNetwork,String fanNativeId, String appLovinNativeId, String appLovinDiscMrecZoneId, String PangleNativeId, boolean darkTheme, boolean legacyGDPR, String nativeAdStyle, int nativeBackgroundLight, int nativeBackgroundDark) {
        if (adStatus.equals(AD_STATUS_ON)) {
            if (placementStatus != 0) {
                switch (backupAdNetwork) {

                    case FACEBOOK:
                        if (fanNativeAdLayout.getVisibility() != View.VISIBLE) {
                            fanNativeAd = new com.facebook.ads.NativeAd(context, fanNativeId);
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
                                    nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    if (fanNativeAd != ad) {
                                        return;
                                    }
                                    // Inflate Native Ad into Container
                                    //inflateAd(nativeAd);
                                    fanNativeAd.unregisterView();
                                    // Add the Ad view into the ad container.
                                    LayoutInflater inflater = LayoutInflater.from(context);
                                    // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                                    LinearLayout nativeAdView;

                                    switch (nativeAdStyle) {
                                        case Constant.STYLE_NEWS:
                                        case Constant.STYLE_MEDIUM:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_news_template_view, fanNativeAdLayout, false);
                                            break;
                                        case Constant.STYLE_VIDEO_SMALL:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_video_small_template_view, fanNativeAdLayout, false);
                                            break;
                                        case Constant.STYLE_VIDEO_LARGE:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_video_large_template_view, fanNativeAdLayout, false);
                                            break;
                                        case Constant.STYLE_RADIO:
                                        case Constant.STYLE_SMALL:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_radio_template_view, fanNativeAdLayout, false);
                                            break;
                                        default:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_medium_template_view, fanNativeAdLayout, false);
                                            break;
                                    }
                                    fanNativeAdLayout.addView(nativeAdView);

                                    // Add the AdOptionsView
                                    LinearLayout adChoicesContainer = nativeAdView.findViewById(R.id.ad_choices_container);
                                    AdOptionsView adOptionsView = new AdOptionsView(context, fanNativeAd, fanNativeAdLayout);
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
                                        nativeAdTitle.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_primary_text_color));
                                        nativeAdSocialContext.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_primary_text_color));
                                        sponsoredLabel.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_secondary_text_color));
                                        nativeAdBody.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_secondary_text_color));
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
                        }
                        break;


                    case NONE:
                        nativeAdViewContainer.setVisibility(View.GONE);
                        break;

                }
            }
        }
    }

    public void loadNativeAd(Context context, String adStatus, int placementStatus, String adNetwork, String backupAdNetwork,String fanNativeId, String appLovinNativeId, String appLovinDiscMrecZoneId, boolean darkTheme, boolean legacyGDPR, String nativeAdStyle, int nativeBackgroundLight, int nativeBackgroundDark) {
        if (adStatus.equals(AD_STATUS_ON)) {
            if (placementStatus != 0) {
                switch (adNetwork) {

                    case FACEBOOK:
                        if (fanNativeAdLayout.getVisibility() != View.VISIBLE) {
                            fanNativeAd = new com.facebook.ads.NativeAd(context, fanNativeId);
                            NativeAdListener nativeAdListener = new NativeAdListener() {
                                @Override
                                public void onMediaDownloaded(com.facebook.ads.Ad ad) {

                                }

                                @Override
                                public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                    loadBackupNativeAd(context, adStatus, placementStatus, backupAdNetwork,fanNativeId, appLovinNativeId, appLovinDiscMrecZoneId, darkTheme, legacyGDPR, nativeAdStyle, nativeBackgroundLight, nativeBackgroundDark);
                                }

                                @Override
                                public void onAdLoaded(com.facebook.ads.Ad ad) {
                                    // Race condition, load() called again before last ad was displayed
                                    fanNativeAdLayout.setVisibility(View.VISIBLE);
                                    nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    if (fanNativeAd != ad) {
                                        return;
                                    }
                                    // Inflate Native Ad into Container
                                    //inflateAd(nativeAd);
                                    fanNativeAd.unregisterView();
                                    // Add the Ad view into the ad container.
                                    LayoutInflater inflater = LayoutInflater.from(context);
                                    // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                                    LinearLayout nativeAdView;

                                    switch (nativeAdStyle) {
                                        case Constant.STYLE_NEWS:
                                        case Constant.STYLE_MEDIUM:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_news_template_view, fanNativeAdLayout, false);
                                            break;
                                        case Constant.STYLE_VIDEO_SMALL:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_video_small_template_view, fanNativeAdLayout, false);
                                            break;
                                        case Constant.STYLE_VIDEO_LARGE:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_video_large_template_view, fanNativeAdLayout, false);
                                            break;
                                        case Constant.STYLE_RADIO:
                                        case Constant.STYLE_SMALL:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_radio_template_view, fanNativeAdLayout, false);
                                            break;
                                        default:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_medium_template_view, fanNativeAdLayout, false);
                                            break;
                                    }
                                    fanNativeAdLayout.addView(nativeAdView);

                                    // Add the AdOptionsView
                                    LinearLayout adChoicesContainer = nativeAdView.findViewById(R.id.ad_choices_container);
                                    AdOptionsView adOptionsView = new AdOptionsView(context, fanNativeAd, fanNativeAdLayout);
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
                                        nativeAdTitle.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_primary_text_color));
                                        nativeAdSocialContext.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_primary_text_color));
                                        sponsoredLabel.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_secondary_text_color));
                                        nativeAdBody.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_secondary_text_color));
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
                        }
                        break;



                }
            }
        }
    }

    public void loadBackupNativeAd(Context context, String adStatus, int placementStatus, String backupAdNetwork,String fanNativeId, String appLovinNativeId, String appLovinDiscMrecZoneId, boolean darkTheme, boolean legacyGDPR, String nativeAdStyle, int nativeBackgroundLight, int nativeBackgroundDark) {
        if (adStatus.equals(AD_STATUS_ON)) {
            if (placementStatus != 0) {
                switch (backupAdNetwork) {

                    case FACEBOOK:
                        if (fanNativeAdLayout.getVisibility() != View.VISIBLE) {
                            fanNativeAd = new com.facebook.ads.NativeAd(context, fanNativeId);
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
                                    nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    if (fanNativeAd != ad) {
                                        return;
                                    }
                                    // Inflate Native Ad into Container
                                    //inflateAd(nativeAd);
                                    fanNativeAd.unregisterView();
                                    // Add the Ad view into the ad container.
                                    LayoutInflater inflater = LayoutInflater.from(context);
                                    // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                                    LinearLayout nativeAdView;

                                    switch (nativeAdStyle) {
                                        case Constant.STYLE_NEWS:
                                        case Constant.STYLE_MEDIUM:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_news_template_view, fanNativeAdLayout, false);
                                            break;
                                        case Constant.STYLE_VIDEO_SMALL:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_video_small_template_view, fanNativeAdLayout, false);
                                            break;
                                        case Constant.STYLE_VIDEO_LARGE:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_video_large_template_view, fanNativeAdLayout, false);
                                            break;
                                        case Constant.STYLE_RADIO:
                                        case Constant.STYLE_SMALL:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_radio_template_view, fanNativeAdLayout, false);
                                            break;
                                        default:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_medium_template_view, fanNativeAdLayout, false);
                                            break;
                                    }
                                    fanNativeAdLayout.addView(nativeAdView);

                                    // Add the AdOptionsView
                                    LinearLayout adChoicesContainer = nativeAdView.findViewById(R.id.ad_choices_container);
                                    AdOptionsView adOptionsView = new AdOptionsView(context, fanNativeAd, fanNativeAdLayout);
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
                                        nativeAdTitle.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_primary_text_color));
                                        nativeAdSocialContext.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_primary_text_color));
                                        sponsoredLabel.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_secondary_text_color));
                                        nativeAdBody.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_secondary_text_color));
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
                        }
                        break;



                    case NONE:
                        nativeAdViewContainer.setVisibility(View.GONE);
                        break;

                }
            }
        }
    }

    public void loadNativeAd(Context context, String adStatus, int placementStatus, String adNetwork, String backupAdNetwork,String fanNativeId, String appLovinNativeId, boolean darkTheme, boolean legacyGDPR, String nativeAdStyle, int nativeBackgroundLight, int nativeBackgroundDark) {
        if (adStatus.equals(AD_STATUS_ON)) {
            if (placementStatus != 0) {
                switch (adNetwork) {
                    case FACEBOOK:
                        if (fanNativeAdLayout.getVisibility() != View.VISIBLE) {
                            fanNativeAd = new com.facebook.ads.NativeAd(context, fanNativeId);
                            NativeAdListener nativeAdListener = new NativeAdListener() {
                                @Override
                                public void onMediaDownloaded(com.facebook.ads.Ad ad) {

                                }

                                @Override
                                public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                    loadBackupNativeAd(context, adStatus, placementStatus, backupAdNetwork,fanNativeId, appLovinNativeId, darkTheme, legacyGDPR, nativeAdStyle, nativeBackgroundLight, nativeBackgroundDark);
                                }

                                @Override
                                public void onAdLoaded(com.facebook.ads.Ad ad) {
                                    // Race condition, load() called again before last ad was displayed
                                    fanNativeAdLayout.setVisibility(View.VISIBLE);
                                    nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    if (fanNativeAd != ad) {
                                        return;
                                    }
                                    // Inflate Native Ad into Container
                                    //inflateAd(nativeAd);
                                    fanNativeAd.unregisterView();
                                    // Add the Ad view into the ad container.
                                    LayoutInflater inflater = LayoutInflater.from(context);
                                    // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                                    LinearLayout nativeAdView;

                                    switch (nativeAdStyle) {
                                        case Constant.STYLE_NEWS:
                                        case Constant.STYLE_MEDIUM:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_news_template_view, fanNativeAdLayout, false);
                                            break;
                                        case Constant.STYLE_VIDEO_SMALL:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_video_small_template_view, fanNativeAdLayout, false);
                                            break;
                                        case Constant.STYLE_VIDEO_LARGE:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_video_large_template_view, fanNativeAdLayout, false);
                                            break;
                                        case Constant.STYLE_RADIO:
                                        case Constant.STYLE_SMALL:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_radio_template_view, fanNativeAdLayout, false);
                                            break;
                                        default:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_medium_template_view, fanNativeAdLayout, false);
                                            break;
                                    }
                                    fanNativeAdLayout.addView(nativeAdView);

                                    // Add the AdOptionsView
                                    LinearLayout adChoicesContainer = nativeAdView.findViewById(R.id.ad_choices_container);
                                    AdOptionsView adOptionsView = new AdOptionsView(context, fanNativeAd, fanNativeAdLayout);
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
                                        nativeAdTitle.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_primary_text_color));
                                        nativeAdSocialContext.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_primary_text_color));
                                        sponsoredLabel.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_secondary_text_color));
                                        nativeAdBody.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_secondary_text_color));
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
                        }
                        break;


                }
            }
        }
    }

    public void loadBackupNativeAd(Context context, String adStatus, int placementStatus, String backupAdNetwork,String fanNativeId, String appLovinNativeId, boolean darkTheme, boolean legacyGDPR, String nativeAdStyle, int nativeBackgroundLight, int nativeBackgroundDark) {
        if (adStatus.equals(AD_STATUS_ON)) {
            if (placementStatus != 0) {
                switch (backupAdNetwork) {
                    case FACEBOOK:
                        if (fanNativeAdLayout.getVisibility() != View.VISIBLE) {
                            fanNativeAd = new com.facebook.ads.NativeAd(context, fanNativeId);
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
                                    nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    if (fanNativeAd != ad) {
                                        return;
                                    }
                                    // Inflate Native Ad into Container
                                    //inflateAd(nativeAd);
                                    fanNativeAd.unregisterView();
                                    // Add the Ad view into the ad container.
                                    LayoutInflater inflater = LayoutInflater.from(context);
                                    // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                                    LinearLayout nativeAdView;

                                    switch (nativeAdStyle) {
                                        case Constant.STYLE_NEWS:
                                        case Constant.STYLE_MEDIUM:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_news_template_view, fanNativeAdLayout, false);
                                            break;
                                        case Constant.STYLE_VIDEO_SMALL:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_video_small_template_view, fanNativeAdLayout, false);
                                            break;
                                        case Constant.STYLE_VIDEO_LARGE:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_video_large_template_view, fanNativeAdLayout, false);
                                            break;
                                        case Constant.STYLE_RADIO:
                                        case Constant.STYLE_SMALL:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_radio_template_view, fanNativeAdLayout, false);
                                            break;
                                        default:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_medium_template_view, fanNativeAdLayout, false);
                                            break;
                                    }
                                    fanNativeAdLayout.addView(nativeAdView);

                                    // Add the AdOptionsView
                                    LinearLayout adChoicesContainer = nativeAdView.findViewById(R.id.ad_choices_container);
                                    AdOptionsView adOptionsView = new AdOptionsView(context, fanNativeAd, fanNativeAdLayout);
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
                                        nativeAdTitle.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_primary_text_color));
                                        nativeAdSocialContext.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_primary_text_color));
                                        sponsoredLabel.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_secondary_text_color));
                                        nativeAdBody.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_secondary_text_color));
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
                        }
                        break;



                    case NONE:
                        nativeAdViewContainer.setVisibility(View.GONE);
                        break;

                }
            }
        }
    }

    public void loadNativeAd(Context context, String adStatus, int placementStatus, String adNetwork, String backupAdNetwork,String fanNativeId, String appLovinNativeId, boolean darkTheme, boolean legacyGDPR, String nativeAdStyle) {
        if (adStatus.equals(AD_STATUS_ON)) {
            if (placementStatus != 0) {
                switch (adNetwork) {
                    case FACEBOOK:
                        if (fanNativeAdLayout.getVisibility() != View.VISIBLE) {
                            fanNativeAd = new com.facebook.ads.NativeAd(context, fanNativeId);
                            NativeAdListener nativeAdListener = new NativeAdListener() {
                                @Override
                                public void onMediaDownloaded(com.facebook.ads.Ad ad) {

                                }

                                @Override
                                public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                    loadBackupNativeAd(context, adStatus, placementStatus, backupAdNetwork,fanNativeId, appLovinNativeId, darkTheme, legacyGDPR, nativeAdStyle);
                                }

                                @Override
                                public void onAdLoaded(com.facebook.ads.Ad ad) {
                                    // Race condition, load() called again before last ad was displayed
                                    fanNativeAdLayout.setVisibility(View.VISIBLE);
                                    nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    if (fanNativeAd != ad) {
                                        return;
                                    }
                                    // Inflate Native Ad into Container
                                    //inflateAd(nativeAd);
                                    fanNativeAd.unregisterView();
                                    // Add the Ad view into the ad container.
                                    LayoutInflater inflater = LayoutInflater.from(context);
                                    // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                                    LinearLayout nativeAdView;

                                    switch (nativeAdStyle) {
                                        case Constant.STYLE_NEWS:
                                        case Constant.STYLE_MEDIUM:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_news_template_view, fanNativeAdLayout, false);
                                            break;
                                        case Constant.STYLE_VIDEO_SMALL:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_video_small_template_view, fanNativeAdLayout, false);
                                            break;
                                        case Constant.STYLE_VIDEO_LARGE:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_video_large_template_view, fanNativeAdLayout, false);
                                            break;
                                        case Constant.STYLE_RADIO:
                                        case Constant.STYLE_SMALL:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_radio_template_view, fanNativeAdLayout, false);
                                            break;
                                        default:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_medium_template_view, fanNativeAdLayout, false);
                                            break;
                                    }
                                    fanNativeAdLayout.addView(nativeAdView);

                                    // Add the AdOptionsView
                                    LinearLayout adChoicesContainer = nativeAdView.findViewById(R.id.ad_choices_container);
                                    AdOptionsView adOptionsView = new AdOptionsView(context, fanNativeAd, fanNativeAdLayout);
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

                                    if (darkTheme) {
                                        nativeAdTitle.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_primary_text_color));
                                        nativeAdSocialContext.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_primary_text_color));
                                        sponsoredLabel.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_secondary_text_color));
                                        nativeAdBody.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_secondary_text_color));
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
                        }
                        break;



                }
            }
        }
    }

    public void loadBackupNativeAd(Context context, String adStatus, int placementStatus, String backupAdNetwork,String fanNativeId, String appLovinNativeId, boolean darkTheme, boolean legacyGDPR, String nativeAdStyle) {
        if (adStatus.equals(AD_STATUS_ON)) {
            if (placementStatus != 0) {
                switch (backupAdNetwork) {
                    case FACEBOOK:
                        if (fanNativeAdLayout.getVisibility() != View.VISIBLE) {
                            fanNativeAd = new com.facebook.ads.NativeAd(context, fanNativeId);
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
                                    nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    if (fanNativeAd != ad) {
                                        return;
                                    }
                                    // Inflate Native Ad into Container
                                    //inflateAd(nativeAd);
                                    fanNativeAd.unregisterView();
                                    // Add the Ad view into the ad container.
                                    LayoutInflater inflater = LayoutInflater.from(context);
                                    // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                                    LinearLayout nativeAdView;

                                    switch (nativeAdStyle) {
                                        case Constant.STYLE_NEWS:
                                        case Constant.STYLE_MEDIUM:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_news_template_view, fanNativeAdLayout, false);
                                            break;
                                        case Constant.STYLE_VIDEO_SMALL:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_video_small_template_view, fanNativeAdLayout, false);
                                            break;
                                        case Constant.STYLE_VIDEO_LARGE:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_video_large_template_view, fanNativeAdLayout, false);
                                            break;
                                        case Constant.STYLE_RADIO:
                                        case Constant.STYLE_SMALL:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_radio_template_view, fanNativeAdLayout, false);
                                            break;
                                        default:
                                            nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_medium_template_view, fanNativeAdLayout, false);
                                            break;
                                    }
                                    fanNativeAdLayout.addView(nativeAdView);

                                    // Add the AdOptionsView
                                    LinearLayout adChoicesContainer = nativeAdView.findViewById(R.id.ad_choices_container);
                                    AdOptionsView adOptionsView = new AdOptionsView(context, fanNativeAd, fanNativeAdLayout);
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

                                    if (darkTheme) {
                                        nativeAdTitle.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_primary_text_color));
                                        nativeAdSocialContext.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_primary_text_color));
                                        sponsoredLabel.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_secondary_text_color));
                                        nativeAdBody.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_secondary_text_color));
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
                        }
                        break;



                    case NONE:
                        nativeAdViewContainer.setVisibility(View.GONE);
                        break;

                }
            }
        }
    }

    public void setNativeAdPadding(int left, int top, int right, int bottom) {
        nativeAdViewContainer.setPadding(left, top, right, bottom);
    }

    public void setNativeAdMargin(int left, int top, int right, int bottom) {
        setMargins(nativeAdViewContainer, left, top, right, bottom);
    }

    public void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public void setNativeAdBackgroundResource(int drawableBackground) {
        nativeAdViewContainer.setBackgroundResource(drawableBackground);
    }

    public void setNativeAdBackgroundColor(Context context, boolean darkTheme, int nativeBackgroundLight, int nativeBackgroundDark) {
        if (darkTheme) {
            nativeAdViewContainer.setBackgroundColor(ContextCompat.getColor(context, nativeBackgroundDark));
        } else {
            nativeAdViewContainer.setBackgroundColor(ContextCompat.getColor(context, nativeBackgroundLight));
        }
    }




}
