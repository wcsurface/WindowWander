package com.roadtripbingo.app.data

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

object AdMobManager {

    private const val TAG = "AdMobManager"
    
    // Official Google AdMob Rewarded Test Ad Unit ID for Android
    private const val TEST_REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

    private var isMobileAdsInitialized = false

    fun initialize(activity: Activity) {
        if (!isMobileAdsInitialized) {
            MobileAds.initialize(activity) {
                isMobileAdsInitialized = true
                Log.d(TAG, "Google Mobile Ads SDK Initialized")
            }
        }
    }

    /**
     * Loads and shows a Google AdMob Rewarded Video Test Ad.
     * Invokes [onRewardEarned] on the UI Thread when player finishes watching the video.
     */
    fun showRewardedAd(
        activity: Activity,
        onRewardEarned: () -> Unit,
        onAdFailed: () -> Unit
    ) {
        initialize(activity)

        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            activity,
            TEST_REWARDED_AD_UNIT_ID,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    Log.d(TAG, "Rewarded Ad loaded successfully")
                    rewardedAd.show(activity) { rewardItem ->
                        Log.d(TAG, "User earned reward: ${rewardItem.amount} ${rewardItem.type}")
                        activity.runOnUiThread {
                            onRewardEarned()
                        }
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.e(TAG, "Rewarded Ad failed to load: ${loadAdError.message}")
                    activity.runOnUiThread {
                        onRewardEarned()
                    }
                }
            }
        )
    }
}
