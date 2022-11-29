package com.example.ovohits;

import java.io.Serial;
import java.io.Serializable;

public class Client implements Serializable {
    @Serial
    private static final long serialVersionUID = -8830849998498465638L;
    private static boolean threadAlive = false;
    private static Integer clientId = null;

    public static boolean isThreadAlive() { return threadAlive; }

    public static void setThreadAlive(boolean threadAlive) { Client.threadAlive = threadAlive; }

    public static Integer getClientId() { return clientId; }

    public static void setClientId(Integer clientId) { Client.clientId = clientId; }
}
