/**
 * 
 */
package io.aerodox.desktop.connection;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;

/**
 * @author maeglin89273
 *
 */
public class AsyncResponseChannel implements AutoCloseable {
	private Gson gson;
	private ExecutorService executor;
	private Writer writer;
	public AsyncResponseChannel(Writer writer) {
		this.gson = new Gson();
		this.executor = Executors.newSingleThreadExecutor();
		this.writer = writer;
	}
	
	public void respond(final Object response) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				
				gson.toJson(response, writer);
				try {
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		});
	}
	
	@Override
	public void close() throws IOException {
		writer.close();
		executor.shutdown();
	}
}
