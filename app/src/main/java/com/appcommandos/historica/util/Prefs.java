package com.appcommandos.historica.util;

import android.app.Activity;
import android.content.SharedPreferences;

public class Prefs {
    private SharedPreferences preferances;

    public Prefs(Activity activity) {
        this.preferances = activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void saveHighestSocre(int score){
        int currentScore = score;

        int lastScore = preferances.getInt("high_score",0);
        // we have new high score and save it
        if (currentScore > lastScore){
            preferances.edit().putInt("high_score",currentScore).apply();
        }

    }

    public int getHighScore(){
        return  preferances.getInt("high_score",0);
    }

    public void setState(int index){
        preferances.edit().putInt("index_state",index).apply();
    }

    public  int getState(){
        return  preferances.getInt("index_state",0);
    }


}
