package fis.telegrams;

import fis.ConfigurationException;
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
	TelegramSocket mockedSocket;
	byte[] buf = new byte[255];

	@Before
	public void setUp() throws Exception {
		mockedReceiver = mock(TelegramReceiver.class);
		realConfig = new TelegramReceiverConfig();
		mockedConfig = spy(realConfig);
		mockedSocket = mock(TelegramSocket.class);
		realReceiverController = new TelegramReceiverController(mockedReceiver,mockedConfig, mockedSocket);

		OutputStream out = new ByteArrayOutputStream();
		InputStream in = new ByteArrayInputStream(buf);


		//stubbbing mockedSocket to return a pre-filled output stream
		doReturn(out).when(mockedSocket).getOutputStream();
		doReturn(in).when(mockedSocket).getInputStream();

		doNothing().when(mockedSocket).connect(any(),anyInt());
		doNothing().when(mockedReceiver).handleConnection(any());

	}

	@Test
	public void testConfigNotNull() {
		try {
			realReceiverController.connectToHost();
			fail("telegramserver configuration must have valid values");
		} catch (ConfigurationException e) {
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void testConnecting() throws InterruptedException, IOException {
		realConfig.setHostname("localhost");
		realConfig.setPort(42);
		realConfig.setTimeout(23);
		//updating spy
		mockedConfig = spy(realConfig);
		//creating it again because config has changed
		realReceiverController = new TelegramReceiverController(mockedReceiver,mockedConfig, mockedSocket);
		realReceiverController.start();
		Thread.sleep(1000);
		assertEquals("Receiver not connected",ConnectionStatus.ONLINE, realReceiverController.getConnectionStatus());
		realReceiverController.interrupt();
		while(realReceiverController.isAlive()) {
			Thread.sleep(5);
		}
		verify(mockedSocket).connect(any(),anyInt());
	}

	@After
	public void tearDown() throws Exception {

	}
}
