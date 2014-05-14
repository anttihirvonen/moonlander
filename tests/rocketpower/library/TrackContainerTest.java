package rocketpower.library;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import rocketpower.library.*;

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
        tracks.getTrack("new_track");
        assertEquals(listener.lastName, "new_track");
        tracks.removeEventListener(listener);

        // listener should not listen to changes anymore
        tracks.getTrack("other_track");
        assertEquals(listener.lastName, "new_track");

    }

    @Test
    public void testGetTrack() {
        Track track = tracks.getTrack("new_track");
        assertEquals(tracks.getTracks().size(), 1);
        assertEquals(tracks.getTrack("new_track"), track);
        assertEquals(listener.lastName, "new_track");

    }

    @Test
    public void testDeleteTrack() {
        tracks.getTrack("test"); tracks.deleteTrack("test");
        assertEquals(tracks.getTracks().size(), 0);
        assertEquals(listener.lastName, "test");
    }
}
