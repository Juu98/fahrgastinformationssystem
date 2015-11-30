package fis.telegrams;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.*;
import java.net.Socket;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by spiollinux on 22.11.15.
 */
public class TelegramReceiverControllerTest {
	TelegramReceiverController realReceiverController;
	TelegramReceiver mockedReceiver;
	TelegramReceiverConfig realConfig ;
	TelegramReceiverConfig mockedConfig ;
	Socket mockedSocket;
	byte[] buf = new byte[255];

	@Before
	public void setUp() throws Exception {
		mockedReceiver = mock(TelegramReceiver.class);
		realConfig = new TelegramReceiverConfig();
		mockedConfig = spy(realConfig);
		realReceiverController = new TelegramReceiverController();
		ReflectionTestUtils.setField(realReceiverController,"receiverConfig",mockedConfig);
		mockedSocket = mock(Socket.class);
		ReflectionTestUtils.setField(realReceiverController,"server",mockedSocket);

		OutputStream out = new ByteArrayOutputStream();
		InputStream in = new ByteArrayInputStream(buf);


		//stubbbing mockedSocket to return a pre-filled output stream
		doReturn(out).when(mockedSocket).getOutputStream();
		doReturn(in).when(mockedSocket).getInputStream();

		doNothing().when(mockedSocket).connect(any(),anyInt());
		doNothing().when(mockedReceiver).handleConnection(any());

	}

	@Test
	public void configNotNull() {

	}


	@Test
	public void connecting() throws InterruptedException, IOException {
		realConfig.setHostname("localhost");
		realConfig.setPort(42);
		realConfig.setTimeout(23);
		//updating spy
		mockedConfig = spy(realConfig);
		ReflectionTestUtils.setField(realReceiverController, "receiverConfig", mockedConfig);
		ReflectionTestUtils.setField(realReceiverController, "receiver", mockedReceiver);
		realReceiverController.start();
		Thread.sleep(1000);
		realReceiverController.interrupt();
		while(realReceiverController.isAlive()) {
			Thread.sleep(5);
			System.out.println(realReceiverController.isInterrupted());
		}
		verify(mockedSocket).connect(any(),anyInt());
		assertEquals("Receiver not connected",ConnectionStatus.ONLINE, realReceiverController.getConnectionStatus());
	}

	@After
	public void tearDown() throws Exception {

	}
}
