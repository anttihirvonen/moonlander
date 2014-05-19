package rocketpower.library;


/*
 * Controller for running Rocket integration
 * without music track. Just tracks time and
 * advances row as fast as desired.
 */
class TimeController extends RocketController {
    int rps;
    long lastMeterPoint;

    TimeController(int rowsPerSecond) {
        rps = rowsPerSecond;
        lastMeterPoint = 0;
    }

    public void update() {
        if (!isPlaying())
            return;

        if (lastMeterPoint == 0)
            lastMeterPoint = System.nanoTime();

        // Update current time
        long meter = System.nanoTime();
        long timespan = meter - lastMeterPoint;
        lastMeterPoint = meter;
        setCurrentRow(currentRow + (timespan/1e9)*rps, false);
    }
}
