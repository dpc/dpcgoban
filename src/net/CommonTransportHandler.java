import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * Common code for protocol.
 */
abstract class CommonTransportHandler {
	/**
	 * Next command should be sent with that id.
	 */
	private int messageOutCounter = 0;
	/**
	 * Next command should be received with that id.
	 */
	private int messageInCounter = 0;

	private byte[] receivingBuf = new byte[0];

	protected InputStream in;
	protected OutputStream out;

	protected abstract void handleIncomingCommand(String s);
	protected abstract void protocolFailure(String s);

	/**
	 * Receive from stream.
	 *
	 * Non-blocking function that should
	 * handle incoming acks and commands.
	 */
	private boolean receiveOne() throws IOException {
		int available = in.available();
		// XXX: TODO: potential bug
		// utf strings that comes partially may be
		// broken in such a way that
		// String(new_data) will fail
		// screw that for now
		if (available > 0) {
			byte new_data[] = new byte[available];
			in.read(new_data);
			int newBufSize = receivingBuf.length + available;
			byte[] newBuf = new byte[newBufSize];
			for (int i = 0; i < receivingBuf.length; ++i) {
				newBuf[i] = receivingBuf[i];
			}

			for (int i = receivingBuf.length; i < newBufSize; ++i) {
				newBuf[i] = new_data[i - receivingBuf.length];
			}
			receivingBuf = newBuf;
		}
		String bufString = new String(receivingBuf, "UTF8");

		int break_index = bufString.indexOf('\n');
		if (break_index != -1) {
			// cmd without break
			String cmd = bufString.substring(0, break_index);
			// rest of the buf without break
			receivingBuf = bufString.substring(
					break_index + 1, bufString.length()
					).getBytes("UTF8");
					if (!cmd.equals("")) {
				handleIncomingRawCommand(cmd);
				return true;
			}
		}
		return false;
	}

	protected void receive() throws IOException {
		for (int i = 0; i < 10; ++i) {
			if (!receiveOne()) {
				break;
			}
		}
	}

	protected void send(String cmd) {
		try {
			sendRaw(String.valueOf(messageOutCounter) + ": " + cmd + "\n");
		} catch (IOException e) {
			// XXX: is it OK?
			protocolFailure("couldn't write");
		}
		messageOutCounter++;
	}

	/**
	 * Handle incomming protocol command with id.
	 */
	private void handleIncomingRawCommand(String cmd) {
		System.out.println("R:" + cmd);
		int break_index = cmd.indexOf(':');
		if (break_index == -1) {
			protocolFailure("no ':' found");
			return;
		}
		int id = Integer.parseInt(cmd.substring(0, break_index).trim());
		if (id != messageInCounter) {
			protocolFailure(
					"invalid id: "
					+ String.valueOf(id)
					+ "; should be: "
					+ String.valueOf(messageInCounter)
					);
		}
		messageInCounter++;
		handleIncomingCommand(cmd.substring(
					break_index+1, cmd.length()
					).trim());
	}

	/**
	 * Send the data through connection.
	 *
	 * Append a out counter number and
	 * write to StreamConnection.
	 *
	 * Must be non-blocking!
	 */
	private void sendRaw(String s) throws IOException {
		System.out.println("W:" + s);
		out.write(s.getBytes("UTF8"));
		out.flush();
		System.out.println("WEND");
	}

}
