package rocketpower.library;


/*
 * Controller for running Rocket integration
 * without music track. Just tracks time and
 * advances row as fast as desired.
 */
class TimeController extends RocketController {
    int rps;
    long lastMeterPoint;
    boolean playing;

    TimeController(int rowsPerSecond) {
        rps = rowsPerSecond;
        playing = false;
        lastMeterPoint = 0;
    }

    public void update() {
        if (!playing)
            return;

        if (lastMeterPoint == 0)
            lastMeterPoint = System.nanoTime();

        // Update current time
        long meter = System.nanoTime();
        long timespan = meter - lastMeterPoint;
        lastMeterPoint = meter;
        setCurrentRow(currentRow + (timespan/1e9)*rps, false);
    }

    public boolean isPlaying() {
        return playing;
    }

    public void pause() {
        if (playing)
            playing = false;
    }

    public void play() {
        if (!playing) {
            playing = true;
            lastMeterPoint = System.nanoTime();
            for (ControllerListener listener: listeners)
                listener.controllerStatusChanged(true);
        }
    }

}
