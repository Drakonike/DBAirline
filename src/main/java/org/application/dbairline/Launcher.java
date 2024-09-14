package org.application.dbairline;

public class Launcher {
    public static void main(String[] args) {
        try {
            App.main(args);
        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }
    }
}