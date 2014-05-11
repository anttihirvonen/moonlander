package rocketpower.library;


public class TrackKey implements Comparable<TrackKey> {
    public enum KeyType {
        STEP,
        LINEAR,
        SMOOTH,
        RAMP
    }

    private int row;
    private float value;
    private KeyType keyType;

    public TrackKey(int row, float value, KeyType keyType) {
        this.row = row;
        this.value = value;
        this.keyType = keyType;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public KeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(KeyType keyType) {
        this.keyType = keyType;
    }

    public int compareTo(TrackKey other) {
        return row > other.row ? 1 : row < other.row ? -1 : 0;
    }

    public String toString() {
        return String.format("TrackKey(row=%d value=%f type=%s)", row, value, keyType);
    }

    public static double interpolate(TrackKey first, TrackKey second, double row) {
        double t = (row - first.row) / (second.row - first.row);

        switch (first.keyType) {
            case STEP:
                return first.value;
            //case KeyType.LINEAR:
            case SMOOTH:
                t = t * t * (3 - 2*t);
                break;
            case RAMP:
                t = Math.pow(t, 2.0);
                break;
        }

        return first.value + (second.value - first.value) * t;
    }
}
