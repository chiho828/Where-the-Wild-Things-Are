package test;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class MyWebSocketHandler {
	final static int interval = 50;
	final static int bufferLength = 2972;
	final static int sampleRate = 16000;
	final StreamDispatcher dispatcher = StreamDispatcher.INSTANCE;	
	
    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
    		System.out.println("Close: statusCode=" + statusCode + ", reason=" + reason);
    }

    @OnWebSocketError
    public void onError(Throwable t) {
    		System.out.println("Error: " + t.getMessage());
    }

    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException, InterruptedException {
	    	System.out.println("Connected to: " + session.getRemoteAddress().getAddress());
	    	System.out.println("Press 'Record' on the web page.");
	    	dispatcher.session = session;
	    	
	    	try {
	    		session.getRemote().sendString("Ready to record.");
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	}
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
    		if (data.equals("STOP")) {
    			System.exit(0);
    		} else {
    			System.out.println(data);
    		}
    }

    @OnWebSocketMessage
    public void onMessage(final byte[] data, int offset, int length) {
		dispatcher.broadcastToListener(data);
    }
}