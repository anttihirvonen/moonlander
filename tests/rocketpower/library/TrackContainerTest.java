package rocketpower.library;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import rocketpower.library.*;

import java.util.List;



@RunWith(JUnit4.class)
public class TrackContainerTest {
    private TrackContainer tracks;

    @Before
    public void setUp() throws Exception {
        tracks = new TrackContainer();
    }

    @Test
    public void testGetTrack() {
        Track track = tracks.getTrack("test");
        assertEquals(tracks.getTracks().size(), 1);
        assertEquals(tracks.getTrack("test"), track);
    }

    @Test
    public void testDeleteTrack() {
        tracks.getTrack("test"); tracks.deleteTrack("test");
        assertEquals(tracks.getTracks().size(), 0);
    }
}
