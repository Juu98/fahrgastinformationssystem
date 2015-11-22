package fis.telegramReceiver;

public class TelegramReceiver {
	private ConnectionStatus connectionStatus;
	
	public TelegramReceiver(){
		connectionStatus=ConnectionStatus.OFFLINE;
	}
	
	public ConnectionStatus getConnectionStatus(){
		return connectionStatus;
	}
}
