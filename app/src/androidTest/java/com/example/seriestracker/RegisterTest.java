package com.example.seriestracker;


import com.example.seriestracker.firebaseapi.FB;

import org.junit.Test;

public class RegisterTest {
    @Test
    public void successfulRegisterTest(){
        int random = (int) Math.floor(Math.random() *(100000 + 1));
        FB.auth.createUserWithEmailAndPassword("testingXYZ321987a" + random + "@gmail.com", "testing").addOnCompleteListener(task -> {
            assert task.isSuccessful();
        });
    }

    @Test
    public void failureRegisterTest(){
        FB.auth.createUserWithEmailAndPassword("z@gmail.com", "test").addOnCompleteListener(task -> {
            assert !task.isSuccessful();
        });

        FB.auth.createUserWithEmailAndPassword("z", "testing").addOnCompleteListener(task -> {
            assert !task.isSuccessful();
        });
    }
}
