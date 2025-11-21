package com.legends.io;

public class ConsoleOutput implements Output {
    @Override
    public void print(Object s) {
        System.out.print(s);
    }

    @Override
    public void println(Object s) {
        System.out.println(s);
    }

    @Override
    public void println() {
        System.out.println();
    }

    @Override
    public void printError(Object s) {
        System.err.println(s);
    }
}
