package fis.telegramReceiver;

import fis.telegrams.SendableTelegram;
import fis.telegrams.Telegram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Future;

/**
 * Created by spiollinux on 07.11.15.
 */
@Service
public class TelegramReceiver {

	private final TelegramReceiverConfig receiverConfig;
	//needed for events

	@Autowired
    public TelegramReceiver(TelegramReceiverConfig config) {
		this.receiverConfig = config;
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

    public void sendTelegram(OutputStream out, SendableTelegram telegram) throws IOException {
	    out.write(telegram.getRawTelegram());
    }

}
