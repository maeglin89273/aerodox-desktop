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
public abstract class AsyncResponseChannel implements AutoCloseable {
	private Gson gson;
	private ExecutorService executor;
	
	public AsyncResponseChannel() {
		this.gson = new Gson();
		this.executor = Executors.newSingleThreadExecutor();
		
	}
	
	public void respond(final JsonResponse response) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				String jsonLiteral = gson.toJson(response);
				
				try {
					write(jsonLiteral + "\n");
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		});
	}
	
	protected abstract void write(String jsonLiteral) throws IOException;
	
	@Override
	public void close() throws IOException {
		executor.shutdown();
	}
}
