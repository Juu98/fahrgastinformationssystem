package fis.telegrams;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class RegistrationTelegramTest {
	private RegistrationTelegram telegram;
	private byte clientID;
	private byte tEBL;
	private byte ZugStatus;
	private byte[] rawReferenceTelegram;
	@Before
	public void setup(){
		this.clientID = (byte) 41;
		this.tEBL = (byte) 1;
		this.ZugStatus = (byte) 1;
		this.rawReferenceTelegram = new byte[Telegram.rawTelegramMaxLength];
		int i = 0;
		for(; i < 3; i++){
			this.rawReferenceTelegram[i] = (byte) 255;
		}
		//skip length byte for now
		i++;
		this.rawReferenceTelegram[i++] = (byte) 251;
		this.rawReferenceTelegram[i++] = this.clientID;
		this.rawReferenceTelegram[i++] = this.tEBL;
		this.rawReferenceTelegram[i] = this.ZugStatus;
		this.rawReferenceTelegram[3] = (byte) (i-3);
	}
	
	@Test
	public void GetRawTelegramAndConstructorTestTest(){
		this.telegram = new RegistrationTelegram(this.clientID);
		assertArrayEquals("The getRawTelegram()-Method didn't return the expected value!", this.telegram.getRawTelegram(), this.rawReferenceTelegram);
		//System.err.println(telegram);
	}
}
