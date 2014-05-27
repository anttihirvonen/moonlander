package moonlander.library;


class TrackKey implements Comparable<TrackKey> {
    /**
     * Interpolation type.
     *
     * Note: it's important that enum's
     * ordinal values map to the same values on
     * Rocket's side!
     */
    public enum KeyType {
        /**
         * No interpolation, just steps to next value.
         */
        STEP, // ordinal = 0 and so on..
        /**
         * Linear 
         */
        LINEAR,
        /**
         * Smooth
         */
        SMOOTH,
        /**
         * Ramp up
         */
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

    public TrackKey(int row, float value, int keyType) {
        this(row, value, KeyType.values()[keyType]);
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

    /**
     * Keeps tracks sorted in ascending order by 
     * the row value.
     */
    public int compareTo(TrackKey other) {
        return row > other.row ? 1 : row < other.row ? -1 : 0;
    }

    public String toString() {
        return String.format("TrackKey(row=%d value=%f type=%s)", row, value, keyType);
    }

    /**
     * Interpolates value for row between two TrackKey-objects.
     *
     * Interpolation type depends on the first key; only the value is
     * used from the second key in the process.
     *
     * Row can naturally be fractional, so interpolation between
     * values flowing in time is continuous.
     */
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
