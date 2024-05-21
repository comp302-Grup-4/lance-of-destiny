package network;

import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.stream.Collectors;

public class MessageSender extends HashMap<Message, String> implements Flushable {
	private static final long serialVersionUID = -2967253042072994934L;
	
	private PrintWriter writer;
	
	public MessageSender(OutputStream output) {
		writer = new PrintWriter(output, false);
	}

	@Override
	public void flush() throws IOException {
		String message = this.entrySet().stream()
				                .map(entry -> (entry.getKey() + " " + entry.getValue()))
								.collect(Collectors.joining(";"));
		writer.println(message);
		
		this.clear();
		writer.flush();
	}
	
}
