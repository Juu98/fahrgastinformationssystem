package fis.telegrams;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

/**
 * @author schmittlauch, Robert
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
		// construct a rawTelegram
		this.rawReferenceTelegram = ByteConversions.fromString(
			"FF FF FF 09 F3 46 49 53 00 00 00 00 00"
		);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void NullConstructorTest() throws UnsupportedEncodingException{
		this.telegram = new ClientStatusTelegram(null, this.status);
	}
	
	@Test
	public void GetRawTelegramAndConstructorTest() throws UnsupportedEncodingException{
		this.telegram = new ClientStatusTelegram(ID, status);
		// compare
		assertArrayEquals("ClientStatusTelegram doesn't have expected value", this.rawReferenceTelegram, this.telegram.getRawTelegram());
	}
}