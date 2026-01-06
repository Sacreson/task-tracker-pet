package com.sacreson.tasktracker.api.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

    private JwtUtil jwtUtil = new JwtUtil();

    @Test
    void checkTokenLogic(){
        String generatedToker = jwtUtil.generatedToker("test");
        System.out.println(generatedToker);

        String validateTokenSubject = jwtUtil.validateToken(generatedToker);
        System.out.println(validateTokenSubject);

        Assertions.assertEquals("test", validateTokenSubject);
    }


}