/**
 * Game Screen is where the gameplay takes place and it uses the CardCombination class inside of it.
 */
package cmpt276.assignment.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;
import java.util.Random;

import cmpt276.assignment.myapplication.model.CardCombination;
import cmpt276.assignment.myapplication.model.Clock;
import cmpt276.assignment.myapplication.model.ExportCard;
import cmpt276.assignment.myapplication.model.ListOfFlickr;
import cmpt276.assignment.myapplication.model.PictureContent;
import cmpt276.assignment.myapplication.model.HighScoreTest;

import static cmpt276.assignment.myapplication.OptionsScreen.getDrawPileSize;
import static cmpt276.assignment.myapplication.OptionsScreen.getImageSet;
import static cmpt276.assignment.myapplication.OptionsScreen.getNumImagesPerCard;
import static cmpt276.assignment.myapplication.OptionsScreen.getMode;
import static cmpt276.assignment.myapplication.UsernameScreen.SHARED_PREFS;
//the idea for sound effects is inspired from this video https://youtu.be/9oj4f8721LM
public class GameScreen extends AppCompatActivity {
    //Export Images
    Bitmap imgBitmap;
    //Millisecond timer
    float time = 0;
    int score = 0;
    Clock clock = new Clock();
    private int size = 0;
    boolean isFlickrMode = true;
    int difficulty;
    float[] Scales;
    float[] TextScales;
    //1 = export, 0 = no
    int export;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        size = getDrawPileSize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        setTitle("Timed Solo Deck");
        ListOfFlickr refresh = ListOfFlickr.getInstance();
        refresh.refresh();
        PictureContent.loadSavedImages(new File(SavedImagesActivity.loadData(this)));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //finding out difficulty
        difficulty = OptionsScreen.getDifficulty(this);
        export = OptionsScreen.getExport(this);
        int imageForCards = refresh.getLength();
        if(OptionsScreen.getMode(this) == 2){
            if(OptionsScreen.getNumImagesPerCard(this) == 3 && imageForCards < 7){
                SharedPreferences mode = getSharedPreferences("mode", MODE_PRIVATE);
                SharedPreferences.Editor edit= mode.edit();
                edit.putInt("mode", 0);
                edit.apply();
            }
            if(OptionsScreen.getNumImagesPerCard(this) == 4 && imageForCards < 13){
                SharedPreferences mode = getSharedPreferences("mode", MODE_PRIVATE);
                SharedPreferences.Editor edit= mode.edit();
                edit.putInt("mode", 0);
                edit.apply();

            }
            if(OptionsScreen.getNumImagesPerCard(this) == 6 && imageForCards < 31){
                SharedPreferences mode = getSharedPreferences("mode", MODE_PRIVATE);
                SharedPreferences.Editor edit= mode.edit();
                edit.putInt("mode", 0);
                edit.apply();
            }
        }
        initializeScales();
        initializeTextScales();
        if(getMode(this)<2) {
            CardCombination cards = CardCombination.getInstance();
            cards.refresh();
            setUpImageSet();
            setDiscardPile();
            setEmptyCardPile();
            //setdrawpile();
        }
        else {
            CardCombination cards = CardCombination.getInstance();
            cards.refresh();
            setUpFlickrImageSet();
            setFlickrDiscardPile();
            setFlickrDrawPile();
            setEmptyCardPile();
        }
    }
    //setdrawpile();

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }

    private void setUpImageSet(){
        CardCombination CARDS = CardCombination.getInstance();
        int selection = getImageSet(this);
        List<int[]> The_Cards= CARDS.getCards();
        if(getNumImagesPerCard(this)==3) {
            int[][] combinations3 = {{0, 1, 4}, {2, 3, 4}, {0, 2, 5}, {1, 3, 5}, {0, 3, 6}, {1, 2, 6}, {4, 5, 6}};
            for (int j = 0; j < 7; j++) {
                for (int y = 0; y < 3; y++) {
                    combinations3[j][y] += R.drawable.a0;
                    combinations3[j][y] += selection;
                }
            }
            for(int i=0; i<7;i++){
                The_Cards.add(combinations3[i]);
            }
        }
        else if(getNumImagesPerCard(this)==4){
            int[][] combinations4 = { {0,1,2,9},{9,3,4,5},{8,9,6,7},{0,10,3,6},{1,10,4,7},{8,2,10,5},{0,8,11,4},
                    {1,11,5,6},{11,2,3,7},{0,12,5,7},{8,1,3,12},{12,2,4,6},{9,10,11,12} };
            for (int j = 0; j < 13; j++) {
                for (int y = 0; y < 4; y++) {
                    combinations4[j][y] += R.drawable.a0;
                    combinations4[j][y] += selection;
                }
            }
            for(int i=0; i<13;i++){
                The_Cards.add(combinations4[i]);
            }
        }
        else if(getNumImagesPerCard(this)==6){
            int[][] combinations6= {{0,1,2,3,4,25},{5,6,7,8,9,25},{10,11,12,13,14,25},{15,16,17,18,19,25},{20,21,22,23,24,25},
                    {0,5,10,15,20,26},{1,6,11,16,21,26},{2,7,12,17,22,26},{3,8,13,18,23,26},{4,9,14,19,24,26},{0,6,12,18,24,27},{1,7,13,19,20,27},
                    {2,8,14,15,21,27},{3,9,10,16,22,27},{4,5,11,17,23,27},{0,7,14,16,23,28},{1,8,10,17,24,28},{2,9,11,18,20,28},{3,5,12,19,21,28},
                    {4,6,13,15,22,28},{0,8,11,19,22,29},{1,9,12,15,23,29},{2,5,13,16,24,29},{3,6,14,17,20,29},{4,7,10,18,21,29},{0,9,13,17,21,30},
                    {1,5,14,18,22,30},{2,6,10,19,23,30},{3,7,11,15,24,30},{4,8,12,16,20,30},{25,26,27,28,29,30}};
            for (int j = 0; j < 31; j++) {
                for (int y = 0; y < 6; y++) {
                    combinations6[j][y] += R.drawable.a0;
                    combinations6[j][y] += selection;
                }
            }
            for(int i=0; i<31;i++){
                The_Cards.add(combinations6[i]);
            }
        }
    }
    private void setUpFlickrImageSet(){

        CardCombination CARDS = CardCombination.getInstance();
        int selection = getImageSet(this);
        List<int[]> The_Cards= CARDS.getCards();
        if(getNumImagesPerCard(this)==3) {
            int[][] combinations3 = {{0, 1, 4}, {2, 3, 4}, {0, 2, 5}, {1, 3, 5}, {0, 3, 6}, {1, 2, 6}, {4, 5, 6}};
            for (int j = 0; j < 7; j++) {
                for (int y = 0; y < 3; y++) {
                    combinations3[j][y] += 0;
                    combinations3[j][y] += selection;
                }
            }
            for(int i=0; i<7;i++){
                The_Cards.add(combinations3[i]);
            }
        }
        else if(getNumImagesPerCard(this)==4){
            int[][] combinations4 = { {0,1,2,9},{9,3,4,5},{8,9,6,7},{0,10,3,6},{1,10,4,7},{8,2,10,5},{0,8,11,4},
                    {1,11,5,6},{11,2,3,7},{0,12,5,7},{8,1,3,12},{12,2,4,6},{9,10,11,12} };
            for (int j = 0; j < 13; j++) {
                for (int y = 0; y < 4; y++) {
                    combinations4[j][y] += 0;
                    combinations4[j][y] += selection;
                }
            }
            for(int i=0; i<13;i++){
                The_Cards.add(combinations4[i]);
            }
        }
        else if(getNumImagesPerCard(this)==6){
            int[][] combinations6= {{0,1,2,3,4,25},{5,6,7,8,9,25},{10,11,12,13,14,25},{15,16,17,18,19,25},{20,21,22,23,24,25},
                    {0,5,10,15,20,26},{1,6,11,16,21,26},{2,7,12,17,22,26},{3,8,13,18,23,26},{4,9,14,19,24,26},{0,6,12,18,24,27},{1,7,13,19,20,27},
                    {2,8,14,15,21,27},{3,9,10,16,22,27},{4,5,11,17,23,27},{0,7,14,16,23,28},{1,8,10,17,24,28},{2,9,11,18,20,28},{3,5,12,19,21,28},
                    {4,6,13,15,22,28},{0,8,11,19,22,29},{1,9,12,15,23,29},{2,5,13,16,24,29},{3,6,14,17,20,29},{4,7,10,18,21,29},{0,9,13,17,21,30},
                    {1,5,14,18,22,30},{2,6,10,19,23,30},{3,7,11,15,24,30},{4,8,12,16,20,30},{25,26,27,28,29,30}};
            for (int j = 0; j < 31; j++) {
                for (int y = 0; y < 6; y++) {
                    combinations6[j][y] += 0;
                    combinations6[j][y] += selection;
                }
            }
            for(int i=0; i<31;i++){
                The_Cards.add(combinations6[i]);
            }
        }
    }

    private void startTimer() {
        clock.startTimer();
    }

    private void setEmptyCardPile() {
        TextView textDraw1 = (TextView) findViewById(R.id.textdrawpile1);
        TextView textDraw2 = (TextView) findViewById(R.id.textdrawpile2);
        TextView textDraw3 = (TextView) findViewById(R.id.textdrawpile3);
        TextView textDraw4 = (TextView) findViewById(R.id.textdrawpile4);
        TextView textDraw5 = (TextView) findViewById(R.id.textdrawpile5);
        TextView textDraw6 = (TextView) findViewById(R.id.textdrawpile6);
        textDraw1.setText("");
        textDraw2.setText("");
        textDraw3.setText("");
        textDraw4.setText("");
        textDraw5.setText("");
        textDraw6.setText("");
        ImageView draw1 = (ImageView) findViewById(R.id.drawpile1);
        ImageView draw2 = (ImageView) findViewById(R.id.drawpile2);
        ImageView draw3 = (ImageView) findViewById(R.id.drawpile3);
        ImageView draw4 = (ImageView) findViewById(R.id.drawpile4);
        ImageView draw5 = (ImageView) findViewById(R.id.drawpile5);
        ImageView draw6 = (ImageView) findViewById(R.id.drawpile6);
        draw1.setImageDrawable(getDrawable(R.drawable.white));
        draw2.setImageDrawable(getDrawable(R.drawable.white));
        draw3.setImageDrawable(getDrawable(R.drawable.white));
        draw4.setImageDrawable(getDrawable(R.drawable.white));
        draw5.setImageDrawable(getDrawable(R.drawable.white));
        draw6.setImageDrawable(getDrawable(R.drawable.white));

        CardView card = findViewById(R.id.drawpile);
        int GameMode = getMode(this);
        MediaPlayer beginning = MediaPlayer.create(this, R.raw.beginning);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GameMode<2) {
                    setDrawPile();
                }
                if(GameMode==2){
                    setFlickrDrawPile();
                }
                beginning.start();
                startTimer();
                Chronometer meter = findViewById(R.id.chronometer);
                meter.setBase(SystemClock.elapsedRealtime());
                meter.start();
                card.setOnClickListener(null);
            }
        });
    }

    public static Intent fromMenutoGame(Context c) {
        return new Intent(c, GameScreen.class);
    }
    private void setFlickrDiscardPile(){
        ListOfFlickr list = ListOfFlickr.getInstance();
        CardCombination listofcards = CardCombination.getInstance();
        ImageView discard1 = (ImageView) findViewById(R.id.discardpile1);
        ImageView discard2 = (ImageView) findViewById(R.id.discardpile2);
        ImageView discard3 = (ImageView) findViewById(R.id.discardpile3);
        ImageView discard4 = (ImageView) findViewById(R.id.discardpile4);
        ImageView discard5 = (ImageView) findViewById(R.id.discardpile5);
        ImageView discard6 = (ImageView) findViewById(R.id.discardpile6);
        Random shuffle = new Random();
        int currentImagesOnDiscard = shuffle.nextInt(listofcards.size());
        discard1.setImageBitmap(list.getAtBitmapAtPos(listofcards.get(currentImagesOnDiscard)[0]));
        discard2.setImageBitmap(list.getAtBitmapAtPos(listofcards.get(currentImagesOnDiscard)[1]));
        discard3.setImageBitmap(list.getAtBitmapAtPos(listofcards.get(currentImagesOnDiscard)[2]));
        listofcards.setDiscard1(listofcards.get(currentImagesOnDiscard)[0]);
        listofcards.setDiscard2(listofcards.get(currentImagesOnDiscard)[1]);
        listofcards.setDiscard3(listofcards.get(currentImagesOnDiscard)[2]);

        if(getNumImagesPerCard(this)>3){
            discard4.setImageBitmap(list.getAtBitmapAtPos(listofcards.get(currentImagesOnDiscard)[3]));
            listofcards.setDiscard4(listofcards.get(currentImagesOnDiscard)[3]);
            if(getNumImagesPerCard(this)==6){
                discard5.setImageBitmap(list.getAtBitmapAtPos(listofcards.get(currentImagesOnDiscard)[4]));
                listofcards.setDiscard5(listofcards.get(currentImagesOnDiscard)[4]);
                discard6.setImageBitmap(list.getAtBitmapAtPos(listofcards.get(currentImagesOnDiscard)[5]));
                listofcards.setDiscard6(listofcards.get(currentImagesOnDiscard)[5]);
            }
        }
        Random random = new Random();
        float rotate;

        if(difficulty >= 1) {
            for (int i = 1; i <= OptionsScreen.getNumImagesPerCard(this); i++) {
                if (i == 1) {
                    rotate = (float) random.nextInt(360);
                    discard1.setRotation(rotate);
                }
                if (i == 2) {
                    rotate = (float) random.nextInt(360);
                    discard2.setRotation(rotate);
                }
                if (i == 3) {
                    rotate = (float) random.nextInt(360);
                    discard3.setRotation(rotate);
                }
                if (i == 4) {
                    rotate = (float) random.nextInt(360);
                    discard4.setRotation(rotate);
                }
                if (i == 5) {
                    rotate = (float) random.nextInt(360);
                    discard5.setRotation(rotate);
                }
                if (i == 6) {
                    rotate = (float) random.nextInt(360);
                    discard6.setRotation(rotate);
                }
            }
            if(difficulty==2){
                compressDiscardPileImages();
            }
        }
        size--;
        listofcards.remove(currentImagesOnDiscard);
    }
    private void setDiscardPile() {
        CardCombination listofcards = CardCombination.getInstance();
        ImageView discard1 = (ImageView) findViewById(R.id.discardpile1);
        ImageView discard2 = (ImageView) findViewById(R.id.discardpile2);
        ImageView discard3 = (ImageView) findViewById(R.id.discardpile3);
        ImageView discard4 = (ImageView) findViewById(R.id.discardpile4);
        ImageView discard5 = (ImageView) findViewById(R.id.discardpile5);
        ImageView discard6 = (ImageView) findViewById(R.id.discardpile6);
        Random shuffle = new Random();
        int CurrentImagesonDiscard = shuffle.nextInt(listofcards.size());
        discard1.setImageDrawable(getDrawable(listofcards.get(CurrentImagesonDiscard)[0]));
        discard2.setImageDrawable(getDrawable(listofcards.get(CurrentImagesonDiscard)[1]));
        discard3.setImageDrawable(getDrawable(listofcards.get(CurrentImagesonDiscard)[2]));
        listofcards.setDiscard1(listofcards.get(CurrentImagesonDiscard)[0]);
        listofcards.setDiscard2(listofcards.get(CurrentImagesonDiscard)[1]);
        listofcards.setDiscard3(listofcards.get(CurrentImagesonDiscard)[2]);

        if(getNumImagesPerCard(this)>3){
            discard4.setImageDrawable(getDrawable(listofcards.get(CurrentImagesonDiscard)[3]));
            listofcards.setDiscard4(listofcards.get(CurrentImagesonDiscard)[3]);
            if(getNumImagesPerCard(this)==6){
                discard5.setImageDrawable(getDrawable(listofcards.get(CurrentImagesonDiscard)[4]));
                listofcards.setDiscard5(listofcards.get(CurrentImagesonDiscard)[4]);
                discard6.setImageDrawable(getDrawable(listofcards.get(CurrentImagesonDiscard)[5]));
                listofcards.setDiscard6(listofcards.get(CurrentImagesonDiscard)[5]);
            }
        }
        Random random = new Random();
        float rotate;
        if(difficulty >= 1) {

            for (int i = 1; i <= OptionsScreen.getNumImagesPerCard(this); i++) {
                if (i == 1) {
                    rotate = (float) random.nextInt(360);
                    discard1.setRotation(rotate);
                }
                if (i == 2) {
                    rotate = (float) random.nextInt(360);
                    discard2.setRotation(rotate);
                }
                if (i == 3) {
                    rotate = (float) random.nextInt(360);
                    discard3.setRotation(rotate);
                }
                if (i == 4) {
                    rotate = (float) random.nextInt(360);
                    discard4.setRotation(rotate);
                }
                if (i == 5) {
                    rotate = (float) random.nextInt(360);
                    discard5.setRotation(rotate);
                }
                if (i == 6) {
                    rotate = (float) random.nextInt(360);
                    discard6.setRotation(rotate);
                }
            }
            if(difficulty==2){
                compressDiscardPileImages();
            }
        }

        size--;
        listofcards.remove(CurrentImagesonDiscard);
        setDiscardText();

    }

    private void setDrawPile(){
        CardCombination listofcards = CardCombination.getInstance();
        System.out.println(listofcards.size());
        MediaPlayer end= MediaPlayer.create(this, R.raw.end);
        if(listofcards.size()==0||size==0){
            listofcards.setInstance(null);
            clock.stopTimer();
            Chronometer meter = findViewById(R.id.chronometer);
            meter.stop();
            SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("lastScore", clock.getTime( ));
            editor.apply();
            finishScreen();
            end.start();
            return;
        }

        ImageView draw1 = (ImageView) findViewById(R.id.drawpile1);
        ImageView draw2 = (ImageView) findViewById(R.id.drawpile2);
        ImageView draw3 = (ImageView) findViewById(R.id.drawpile3);
        ImageView draw4 = (ImageView) findViewById(R.id.drawpile4);
        ImageView draw5 = (ImageView) findViewById(R.id.drawpile5);
        ImageView draw6 = (ImageView) findViewById(R.id.drawpile6);
        draw1.setScaleX(Scales[0]);
        draw1.setScaleY(Scales[1]);
        draw2.setScaleX(Scales[2]);
        draw2.setScaleY(Scales[3]);
        draw3.setScaleX(Scales[4]);
        draw3.setScaleY(Scales[5]);
        draw4.setScaleX(Scales[6]);
        draw4.setScaleY(Scales[7]);
        draw5.setScaleX(Scales[8]);
        draw5.setScaleY(Scales[9]);
        draw6.setScaleX(Scales[10]);
        draw6.setScaleY(Scales[11]);
        Random shuffle = new Random();
        int CurrentImagesonDraw = shuffle.nextInt(listofcards.size());
        listofcards.setCombinationOfDraw(listofcards.get(CurrentImagesonDraw));
        draw1.setImageDrawable(getDrawable(listofcards.get(CurrentImagesonDraw)[0]));
        draw2.setImageDrawable(getDrawable(listofcards.get(CurrentImagesonDraw)[1]));
        draw3.setImageDrawable(getDrawable(listofcards.get(CurrentImagesonDraw)[2]));
        listofcards.setDraw1(listofcards.get(CurrentImagesonDraw)[0]);
        listofcards.setDraw2(listofcards.get(CurrentImagesonDraw)[1]);
        listofcards.setDraw3(listofcards.get(CurrentImagesonDraw)[2]);
        if(getNumImagesPerCard(this)>3){
            draw4.setImageDrawable(getDrawable(listofcards.get(CurrentImagesonDraw)[3]));
            listofcards.setDraw4(listofcards.get(CurrentImagesonDraw)[3]);
            if(getNumImagesPerCard(this)==6){
                draw5.setImageDrawable(getDrawable(listofcards.get(CurrentImagesonDraw)[4]));
                listofcards.setDraw5(listofcards.get(CurrentImagesonDraw)[4]);
                draw6.setImageDrawable(getDrawable(listofcards.get(CurrentImagesonDraw)[5]));
                listofcards.setDraw6(listofcards.get(CurrentImagesonDraw)[5]);
            }
        }
        setDrawText();
        Random random = new Random();
        float rotate;

        if(difficulty >= 1 ) {

            for (int i = 1; i <= OptionsScreen.getNumImagesPerCard(this); i++) {
                if (i == 1) {
                    rotate = (float) random.nextInt(360);
                    draw1.setRotation(rotate);
                }
                if (i == 2) {
                    rotate = (float) random.nextInt(360);
                    draw2.setRotation(rotate);
                }
                if (i == 3) {
                    rotate = (float) random.nextInt(360);
                    draw3.setRotation(rotate);
                }
                if (i == 4) {
                    rotate = (float) random.nextInt(360);
                    draw4.setRotation(rotate);
                }
                if (i == 5) {
                    rotate = (float) random.nextInt(360);
                    draw5.setRotation(rotate);
                }
                if (i == 6) {
                    rotate = (float) random.nextInt(360);
                    draw6.setRotation(rotate);
                }
            }
            if(difficulty==2){
                compressDrawPileImages();
            }
        }
        setImagesClickable();
    }
    private void setFlickrDrawPile() {
        CardCombination listofcards = CardCombination.getInstance();
        System.out.println(listofcards.size());
        MediaPlayer end= MediaPlayer.create(this, R.raw.end);
        if (listofcards.size() == 0 || size == 0) {

            listofcards.setInstance(null);
            clock.stopTimer();
            Chronometer meter = findViewById(R.id.chronometer);
            meter.stop();
            SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("lastScore", clock.getTime());
            editor.apply();
            finishScreen();
            end.start();
            return;
        }
        ListOfFlickr list = ListOfFlickr.getInstance();
        ImageView draw1 = (ImageView) findViewById(R.id.drawpile1);
        ImageView draw2 = (ImageView) findViewById(R.id.drawpile2);
        ImageView draw3 = (ImageView) findViewById(R.id.drawpile3);
        ImageView draw4 = (ImageView) findViewById(R.id.drawpile4);
        ImageView draw5 = (ImageView) findViewById(R.id.drawpile5);
        ImageView draw6 = (ImageView) findViewById(R.id.drawpile6);
        draw1.setScaleX(Scales[0]);
        draw1.setScaleY(Scales[1]);
        draw2.setScaleX(Scales[2]);
        draw2.setScaleY(Scales[3]);
        draw3.setScaleX(Scales[4]);
        draw3.setScaleY(Scales[5]);
        draw4.setScaleX(Scales[6]);
        draw4.setScaleY(Scales[7]);
        draw5.setScaleX(Scales[8]);
        draw5.setScaleY(Scales[9]);
        draw6.setScaleX(Scales[10]);
        draw6.setScaleY(Scales[11]);
        Random shuffle = new Random();
        int CurrentImagesonDraw = shuffle.nextInt(listofcards.size());
        listofcards.setCombinationOfDraw(listofcards.get(CurrentImagesonDraw));
        draw1.setImageBitmap(list.getAtBitmapAtPos(listofcards.get(CurrentImagesonDraw)[0]));
        draw2.setImageBitmap(list.getAtBitmapAtPos(listofcards.get(CurrentImagesonDraw)[1]));
        draw3.setImageBitmap(list.getAtBitmapAtPos(listofcards.get(CurrentImagesonDraw)[2]));
        listofcards.setDraw1(listofcards.get(CurrentImagesonDraw)[0]);
        listofcards.setDraw2(listofcards.get(CurrentImagesonDraw)[1]);
        listofcards.setDraw3(listofcards.get(CurrentImagesonDraw)[2]);
        if(getNumImagesPerCard(this)>3){
            draw4.setImageBitmap(list.getAtBitmapAtPos(listofcards.get(CurrentImagesonDraw)[3]));
            listofcards.setDraw4(listofcards.get(CurrentImagesonDraw)[3]);
            if(getNumImagesPerCard(this)==6){
                draw5.setImageBitmap(list.getAtBitmapAtPos(listofcards.get(CurrentImagesonDraw)[4]));
                listofcards.setDraw5(listofcards.get(CurrentImagesonDraw)[4]);
                draw6.setImageBitmap(list.getAtBitmapAtPos(listofcards.get(CurrentImagesonDraw)[5]));
                listofcards.setDraw6(listofcards.get(CurrentImagesonDraw)[5]);
            }
        }
       // setDrawText();
        if (difficulty >= 1){
            Random random = new Random();
            float rotate;

            for(int i = 1; i <= OptionsScreen.getNumImagesPerCard(this) ; i++) {
                if (i == 1) {
                    rotate = (float) random.nextInt(360);
                    draw1.setRotation(rotate);
                }
                if (i == 2) {
                    rotate = (float) random.nextInt(360);
                    draw2.setRotation(rotate);
                }
                if (i == 3) {
                    rotate = (float) random.nextInt(360);
                    draw3.setRotation(rotate);
                }
                if (i == 4) {
                    rotate = (float) random.nextInt(360);
                    draw4.setRotation(rotate);
                }
                if (i == 5) {
                    rotate = (float) random.nextInt(360);
                    draw5.setRotation(rotate);
                }
                if (i == 6) {
                    rotate = (float) random.nextInt(360);
                    draw6.setRotation(rotate);
                }
            }
            if(difficulty==2){
                compressDrawPileImages();
            }
        }
        setImagesClickable();
    }
    private void finishScreen() {
        FragmentManager manager = getSupportFragmentManager();
       // TextView highscoretext = (TextView) findViewById(R.id.scoretext);
      //  highscoretext.setText(" Seconds");
        WinMessageFragment dialog = new WinMessageFragment();
        HighScoreTest test = HighScoreTest.getinstance();
        test.setState(true);
        dialog.show(manager,"DIALOG");
    }

    private void setImagesClickable(){
        MediaPlayer correct= MediaPlayer.create(this, R.raw.correctclick);
        MediaPlayer incorrect= MediaPlayer.create(this, R.raw.incorrectclick);
        ImageView draw1 = (ImageView) findViewById(R.id.drawpile1);
        ImageView draw2 = (ImageView) findViewById(R.id.drawpile2);
        ImageView draw3 = (ImageView) findViewById(R.id.drawpile3);
        ImageView draw4 = (ImageView) findViewById(R.id.drawpile4);
        ImageView draw5 = (ImageView) findViewById(R.id.drawpile5);
        ImageView draw6 = (ImageView) findViewById(R.id.drawpile6);


        final CardCombination listOfCards = CardCombination.getInstance();
        draw1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listOfCards.getDraw1()== listOfCards.getDiscard1() || listOfCards.getDraw1()== listOfCards.getDiscard2()|| listOfCards.getDraw1()== listOfCards.getDiscard3() || listOfCards.getDraw1()== listOfCards.getDiscard4() || listOfCards.getDraw1()== listOfCards.getDiscard5() || listOfCards.getDraw1()== listOfCards.getDiscard6()){
                    correct.start();
                    moveDrawToDiscard();
                }
                else if(listOfCards.getDraw1()>0){
                    incorrect.start();
                }
            }
        });
        draw2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listOfCards.getDraw2()== listOfCards.getDiscard1() || listOfCards.getDraw2()== listOfCards.getDiscard2()|| listOfCards.getDraw2()== listOfCards.getDiscard3()|| listOfCards.getDraw2()== listOfCards.getDiscard4() || listOfCards.getDraw2()== listOfCards.getDiscard5()|| listOfCards.getDraw2()== listOfCards.getDiscard6()){
                    correct.start();
                    moveDrawToDiscard();
                }
                else if(listOfCards.getDraw2()>0) {
                    incorrect.start();
                }
            }
        });
        draw3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listOfCards.getDraw3()== listOfCards.getDiscard1() || listOfCards.getDraw3()== listOfCards.getDiscard2()|| listOfCards.getDraw3()== listOfCards.getDiscard3() || listOfCards.getDraw3()== listOfCards.getDiscard4() || listOfCards.getDraw3()== listOfCards.getDiscard5()|| listOfCards.getDraw3()== listOfCards.getDiscard6()){
                    correct.start();
                    moveDrawToDiscard();
                }
                else if(listOfCards.getDraw3()>0){
                    incorrect.start();
                }
            }
        });
        draw4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listOfCards.getDraw4()== listOfCards.getDiscard1() || listOfCards.getDraw4()== listOfCards.getDiscard2()|| listOfCards.getDraw4()== listOfCards.getDiscard3() || listOfCards.getDraw4()== listOfCards.getDiscard4() || listOfCards.getDraw4()== listOfCards.getDiscard5()|| listOfCards.getDraw4()== listOfCards.getDiscard6()){
                    correct.start();
                    moveDrawToDiscard();
                }
                else if(listOfCards.getDraw4()>0) {
                    incorrect.start();
                }
            }
        });
        draw5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listOfCards.getDraw5()== listOfCards.getDiscard1() || listOfCards.getDraw5()== listOfCards.getDiscard2()|| listOfCards.getDraw5()== listOfCards.getDiscard3() || listOfCards.getDraw5()== listOfCards.getDiscard4() || listOfCards.getDraw5()== listOfCards.getDiscard5()|| listOfCards.getDraw5()== listOfCards.getDiscard6()){
                    correct.start();
                    moveDrawToDiscard();
                }
                else if(listOfCards.getDraw5()>0) {
                    incorrect.start();
                }
            }
        });
        draw6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listOfCards.getDraw6()== listOfCards.getDiscard1() || listOfCards.getDraw6()== listOfCards.getDiscard2()|| listOfCards.getDraw6()== listOfCards.getDiscard3() || listOfCards.getDraw6()== listOfCards.getDiscard4() || listOfCards.getDraw6()== listOfCards.getDiscard5()|| listOfCards.getDraw6()== listOfCards.getDiscard6()){
                    correct.start();
                    moveDrawToDiscard();
                }
                else if(listOfCards.getDraw6()>0) {
                    incorrect.start();
                }
            }
        });
    }
    private void moveDrawToDiscard(){
        CardCombination Cards = CardCombination.getInstance();
        int[] currentCombonDraw = CardCombination.getInstance().getCombinationOfDraw();
        TextView textDraw1 = (TextView) findViewById(R.id.textdrawpile1);
        TextView textDraw2 = (TextView) findViewById(R.id.textdrawpile2);
        TextView textDraw3 = (TextView) findViewById(R.id.textdrawpile3);
        TextView textDraw4 = (TextView) findViewById(R.id.textdrawpile4);
        TextView textDraw5 = (TextView) findViewById(R.id.textdrawpile5);
        TextView textDraw6 = (TextView) findViewById(R.id.textdrawpile6);
        TextView textDiscard1 = (TextView) findViewById(R.id.textdiscard1);
        TextView textDiscard2 = (TextView) findViewById(R.id.textdiscard2);
        TextView textDiscard3 = (TextView) findViewById(R.id.textdiscard3);
        TextView textDiscard4 = (TextView) findViewById(R.id.textdiscard4);
        TextView textDiscard5 = (TextView) findViewById(R.id.textdiscard5);
        TextView textDiscard6 = (TextView) findViewById(R.id.textdiscard6);
        textDiscard1.setText(textDraw1.getText());
        textDiscard2.setText(textDraw2.getText());
        textDiscard3.setText(textDraw3.getText());
        textDiscard4.setText(textDraw4.getText());
        textDiscard5.setText(textDraw5.getText());
        textDiscard6.setText(textDraw6.getText());
        ImageView draw1 = (ImageView) findViewById(R.id.drawpile1);
        ImageView draw2 = (ImageView) findViewById(R.id.drawpile2);
        ImageView draw3 = (ImageView) findViewById(R.id.drawpile3);
        ImageView draw4 = (ImageView) findViewById(R.id.drawpile4);
        ImageView draw5 = (ImageView) findViewById(R.id.drawpile5);
        ImageView draw6 = (ImageView) findViewById(R.id.drawpile6);
        ImageView discard1 = (ImageView) findViewById(R.id.discardpile1);
        ImageView discard2 = (ImageView) findViewById(R.id.discardpile2);
        ImageView discard3 = (ImageView) findViewById(R.id.discardpile3);
        ImageView discard4 = (ImageView) findViewById(R.id.discardpile4);
        ImageView discard5 = (ImageView) findViewById(R.id.discardpile5);
        ImageView discard6 = (ImageView) findViewById(R.id.discardpile6);

        CardView cardView = (CardView) findViewById(R.id.discardpile);
        //Export Images

        if(export == 1) {
            imgBitmap = ExportCard.getBitmapFromCardView(cardView);
            ExportCard.saveToGallery(imgBitmap);
        }

        discard1.setImageDrawable(draw1.getDrawable());
        discard2.setImageDrawable(draw2.getDrawable());
        discard3.setImageDrawable(draw3.getDrawable());
        Cards.setDiscard1(currentCombonDraw[0]);
        Cards.setDiscard2(currentCombonDraw[1]);
        Cards.setDiscard3(currentCombonDraw[2]);
        if(getNumImagesPerCard(this)>3){
            discard4.setImageDrawable(draw4.getDrawable());
            Cards.setDiscard4(currentCombonDraw[3]);
            if(getNumImagesPerCard(this)==6){
                discard5.setImageDrawable(draw5.getDrawable());
                Cards.setDiscard5(currentCombonDraw[4]);
                discard6.setImageDrawable(draw6.getDrawable());
                Cards.setDiscard6(currentCombonDraw[5]);
            }
        }
        discard1.setRotation(draw1.getRotation());
        discard2.setRotation(draw2.getRotation());
        discard3.setRotation(draw3.getRotation());
        discard4.setRotation(draw4.getRotation());
        discard5.setRotation(draw5.getRotation());
        discard6.setRotation(draw6.getRotation());

        discard1.setScaleX(draw1.getScaleX());
        discard2.setScaleX(draw2.getScaleX());
        discard3.setScaleX(draw3.getScaleX());
        discard4.setScaleX(draw4.getScaleX());
        discard5.setScaleX(draw5.getScaleX());
        discard6.setScaleX(draw6.getScaleX());

        discard1.setScaleY(draw1.getScaleY());
        discard2.setScaleY(draw2.getScaleY());
        discard3.setScaleY(draw3.getScaleY());
        discard4.setScaleY(draw4.getScaleY());
        discard5.setScaleY(draw5.getScaleY());
        discard6.setScaleY(draw6.getScaleY());

        textDiscard1.setRotation(textDraw1.getRotation());
        textDiscard2.setRotation(textDraw2.getRotation());
        textDiscard3.setRotation(textDraw3.getRotation());
        textDiscard4.setRotation(textDraw4.getRotation());
        textDiscard5.setRotation(textDraw5.getRotation());
        textDiscard6.setRotation(textDraw6.getRotation());

        textDiscard1.setScaleX(textDraw1.getScaleX());
        textDiscard2.setScaleX(textDraw2.getScaleX());
        textDiscard3.setScaleX(textDraw3.getScaleX());
        textDiscard4.setScaleX(textDraw4.getScaleX());
        textDiscard5.setScaleX(textDraw5.getScaleX());
        textDiscard6.setScaleX(textDraw6.getScaleX());

        textDiscard1.setScaleY(textDraw1.getScaleY());
        textDiscard2.setScaleY(textDraw2.getScaleY());
        textDiscard3.setScaleY(textDraw3.getScaleY());
        textDiscard4.setScaleY(textDraw4.getScaleY());
        textDiscard5.setScaleY(textDraw5.getScaleY());
        textDiscard6.setScaleY(textDraw6.getScaleY());


        Cards.removeObject(currentCombonDraw);
        size--;
        if(Cards.getCards().size()<=0||size<=0){
            setEmptyCardPile();
        }
        if(getMode(this) <2) {
            setDrawPile();
        }
        else{
            setFlickrDrawPile();
        }
    }
    private void setDiscardText(){
        String[] TEXTS = getResources().getStringArray(R.array.Texts);
        int[] TextPositions = {0,0,0,0,0,0};
        CardCombination cards = CardCombination.getInstance();
        ImageView discard1 = (ImageView) findViewById(R.id.discardpile1);
        ImageView discard2 = (ImageView) findViewById(R.id.discardpile2);
        ImageView discard3 = (ImageView) findViewById(R.id.discardpile3);
        ImageView discard4 = (ImageView) findViewById(R.id.discardpile4);
        ImageView discard5 = (ImageView) findViewById(R.id.discardpile5);
        ImageView discard6 = (ImageView) findViewById(R.id.discardpile6);
        TextView textDiscard1 = (TextView) findViewById(R.id.textdiscard1);
        TextView textDiscard2 = (TextView) findViewById(R.id.textdiscard2);
        TextView textDiscard3 = (TextView) findViewById(R.id.textdiscard3);
        TextView textDiscard4 = (TextView) findViewById(R.id.textdiscard4);
        TextView textDiscard5 = (TextView) findViewById(R.id.textdiscard5);
        TextView textDiscard6 = (TextView) findViewById(R.id.textdiscard6);


        Random number = new Random();
        int TextPosition = number.nextInt(3);
        TextPositions[TextPosition]=1;
        if(getNumImagesPerCard(this)>3){
            do {
                TextPosition=number.nextInt(4);
            }while(TextPositions[TextPosition]==1);
        }
        TextPositions[TextPosition]=1;
        if(getNumImagesPerCard(this)==6){
            do {
                TextPosition=number.nextInt(6);
            }while(TextPositions[TextPosition]==1);
        }
        //0 = mode1
        //1 = mode2
         int gamemode = getMode(this);
        TextPositions[TextPosition] = 1;
        if(TextPositions[0]==1 && gamemode ==1){
            discard1.setImageDrawable(getDrawable(R.drawable.white));
            textDiscard1.setText(TEXTS[cards.getDiscard1()-R.drawable.a0]);
        }
        if(TextPositions[1]==1 && gamemode ==1){
            discard2.setImageDrawable(getDrawable(R.drawable.white));
            textDiscard2.setText(TEXTS[cards.getDiscard2()-R.drawable.a0]);
        }
        if(TextPositions[2]==1 && gamemode ==1){
            discard3.setImageDrawable(getDrawable(R.drawable.white));
            textDiscard3.setText(TEXTS[cards.getDiscard3()-R.drawable.a0]);
        }
        if(TextPositions[3]==1 && gamemode ==1){
            discard4.setImageDrawable(getDrawable(R.drawable.white));
            textDiscard4.setText(TEXTS[cards.getDiscard4()-R.drawable.a0]);
        }
        if(TextPositions[4]==1 && gamemode ==1){
            discard5.setImageDrawable(getDrawable(R.drawable.white));
            textDiscard5.setText(TEXTS[cards.getDiscard5()-R.drawable.a0]);
        }
        if(TextPositions[5]==1 && gamemode ==1){
            discard6.setImageDrawable(getDrawable(R.drawable.white));
            textDiscard6.setText(TEXTS[cards.getDiscard6()-R.drawable.a0]);
        }
        for(int i=0; i<6;i++){
            System.out.println(TextPositions[i]);
        }
        Random random = new Random();
        float rotate;

        if(difficulty >= 1) {

            for (int i = 1; i <= OptionsScreen.getNumImagesPerCard(this); i++) {
                if (i == 1) {
                    rotate = (float) random.nextInt(360);
                    textDiscard1.setRotation(rotate);
                }
                if (i == 2) {
                    rotate = (float) random.nextInt(360);
                    textDiscard2.setRotation(rotate);
                }
                if (i == 3) {
                    rotate = (float) random.nextInt(360);
                    textDiscard3.setRotation(rotate);
                }
                if (i == 4) {
                    rotate = (float) random.nextInt(360);
                    textDiscard4.setRotation(rotate);
                }
                if (i == 5) {
                    rotate = (float) random.nextInt(360);
                    textDiscard5.setRotation(rotate);
                }
                if (i == 6) {
                    rotate = (float) random.nextInt(360);
                    textDiscard6.setRotation(rotate);
                }
            }
        }
    }


    private void setDrawText(){
        String[] TEXTS = getResources().getStringArray(R.array.Texts);
        int[] TextPositions = {0,0,0,0,0,0};
        CardCombination cards = CardCombination.getInstance();
        ImageView draw1 = (ImageView) findViewById(R.id.drawpile1);
        ImageView draw2 = (ImageView) findViewById(R.id.drawpile2);
        ImageView draw3 = (ImageView) findViewById(R.id.drawpile3);
        ImageView draw4 = (ImageView) findViewById(R.id.drawpile4);
        ImageView draw5 = (ImageView) findViewById(R.id.drawpile5);
        ImageView draw6 = (ImageView) findViewById(R.id.drawpile6);
        TextView textDraw1 = (TextView) findViewById(R.id.textdrawpile1);
        TextView textDraw2 = (TextView) findViewById(R.id.textdrawpile2);
        TextView textDraw3 = (TextView) findViewById(R.id.textdrawpile3);
        TextView textDraw4 = (TextView) findViewById(R.id.textdrawpile4);
        TextView textDraw5 = (TextView) findViewById(R.id.textdrawpile5);
        TextView textDraw6 = (TextView) findViewById(R.id.textdrawpile6);

        textDraw1.setScaleX(TextScales[0]);
        textDraw1.setScaleY(TextScales[1]);
        textDraw2.setScaleX(TextScales[2]);
        textDraw2.setScaleY(TextScales[3]);
        textDraw3.setScaleX(TextScales[4]);
        textDraw3.setScaleY(TextScales[5]);
        textDraw4.setScaleX(TextScales[6]);
        textDraw4.setScaleY(TextScales[7]);
        textDraw5.setScaleX(TextScales[8]);
        textDraw5.setScaleY(TextScales[9]);
        textDraw6.setScaleX(TextScales[10]);
        textDraw6.setScaleY(TextScales[11]);

        textDraw1.setText("");
        textDraw2.setText("");
        textDraw3.setText("");
        textDraw4.setText("");
        textDraw5.setText("");
        textDraw6.setText("");
        Random number = new Random();
        int TextPosition = number.nextInt(3);
        TextPositions[TextPosition]=1;
        if(getNumImagesPerCard(this)>3){
            do {
                TextPosition=number.nextInt(4);
            }while(TextPositions[TextPosition]==1);
        }
        TextPositions[TextPosition]=1;
        if(getNumImagesPerCard(this)==6){
            do {
                TextPosition=number.nextInt(6);
            }while(TextPositions[TextPosition]==1);
        }
        TextPositions[TextPosition]=1;
        //0 = mode1
        //1 = mode2
        int gamemode = getMode(this);
        if(TextPositions[0]==1 && gamemode == 1 ){
            draw1.setImageDrawable(getDrawable(R.drawable.white));
            textDraw1.setText(TEXTS[cards.getDraw1()-R.drawable.a0]);
        }
        if(TextPositions[1]==1 && gamemode == 1 ){
            draw2.setImageDrawable(getDrawable(R.drawable.white));
            textDraw2.setText(TEXTS[cards.getDraw2()-R.drawable.a0]);
        }
        if(TextPositions[2]==1 && gamemode == 1 ){
            draw3.setImageDrawable(getDrawable(R.drawable.white));
            textDraw3.setText(TEXTS[cards.getDraw3()-R.drawable.a0]);
        }
        if(TextPositions[3]==1 && gamemode == 1 ){
            draw4.setImageDrawable(getDrawable(R.drawable.white));
            textDraw4.setText(TEXTS[cards.getDraw4()-R.drawable.a0]);
        }
        if(TextPositions[4]==1 && gamemode == 1 ){
            draw5.setImageDrawable(getDrawable(R.drawable.white));
            textDraw5.setText(TEXTS[cards.getDraw5()-R.drawable.a0]);
        }
        if(TextPositions[5]==1 && gamemode == 1 ){
            draw6.setImageDrawable(getDrawable(R.drawable.white));
            textDraw6.setText(TEXTS[cards.getDraw6()-R.drawable.a0]);
        }
        Random random = new Random();
        float rotate;

        if(difficulty >= 1) {

            for (int i = 1; i <= OptionsScreen.getNumImagesPerCard(this); i++) {
                if (i == 1) {
                    rotate = (float) random.nextInt(360);
                    textDraw1.setRotation(rotate);
                }
                if (i == 2) {
                    rotate = (float) random.nextInt(360);
                    textDraw2.setRotation(rotate);
                }
                if (i == 3) {
                    rotate = (float) random.nextInt(360);
                    textDraw3.setRotation(rotate);
                }
                if (i == 4) {
                    rotate = (float) random.nextInt(360);
                    textDraw4.setRotation(rotate);
                }
                if (i == 5) {
                    rotate = (float) random.nextInt(360);
                    textDraw5.setRotation(rotate);
                }
                if (i == 6) {
                    rotate = (float) random.nextInt(360);
                    textDraw6.setRotation(rotate);
                }
            }
        }
    }

    public void compressDiscardPileImages(){
        boolean[] options={false,false,false,false,false,false};
        ImageView discard1 = (ImageView) findViewById(R.id.discardpile1);
        ImageView discard2 = (ImageView) findViewById(R.id.discardpile2);
        ImageView discard3 = (ImageView) findViewById(R.id.discardpile3);
        ImageView discard4 = (ImageView) findViewById(R.id.discardpile4);
        ImageView discard5 = (ImageView) findViewById(R.id.discardpile5);
        ImageView discard6 = (ImageView) findViewById(R.id.discardpile6);
        TextView textDiscard1 = (TextView) findViewById(R.id.textdiscard1);
        TextView textDiscard2 = (TextView) findViewById(R.id.textdiscard2);
        TextView textDiscard3 = (TextView) findViewById(R.id.textdiscard3);
        TextView textDiscard4 = (TextView) findViewById(R.id.textdiscard4);
        TextView textDiscard5 = (TextView) findViewById(R.id.textdiscard5);
        TextView textDiscard6 = (TextView) findViewById(R.id.textdiscard6);
        Random random= new Random();
        int choice=0;
        do {
            choice = random.nextInt(3);
        }while(options[choice]==true);
        options[choice]=true;
        if(getNumImagesPerCard(this)>3){
            do {
                choice = random.nextInt(4);
            }while(options[choice]==true);
            options[choice]=true;
            if(getNumImagesPerCard(this)==6){
                do {
                    choice = random.nextInt(6);
                }while(options[choice]==true);
                options[choice]=true;
            }
        }
        if(options[0]==true){
            discard1.setScaleX(discard1.getScaleX()/2);
            discard1.setScaleY(discard1.getScaleY()/2);
            textDiscard1.setScaleX(textDiscard1.getScaleX()/2);
            textDiscard1.setScaleY(textDiscard1.getScaleY()/2);
        }
        if(options[1]==true){
            discard2.setScaleX(discard2.getScaleX()/2);
            discard2.setScaleY(discard2.getScaleY()/2);
            textDiscard2.setScaleX(textDiscard2.getScaleX()/2);
            textDiscard2.setScaleY(textDiscard2.getScaleY()/2);
        }
        if(options[2]==true){
            discard3.setScaleX(discard3.getScaleX()/2);
            discard3.setScaleY(discard3.getScaleY()/2);
            textDiscard3.setScaleX(textDiscard3.getScaleX()/2);
            textDiscard3.setScaleY(textDiscard3.getScaleY()/2);
        }
        if(options[3]==true){
            discard4.setScaleX(discard4.getScaleX()/2);
            discard4.setScaleY(discard4.getScaleY()/2);
            textDiscard4.setScaleX(textDiscard4.getScaleX()/2);
            textDiscard4.setScaleY(textDiscard4.getScaleY()/2);
        }
        if(options[4]==true){
            discard5.setScaleX(discard5.getScaleX()/2);
            discard5.setScaleY(discard5.getScaleY()/2);
            textDiscard5.setScaleX(textDiscard5.getScaleX()/2);
            textDiscard5.setScaleY(textDiscard5.getScaleY()/2);
        }
        if(options[5]==true){
            discard6.setScaleX(discard6.getScaleX()/2);
            discard6.setScaleY(discard6.getScaleY()/2);
            textDiscard6.setScaleX(textDiscard6.getScaleX()/2);
            textDiscard6.setScaleY(textDiscard6.getScaleY()/2);
        }
    }

    public void compressDrawPileImages(){
        boolean[] options={false,false,false,false,false,false};
        ImageView draw1 = (ImageView) findViewById(R.id.drawpile1);
        ImageView draw2 = (ImageView) findViewById(R.id.drawpile2);
        ImageView draw3 = (ImageView) findViewById(R.id.drawpile3);
        ImageView draw4 = (ImageView) findViewById(R.id.drawpile4);
        ImageView draw5 = (ImageView) findViewById(R.id.drawpile5);
        ImageView draw6 = (ImageView) findViewById(R.id.drawpile6);
        TextView textDraw1 = (TextView) findViewById(R.id.textdrawpile1);
        TextView textDraw2 = (TextView) findViewById(R.id.textdrawpile2);
        TextView textDraw3 = (TextView) findViewById(R.id.textdrawpile3);
        TextView textDraw4 = (TextView) findViewById(R.id.textdrawpile4);
        TextView textDraw5 = (TextView) findViewById(R.id.textdrawpile5);
        TextView textDraw6 = (TextView) findViewById(R.id.textdrawpile6);
        Random random= new Random();
        int choice=0;
        do {
            choice = random.nextInt(3);
        }while(options[choice]==true);
        options[choice]=true;
        if(getNumImagesPerCard(this)>3){
            do {
                choice = random.nextInt(4);
            }while(options[choice]==true);
            options[choice]=true;
            if(getNumImagesPerCard(this)==6){
                do {
                    choice = random.nextInt(6);
                }while(options[choice]==true);
                options[choice]=true;
            }
        }
        if(options[0]==true){
            draw1.setScaleX(draw1.getScaleX()/2);
            draw1.setScaleY(draw1.getScaleY()/2);
            textDraw1.setScaleX(textDraw1.getScaleX()/2);
            textDraw1.setScaleY(textDraw1.getScaleY()/2);
        }
        if(options[1]==true){
            draw2.setScaleX(draw2.getScaleX()/2);
            draw2.setScaleY(draw2.getScaleY()/2);
            textDraw2.setScaleX(textDraw2.getScaleX()/2);
            textDraw2.setScaleY(textDraw2.getScaleY()/2);

        }
        if(options[2]==true){
            draw3.setScaleX(draw3.getScaleX()/2);
            draw3.setScaleY(draw3.getScaleY()/2);
            textDraw3.setScaleX(textDraw3.getScaleX()/2);
            textDraw3.setScaleY(textDraw3.getScaleY()/2);
        }
        if(options[3]==true){
            draw4.setScaleX(draw4.getScaleX()/2);
            draw4.setScaleY(draw4.getScaleY()/2);
            textDraw4.setScaleX(textDraw4.getScaleX()/2);
            textDraw4.setScaleY(textDraw4.getScaleY()/2);
        }
        if(options[4]==true){
            draw5.setScaleX(draw5.getScaleX()/2);
            draw5.setScaleY(draw5.getScaleY()/2);
            textDraw5.setScaleX(textDraw5.getScaleX()/2);
            textDraw5.setScaleY(textDraw5.getScaleY()/2);
        }
        if(options[5]==true){
            draw6.setScaleX(draw6.getScaleX()/2);
            draw6.setScaleY(draw6.getScaleY()/2);
            textDraw6.setScaleX(textDraw6.getScaleX()/2);
            textDraw6.setScaleY(textDraw6.getScaleY()/2);

        }
    }
    public void initializeScales(){
        ImageView draw1 = (ImageView) findViewById(R.id.drawpile1);
        ImageView draw2 = (ImageView) findViewById(R.id.drawpile2);
        ImageView draw3 = (ImageView) findViewById(R.id.drawpile3);
        ImageView draw4 = (ImageView) findViewById(R.id.drawpile4);
        ImageView draw5 = (ImageView) findViewById(R.id.drawpile5);
        ImageView draw6 = (ImageView) findViewById(R.id.drawpile6);
        Scales= new float[12];
        Scales[0]=draw1.getScaleX();
        Scales[1]=draw1.getScaleY();
        Scales[2]=draw2.getScaleX();
        Scales[3]=draw2.getScaleY();
        Scales[4]=draw3.getScaleX();
        Scales[5]=draw3.getScaleY();
        Scales[6]=draw4.getScaleX();
        Scales[7]=draw4.getScaleY();
        Scales[8]=draw5.getScaleX();
        Scales[9]=draw5.getScaleY();
        Scales[10]=draw6.getScaleX();
        Scales[11]=draw6.getScaleY();
    }
    public void initializeTextScales(){
        TextView textDraw1 = (TextView) findViewById(R.id.textdrawpile1);
        TextView textDraw2 = (TextView) findViewById(R.id.textdrawpile2);
        TextView textDraw3 = (TextView) findViewById(R.id.textdrawpile3);
        TextView textDraw4 = (TextView) findViewById(R.id.textdrawpile4);
        TextView textDraw5 = (TextView) findViewById(R.id.textdrawpile5);
        TextView textDraw6 = (TextView) findViewById(R.id.textdrawpile6);
        TextScales=new float[12];
        TextScales[0]= textDraw1.getScaleX();
        TextScales[1]= textDraw1.getScaleY();
        TextScales[2]= textDraw2.getScaleX();
        TextScales[3]= textDraw2.getScaleY();
        TextScales[4]=textDraw3.getScaleX();
        TextScales[5]=textDraw3.getScaleY();
        TextScales[6]=textDraw4.getScaleX();
        TextScales[7]=textDraw4.getScaleY();
        TextScales[8]=textDraw5.getScaleX();
        TextScales[9]=textDraw5.getScaleY();
        TextScales[10]=textDraw6.getScaleX();
        TextScales[11]=textDraw6.getScaleY();
    }
}
