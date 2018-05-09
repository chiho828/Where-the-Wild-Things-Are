package test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class NFXStream extends InputStream {
	private ArrayList<Byte> buffers;
    private int pos; 	// position to read buffers
    private int count;	// length of buffers
    
    public NFXStream() {
    		buffers = new ArrayList<Byte>();
    		pos = 0;
    		count = 0;
    }
    
    public byte[] getBuffers() {
    		byte[] ret = new byte[count];
    		for (int i = 0; i < count; i++) {
    			ret[i] = buffers.get(i);
    		}
    		return ret;
    }
    
    public int getPosition() {
    		return this.pos;
    }
    
    public synchronized void setPosition(int pos) {
    		this.pos = pos;
    }
    
    public int size() {
    		return count;
    }
    
	public synchronized void add(byte[] buffer) {
		for (int i = 0; i < buffer.length; i++) {
			buffers.add(buffer[i]);
		}
		count += buffer.length;
	}
        
	public int available() throws IOException {
        return count - pos;
	}

	 public synchronized int read() {
		 return (pos < count) ? (buffers.get(pos++) & 0xff) : -1;
	 }

	 public int read(byte b[], int off, int len) throws IOException {
		 if (b == null) {
			 throw new NullPointerException();
		 } else if (off < 0 || len < 0 || len > b.length - off) {
			 throw new IndexOutOfBoundsException();
		 } else if (len == 0) {
			 return 0;
		 }

		 int c = read();
		 if (c == -1) {
			 return -1;
		 }
		 b[off] = (byte)c;

		 int i = 1;
		 for (; i < len ; i++) {
			 c = read();
			 if (c == -1) {
				 break;
			 }
			 b[off + i] = (byte)c;
		 }
		 return i;
	 }
	 
	 public synchronized void clear() {
		 buffers.clear();
		 pos = 0;
		 count = 0;
	 }
}
