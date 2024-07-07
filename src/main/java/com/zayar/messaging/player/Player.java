package com.zayar.messaging.player;

/**
 * Player class that can communicate between Players.
 * The type of communication has to be specified with the inheritance.
 *
 */
public abstract class Player {
    private static int idCounter = 0;

    protected int id;
    protected int sentMessageCounter;

    protected Player() {
        id = idCounter++;
        sentMessageCounter = 1;
    }

    public int getSentMessageCounter() {
        return sentMessageCounter;
    }

    /**
     * Take the target player and the message string, send the message string to the target player.
     * @param p the target player
     * @param s message
     */
    public abstract void sendMessage(Player p,String s);

    /**
     * Take the source player and the message string, receive the message string from the source player.
     * @param p the source player
     * @param s message
     */
    public abstract void receiveMessage(Player p, String s);

    /**
     * Retrieve the previously received message.
     * @return received message
     */
    public abstract String getReceivedMessage();

    /**
     * Close any connection that has established or flush the stored messages.
     */
    public abstract void close();
}
