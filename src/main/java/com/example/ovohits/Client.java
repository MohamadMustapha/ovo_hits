package com.example.ovohits;

public class Client {
    private static boolean threadAlive = false;
    private static Integer clientId = null;

    public static boolean isThreadAlive() { return threadAlive; }

    public static void setThreadAlive(boolean threadAlive) { Client.threadAlive = threadAlive; }

    public static Integer getClientId() { return clientId; }

    public static void setClientId(Integer clientId) { Client.clientId = clientId; }
}
