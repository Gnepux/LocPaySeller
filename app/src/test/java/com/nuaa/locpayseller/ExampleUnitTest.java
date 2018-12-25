package com.nuaa.locpayseller;

import com.nuaa.locpayseller.util.RSAUtil;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
//        assertEquals(4, 2 + 2);
        String key = null;
        try {
            key = RSAUtil.genKeyPair("xupeng")[1];
            System.out.println(key);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}