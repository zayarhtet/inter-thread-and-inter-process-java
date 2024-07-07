package com.zayar.messaging;

import com.zayar.messaging.player.Player;
import com.zayar.messaging.player.SharedMemoryPlayer;

/**
 * The receiver or the server
 * The echo server with the Player using inter-process communication
 */
public class ReceiverPlayer {
    public static void main(String[] args) {

        try {
            Player receiver =
                    new SharedMemoryPlayer("receive_memory.dat",
                    "send_memory.dat");

            System.out.println("Receiver ready, waiting for messages...");

            while (true) {
                String message = receiver.getReceivedMessage();

                if (!message.isEmpty()) {
                    System.out.println("Receiver received: " + message);
                    if (message.equals("eof")) break;
                    receiver.sendMessage(null, message + " " + receiver.getSentMessageCounter());
                }

                Thread.sleep(1000);
            }
            receiver.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
