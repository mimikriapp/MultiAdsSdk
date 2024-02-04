package com.mimikridev.ad.sdk.format;

import static com.mimikridev.ad.sdk.util.Constant.AD_STATUS_ON;
import static com.mimikridev.ad.sdk.util.Constant.APPLOVIN_DISCOVERY;
import static com.mimikridev.ad.sdk.util.Constant.APPLOVIN_MAX;
import static com.mimikridev.ad.sdk.util.Constant.FACEBOOK;
import static com.mimikridev.ad.sdk.util.Constant.IRONSOURCE;
import static com.mimikridev.ad.sdk.util.Constant.PANGLE;
import static com.mimikridev.ad.sdk.util.Constant.STARTAPP;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.bytedance.sdk.openadsdk.api.reward.PAGRewardItem;
import com.bytedance.sdk.openadsdk.api.reward.PAGRewardedAd;
import com.bytedance.sdk.openadsdk.api.reward.PAGRewardedAdInteractionListener;
import com.bytedance.sdk.openadsdk.api.reward.PAGRewardedAdLoadListener;
import com.bytedance.sdk.openadsdk.api.reward.PAGRewardedRequest;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.RewardedVideoAdListener;

import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.LevelPlayRewardedVideoListener;
import com.mimikridev.ad.sdk.util.OnRewardedAdCompleteListener;
import com.mimikridev.ad.sdk.util.OnRewardedAdDismissedListener;
import com.mimikridev.ad.sdk.util.OnRewardedAdErrorListener;



public class RewardedAd {

    @SuppressWarnings("deprecation")
    public static class Builder {

        private static final String TAG = "AdNetwork Rewarded";
        private final Activity activity;

        private com.facebook.ads.RewardedVideoAd fanRewardedVideoAd;
        //public static PAGRewardedAd mPAGRewardedAd;
        public PAGRewardedAd mPAGRewardedAd;

        private String adStatus = "";
        private String mainAds = "";
        private String backupAds = "";
        private String fanRewardedId = "";
        private String PangleRewardedId = "";
        private String applovinMaxRewardedId = "";
        private String applovinDiscRewardedZoneId = "";
        private String ironSourceRewardedId = "";

        private int placementStatus = 1;
        private boolean legacyGDPR = false;

        public Builder(Activity activity) {
            this.activity = activity;

        }

        public Builder build(OnRewardedAdCompleteListener onComplete, OnRewardedAdDismissedListener onDismiss) {
            loadRewardedAd(onComplete, onDismiss);
            return this;
        }

        public Builder show(OnRewardedAdCompleteListener onComplete, OnRewardedAdDismissedListener onDismiss, OnRewardedAdErrorListener onError) {
            showRewardedAd(onComplete, onDismiss, onError);
            return this;
        }

        public Builder setAdStatus(String adStatus) {
            this.adStatus = adStatus;
            return this;
        }

        public Builder setMainAds(String mainAds) {
            this.mainAds = mainAds;
            return this;
        }

        public Builder setBackupAds(String backupAds) {
            this.backupAds = backupAds;
            return this;
        }



        public Builder setFanRewardedId(String fanRewardedId) {
            this.fanRewardedId = fanRewardedId;
            return this;
        }


        public Builder setApplovinMaxRewardedId(String applovinMaxRewardedId) {
            this.applovinMaxRewardedId = applovinMaxRewardedId;
            return this;
        }

        public Builder setApplovinDiscRewardedZoneId(String applovinDiscRewardedZoneId) {
            this.applovinDiscRewardedZoneId = applovinDiscRewardedZoneId;
            return this;
        }

        public Builder setIronSourceRewardedId(String ironSourceRewardedId) {
            this.ironSourceRewardedId = ironSourceRewardedId;
            return this;
        }

        public Builder setPangleRewardedId(String PangleRewardedId) {
            this.PangleRewardedId = PangleRewardedId;
            return this;
        }



        public Builder setPlacementStatus(int placementStatus) {
            this.placementStatus = placementStatus;
            return this;
        }

        public Builder setLegacyGDPR(boolean legacyGDPR) {
            this.legacyGDPR = legacyGDPR;
            return this;
        }

