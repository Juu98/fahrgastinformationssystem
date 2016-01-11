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
		this.rawReferenceTelegram = ByteConversions.fromString(
				"FF FF FF 04 FB 29 01 01"
		);
	}
	
	@Test
	public void GetRawTelegramAndConstructorTestTest(){
		this.telegram = new RegistrationTelegram(this.clientID);
		assertArrayEquals("The getRawTelegram()-Method didn't return the expected value!", this.rawReferenceTelegram, this.telegram.getRawTelegram());
		//System.err.println(telegram);
	}
}
