package com.buckriderstudio.numbergame.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.buckriderstudio.numbergame.ActionResolver;
import com.buckriderstudio.numbergame.DialogCreator;
import com.buckriderstudio.numbergame.GameScreen;
import com.buckriderstudio.numbergame.NumberGame;
import com.buckriderstudio.numbergame.android.R;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;


public class AndroidLauncher extends AndroidApplication
        implements ActionResolver,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    // Client used to interact with Google APIs
    private GoogleApiClient mGoogleApiClient;

    // Are we currently resolving a connection failure?
    private boolean mResolvingConnectionFailure = false;

    // Has the user clicked the sign-in button?
    private boolean mSignInClicked = false;

    // Automatically start the sign-in flow when the Activity starts
    private boolean mAutoStartSignInFlow = true;

    // request codes we use when invoking an external activity
    private static final int RC_RESOLVE = 5000;
    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;

    // tag for debug logging
    final boolean ENABLE_DEBUG = true;
    final String TAG = "MainActivity";

    // achievements and scores we're pending to push to the cloud
    // (waiting for the user to sign in, for instance)
    AccomplishmentsOutbox mOutbox = new AccomplishmentsOutbox();

    private InterstitialAd mInterstitialAd;
    private AdView mAdView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new NumberGame(this), config);

        //Banner
        /*
        mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        */


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad));

        requestNewInterstitial();

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                //beginSecondActivity();
            }
        });


    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                BaseGameUtils.showActivityResultError(this, requestCode, resultCode, R.string.signin_other_error);
            }
        }
    }

    private boolean isSignedIn()
    {
        return (mGoogleApiClient != null && mGoogleApiClient.isConnected());
    }

    @Override
    public void onStart()
    {
        super.onStart();

        if (mGoogleApiClient == null)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                    .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                    .build();
        }

        Gdx.app.log(TAG, "onStart(): connecting");
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Gdx.app.log(TAG, "onStop(): disconnecting");
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Gdx.app.log(TAG, "onConnected(): connected to Google APIs");
        // Show sign-out button on main menu

        // Show "you are signed in" message on win screen, with no sign in button.

        // Set the greeting appropriately on main menu
        Player p = Games.Players.getCurrentPlayer(mGoogleApiClient);
        String displayName;
        if (p == null) {
            Gdx.app.log(TAG, "mGamesClient.getCurrentPlayer() is NULL!");
            displayName = "???";
        } else {
            displayName = p.getDisplayName();
        }
        Gdx.app.log(TAG, "Hello, " + displayName);

        // if we have accomplishments to push, push them
