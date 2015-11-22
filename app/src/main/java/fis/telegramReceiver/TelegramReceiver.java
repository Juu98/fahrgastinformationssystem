package fis.telegramReceiver;

import org.springframework.stereotype.Component;

@Component
public class TelegramReceiver {
	private ConnectionStatus connectionStatus;
	
	public TelegramReceiver(){
		connectionStatus=ConnectionStatus.OFFLINE;
	}
	
	public ConnectionStatus getConnectionStatus(){
		return connectionStatus;
	}
}
