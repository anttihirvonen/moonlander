package rocketpower.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import rocketpower.library.*;

import java.util.List;


@RunWith(JUnit4.class)
public class TrackTest {
    private Track track;
    private TrackKey key;

    @Before
    public void setUp() throws Exception {
        track = new Track("test");
        key = new TrackKey(5, 1.f, TrackKey.KeyType.STEP);
        track.insertKey(key);
        track.insertKey(new TrackKey(1, 1.f, TrackKey.KeyType.LINEAR));
        track.insertKey(new TrackKey(15, 1.f, TrackKey.KeyType.SMOOTH));
        track.insertKey(new TrackKey(10, 1.f, TrackKey.KeyType.RAMP));
    }

    @Test
    public void testKeysAreSorted() {
        List<TrackKey> keys = track.getKeys();
        assertEquals(keys.get(0).getRow(), 1);
        assertEquals(keys.get(3).getRow(), 15);
    }

    @Test
    public void testGetKey() {
        // Test exact and non-exact matches
        assertEquals(track.getKey(5), key);
        assertEquals(track.getKey(9), key);
        assertEquals(track.getKey(0), null);
    }
}