/*
        if (!mOutbox.isEmpty()) {
            pushAccomplishments();
            Toast.makeText(this, getString(R.string.your_progress_will_be_uploaded),
                    Toast.LENGTH_LONG).show();
        }
*/
    }

    void pushAccomplishments() {
        if (!isSignedIn()) {
            // can't push to the cloud, so save locally
            mOutbox.saveLocal(this);
            return;
        }
        if (mOutbox.mPrimeAchievement) {
            Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_prime));
            mOutbox.mPrimeAchievement = false;
        }
        if (mOutbox.mArrogantAchievement) {
            Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_arrogant));
            mOutbox.mArrogantAchievement = false;
        }
        if (mOutbox.mHumbleAchievement) {
            Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_humble));
            mOutbox.mHumbleAchievement = false;
        }
        if (mOutbox.mLeetAchievement) {
            Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_leet));
            mOutbox.mLeetAchievement = false;
        }
        if (mOutbox.mDevilAchievement) {
            Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_devil));
            mOutbox.mLeetAchievement = false;
        }
        if (mOutbox.mMagicNumber) {
            Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_magical_score));
            mOutbox.mMagicNumber= false;
        }
        if (mOutbox.mDeveloperBirth) {
            Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_developers_date_of_birth));
            mOutbox.mDeveloperBirth = false;
        }
        if (mOutbox.mRicksBirth) {
            Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_ricks_date_of_birth));
            mOutbox.mRicksBirth = false;
        }
        if (mOutbox.mGirlFriendsBirth) {
            Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_girlfriends_date_of_birth));
            mOutbox.mGirlFriendsBirth = false;
        }

        if (mOutbox.mBoredSteps > 0) {
            Games.Achievements.increment(mGoogleApiClient, getString(R.string.achievement_really_bored),
                    mOutbox.mBoredSteps);
            Games.Achievements.increment(mGoogleApiClient, getString(R.string.achievement_bored),
                    mOutbox.mBoredSteps);
        }
        if (mOutbox.mEasyModeScore >= 0) {
            Games.Leaderboards.submitScore(mGoogleApiClient, getString(R.string.leaderboard_easy),
                    mOutbox.mEasyModeScore);
            mOutbox.mEasyModeScore = -1;
        }
        if (mOutbox.mHardModeScore >= 0) {
            Games.Leaderboards.submitScore(mGoogleApiClient, getString(R.string.leaderboard_hard),
                    mOutbox.mHardModeScore);
            mOutbox.mHardModeScore = -1;
        }

        mOutbox.saveLocal(this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Gdx.app.log(TAG, "onConnectionSuspended(): attempting to connect");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Gdx.app.log(TAG, "onConnectionFailed(): attempting to resolve");
        if (mResolvingConnectionFailure) {
            Gdx.app.log(TAG, "onConnectionFailed(): already resolving");
            return;
        }

        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;
            if (!BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, getString(R.string.signin_other_error))) {
                mResolvingConnectionFailure = false;
            }
        }

        // Sign-in failed, so show sign-in button on main menu
        Gdx.app.log(TAG, getString(R.string.signed_out_greeting));
    }

    @Override
    public boolean getSignedIn() {
        return false;
    }

    @Override
    public void login() {

    }

    @Override
    public void enteredScore(int requestedScore, boolean hardMode, GameScreen screen) {
        int finalScore = hardMode ? requestedScore / 2 : requestedScore;
        screen.getDisplay().setText("");

        checkForAchievements(requestedScore, finalScore, screen);

        updateLeaderBoards(finalScore, hardMode);

        pushAccomplishments();

    }

    /**
     * Update leaderboards with the user's score.
     *
     * @param finalScore The score the user got.
     */
    void updateLeaderBoards(int finalScore, boolean hardMode) {
        if (hardMode && mOutbox.mHardModeScore < finalScore) {
            mOutbox.mHardModeScore = finalScore;
        } else if (!hardMode && mOutbox.mEasyModeScore < finalScore) {
            mOutbox.mEasyModeScore = finalScore;
        }
    }

    boolean isPrime(int n) {
        int i;
        if (n == 0 || n == 1) return false;
        for (i = 2; i <= n / 2; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check for achievements and unlock the appropriate ones.
     *
     * @param requestedScore the score the user requested.
     * @param finalScore the score the user got.
     */
    void checkForAchievements(int requestedScore, int finalScore, GameScreen screen) {
        // Check if each condition is met; if so, unlock the corresponding
        // achievement.
        if (isPrime(finalScore)) {
            mOutbox.mPrimeAchievement = true;
            //achievementToast(getString(R.string.achievement_prime_toast_text));
            DialogCreator.AchievementDialog(screen, finalScore + "\n" + getString(R.string.achievement_prime_toast_text));
        }
        else if (requestedScore == 9999) {
            mOutbox.mArrogantAchievement = true;
            //achievementToast(getString(R.string.achievement_arrogant_toast_text));
            DialogCreator.AchievementDialog(screen, finalScore + "\n" + getString(R.string.achievement_arrogant_toast_text));
        }
        else if (requestedScore == 0) {
            mOutbox.mHumbleAchievement = true;
            //achievementToast(getString(R.string.achievement_humble_toast_text));
            DialogCreator.AchievementDialog(screen, finalScore + "\n" + getString(R.string.achievement_humble_toast_text));
        }
        else if (finalScore == 1337) {
            mOutbox.mLeetAchievement = true;
            //achievementToast(getString(R.string.achievement_leet_toast_text));
            DialogCreator.AchievementDialog(screen, finalScore + "\n" + getString(R.string.achievement_leet_toast_text));
        }
        else if (finalScore == 666) {
            mOutbox.mDevilAchievement = true;
            //achievementToast(getString(R.string.achievement_leet_toast_text));
            DialogCreator.AchievementDialog(screen, finalScore + "\n" + getString(R.string.achievement_devil_toast_text));
        }
        else if (finalScore == 69) {
            mOutbox.mMagicNumber = true;
            //achievementToast(getString(R.string.achievement_leet_toast_text));
            DialogCreator.AchievementDialog(screen, finalScore + "\n" + getString(R.string.achievement_magical_text));
        }
        else if (finalScore == 7380) {
            mOutbox.mRicksBirth = true;
            //achievementToast(getString(R.string.achievement_leet_toast_text));
            DialogCreator.AchievementDialog(screen, finalScore + "\n" + getString(R.string.achievement_ricks_date_of_birth_text));
        }
        else if (finalScore == 1806) {
            mOutbox.mDeveloperBirth = true;
            //achievementToast(getString(R.string.achievement_leet_toast_text));
            DialogCreator.AchievementDialog(screen, finalScore + "\n" + getString(R.string.achievement_developers_date_of_birth_text));
        }
        else if (finalScore == 6685) {
            mOutbox.mGirlFriendsBirth = true;
            //achievementToast(getString(R.string.achievement_leet_toast_text));
            DialogCreator.AchievementDialog(screen, finalScore + "\n" + getString(R.string.achievement_girlfriends_date_of_birth_text));
        }
        else if (finalScore > 1000)
        {
            DialogCreator.AchievementDialog(screen, finalScore + "\n" + "Does not unlock anything but at least it's more then thousand.");

        }
        else
        {
            DialogCreator.AchievementDialog(screen, finalScore + "\n" + "Does not unlock anything.");
        }
        mOutbox.mBoredSteps++;
    }


    @Override
    public void getLeaderboard() {
        if (isSignedIn()) {
        startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(mGoogleApiClient),
                RC_UNUSED);
    } else {
        BaseGameUtils.makeSimpleDialog(this, getString(R.string.leaderboards_not_available)).show();
    }

    }

    @Override
    public void getAchievements() {
        if (isSignedIn()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient),
                    RC_UNUSED);
        } else {
            BaseGameUtils.makeSimpleDialog(this, getString(R.string.achievements_not_available)).show();
        }
    }




    class AccomplishmentsOutbox {
        boolean mPrimeAchievement = false;
        boolean mHumbleAchievement = false;
        boolean mLeetAchievement = false;
        boolean mArrogantAchievement = false;
        boolean mDevilAchievement = false;
        boolean mMagicNumber = false;
        boolean mRicksBirth = false;
        boolean mDeveloperBirth = false;
        boolean mGirlFriendsBirth = false;

        int mBoredSteps = 0;
        int mEasyModeScore = -1;
        int mHardModeScore = -1;

        boolean isEmpty() {
            return !mPrimeAchievement && !mHumbleAchievement && !mLeetAchievement &&
                    !mArrogantAchievement && mBoredSteps == 0 && mEasyModeScore < 0 &&
                    mHardModeScore < 0;
        }

        public void saveLocal(Context ctx) {
            /* TODO: This is left as an exercise. To make it more difficult to cheat,
             * this data should be stored in an encrypted file! And remember not to
             * expose your encryption key (obfuscate it by building it from bits and
             * pieces and/or XORing with another string, for instance). */
         }

        public void loadLocal(Context ctx) {
            /* TODO: This is left as an exercise. Write code here that loads data
             * from the file you wrote in saveLocal(). */
        }


    }

    @Override
    public void showInterstitialAd() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                else
                {
                    Gdx.app.log("MainActivity", "Add not loaded!");
                }

            }
        });
    }


}
