package at.edu.c02.ledcontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    /**
     * This is the main program entry point. TODO: add new commands when implementing additional features.
     */
    public static void main(String[] args) throws IOException {
        LedController ledController = new LedControllerImpl(new ApiServiceImpl());

        String input = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while(!input.equalsIgnoreCase("exit"))
        {
            System.out.println("=== LED Controller ===");
            System.out.println("Enter 'demo' to send a demo request");
            System.out.println("Enter 'status' to check the status of an LED");
            System.out.println("Enter 'groupstatus' to check the status of a group of LEDs");
            System.out.println("Enter 'exit' to exit the program");
            input = reader.readLine();
            if(input.equalsIgnoreCase("demo"))
            {
                ledController.demo();
            }
            if (input.equalsIgnoreCase("groupstatus"))
            {
                ledController.getGroupLeds("H");
            }
            if (input.equalsIgnoreCase("status")) {
                System.out.println("Please specify LED ID:");
                String ledIdInput = reader.readLine();
                try {
                    int ledId = Integer.parseInt(ledIdInput);
                    ledController.getSingleLed(ledId);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid LED ID. Please enter a numeric value.");
                }
            }
        }
    }
}
