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
