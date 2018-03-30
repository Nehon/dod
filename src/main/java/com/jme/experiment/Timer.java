package com.jme.experiment;

public class Timer {

    private long startTime = 0;
    private String label;

    public void start(String label){
        startTime = System.nanoTime();
        this.label = label;
    }

    public void stop(){
        long time = System.nanoTime() - startTime;
        double result = time / 1000000.0;
        System.out.format("%s: %.2f milliseconds%n", label, result);
    }


}
