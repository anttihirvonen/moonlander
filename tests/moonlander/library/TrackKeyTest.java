package moonlander.library;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import moonlander.library.*;


@RunWith(JUnit4.class)
public class TrackKeyTest {
    @Test
    public void testConstructor() {
        TrackKey t = new TrackKey(1, 0.f, 1);
        assertEquals(t.getKeyType(), TrackKey.KeyType.LINEAR);
    }

    @Test
    public void testCompareTo() {
        TrackKey first = new TrackKey(1, 0.f, TrackKey.KeyType.STEP);
        TrackKey second = new TrackKey(2, 0.f, TrackKey.KeyType.STEP);

        assertEquals(first.compareTo(second), -1);
        assertEquals(second.compareTo(first), 1);
        assertEquals(second.compareTo(second), 0);
    }

    @Test
    public void testInterpolate() {
        TrackKey first = new TrackKey(1, 1.f, TrackKey.KeyType.STEP);
        TrackKey second = new TrackKey(11, 2.f, TrackKey.KeyType.STEP);
        assertEquals(TrackKey.interpolate(first, second, 1), 1.0, 0.0001);
        assertEquals(TrackKey.interpolate(first, second, 10.9), 1.0, 0.0001);

        first.setKeyType(TrackKey.KeyType.LINEAR);
        assertEquals(TrackKey.interpolate(first, second, 1), 1.0, 0.0001);
        assertEquals(TrackKey.interpolate(first, second, 6), 1.5, 0.0001);

        first.setKeyType(TrackKey.KeyType.SMOOTH);
        assertEquals(TrackKey.interpolate(first, second, 5), 1.352, 0.0001);

        first.setKeyType(TrackKey.KeyType.RAMP);
        assertEquals(TrackKey.interpolate(first, second, 5), 1.16, 0.0001);
    }
}
