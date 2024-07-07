package com.zayar.messaging.player;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Player communication by the Thread.
 * The implementation of the thread-inter communication.
 */
public class ThreadPlayer extends Player {
    private BlockingQueue<String> receivedMessageQueue;

    public ThreadPlayer() {
        super();
        receivedMessageQueue = new ArrayBlockingQueue<>(100);
    }

    @Override
    public void sendMessage(Player p, String message) {
        sentMessageCounter++;
        p.receiveMessage(this, message);
    }

    @Override
    public void receiveMessage(Player p, String message) {
        System.out.println("Player " + id + " receives from Player " + p.id);
        System.out.println(message);
        try { receivedMessageQueue.put(message); }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    @Override
    public String getReceivedMessage() {
        String message;

        try { message = receivedMessageQueue.poll(1000, TimeUnit.MILLISECONDS); }
        catch (InterruptedException e) { message = ""; }

        return message;
    }

    public int getSentMessageCounter() {
        return sentMessageCounter;
    }

    @Override
    public void close() {
        receivedMessageQueue.clear();
    }
}
