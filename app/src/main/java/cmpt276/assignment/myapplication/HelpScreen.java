/**
 * Help Screen contains a little description of the game and the citation for images
 */
package cmpt276.assignment.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

public class HelpScreen extends AppCompatActivity {
    //Creates an intent to switch to a different activity
    public static Intent fromMenuToHelp(Context c) {
        Intent intent = new Intent(c, HelpScreen.class);
        return intent;
    }

    //On Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_screen);
        setTitle("Help");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Makes Hyperlinks clickable
        TextView tv = (TextView) findViewById(R.id.textView_ResourcesLink);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setLinkTextColor(Color.BLUE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }
}

