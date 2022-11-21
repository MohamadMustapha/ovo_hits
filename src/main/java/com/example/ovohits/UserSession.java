package com.example.ovohits;

public class UserSession {
    private static Integer sessionId = null;

    public static Integer getSessionId() { return sessionId; }

    public static void setSessionId(Integer sessionId) { UserSession.sessionId = sessionId; }
}
