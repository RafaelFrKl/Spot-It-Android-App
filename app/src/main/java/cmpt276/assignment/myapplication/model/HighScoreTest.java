/**
 * HighScoreTest is used to change the highscores when relevent
 */
package cmpt276.assignment.myapplication.model;

public class HighScoreTest {
    boolean state;
    private static HighScoreTest instance ;

    public HighScoreTest() {
        this.state = false;
    }

    public static HighScoreTest getinstance(){
        if( instance == null){
            instance = new HighScoreTest();
        }
        return instance;
    }

    public boolean getState(){
        return state;
    }

    public void setState(boolean set){
        state = set;
        return;
    }
}
