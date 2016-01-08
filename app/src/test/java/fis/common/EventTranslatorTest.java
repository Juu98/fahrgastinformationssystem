package fis.common;

import fis.data.TimetableEvent;
import fis.data.TimetableEventType;
import fis.telegramReceiver.ConnectionStatus;
import fis.telegramReceiver.ConnectionStatusEvent;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by spiollinux on 08.01.16.
 */
public class EventTranslatorTest {
	private EventTranslator translator;
	ApplicationEventPublisher publisherMock;
	TimetableEvent parse;
	TimetableEvent cleanup;

	@Before
	public void setUp() throws Exception {
		publisherMock = mock(ApplicationEventPublisher.class);
		doNothing().when(publisherMock).publishEvent(any());
		translator = new EventTranslator();
		translator.setApplicationEventPublisher(publisherMock);
		parse = new TimetableEvent(TimetableEventType.parseRailML);
		cleanup = new TimetableEvent(TimetableEventType.cleanup);
	}

	@Test
	public void testTranslateReceiverToTimetable() throws Exception {
		translator.translateReceiverToTimetable(new ConnectionStatusEvent(ConnectionStatus.OFFLINE));
		translator.translateReceiverToTimetable(new ConnectionStatusEvent(ConnectionStatus.OFFLINE));
		verify(publisherMock,times(1)).publishEvent(eq(parse));
		translator.translateReceiverToTimetable(new ConnectionStatusEvent(ConnectionStatus.INIT));
		verify(publisherMock).publishEvent(eq(cleanup));
	}
}