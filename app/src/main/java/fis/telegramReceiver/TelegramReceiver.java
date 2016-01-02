package fis.telegramReceiver;

import fis.telegrams.SendableTelegram;
import fis.telegrams.Telegram;
import fis.telegrams.TelegramParser;
import org.apache.log4j.Logger;
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
	private final static Logger LOGGER = Logger.getLogger(TelegramReceiver.class);
	private final TelegramReceiverConfig receiverConfig;
	//needed for events

	@Autowired
    public TelegramReceiver(TelegramReceiverConfig config) {
		this.receiverConfig = config;
    }

    @Async
    Future<byte[]> parseConnection(InputStream in) throws IOException {
        byte[] response = new byte[Telegram.getRawTelegramMaxLength()];
	    int pos = 0;
	    while (pos < 3) {
		    //read one byte and look whether it is 0xFF, which marks beginning of a new Telegram
		    in.read(response, pos, 1);
		    //throw away invalid data
		    if (response[pos] != TelegramParser.toUByte(0xFF)) {
			    //reset telegram, start again
			    if (pos > 0) {
				    pos = 0;
			    }
			    continue;
		    }
		    pos++;
	    }
	    //read length byte
	    in.read(response, pos, 1);
	    int length = TelegramParser.toUInt(response[pos]);
	    //read telegram data
	    in.read(response, ++pos, length);
	    return new AsyncResult<>(response);
    }

    public void sendTelegram(OutputStream out, SendableTelegram telegram) throws IOException {
	    out.write(telegram.getRawTelegram());
    }

}