        public void loadRewardedAd(OnRewardedAdCompleteListener onComplete, OnRewardedAdDismissedListener onDismiss) {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (mainAds) {
                    case FACEBOOK:
                        fanRewardedVideoAd = new com.facebook.ads.RewardedVideoAd(activity, fanRewardedId);
                        fanRewardedVideoAd.loadAd(fanRewardedVideoAd.buildLoadAdConfig()
                                .withAdListener(new RewardedVideoAdListener() {
                                    @Override
                                    public void onRewardedVideoCompleted() {
                                        onComplete.onRewardedAdComplete();
                                        Log.d(TAG, "[" + mainAds + "] " + "rewarded ad complete");
                                    }

                                    @Override
                                    public void onRewardedVideoClosed() {
                                        loadRewardedAd(onComplete, onDismiss);
                                        onDismiss.onRewardedAdDismissed();
                                        Log.d(TAG, "[" + mainAds + "] " + "rewarded ad closed");
                                    }

                                    @Override
                                    public void onError(Ad ad, AdError adError) {
                                        loadRewardedBackupAd(onComplete, onDismiss);
                                        Log.d(TAG, "[" + mainAds + "] " + "failed to load rewarded ad: " + fanRewardedId + ", try to load backup ad: " + backupAds);
                                    }

                                    @Override
                                    public void onAdLoaded(Ad ad) {
                                        Log.d(TAG, "[" + mainAds + "] " + "rewarded ad loaded");
                                    }

                                    @Override
                                    public void onAdClicked(Ad ad) {

                                    }

                                    @Override
                                    public void onLoggingImpression(Ad ad) {

                                    }
                                })
                                .build());
                        break;



                    case PANGLE:
                        PAGRewardedRequest request = new PAGRewardedRequest();
                        PAGRewardedAd.loadAd(PangleRewardedId, request,
                                new PAGRewardedAdLoadListener() {
                                    @Override
                                    public void onError(int code, String message) {
                                        loadRewardedBackupAd(onComplete, onDismiss);
                                        Log.d(TAG, "[" + mainAds + "] " + "failed to load rewarded ad: "+" kode "+code +" "+ message + ", try to load backup ad: " + backupAds);
                                    }

                                    @Override
                                    public void onAdLoaded(PAGRewardedAd rewardedAd) {
                                        Log.d(TAG, "[" + mainAds + "] " + "rewarded ad loaded");
                                    }
                                });
                         new PAGRewardedAdInteractionListener() {
                             @Override
                             public void onAdShowed() {

                             }

                             @Override
                             public void onAdClicked() {

                             }

                             @Override
                             public void onAdDismissed() {
                                 loadRewardedAd(onComplete, onDismiss);
                                 onDismiss.onRewardedAdDismissed();
                             }

                             @Override
                             public void onUserEarnedReward(PAGRewardItem item) {
                                 onComplete.onRewardedAdComplete();
                                 Log.d(TAG, "[" + mainAds + "] " + "rewarded ad complete");
                             }

                             @Override
                             public void onUserEarnedRewardFail(int errorCode, String errorMsg) {
                                 loadRewardedAd(onComplete, onDismiss);
                                 onDismiss.onRewardedAdDismissed();
                                 Log.d(TAG, "[" + mainAds + "] " + "failed to load rewarded ad: " + errorCode + errorMsg + ", try to load backup ad: " + backupAds);

                             }
                         };


/*
                        mPAGRewardedAd.setAdInteractionListener(new PAGRewardedAdInteractionListener(){

                            @Override
                            public void onAdShowed() {

                            }

                            @Override
                            public void onAdClicked() {

                            }

                            @Override
                            public void onAdDismissed() {
                                loadRewardedAd(onComplete, onDismiss);
                                onDismiss.onRewardedAdDismissed();
                            }

                            @Override
                            public void onUserEarnedReward(PAGRewardItem item) {
                                onComplete.onRewardedAdComplete();
                                Log.d(TAG, "[" + mainAds + "] " + "rewarded ad complete");
                            }

                            @Override
                            public void onUserEarnedRewardFail(int errorCode, String errorMsg) {
                                loadRewardedAd(onComplete, onDismiss);
                                onDismiss.onRewardedAdDismissed();
                                Log.d(TAG, "[" + mainAds + "] " + "failed to load rewarded ad: " + errorCode + errorMsg + ", try to load backup ad: " + backupAds);

                            }
                        });



 */



                        break;

                    case IRONSOURCE:
                        IronSource.setLevelPlayRewardedVideoListener(new LevelPlayRewardedVideoListener() {
                            @Override
                            public void onAdAvailable(AdInfo adInfo) {
                                Log.d(TAG, "[" + mainAds + "] " + "rewarded ad is ready");
                            }

                            @Override
                            public void onAdUnavailable() {

                            }

                            @Override
                            public void onAdOpened(AdInfo adInfo) {

                            }

                            @Override
                            public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
                                loadRewardedBackupAd(onComplete, onDismiss);
                                Log.d(TAG, "[" + mainAds + "] " + "failed to load rewarded ad: " + ironSourceError.getErrorMessage() + ", try to load backup ad: " + backupAds);
                            }

                            @Override
                            public void onAdClicked(Placement placement, AdInfo adInfo) {

                            }

                            @Override
                            public void onAdRewarded(Placement placement, AdInfo adInfo) {
                                onComplete.onRewardedAdComplete();
                                Log.d(TAG, "[" + mainAds + "] " + "rewarded ad complete");
                            }

                            @Override
                            public void onAdClosed(AdInfo adInfo) {
                                loadRewardedAd(onComplete, onDismiss);
                            }
                        });
                        break;


                }
            }
        }

        public void loadRewardedBackupAd(OnRewardedAdCompleteListener onComplete, OnRewardedAdDismissedListener onDismiss) {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (backupAds) {

                    case FACEBOOK:
                        fanRewardedVideoAd = new com.facebook.ads.RewardedVideoAd(activity, fanRewardedId);
                        fanRewardedVideoAd.loadAd(fanRewardedVideoAd.buildLoadAdConfig()
                                .withAdListener(new RewardedVideoAdListener() {
                                    @Override
                                    public void onRewardedVideoCompleted() {
                                        onComplete.onRewardedAdComplete();
                                        Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad complete");
                                    }

                                    @Override
                                    public void onRewardedVideoClosed() {
                                        loadRewardedAd(onComplete, onDismiss);
                                        onDismiss.onRewardedAdDismissed();
                                        Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad closed");
                                    }

                                    @Override
                                    public void onError(Ad ad, AdError adError) {
                                        Log.d(TAG, "[" + backupAds + "] [backup] " + "failed to load rewarded ad: " + fanRewardedId + ", try to load backup ad: " + backupAds);
                                    }

                                    @Override
                                    public void onAdLoaded(Ad ad) {
                                        Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad loaded");
                                    }

                                    @Override
                                    public void onAdClicked(Ad ad) {

                                    }

                                    @Override
                                    public void onLoggingImpression(Ad ad) {

                                    }
                                })
                                .build());
                        break;




                    case PANGLE:
                        PAGRewardedRequest request = new PAGRewardedRequest();
                        PAGRewardedAd.loadAd(PangleRewardedId, request,
                                new PAGRewardedAdLoadListener() {
                                    @Override
                                    public void onError(int code, String message) {
                                        Log.d(TAG, "[" + backupAds + "] [backup] " + "failed to load rewarded ad: "+code +" "+ message + ", try to load backup ad: " + backupAds);
                                    }

                                    @Override
                                    public void onAdLoaded(PAGRewardedAd rewardedAd) {
                                        Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad loaded");
                                    }
                                });
                        new PAGRewardedAdInteractionListener() {
                            @Override
                            public void onAdShowed() {

                            }

                            @Override
                            public void onAdClicked() {

                            }

                            @Override
                            public void onAdDismissed() {
                                loadRewardedAd(onComplete, onDismiss);
                                //onDismiss.onRewardedAdDismissed();
                            }

                            @Override
                            public void onUserEarnedReward(PAGRewardItem item) {
                                onComplete.onRewardedAdComplete();
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad complete");
                            }

                            @Override
                            public void onUserEarnedRewardFail(int errorCode, String errorMsg) {
                                loadRewardedAd(onComplete, onDismiss);
                            }
                        };

                    /*    mPAGRewardedAd.setAdInteractionListener(new PAGRewardedAdInteractionListener(){

                            @Override
                            public void onAdShowed() {

                            }

                            @Override
                            public void onAdClicked() {

                            }

                            @Override
                            public void onAdDismissed() {
                                loadRewardedAd(onComplete, onDismiss);
                                //onDismiss.onRewardedAdDismissed();
                            }

                            @Override
                            public void onUserEarnedReward(PAGRewardItem item) {
                                onComplete.onRewardedAdComplete();
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad complete");
                            }

                            @Override
                            public void onUserEarnedRewardFail(int errorCode, String errorMsg) {
                                loadRewardedAd(onComplete, onDismiss);
                            }
                        });

                     */



                        break;





                    case IRONSOURCE:
                        IronSource.setLevelPlayRewardedVideoListener(new LevelPlayRewardedVideoListener() {
                            @Override
                            public void onAdAvailable(AdInfo adInfo) {
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad is ready");
                            }

                            @Override
                            public void onAdUnavailable() {

                            }

                            @Override
                            public void onAdOpened(AdInfo adInfo) {

                            }

                            @Override
                            public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "failed to load rewarded ad: " + ironSourceError.getErrorMessage() + ", try to load backup ad: " + backupAds);
                            }

                            @Override
                            public void onAdClicked(Placement placement, AdInfo adInfo) {

                            }

                            @Override
                            public void onAdRewarded(Placement placement, AdInfo adInfo) {
                                onComplete.onRewardedAdComplete();
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad complete");
                            }

                            @Override
                            public void onAdClosed(AdInfo adInfo) {
                                loadRewardedAd(onComplete, onDismiss);
                            }
                        });
                        break;


                }
            }
        }

        public void showRewardedAd(OnRewardedAdCompleteListener onComplete, OnRewardedAdDismissedListener onDismiss, OnRewardedAdErrorListener onError) {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (mainAds) {

                    case FACEBOOK:
                        if (fanRewardedVideoAd != null && fanRewardedVideoAd.isAdLoaded()) {
                            fanRewardedVideoAd.show();
                        } else {
                            showRewardedBackupAd(onComplete, onDismiss, onError);
                        }
                        break;

                    case IRONSOURCE:
                        if (IronSource.isRewardedVideoAvailable()) {
                            IronSource.showRewardedVideo(ironSourceRewardedId);
                        } else {
                            showRewardedBackupAd(onComplete, onDismiss, onError);
                        }
                        break;

                    case PANGLE:
                        if (mPAGRewardedAd != null) {
                            mPAGRewardedAd.show(activity);
                        } else {
                            showRewardedBackupAd(onComplete, onDismiss, onError);
                        }

                        break;



                    default:
                        onError.onRewardedAdError();
                        break;
                }
            }

        }

        public void showRewardedBackupAd(OnRewardedAdCompleteListener onComplete, OnRewardedAdDismissedListener onDismiss, OnRewardedAdErrorListener onError) {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (backupAds) {

                    case FACEBOOK:
                        if (fanRewardedVideoAd != null && fanRewardedVideoAd.isAdLoaded()) {
                            fanRewardedVideoAd.show();
                        } else {
                            onError.onRewardedAdError();
                        }
                        break;

                    case PANGLE:
                        if (mPAGRewardedAd != null) {
                            mPAGRewardedAd.show(activity);
                        } else {
                            onError.onRewardedAdError();
                        }

                        break;

                    case IRONSOURCE:
                        if (IronSource.isRewardedVideoAvailable()) {
                            IronSource.showRewardedVideo(ironSourceRewardedId);
                        } else {
                            onError.onRewardedAdError();
                        }
                        break;


                    default:
                        onError.onRewardedAdError();
                        break;
                }
            }

        }

        public void destroyRewardedAd() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (mainAds) {
                       case FACEBOOK:
                        if (fanRewardedVideoAd != null) {
                            fanRewardedVideoAd.destroy();
                            fanRewardedVideoAd = null;
                        }
                        break;
                }

                switch (backupAds) {
                    case FACEBOOK:
                        if (fanRewardedVideoAd != null) {
                            fanRewardedVideoAd.destroy();
                            fanRewardedVideoAd = null;
                        }
                        break;
                }
            }
        }

    }

}
