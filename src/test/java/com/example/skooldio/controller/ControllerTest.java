package com.example.skooldio.controller;

import org.junit.jupiter.api.BeforeAll;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ControllerTest {

    public static String IP;

    @BeforeAll
    public static void getLocalHost() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        IP = String.format("%s%s%s", "http://", inetAddress.getHostAddress(), ":");
    }
}
