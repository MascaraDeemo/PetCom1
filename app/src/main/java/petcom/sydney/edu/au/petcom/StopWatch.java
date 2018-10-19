package petcom.sydney.edu.au.petcom;

public class StopWatch {

    private long startTime = 0;
    private long stopTime = 0;

    public void start(long stopTime) {
        this.startTime = System.currentTimeMillis();
        this.stopTime = stopTime;
    }

    // elaspsed time in milliseconds
    public long getElapsedTime() {
        return stopTime - startTime;
    }

    public void stop(){
        this.stopTime = System.currentTimeMillis();
    }
    // elaspsed time in seconds
    public long getElapsedTimeSecs() {
        return ((stopTime - startTime) / 1000);
    }
}