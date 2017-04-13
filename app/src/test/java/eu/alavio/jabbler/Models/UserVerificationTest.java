package eu.alavio.jabbler.Models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Tests for username validation
 */
@RunWith(Parameterized.class)
public class UserVerificationTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"' or 1=1 DROP TABLE;--", false},
                {"hello!!", false},
                {"skala_honza", true},
                {"jaromir_doe_123", true}
        });
    }

    private String username;
    private boolean result;

    public UserVerificationTest(String username, boolean result) {
        this.username = username;
        this.result = result;
    }

    @Test
    public void test() {
        assertEquals(result, Helper.validateUsername(username));
    }
}