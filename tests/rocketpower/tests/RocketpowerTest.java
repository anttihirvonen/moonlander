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
    public void blackBoxTest() {
        // Test library as black box

    }
}
