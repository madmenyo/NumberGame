package com.buckriderstudio.numbergame.desktop;

import com.buckriderstudio.numbergame.ActionResolver;
import com.buckriderstudio.numbergame.DialogCreator;
import com.buckriderstudio.numbergame.GameScreen;

/**
 * Created by Menyo on 1/8/2015.
 */
public class ActionResolverDesktop implements ActionResolver {
    @Override
    public boolean getSignedIn() {
        System.out.println("Desktop application cannot sign in!");
        return false;
    }

    @Override
    public void login() {
        System.out.println("Desktop application cannot sign in!");
    }

    @Override
    public void enteredScore(int requestedScore, boolean hardMode, GameScreen screen) {
        DialogCreator.AchievementDialog(screen, "No achievements can be gained on desktop.");
    }

    @Override
    public void getLeaderboard() {

    }

    @Override
    public void getAchievements() {

    }

    @Override
    public void showInterstitialAd() {

    }
}
