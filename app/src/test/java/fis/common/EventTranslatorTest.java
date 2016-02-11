/* Copyright 2016 Erik Schölzel, Robert Mörseburg, Zdravko Yanakiev, Jonas Schenke, Oliver Schmidt
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
package fis.common;

import fis.data.TimetableEvent;
import fis.data.TimetableEventType;
import fis.telegramReceiver.ConnectionStatus;
import fis.telegramReceiver.ConnectionStatusEvent;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationEventPublisher;
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
