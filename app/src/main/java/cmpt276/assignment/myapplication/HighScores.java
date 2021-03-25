/**
 * High Scores Screen UI displays all the hichscores with their corresponding dates
 */
package cmpt276.assignment.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.text.DateFormat;
import java.util.Calendar;

import cmpt276.assignment.myapplication.model.CardCombination;
import cmpt276.assignment.myapplication.model.HighScoreTest;
import cmpt276.assignment.myapplication.model.ListOfFlickr;
import cmpt276.assignment.myapplication.model.PictureContent;

import static cmpt276.assignment.myapplication.UsernameScreen.SHARED_PREFS;
import static cmpt276.assignment.myapplication.UsernameScreen.TEXT;

public class HighScores extends AppCompatActivity {
    //Creates an intent to switch to a different activity
    public static Intent fromMenuToHighScores(Context c) {
        Intent intent = new Intent(c, HighScores.class);
        return intent;
    }
    TextView highScoresText;

    private String lastUsername;

    private String user_3x5_1, user_3x5_2, user3x5_3, user3x5_4, user3x5_5;
    private String user_3xall_1, user_3xall_2, user3xall_3, user3xall_4, user3xall_5;
    private String user_4x5_1, user_4x5_2, user4x5_3, user4x5_4, user4x5_5;
    private String user_4x10_1, user_4x10_2, user4x10_3, user4x10_4, user4x10_5;
    private String user_4x13_1, user_4x13_2, user4x13_3, user4x13_4, user4x13_5;
    private String user_6x5_1, user_6x5_2, user6x5_3, user6x5_4, user6x5_5;
    private String user_6x10_1, user_6x10_2, user6x10_3, user6x10_4, user6x10_5;
    private String user_6x15_1, user_6x15_2, user6x15_3, user6x15_4, user6x15_5;
    private String user_6xall_1, user_6xall_2, user6xall_3, user6xall_4, user6xall_5;

    private int high_3x5_1, high_3x5_2, high3x5_3, high3x5_4, high3x5_5;
    private String date_3x5_1, date_3x5_2, date3x5_3, date3x5_4, date3x5_5;

    int lastScore;
    private String lastDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        setTitle("Highscores");
        ListOfFlickr refresh = ListOfFlickr.getInstance();
        refresh.refresh();
        PictureContent.loadSavedImages(new File(SavedImagesActivity.loadData(this)));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setupSettings();

