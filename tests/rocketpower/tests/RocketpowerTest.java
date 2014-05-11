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
public class RocketpowerTest {
    private Rocketpower rocket;

    @Before
    public void setUp() throws Exception {
        rocket = new Rocketpower();
    }

    @Test
    public void testGetTrack() {
        Track track = rocket.getTrack("test");
        assertEquals(rocket.getTracks().size(), 1);
        assertEquals(rocket.getTrack("test"), track);
    }

    @Test
    public void testDeleteTrack() {
        rocket.getTrack("test"); rocket.deleteTrack("test");
        assertEquals(rocket.getTracks().size(), 0);
    }
}
