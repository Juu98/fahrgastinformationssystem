package fis.telegrams;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by spiollinux on 07.11.15.
 */
@Service
public class TelegramReceiver {

    private List<byte[]> telegramQueue;
	private int timeout;

    public TelegramReceiver(int timeout) {
        this.telegramQueue = new LinkedList<>();
	    this.timeout = timeout;
    }


	void handleConnection(InputStream in, OutputStream out) throws IOException {
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

    public Telegram sendTelegram(InputStream in, OutputStream out, SendableTelegram telegram) throws IOException {
	    out.write(telegram.getRawTelegram());
	    		Future<byte[]> response = parseConnection(in);
		byte[] rawResponse = null;
		try {
			rawResponse = response.get(timeout, TimeUnit.MILLISECONDS);
		} catch (TimeoutException e) {
			//Todo: raise login error
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
			//Todo: handle
		}
	    return Telegram.parse(rawResponse);
	    //Todo: handle parse error
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
