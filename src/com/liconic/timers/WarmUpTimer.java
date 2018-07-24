package com.liconic.timers;

import com.liconic.stages.ImportStage;
import java.util.Timer;
import java.util.TimerTask;

public class WarmUpTimer {

//    Toolkit toolkit;
    Timer timer;

    private ImportStage importStage;

    public WarmUpTimer(ImportStage importStage) {

        System.out.println(" >>> RUN WARMUP TIMER");
        this.importStage = importStage;

        timer = new Timer();
        timer.schedule(new RemindTask(), 0, 30 * 1000);
    }

    class RemindTask extends TimerTask {

        public void run() {
            importStage.DrawWarmupDelay();

        }
    }

    public void Stop() {
        System.out.println(" >>> STOP WARMUP TIMER");
        timer.cancel();
    }

}
