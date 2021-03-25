/**
 * The game timer is for measuring the time it takes for the user to finish a game
 */
package cmpt276.assignment.myapplication.model;

import java.util.Timer;
import java.util.TimerTask;

public class Clock {
    public static long start;
    public static long end;
    boolean running = false;
    private static Timer timer;


    public void startTimer() {


        timer = new Timer();
        if(running == false) {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    System.out.println("Timer running");
                }
            };
            start = System.currentTimeMillis();
            timer.schedule(task, 0);
            running = true;
        }
    }

    public static int getTime() {
        float sec = (end - start) / 1000F;
        return (int) sec;
    }

    public void stopTimer() {
        timer.cancel();
        end = System.currentTimeMillis();
        running = false;
    }
}
