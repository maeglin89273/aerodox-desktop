/**
 * 
 */
package io.aerodox.desktop.connection;

import java.io.IOException;
import java.io.Writer;

/**
 * @author maeglin89273
 *
 */
public class WriterAsyncResponseChannel extends AsyncResponseChannel {
	
	private Writer writer;
	
	public WriterAsyncResponseChannel(Writer writer) {
		this.writer = writer;
	}
	/* (non-Javadoc)
	 * @see io.aerodox.desktop.connection.AsyncResponseChannel#write(java.lang.String)
	 */
	@Override
	protected void write(String jsonLiteral) throws IOException {
		
		writer.write(jsonLiteral);
		writer.flush();
	}
	
	@Override
	public void close() throws IOException {
		super.close();
		writer.close();
	}
}
