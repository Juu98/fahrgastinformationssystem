package fis.telegrams;

import org.springframework.stereotype.Component;

import java.net.Socket;

/**
 * Just a java.net.Socket marked with @Component to make autowiring work
 */
@Component
public class TelegramSocket extends Socket {
}
