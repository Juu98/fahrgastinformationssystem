package fis.telegramReceiver;

import fis.common.ConfigurationException;
import fis.telegrams.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.AsyncResult;

import java.io.*;
import java.time.LocalTime;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * Testet Funktionalität der TelegramReceiverController Klasse
 * @author spiollinux
 */
public class TelegramReceiverControllerTest {
	TelegramReceiverController realReceiverController;
	TelegramReceiver mockedReceiver;
	TelegramReceiverConfig realConfig ;
	TelegramReceiverConfig mockedConfig ;
	TelegramSocket mockedSocket;
	TelegramParser mockedParser;
	ApplicationEventPublisher mockedPublisher;
	byte[] buf = new byte[TelegramPart.RAW_DATA.maxLength()];

	/**
	 * erstellt allgemein benötogte mocks und Objekte
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		//create mocks for collaborators
		mockedReceiver = mock(TelegramReceiver.class);
		mockedParser = mock(TelegramParser.class);
		realConfig = new TelegramReceiverConfig();
		//spy for checking whether methods have been called
		mockedConfig = spy(realConfig);
		mockedSocket = mock(TelegramSocket.class);
		mockedPublisher = mock(ApplicationEventPublisher.class);

		//create IO streams to be returned from socket
		OutputStream out = new ByteArrayOutputStream();
		InputStream in = new ByteArrayInputStream(buf);


		//stubbbing mockedSocket to return a pre-filled output stream
		doReturn(out).when(mockedSocket).getOutputStream();
		doReturn(in).when(mockedSocket).getInputStream();

		//stubbing away to do nothing
		doNothing().when(mockedSocket).connect(any(),anyInt());
		doNothing().when(mockedPublisher).publishEvent(any(ApplicationEvent.class));

		realReceiverController = new TelegramReceiverController(mockedConfig, mockedSocket,mockedReceiver, mockedParser);
		//mocking publisher
		realReceiverController.setApplicationEventPublisher(mockedPublisher);
	}

	/**
	 * Testet, ob null Werte in der Konfiguration zu Fehlern führen
	 */
	@Test
	public void testConfigNotNull() {
		try {
			realReceiverController.connectToHost();
			fail("telegramserver Konfiguration muss gültige Werte haben");
		} catch (ConfigurationException e) {
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Testet, ob die Steuerlogik unter Beibehaltung der Protokollspezifikation zum Zustandekommen einer Verbindung führt
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws TelegramParseException
	 * @throws ConfigurationException
	 * @throws ExecutionException
	 */
	@Test
	public void testConnecting() throws InterruptedException, IOException, TelegramParseException, ConfigurationException, ExecutionException {
		//setting additional mocks
		byte[] dummy = new byte[255];
		//dummy raw telegram != null, content not important because TelegramParser.parse is mocked
		dummy[0] = (byte) 0xFF;
		TrainRouteTelegram.TrainRouteData mockedTrainRouteData = mock(TrainRouteTelegram.TrainRouteData.class);
		AsyncResult<byte[]> mockedResult = mock(AsyncResult.class);
		//receiving first isn't done so the loop runs again, then is done 4x (4 Telegrams get parsed), then not done again
		when(mockedResult.isDone()).thenReturn(false, true, true, true, true, false);
		when(mockedResult.get()).thenReturn(dummy);
		//stub to return 4 Telegrams needed to establish a connection
		when(mockedParser.parse(any())).thenReturn(new LabTimeTelegram(LocalTime.now()),
				new StationNameTelegram((byte) 0x01, "FOO", "foobar", -1f, -1f),
				new TrainRouteTelegram(mockedTrainRouteData),
				new TrainRouteEndTelegram());
		//don't really send a telegram
		doNothing().when(mockedReceiver).sendTelegram(any(OutputStream.class), any(RegistrationTelegram.class));
		doReturn(mockedResult).when(mockedReceiver).parseConnection(any(InputStream.class));
		//socket is always connected
		when(mockedSocket.isConnected()).thenReturn(true);
		//setup test configuration
		realConfig.setHostname("localhost");
		realConfig.setPort(42);
		realConfig.setTimeout(23);
		//updating spy
		mockedConfig = spy(realConfig);
		//creating it again because config has changed
		realReceiverController = spy(new TelegramReceiverController(mockedConfig, mockedSocket, mockedReceiver, mockedParser));
		//mocking publisher
		realReceiverController.setApplicationEventPublisher(mockedPublisher);

		//launch real testing procedure
		realReceiverController.start();
		Thread.sleep(2000);
		//check whether receiver is connected
		assertEquals("TelegramReceiver nicht verbunden",ConnectionStatus.ONLINE, realReceiverController.getConnectionStatus());
		realReceiverController.interrupt();
		while(realReceiverController.isAlive()) {
			Thread.sleep(5);
		}

		//verify that methods have been called as they should've been
		verify(realReceiverController).connectToHost();
		verify(mockedSocket).connect(any(), anyInt());
		verify(mockedReceiver, times(2)).sendTelegram(any(),any());

		verify(mockedParser, times(4)).parse(any());
	}

	@After
	public void tearDown() throws Exception {

	}
}