        registerClickCallback();
        updateScores();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }

    private void updateScores() {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        //Get Username and load old usernames
        lastUsername = preferences.getString(TEXT, "");

        //finding out the gamemode
        CardCombination game = CardCombination.getInstance();
        int images = OptionsScreen.getNumImagesPerCard(this);
        int deck = OptionsScreen.getDrawPileSize(this);
        TextView text = findViewById(R.id.highscoresize);
        text.setText("Showing high scores for: \nImages per card: " + images + "\nDeck Size: " + deck);

        if (images == 3) {
            if (deck == 5) {
                changeScore("user_3x5_1","user_3x5_2","user_3x5_3","user_3x5_4","user_3x5_5",
                        "high_3x5_1","high_3x5_2","high_3x5_3","high_3x5_4","high_3x5_5",
                        "date_3x5_1","date_3x5_2","date_3x5_3","date_3x5_4","date_3x5_5");
            }
            else {
                changeScore("user_3xall_1","user_3xall_2","user_3xall_3","user_3xall_4","user_3xall_5",
                        "high_3xall_1","high_3xall_2","high_3xall_3","high_3xall_4","high_3xall_5",
                        "date_3xall_1","date_3xall_2","date_3xall_3","date_3xall_4","date_3xall_5");
                }
            }
            if (images == 4) {
                if (deck == 5) {
                    changeScore("user_4x5_1","user_4x5_2","user_4x5_3","user_4x5_4","user_4x5_5",
                            "high_4x5_1","high_4x5_2","high_4x5_3","high_4x5_4","high_4x5_5",
                            "date_4x5_1","date_4x5_2","date_4x5_3","date_4x5_4","date_4x5_5");
                }
                if (deck == 10) {
                    changeScore("user_4x10_1","user_4x10_2","user_4x10_3","user_4x10_4","user_4x10_5",
                            "high_4x10_1","high_4x10_2","high_4x10_3","high_4x10_4","high_4x10_5",
                            "date_4x10_1","date_4x10_2","date_4x10_3","date_4x10_4","date_4x10_5");
                }
                if (deck == 12) {
                    changeScore("user_4xall_1","user_4xall_2","user_4xall_3","user_4xall_4","user_4xall_5",
                            "high_4xall_1","high_4xall_2","high_4xall_3","high_4xall_4","high_4xall_5",
                            "date_4xall_1","date_4xall_2","date_4xall_3","date_4xall_4","date_4xall_5");
                }
            }

            if (images == 6) {
                if (deck == 5) {
                    changeScore("user_6x5_1","user_6x5_2","user_6x5_3","user_6x5_4","user_6x5_5",
                            "high_6x5_1","high_6x5_2","high_6x5_3","high_6x5_4","high_6x5_5",
                            "date_6x5_1","date_6x5_2","date_6x5_3","date_6x5_4","date_6x5_5");

                }
                if (deck == 10) {
                    changeScore("user_6x10_1","user_6x10_2","user_6x10_3","user_6x10_4","user_6x10_5",
                            "high_6x10_1","high_6x10_2","high_6x10_3","high_6x10_4","high_6x10_5",
                            "date_6x10_1","date_6x10_2","date_6x10_3","date_6x10_4","date_6x10_5");
                }

                if (deck == 15) {
                    changeScore("user_6x15_1","user_6x15_2","user_6x15_3","user_6x15_4","user_6x15_5",
                            "high_6x15_1","high_6x15_2","high_6x15_3","high_6x15_4","high_6x15_5",
                            "date_6x15_1","date_6x15_2","date_6x15_3","date_6x15_4","date_6x15_5");
                }

                if (deck == 20) {
                    changeScore("user_6x20_1","user_6x20_2","user_6x20_3","user_6x20_4","user_6x20_5",
                            "high_6x20_1","high_6x20_2","high_6x20_3","high_6x20_4","high_6x20_5",
                            "date_6x20_1","date_6x20_2","date_6x20_3","date_6x20_4","date_6x20_5");

                }
                if (deck == 30) {
                    changeScore("user_6xall_1","user_6xall_2","user_6xall_3","user_6xall_4","user_6xall_5",
                            "high_6xall_1","high_6xall_2","high_6xall_3","high_6xall_4","high_6xall_5",
                            "date_6xall_1","date_6xall_2","date_6xall_3","date_6xall_4","date_6xall_5");
                }
            }
        }

    private void changeScore(String user1, String user2, String user3, String user4, String user5,
                             String high1, String high2, String high3, String high4, String high5,
                             String date1, String date2, String date3, String date4, String date5){
        highScoresText = (TextView) findViewById(R.id.highscorestext);
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        String oldUser1 = preferences.getString(user1, "The Stig");
        String oldUser2 = preferences.getString(user2, "The Stig");
        String oldUser3 = preferences.getString(user3, "The Stig");
        String oldUser4 = preferences.getString(user4, "The Stig");
        String oldUser5 = preferences.getString(user5, "The Stig");
        //load old scores

        lastScore = preferences.getInt("lastScore", 0);
        int def1 = 15;
        int def2 = 19;
        int def3 = 26;
        int def4 = 33;
        int def5 = 40;
        if(user1.equals("user_4x10_1") || user1.equals("user_4xall_1")){
            def1+=14;
            def2+=14;
            def3+=14;
            def4+=14;
            def5+=14;
        }
        if(user1.equals("user_6x10_1") || user1.equals("user_6xall_1") || user1.equals("user_6x15_1")|| user1.equals("user_6x20_1")){
            def1+=30;
            def2+=30;
            def3+=25;
            def4+=25;
            def5+=25;
        }

        int oldHigh1 = preferences.getInt(high1, def1);
        int oldHigh2 = preferences.getInt(high2, def2);
        int oldHigh3 = preferences.getInt(high3, def3);
        int oldHigh4 = preferences.getInt(high4, def4);
        int oldHigh5 = preferences.getInt(high5, def5);

        //getting todays date
        Calendar cal = Calendar.getInstance();
        String lastDate = DateFormat.getDateInstance().format(cal.getTime());

        //load old dates
        String oldDate1 = preferences.getString(date1, "Jul 8, 2020");
        String oldDate2 = preferences.getString(date2, "Jul 10, 2020");
        String oldDate3 = preferences.getString(date3, "Jul 12, 2020");
        String oldDate4 = preferences.getString(date4, "July 4, 2020");
        String oldDate5 = preferences.getString(date5, "July 7, 2020");

        //will only update if a game has actually been played
        HighScoreTest test = HighScoreTest.getinstance();
        //Replace if there is a new high score
        if(test.getState() == true && lastScore > 0){
            //System.out.println("THE STATE IS     " + test.getState());
            test.setState(false);
            if(lastScore <= oldHigh5) {
                oldHigh5 = lastScore;
                oldUser5 = lastUsername;
                oldDate5 = lastDate;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(high5, oldHigh5);
                editor.putString(user5, oldUser5);
                editor.putString(date5, oldDate5);
                editor.apply();
            }
            if(lastScore <= oldHigh4) {
                int temp = oldHigh4;
                oldHigh4 = lastScore;
                oldHigh5 = temp;
                String tempString = oldUser4;
                oldUser4 = lastUsername;
                oldUser5 = tempString;
                String tempDate = oldDate4;
                oldDate4 = lastDate;
                oldDate5 = tempDate;

                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(high5, oldHigh5);
                editor.putInt(high4, oldHigh4);
                editor.putString(user5, oldUser5);
                editor.putString(user4, oldUser4);
                editor.putString(date5, oldDate5);
                editor.putString(date4, oldDate4);
                editor.apply();
            }
            if(lastScore <= oldHigh3) {
                int temp = oldHigh3;
                oldHigh3 = lastScore;
                oldHigh4 = temp;
                String tempString = oldUser3;
                oldUser3 = lastUsername;
                oldUser4 = tempString;
                String tempDate = oldDate3;
                oldDate3 = lastDate;
                oldDate4 = tempDate;

                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(high4, oldHigh4);
                editor.putInt(high3, oldHigh3);
                editor.putString(user4, oldUser4);
                editor.putString(user3, oldUser3);
                editor.putString(date4, oldDate4);
                editor.putString(date3, oldDate3);
                editor.apply();
            }
            if(lastScore <= oldHigh2) {
                int temp = oldHigh2;
                oldHigh2 = lastScore;
                oldHigh3 = temp;
                String tempString = oldUser2;
                oldUser2 = lastUsername;
                oldUser3 = tempString;
                String tempDate = oldDate2;
                oldDate2 = lastDate;
                oldDate3 = tempDate;

                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(high3, oldHigh3);
                editor.putInt(high2, oldHigh2);
                editor.putString(user3, oldUser3);
                editor.putString(user2, oldUser2);
                editor.putString(date3, oldDate3);
                editor.putString(date2, oldDate2);
                editor.apply();
            }
            if(lastScore <= oldHigh1) {
                int temp = oldHigh1;
                oldHigh1 = lastScore;
                oldHigh2 = temp;
                String tempString = oldUser1;
                oldUser1 = lastUsername;
                oldUser2 = tempString;
                String tempDate = oldDate1;
                oldDate1 = lastDate;
                oldDate2 = tempDate;

                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(high2, oldHigh2);
                editor.putInt(high1, oldHigh1);
                editor.putString(user2, oldUser2);
                editor.putString(user1, oldUser1);
                editor.putString(date2, oldDate2);
                editor.putString(date1, oldDate1);
                editor.apply();
            }
        }
        highScoresText.setText(getString(R.string.lastscore) + lastScore + getString(R.string.seconds) + "\n\n" +
                    "1. " + oldHigh1 + getString(R.string.secondsby) + oldUser1 + getString(R.string.on) + oldDate1 + "\n" +
                    "2. " + oldHigh2 + getString(R.string.secondsby) + oldUser2 + getString(R.string.on) + oldDate2 + "\n" +
                    "3. " + oldHigh3 + getString(R.string.secondsby) + oldUser3 + getString(R.string.on) + oldDate3 + "\n" +
                    "4. " + oldHigh4 + getString(R.string.secondsby) + oldUser4 + getString(R.string.on) + oldDate4 + "\n" +
                    "5. " + oldHigh5 + getString(R.string.secondsby) + oldUser5 + getString(R.string.on) + oldDate5 + "\n");
    }

    private void setupSettings() {
        Button btn = findViewById(R.id.Settingshighscore);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HighScores.this , OptionsScreen.class);
                startActivity(intent);
            }
        });

    }

    //Creates button functionality
    private void registerClickCallback() {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        //Reset button
        Button reset = (Button) findViewById(R.id.resetbtn);
        //Creates Reset button Functionality
        reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Resets sharedpreferences
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear().apply();
                //Goes to reset the username otherwise it becomes blank
                Intent i = UsernameScreen.fromWelcomeToUsername(HighScores.this);
                startActivity(i);
                HighScores.this.recreate();
            }
        });
    }

    @Override
    public void onRestart() {
        super.onRestart();
        this.recreate();

    }



}