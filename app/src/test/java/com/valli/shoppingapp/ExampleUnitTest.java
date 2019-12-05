package com.valli.shoppingapp;

import android.content.Context;

import com.valli.shoppingapp.Login.LoginActivity;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    public static final String FAKE_STRING ="Login Successful";
    Context mMock;
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
//    @Test
//    public void readFromContext_LocalString()
//    {
//        LoginActivity mObjUnderTest = new LoginActivity(mMock);
//
//        String result =mObjUnderTest.login();
//
//        assertThat(result,is(FAKE_STRING));
//
//    }

}