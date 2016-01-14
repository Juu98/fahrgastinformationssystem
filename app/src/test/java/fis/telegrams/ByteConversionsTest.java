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
}
