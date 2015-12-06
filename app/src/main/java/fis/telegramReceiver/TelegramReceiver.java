package fis.telegramReceiver;

import fis.telegrams.SendableTelegram;
import fis.telegrams.Telegram;
import fis.telegrams.TelegramParseException;
import fis.telegrams.TelegramParsedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

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
public class TelegramReceiver implements ApplicationEventPublisherAware {

    private List<byte[]> telegramQueue;
	private final TelegramReceiverConfig receiverConfig;
	//needed for events
	private ApplicationEventPublisher publisher;

	@Autowired
    public TelegramReceiver(TelegramReceiverConfig config) {
        this.telegramQueue = new LinkedList<>();
		this.receiverConfig = config;
    }


	void handleConnection(InputStream in, OutputStream out) throws IOException {
		Future<byte[]> currentTelegram = parseConnection(in);
		while (!currentTelegram.isDone()) {
			if(Thread.currentThread().isInterrupted())
				return;
			//Parse received telegrams while waiting for next
			if (!telegramQueue.isEmpty()) {
				try {
					Telegram telegramResponse = Telegram.parse(telegramQueue.get(0));
					publisher.publishEvent(new TelegramParsedEvent(telegramResponse));
				} catch (TelegramParseException e) {
					//Todo: log parse error
				}
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
        int readPos = 0;
        byte[] response = new byte[Telegram.getRawTelegramLength()];
        while (readPos < Telegram.getRawTelegramLength()) {
            int responseLength = in.read(response, readPos, Telegram.getRawTelegramLength() - readPos);
            readPos += responseLength;
        }
        //packet read to response
        return new AsyncResult<>(response);
    }

    public Telegram sendTelegram(InputStream in, OutputStream out, SendableTelegram telegram) throws IOException, TelegramParseException {
	    out.write(telegram.getRawTelegram());
	    		Future<byte[]> response = parseConnection(in);
		byte[] rawResponse = null;
		try {
			rawResponse = response.get(receiverConfig.getTimeout(), TimeUnit.MILLISECONDS);
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

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}
}
