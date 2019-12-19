package com.siemens.Simatic_S7;

import android.widget.ProgressBar;

import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadTest {

    private ProgressBar pb_main_progressionTH;
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private Thread monthread;
    private int data;

    public ThreadTest(ProgressBar p) {
        pb_main_progressionTH = p;
    }

    public void Go(){

        monthread = new Thread(new Runnable() {
            @Override
            public void run() {

                for(int i=0;i<20 && isRunning.get();i++) {
                    pb_main_progressionTH.incrementProgressBy(5);
                    data = pb_main_progressionTH.getProgress();
                    if(data == 100) pb_main_progressionTH.setProgress(0);
                    else pb_main_progressionTH.setProgress(data);



                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        isRunning.set(true);
        monthread.start();
    }

    public void Stop()
    {
        isRunning.set(false);
        monthread.interrupt();
        pb_main_progressionTH.setProgress(data);
    }
}
