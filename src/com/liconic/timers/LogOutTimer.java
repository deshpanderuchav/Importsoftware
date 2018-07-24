package com.liconic.timers;

import com.liconic.stages.ImportStage;
import java.util.Timer;
import java.util.TimerTask;

public class LogOutTimer {

    Timer timer;

    private ImportStage importStage;

    public LogOutTimer(ImportStage importStage) {

        System.out.println(" >>> RUN LOGOUT TIMER");

        this.importStage = importStage;

        timer = new Timer();
        timer.schedule(new RemindTask(), 30 * 1000, 60 * 1000 * 10);
    }

    class RemindTask extends TimerTask {

        public void run() {
            importStage.CheckLogOut();
        }
    }

    public void Stop() {
        System.out.println(" >>> STOP LOGOUT TIMER");
        timer.cancel();
    }

}
