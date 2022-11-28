package com.example.ovohits.backend;

public enum PrintColor {
    GREEN("\033[1;32m"),
    PURPLE("\033[1;35m"),
    RED("\033[1;31m"),
    RESET("\033[0m"),
    WHITE("\033[1;37m"),
    YELLOW("\033[1;33m");

    public final String color;

    PrintColor(String color) { this.color = color; }

    @Override
    public String toString() { return color; }
}
