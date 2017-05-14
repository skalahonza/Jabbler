package eu.alavio.jabbler.Models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import eu.alavio.jabbler.Models.Helpers.Helper;

import static org.junit.Assert.assertEquals;

/**
 * Tests for email validation
 */
@RunWith(Parameterized.class)
public class EmailVerificationTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"noArroba.com", false},
                {"Arroba@butnoending", false},
                {"legit@email.com", true},
                {"test@alavio.eu", true}
        });
    }

    private String email;
    private boolean result;

    public EmailVerificationTest(String email, boolean result) {
        this.email = email;
        this.result = result;
    }

    @Test
    public void test() {
        assertEquals(result, Helper.validateEmail(email));
    }
}