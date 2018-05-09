package test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Scanner;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import edu.cmu.pocketsphinx.Config;
import edu.cmu.pocketsphinx.Decoder;
import edu.cmu.pocketsphinx.Hypothesis;


public class WSServer implements StreamDelegate{
	
	final static int bufferLength = 2972;
	final static int sampleRate = 16000;
	final static int maxSize = Integer.MAX_VALUE;
	
	private NFXStream ns = new NFXStream();

	final StreamDispatcher dispatcher = StreamDispatcher.INSTANCE;
	
	public WSServer() throws Exception {
		
    	dispatcher.setListener(this);

        System.loadLibrary("sphinxbase");
    	System.loadLibrary("pocketsphinx");
        
    	Config c = Decoder.defaultConfig();
        c.setString("-hmm", "en-us");
        c.setString("-lm", "ngrams.lm");
        c.setString("-dict", "words.dic");
        Decoder d = new Decoder(c);
        
        d.startUtt();
        d.setRawdataSize(300000);
		
		Server server = new Server(8080);
		WebSocketHandler wsHandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory factory) {
            		factory.register(MyWebSocketHandler.class);
            		WebSocketPolicy policy = factory.getPolicy();  
            		policy.setMaxBinaryMessageBufferSize(131072);  
            		policy.setMaxBinaryMessageSize(262144);  
            		policy.setMaxTextMessageBufferSize(131072);  
            		policy.setMaxTextMessageSize(262144);  
            }
        };
        
        server.setHandler(wsHandler);
		server.start();
		if (server.isStarted()) {
			System.out.println("Server started.");
		}
		
		int nbytes;
		Hypothesis hyp;
		String str = "";
		int offset = 0;
		
		while (server.isRunning()) {
			byte[] b = new byte[bufferLength];
			while ((nbytes = ns.read(b)) >= 0) {
				ByteBuffer bb = ByteBuffer.wrap(b, 0, nbytes);
				bb.order(ByteOrder.LITTLE_ENDIAN);
				short[] s = new short[nbytes/2];
				bb.asShortBuffer().get(s);
				d.processRaw(s, nbytes/2, false, false);
			}
			
			if ((hyp = d.hyp()) != null) {
				Scanner s = new Scanner(hyp.getHypstr());
				int current = 0;
				while (s.hasNext()) {
					String word = s.next();
					if (current >= offset) str += word+" ";
					current++;
				}
				
				
				if (current > offset) {
					try {
						dispatcher.session.getRemote().sendString(str);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					offset = current;
					str = "";
				}
			}
				
//			if (ns.size() >= maxSize) {
//				ns.clear();
//				d.endUtt();
//				d.startUtt();
////				d.delete() // minidump error
//			}
			
			Thread.sleep(1000);
		}
	}

	@Override
	public void processStream(final byte[] data) {
		ns.add(data);
	}
}
