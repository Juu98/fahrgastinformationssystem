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

import java.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit-Tests für die ByteConversions
 * @author Robert
 */
public class ByteConversionsTest {
	@Test
	public void testToUByte(){
		Assert.assertEquals((byte) 0x2A, ByteConversions.toUByte(42));
		Assert.assertEquals((byte) 0xFF, ByteConversions.toUByte(255));
	}
	
	@Test
	public void testToInt(){
		Assert.assertEquals(42, ByteConversions.toInt((byte) 0x2A));
		Assert.assertEquals(-1, ByteConversions.toInt((byte) 0xFF));
	}
	
	@Test
	public void testToIntWordLE(){
		Assert.assertEquals(42, ByteConversions.toInt((byte) 0x2A, (byte) 0, true));
		Assert.assertEquals(-16657, ByteConversions.toInt((byte) 0xEF, (byte) 0xBE, true));
	}
	
	@Test
	public void testToIntWordBE(){
		Assert.assertEquals(42, ByteConversions.toInt((byte) 0, (byte) 0x2A, false));
		Assert.assertEquals(-16657, ByteConversions.toInt((byte) 0xBE, (byte) 0xEF, false));
	}
	
	@Test
	public void testToUInt(){
		Assert.assertEquals(42, ByteConversions.toUInt((byte) 0x2A));
		Assert.assertEquals(255, ByteConversions.toUInt((byte) 0xFF));
	}
	
	@Test
	public void testToUIntWordLE(){
		Assert.assertEquals(42, ByteConversions.toUInt((byte) 0x2A, (byte) 0, true));
		Assert.assertEquals(48879, ByteConversions.toUInt((byte) 0xEF, (byte) 0xBE, true));
	}
	
	@Test
	public void testToUIntWordBE(){
		Assert.assertEquals(42, ByteConversions.toUInt((byte) 0, (byte) 0x2A, false));
		Assert.assertEquals(48879, ByteConversions.toUInt((byte) 0xBE, (byte) 0xEF, false));
	}
	
	@Test
	public void testFromTenthOfMinute() throws TelegramParseException{
		Assert.assertEquals(LocalTime.of(13, 37, 42), ByteConversions.fromTenthOfMinute(7+10*(37+60*13)));
		// Folgetag
		Assert.assertEquals(LocalTime.of(13, 37, 42), ByteConversions.fromTenthOfMinute(20000+7+10*(37+60*13)));
	}
	
	@Test(expected = TelegramParseException.class)
	public void testFromTenthOfMinuteBaseError() throws TelegramParseException{
		ByteConversions.fromTenthOfMinute(-42, null);
	}
	
	@Test
	public void testFromTenthOfMinuteBase() throws TelegramParseException{
		Assert.assertEquals(LocalTime.of(12, 30, 00), ByteConversions.fromTenthOfMinute(300, LocalTime.NOON));
		Assert.assertEquals(LocalTime.of(11, 30, 00), ByteConversions.fromTenthOfMinute(-300, LocalTime.NOON));
	}
	
	@Test
	public void testFromIntArr(){
		Assert.assertArrayEquals(new byte[]{(byte) 0x2A, (byte) 0xFF}, ByteConversions.fromIntArr(new int[]{42, 255}));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testFromStringError(){
		ByteConversions.fromString("ABC");
	}
	
	@Test
	public void testFromString(){
		Assert.assertArrayEquals(new byte[]{(byte) 0x2A, (byte) 0xFF}, ByteConversions.fromString("2AFF"));
		Assert.assertArrayEquals(new byte[]{(byte) 0x2A, (byte) 0xFF}, ByteConversions.fromString("2A FF"));
		Assert.assertArrayEquals(new byte[]{(byte) 0x2A, (byte) 0xFF}, ByteConversions.fromString("0x2AFF"));
		Assert.assertArrayEquals(new byte[]{(byte) 0x2A, (byte) 0xFF}, ByteConversions.fromString("0x\n2A FF"));
	}
	
	@Test
	public void testFromStringArray(){
		Assert.assertArrayEquals(new byte[]{(byte) 0x2A, (byte) 0xFF}, ByteConversions.fromString("2A", "FF"));
	}
	
	@Test
	public void testToByteString(){
		Assert.assertEquals("2A", ByteConversions.toByteString((byte) 42));
	}
	
	@Test
	public void testToByteStringArray(){
		Assert.assertEquals("2A FF", ByteConversions.toByteString(new byte[]{(byte) 42, (byte) 255}));
	}
	
	@Test
	public void testToByteStringArrayLength(){
		Assert.assertEquals("2A FF 00 00", ByteConversions.toByteString(new byte[]{(byte) 42, (byte) 255}, 4));
	}

	//Annahme: fromTenthOfMinutes funktioniert wie gewünscht
	@Test
	public void testToTenthOfMinutes() throws TelegramParseException {
		LocalTime t = LocalTime.of(23, 42, 30);
		Assert.assertEquals("Zehntelminuten micht gleich", t, ByteConversions.fromTenthOfMinute(ByteConversions.toTenthOfMinute(t)));
	}

	@Test
	public void testIsNextDay() {
		LocalTime t1 = LocalTime.of(23,50);
		Assert.assertTrue("Zeit fällt nicht auf nächsten Tag", ByteConversions.isNextDay(1, t1));
		Assert.assertTrue("Zeit fällt nicht auf nächsten Tag", ByteConversions.isNextDay(2, t1));
		Assert.assertFalse("Zeit fällt fälschlicherweise auf nächsten Tag", ByteConversions.isNextDay(0, t1));
	}
}
