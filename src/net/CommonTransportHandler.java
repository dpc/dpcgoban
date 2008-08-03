import java.io.DataOutputStream;
import java.io.DataInputStream;
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

	private String receivingBuf = "";

	protected DataInputStream in;
	protected DataOutputStream out;

	protected abstract void handleIncomingCommand(String s);
	protected abstract void protocolFailure(String s);

	/**
	 * Receive from stream.
	 *
	 * Non-blocking function that should
	 * handle incoming acks and commands.
	 */
	protected void receive() throws IOException {
		int available = in.available();
		// XXX: TODO: potential bug
		// utf strings that comes partially may be
		// broken in such a way that
		// String(new_data) will fail
		// screw that for now
		if (available > 0) {
			byte new_data[] = new byte[available];
			in.read(new_data);
			receivingBuf = receivingBuf + new String(new_data);
		}
		int break_index = receivingBuf.indexOf('\n');
		if (break_index != -1) {
			// cmd without break
			String cmd = receivingBuf.substring(0, break_index);
			// rest of the buf without break
			receivingBuf = receivingBuf.substring(
					break_index + 1, receivingBuf.length()
					);

			if (cmd != "") {
				handleIncomingRawCommand(cmd);
			}
		}
	}

	protected void send(String cmd) {
		try {
			sendRaw(String.valueOf(messageOutCounter) + ": " + cmd);
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
		int break_index = cmd.indexOf(':');
		if (break_index == -1) {
			protocolFailure("no ':' found");
			return;
		}
		int id = Integer.parseInt(cmd.substring(0, break_index));
		if (id != messageInCounter) {
			protocolFailure(
					"invalid id: "
					+ String.valueOf(id)
					+ "; should be: "
					+ String.valueOf(messageInCounter)
					);
		}
		messageInCounter++;
		handleIncomingCommand(cmd.substring(break_index, cmd.length()));
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
		out.writeUTF(s);
	}

}
