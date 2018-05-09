package test;

import org.eclipse.jetty.websocket.api.Session;

interface StreamDelegate {
	void processStream(final byte[] data);
}

public class StreamDispatcher {

	public StreamDelegate listener;
	public int key;
	public static Session session;
	
	public final static StreamDispatcher INSTANCE = new StreamDispatcher();
	private StreamDispatcher() {
	         // Exists only to defeat instantiation.
	}

	public void setListener(StreamDelegate delegate) {

		listener = delegate;

	}
	
	public void setKey(int newKey) {
		key = newKey;
	}

	public void broadcastToListener(final byte[] data) {
		if(listener != null) {
			listener.processStream(data);
		}
	}
}
