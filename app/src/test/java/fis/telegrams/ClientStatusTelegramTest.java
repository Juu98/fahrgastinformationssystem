package fis.telegrams;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

/**
 * Created by spiollinux on 06.12.15.
 */
public class ClientStatusTelegramTest {
	private ClientStatusTelegram telegram;
	private String ID;
	private byte status;
	private byte[] rawReferenceTelegram;

	@Before
	public void setup() throws UnsupportedEncodingException{
		this.ID ="FIS";
		this.status = (byte) 0;
		//construct a rawTelegram
		this.rawReferenceTelegram = new byte[Telegram.rawTelegramMaxLength];
		byte[] rawID = this.ID.getBytes("windows-1252");
		int i = 0;
		for(; i < 3; ++i) {
			this.rawReferenceTelegram[i] = (byte) 255;
		}
		//skipping length byte for now
		i++;
		this.rawReferenceTelegram[i++] = (byte) 243;
		for(; i < 12; ++i) {
			int j = i - 5;
			if(j < rawID.length)
				this.rawReferenceTelegram[i] = rawID[j];
			else
				this.rawReferenceTelegram[i] = 0;
		}
		this.rawReferenceTelegram[i] = this.status;
		//set length
		this.rawReferenceTelegram[3] = (byte) (i-4);
	}
	
	@Test
	public void NullConstructorTest() throws UnsupportedEncodingException{
		boolean exceptionCatched = false;
		try{
			this.telegram = new ClientStatusTelegram(null, this.status);
		} catch (NullPointerException e) {
			exceptionCatched = true;
		}
		assertTrue("The constructor should throw a NullPointerException if given a null parameter!", exceptionCatched);
	}
	
	@Test
	public void GetRawTelegramAndConstructorTest() throws NullPointerException, UnsupportedEncodingException{
		this.telegram = new ClientStatusTelegram(ID, status);
		//compare
		assertArrayEquals("ClientStatusTelegram doesn't have expected value", this.rawReferenceTelegram, this.telegram.getRawTelegram());
	}
}