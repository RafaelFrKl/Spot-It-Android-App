/**
 * The user enters their name on this screen
 */
package cmpt276.assignment.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UsernameScreen extends AppCompatActivity {
    //Creates an intent to switch to a different activity
    public static Intent fromWelcomeToUsername(Context c){
        return new Intent(c, UsernameScreen.class);
    }


    private EditText editText;

    //Username Shared Preferences
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username_screen);
        setTitle("Welcome to Spot It!");

        editText = (EditText) findViewById(R.id.editText);

        registerClickCallback();
    }

    //Creates button functionality
    private void registerClickCallback() {
        //Get Save button
        Button save = (Button) findViewById(R.id.savebtn);
        //Creates Save button Functionality
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                //Goes to selected activity
                Intent i = MainMenu.fromUsernameToMenu(UsernameScreen.this);
                startActivity(i);
                finish();
            }
        });
    }
    //Saves username to Shared preferences
    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT, editText.getText().toString());
        editor.apply();
    }
}