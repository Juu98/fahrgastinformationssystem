package fis.telegramReceiver;

import fis.common.ConfigurationException;
import fis.telegrams.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.AsyncResult;

import java.io.*;
import java.time.LocalTime;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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
	TelegramParser mockedParser;
	ApplicationEventPublisher mockedPublisher;
	byte[] buf = new byte[Telegram.getRawTelegramMaxLength()];

	@Before
	public void setUp() throws Exception {
		mockedReceiver = mock(TelegramReceiver.class);
		mockedParser = mock(TelegramParser.class);
		realConfig = new TelegramReceiverConfig();
		mockedConfig = spy(realConfig);
		mockedSocket = mock(TelegramSocket.class);
		realReceiverController = new TelegramReceiverController(mockedConfig, mockedSocket,mockedReceiver, mockedParser);
		mockedPublisher = mock(ApplicationEventPublisher.class);

		OutputStream out = new ByteArrayOutputStream();
		InputStream in = new ByteArrayInputStream(buf);


		//stubbbing mockedSocket to return a pre-filled output stream
		doReturn(out).when(mockedSocket).getOutputStream();
		doReturn(in).when(mockedSocket).getInputStream();

		doNothing().when(mockedSocket).connect(any(),anyInt());
		doNothing().when(mockedPublisher).publishEvent(any(TelegramParsedEvent.class));

	}

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


	@Test
	public void testConnecting() throws InterruptedException, IOException, TelegramParseException, ConfigurationException, ExecutionException {
		//setting additional mocks
		byte[] dummy = new byte[255];
		//dummy raw telegram != null, content not important because TelegramParser.parse is mocked
		dummy[0] = (byte) 0xFF;
		TrainRouteTelegram.TrainRouteData mockedTrainRouteData = mock(TrainRouteTelegram.TrainRouteData.class);
		AsyncResult<byte[]> mockedResult = mock(AsyncResult.class);
		when(mockedResult.isDone()).thenReturn(false, true, true, true, true, false);
		when(mockedResult.get()).thenReturn(dummy);
		when(mockedParser.parse(any())).thenReturn(new LabTimeTelegram(LocalTime.now()),
				new StationNameTelegram((byte) 0x01, "FOO", "foobar"),
				new TrainRouteTelegram(mockedTrainRouteData),
				new TrainRouteEndTelegram());
		doNothing().when(mockedReceiver).sendTelegram(any(OutputStream.class), any(RegistrationTelegram.class));
		doReturn(mockedResult).when(mockedReceiver).parseConnection(any(InputStream.class));
		when(mockedSocket.isConnected()).thenReturn(true);
		realConfig.setHostname("localhost");
		realConfig.setPort(42);
		realConfig.setTimeout(23);
		//updating spy
		mockedConfig = spy(realConfig);
		//creating it again because config has changed
		realReceiverController = spy(new TelegramReceiverController(mockedConfig, mockedSocket, mockedReceiver, mockedParser));
		//mocking publisher
		realReceiverController.setApplicationEventPublisher(mockedPublisher);
		realReceiverController.start();
		Thread.sleep(2000);
		assertEquals("TelegramReceiver nicht verbunden",ConnectionStatus.ONLINE, realReceiverController.getConnectionStatus());
		realReceiverController.interrupt();
		while(realReceiverController.isAlive()) {
			Thread.sleep(5);
		}
		verify(realReceiverController).connectToHost();
		verify(mockedSocket).connect(any(), anyInt());
		verify(mockedReceiver, times(2)).sendTelegram(any(),any());

		verify(mockedParser, times(4)).parse(any());
	}

	@After
	public void tearDown() throws Exception {

	}
}
