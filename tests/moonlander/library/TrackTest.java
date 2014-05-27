package moonlander.library;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import moonlander.library.*;

import java.util.List;


@RunWith(JUnit4.class)
public class TrackTest {
    private Track track;
    private TrackKey key;

    @Before
    public void setUp() throws Exception {
        track = new Track("test");
        key = new TrackKey(5, 2.f, TrackKey.KeyType.STEP);
        track.addOrUpdateKey(key);
        track.addOrUpdateKey(new TrackKey(1, 1.f, TrackKey.KeyType.LINEAR));
        track.addOrUpdateKey(new TrackKey(15, 4.f, TrackKey.KeyType.SMOOTH));
        track.addOrUpdateKey(new TrackKey(10, 3.f, TrackKey.KeyType.RAMP));
    }

    @Test
    public void testKeysAreSorted() {
        List<TrackKey> keys = track.getKeys();
        assertEquals(keys.get(0).getRow(), 1);
        assertEquals(keys.get(3).getRow(), 15);
    }

    @Test
    public void testAddOrUpdateKey() {
        track.addOrUpdateKey(new TrackKey(4, 1.f, TrackKey.KeyType.LINEAR));
        assertEquals(track.getKeys().size(), 5);
        TrackKey t = new TrackKey(4, 2.f, TrackKey.KeyType.SMOOTH);
        track.addOrUpdateKey(t);
        assertEquals(track.getKeys().size(), 5);
        assertEquals(t, track.getKey(4));
    }

    @Test
    public void testDeleteKey() {
        track.deleteKey(1);
        assertEquals(3, track.getKeys().size());
    }

    @Test
    public void testGetKey() {
        // Test exact and non-exact matches
        assertEquals(track.getKey(5), key);
        assertEquals(track.getKey(9), key);
        assertEquals(track.getKey(0), null);
    }

    @Test
    public void testGetValue() {
        assertEquals(0., (new Track("test")).getValue(1.0), 0.0001);
        assertEquals(0., track.getValue(0), 0.0001);
        assertEquals(4., track.getValue(18), 0.0001);
        assertEquals(1.5, track.getValue(3), 0.0001);
    }
}
