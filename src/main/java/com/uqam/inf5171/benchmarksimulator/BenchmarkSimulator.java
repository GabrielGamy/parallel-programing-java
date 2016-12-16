package com.uqam.inf5171.benchmarksimulator;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BenchmarkSimulator {

    private long startTime;
    private long endTime;

    public BenchmarkSimulator() {
        reset();
    }

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void end() {
        endTime = System.currentTimeMillis();
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public double getTotalTime() {
        return (endTime - startTime) * 0.001; // convert msec to sec
    }

    public void reset() {
        startTime = 0;
        endTime = 0;
    }

    public void warmup() {
        System.out.print("Warmup (1 min)");

        double oneMinute = 60; // 1min
        Thread printDots = printDots();
        
        this.reset();
        this.start();
        printDots.start();
        
        do {
            this.end();
        } while (this.getTotalTime() < oneMinute);

        printDots.stop();
        this.end();
        this.reset();
        
        System.out.println("\nWarmup Ended");
    }

    private Thread printDots() {
        return new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                } finally {System.out.print(".");}
            }
        });
    }
}
