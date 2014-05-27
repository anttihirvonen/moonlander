package moonlander.library;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import moonlander.library.*;

import java.util.List;


class TestListener implements TrackContainerListener {
    String lastName;
    
    public TestListener() {
        lastName = new String();
    }

    public void trackAdded(String name) { lastName = name; }
    public void trackDeleted(String name) { lastName = name; }
}


@RunWith(JUnit4.class)
public class TrackContainerTest {
    private TrackContainer tracks;
    private TestListener listener;

    @Before
    public void setUp() throws Exception {
        listener = new TestListener();
        tracks = new TrackContainer();
        tracks.addEventListener(listener);
    }

    @Test
    public void testEventListenerAddAndRemove() {
        tracks.getOrCreate("new_track");
        assertEquals(listener.lastName, "new_track");
        tracks.removeEventListener(listener);

        // listener should not listen to changes anymore
        tracks.getOrCreate("other_track");
        assertEquals(listener.lastName, "new_track");

    }

    @Test
    public void testGetById() {
        Track t1 = tracks.getOrCreate("1");
        tracks.getOrCreate("2");
        Track t3 = tracks.getOrCreate("3");
        assertEquals(tracks.getById(0), t1);
        assertEquals(tracks.getById(2), t3);
    }

    @Test
    public void testGetOrCreateTrack() {
        Track track = tracks.getOrCreate("new_track");
        assertEquals(tracks.getAll().size(), 1);
        assertEquals(tracks.getOrCreate("new_track"), track);
        assertEquals(listener.lastName, "new_track");

    }

    @Test
    public void testDeleteTrack() {
        tracks.getOrCreate("test"); tracks.delete("test");
        assertEquals(tracks.getAll().size(), 0);
        assertEquals(listener.lastName, "test");
    }
}
