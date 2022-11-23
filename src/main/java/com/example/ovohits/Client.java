package com.example.ovohits;

public class Client {
    private static Integer sessionId = null;

    public static Integer getSessionId() { return sessionId; }

    public static void setSessionId(Integer sessionId) { Client.sessionId = sessionId; }
}
