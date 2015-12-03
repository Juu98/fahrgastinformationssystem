package fis.telegrams;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by spiollinux on 07.11.15.
 */
@Service
public class TelegramReceiver {

    private List<byte[]> telegramQueue;

    public TelegramReceiver() {
        this.telegramQueue = new LinkedList<>();
    }


	void handleConnection(InputStream in) throws IOException {
		Future<byte[]> currentTelegram = parseConnection(in);
		while (!currentTelegram.isDone()) {
			if(Thread.currentThread().isInterrupted())
				return;
			//Parse received telegrams while waiting for next
			if (!telegramQueue.isEmpty()) {
				parseTelegram(telegramQueue.get(0));
				telegramQueue.remove(0);
			}
			else
				Thread.yield();
		}
		try {
			telegramQueue.add(currentTelegram.get());
		} catch (InterruptedException e) {
			//Todo: handle
			e.printStackTrace();
		} catch (ExecutionException e) {
			//Todo: handle
			e.printStackTrace();
		}
	}

    @Async
    Future<byte[]> parseConnection(InputStream in) throws IOException {
        int readPos = 0, maxResponseLength = 255;
        byte[] response = new byte[maxResponseLength];
        while (readPos < maxResponseLength) {
            int responseLength = in.read(response, readPos, maxResponseLength - readPos);
            readPos += responseLength;
        }
        //packet read to response
        parseTelegram(response);
        return new AsyncResult<>(response);
    }

    private void parseTelegram(byte[] response) {
        //Todo: add real parser logic
        //Todo: response is 0000000... if connection ended
        for (int i = 0; i < 3; ++i) {
            if (response[i] != (byte) 0xFF) {
                throw (new RuntimeException("Byte " + i + " hat falsches Format: " + response[i]));
            }
        }
        int messageLength = response[3];
        for (int i = 4; i < 3 + messageLength; ++i) {
            if (i == 4) {
                //Typangabe
            }
            System.out.println("Byte " + i + ": " + response[5]);
        }
    }
}
