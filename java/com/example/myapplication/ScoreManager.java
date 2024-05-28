package com.example.myapplication;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ScoreManager {

    private static final String TAG = "ScoreManager!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"; // Add this line
    private static final String PREF_NAME = "TopScores";
    private static final String KEY_TOP_SCORES = "topScores";

    private Context context;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public ScoreManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    public void addOrUpdateTopScore(String playerName, String level, int livesLeft, String word,String time) {
        List<TopScore> topScores = getTopScores();

        // find the existing score for the level
        for (TopScore topScore : topScores) {
            if (topScore.getLevel().equals(level)) {
                // update the existing score if the new score is higher
                if (livesLeft > topScore.getLivesLeft()) {
                   if( Integer.parseInt(time) >Integer.parseInt(topScore.getTime())){
                       topScore.setPlayerName(playerName);
                       topScore.setLivesLeft(livesLeft);
                       topScore.setWord(word);
                       saveTopScores(topScores);
                       Log.d(TAG, "updated existing score: " + topScore.toString());
                   }

                } else {
                    Log.d(TAG, "new score not higher no update");
                }
                return;
            }
        }

        // No existing score for the level, add a new score
        TopScore newScore = new TopScore(playerName, level, livesLeft, word,time);
        topScores.add(newScore);
        saveTopScores(topScores);
        Log.d(TAG, "Added new score: " + newScore.toString());
    }

    public List<TopScore> getTopScores() {
        String json = sharedPreferences.getString(KEY_TOP_SCORES, null);

        if (json != null) {
            Type type = new TypeToken<List<TopScore>>() {}.getType();
            List<TopScore> topScores = gson.fromJson(json, type);
            Log.d(TAG, "retrieved top scores: " + topScores.toString());
            return topScores;
        } else {
            Log.d(TAG, "no saved top scores found.");
            return new ArrayList<>();
        }
    }

    private void saveTopScores(List<TopScore> topScores) {
        String json = gson.toJson(topScores);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOP_SCORES, json);
        editor.apply();
        Log.d(TAG, "Saved top scores: " + json);
    }

}
