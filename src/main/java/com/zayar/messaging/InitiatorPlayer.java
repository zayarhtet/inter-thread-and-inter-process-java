package com.zayar.messaging;

import com.zayar.messaging.player.Player;
import com.zayar.messaging.player.SharedMemoryPlayer;

import java.util.stream.IntStream;

/**
 * The initiator or the sender or the client.
 * The echo server with the Player using inter-process communication
 */
public class InitiatorPlayer {
    public static void main(String[] args) {

        try {
            Player initiator =
                    new SharedMemoryPlayer("send_memory.dat",
                    "receive_memory.dat");

            System.out.println("Initiator ready, sending messages...");

            IntStream.range(0, 10).forEach(x -> {
                String message = "HelloWorld";
                initiator.sendMessage(null, message);
                System.out.println("Sent: " + message);

                while (true) {
                    String response = initiator.getReceivedMessage();

                    if (!response.isEmpty()) {
                        System.out.println("Received: " + response);
                        break;
                    }

                    try { Thread.sleep(500); }
                    catch (InterruptedException e) { e.printStackTrace(); }
                }
            });

            initiator.sendMessage(null, "eof");
            Thread.sleep(500);

            initiator.close();

        } catch (Exception e) { e.printStackTrace(); }
    }
}
