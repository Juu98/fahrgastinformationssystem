/* Copyright 2016 Eric Schölzel, Robert Mörseburg, Zdravko Yanakiev, Jonas Schenke, Oliver Schmidt
 *
 * This file is part of FIS.
 *
 * FIS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FIS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FIS.  If not, see <http://www.gnu.org/licenses/>.
 */
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
