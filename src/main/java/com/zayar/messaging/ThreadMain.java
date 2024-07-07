package com.zayar.messaging;

import com.zayar.messaging.player.Player;
import com.zayar.messaging.player.ThreadPlayer;

import java.util.stream.IntStream;

/**
 * The echo server with the Player using inter-thread communication
 */
public class ThreadMain {
    public static void main(String [] args) {
        final Player initiator = new ThreadPlayer();
        final Player receiver = new ThreadPlayer();

        Thread initiatorThread = new Thread(() -> {
            IntStream.range(0, 10).forEach(x -> {
                initiator.sendMessage(receiver, "HelloWorld ");

                try { Thread.sleep(100); } // not necessary to sleep; sleep is for the purpose of console printing.
                catch (InterruptedException e) { e.printStackTrace(); }

                System.out.println();
            });
        });

        Thread receiverThread = new Thread(() -> {
            while (true) {
                String receivedMessage = receiver.getReceivedMessage();
                if (receivedMessage == null) break;
                receiver.sendMessage(initiator, receivedMessage + receiver.getSentMessageCounter());
            }
        });

        initiatorThread.start();
        receiverThread.start();

        try {
            initiatorThread.join();
            receiverThread.join();
        } catch (InterruptedException e) { e.printStackTrace(); }

        initiator.close();
        receiver.close();
    }
}