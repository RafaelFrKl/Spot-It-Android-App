/**
 * Welcome Splash screen
 */
package cmpt276.assignment.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import static cmpt276.assignment.myapplication.UsernameScreen.fromWelcomeToUsername;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        setTitle("Welcome to Spot It!");
        setupAnimation();
    }


    private void setupAnimation(){
        final boolean[] SkipIsClicked = {false};
        Button skip = (Button) findViewById(R.id.skipwelcome);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkipIsClicked[0] = true;
                Intent Move = fromWelcomeToUsername(MainActivity.this);
                startActivity(Move);
                finish();
            }
        });
        TextView animation = (TextView) findViewById(R.id.WelcomeMessage);
        YoYo.with(Techniques.Tada).duration(700).repeat(4).playOn(animation);

        ImageView animation2 = (ImageView) findViewById(R.id.spoticon);
        YoYo.with(Techniques.Tada).duration(700).repeat(4).playOn(animation2);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                if(!SkipIsClicked[0]){
                    Intent Move = fromWelcomeToUsername(MainActivity.this);
                    startActivity(Move);
                    finish();
                }
            }
        };
        handler.postDelayed(runnable,5000);
    }
}
