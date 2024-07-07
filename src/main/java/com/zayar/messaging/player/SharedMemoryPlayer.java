package com.zayar.messaging.player;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.stream.IntStream;

/**
 * Player communication by the Shared Memory File. The implementation of the inter-process communication.
 */
public class SharedMemoryPlayer extends Player {
    private static final int MESSAGE_SIZE = 256;
    private static final int MESSAGE_DATA_INDEX = 4;
    private static final int SHARED_MEMORY_SIZE = MESSAGE_DATA_INDEX + MESSAGE_SIZE;

    private MappedByteBuffer sendBuffer;
    private MappedByteBuffer receiveBuffer;

    private File sendFile;
    private File receiveFile;

    public SharedMemoryPlayer(String sendFileName, String receiveFileName) {
        String tempDir = System.getProperty("java.io.tmpdir");
        sendFile = new File(tempDir, sendFileName);
        receiveFile = new File(tempDir, receiveFileName);
        try {
            RandomAccessFile sendFile = new RandomAccessFile(this.sendFile, "rw");
            RandomAccessFile receiveFile = new RandomAccessFile(this.receiveFile, "rw");

            FileChannel sendChannel = sendFile.getChannel();
            FileChannel receiveChannel = receiveFile.getChannel();

            sendBuffer = sendChannel.map(FileChannel.MapMode.READ_WRITE, 0, SHARED_MEMORY_SIZE);
            receiveBuffer = receiveChannel.map(FileChannel.MapMode.READ_WRITE, 0, SHARED_MEMORY_SIZE);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public void sendMessage(Player p, String s) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        if (bytes.length > MESSAGE_SIZE) {
            throw new IllegalArgumentException("Message too long");
        }
        writeMessage(sendBuffer, bytes);
        sentMessageCounter++;
    }

    @Override
    public void receiveMessage(Player p, String s) { }

    @Override
    public String getReceivedMessage() {
        return readMessage(receiveBuffer);
    }

    @Override
    public void close() {
//        deleteFile(sendFile);
//        deleteFile(receiveFile);
    }

    private void deleteFile(File file) {
        if (file != null && file.exists()) {
            if (!file.delete()) {
                System.err.println("Failed to delete file: " + file.getAbsolutePath());
            }
        }
    }

    /**
     * Write message to the shared memory buffer
     * @param buffer buffer to be written
     * @param bytes the message in byte array
     */
    private void writeMessage(MappedByteBuffer buffer, byte[] bytes) {
        buffer.position(0);
        buffer.putInt(1);
        buffer.put(bytes);

        IntStream.range(bytes.length, MESSAGE_SIZE).forEach(x -> buffer.put((byte) 0));
    }

    /**
     * Read message from the shared memory buffer
     * @param buffer buffer to be written
     * @return the value read from the buffer
     */
    private String readMessage(MappedByteBuffer buffer) {
        buffer.position(0);
        int messageCount = buffer.getInt();
        if (messageCount == 0) return "";

        byte[] bytes = new byte[MESSAGE_SIZE];
        buffer.get(bytes);
        String message = new String(bytes, StandardCharsets.UTF_8).trim();

        // Clear the message after reading
        buffer.position(0);
        buffer.putInt(0);
        IntStream.range(0, MESSAGE_SIZE).forEach(x -> buffer.put((byte) 0));

        return message;
    }
}
