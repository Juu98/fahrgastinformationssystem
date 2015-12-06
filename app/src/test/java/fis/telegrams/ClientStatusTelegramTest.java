package fis.telegrams;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by spiollinux on 06.12.15.
 */
public class ClientStatusTelegramTest {

	@Test
	public void testGetRawTelegram() throws Exception {
		String ID ="FIS";
		byte status = (byte) 1;
		ClientStatusTelegram tele = new ClientStatusTelegram(ID, status);
		//construct a rawTelegram
		byte[] rawReferenceTelegram = new byte[Telegram.rawTelegramLength];
		byte[] rawID = ID.getBytes("windows-1252");
		int i = 0;
		for(; i < 3; ++i) {
			rawReferenceTelegram[i] = (byte) 255;
		}
		//skipping length byte for now
		i++;
		rawReferenceTelegram[i++] = (byte) 243;
		for(; i < 12; ++i) {
			int j = i - 5;
			if(j < rawID.length)
				rawReferenceTelegram[i] = rawID[j];
			else
				rawReferenceTelegram[i] = 0;
		}
		rawReferenceTelegram[i] = status;
		//set length
		rawReferenceTelegram[3] = (byte) i;

		//compare
		assertArrayEquals("ClientStatusTelegram doesn't have expected value", rawReferenceTelegram, tele.getRawTelegram());
	}
}