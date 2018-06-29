package com.school;

import com.school.controller.ConnectFourController;

public class Main {

    public static void main(String[] args) {
        int R = 6;
        int C = 7;
        int V = 4;

        if (args.length == 3) {
            try {
                R = Integer.parseInt(args[0]);
                C = Integer.parseInt(args[1]);
                V = Integer.parseInt(args[2]);

            } catch (NumberFormatException ex) {
                System.out.println("Invalid argument");
                return;
            }
        }

        new ConnectFourController(R, C, V);

    }
}
