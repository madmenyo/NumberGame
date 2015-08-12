package com.buckriderstudio.numbergame;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by Menyo on 1/8/2015.
 */
public interface ActionResolver {
    public boolean getSignedIn();
    public void login();
    public void enteredScore(int requestedScore, boolean hardMode, GameScreen screen);
    public void getLeaderboard();
    public void getAchievements();
    public void showInterstitialAd();
}
