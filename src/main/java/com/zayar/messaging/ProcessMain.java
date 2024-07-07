package com.zayar.messaging;

import java.io.IOException;

/**
 * Main Method for invoking both initiator and the receiver player as a process
 */
public class ProcessMain {
    public static void main(String[] args) {

        try {
            ProcessBuilder receiverBuilder = new ProcessBuilder("java", "com.zayar.messaging.ReceiverPlayer");
            receiverBuilder.inheritIO();
            Process receiverProcess = receiverBuilder.start();

            ProcessBuilder initiatorBuilder = new ProcessBuilder("java", "com.zayar.messaging.InitiatorPlayer");
            initiatorBuilder.inheritIO();
            Process initiatorProcess = initiatorBuilder.start();

            int receiverExitCode = receiverProcess.waitFor();
            int initiatorExitCode = initiatorProcess.waitFor();

            System.out.println("Server process exited with code: " + receiverExitCode);
            System.out.println("Client process exited with code: " + initiatorExitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
