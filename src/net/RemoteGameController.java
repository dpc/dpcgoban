import javax.microedition.io.StreamConnection;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Remote controller from the point of view
 * of local arbiter.
 */
class RemoteGameController implements GameController {
	protected StreamConnection stream;
	/**
	 * Next command should be sent with that id.
	 */
	protected int messageOutCounter = 0;
	/**
	 * Next command should be received with that id.
	 */
	protected int messageInCounter = 0;
	protected boolean alive = true;

	String receivingBuf = "";

	protected DataInputStream in;
	protected DataOutputStream out;

	protected Arbiter arbiter;

	RemoteGameController(StreamConnection stream) throws IOException {
		this.stream = stream;
		in = stream.openDataInputStream();
		out = stream.openDataOutputStream();
	}

	public void connected(Arbiter arbiter) {
		this.arbiter = arbiter;
	}

	public void placeStone(int x, int y, int c) {
		sendCmd(
				"stone "
				+ String.valueOf(x) + " "
				+ String.valueOf(y) + " "
				+ String.valueOf(c)
				);
	}

	protected void sendCmd(String cmd) {
		try {
			send(String.valueOf(messageOutCounter) + ": " + cmd);
		} catch (IOException e) {
			// XXX: is it OK?
			protocolFailure("couldn't write");
		}
		messageOutCounter++;
	}

	/**
	 * Send the data through connection.
	 *
	 * Append a out counter number and
	 * write to StreamConnection.
	 *
	 * Must be non-blocking!
	 */
	protected void send(String s) throws IOException {
		out.writeUTF(s);
	}
	
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
				handleIncomingCommand(cmd);
			}
		}
	}

	/**
	 * Handle incomming protocol command with id.
	 */
	protected void handleIncomingRawCommand(String cmd) {
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

	protected void handleIncomingCommand(String cmd_str) {
		int first_break = cmd_str.indexOf(' ');
		if (first_break == -1) {
			// without arguments.
			// TODO: implement
			return;
		}
		String cmd = cmd_str.substring(0, first_break);
		if (cmd == "anything") {
			//TODO: implement
		}
		return;
	}

	/**
	 * Protocol failure handling.
	 */
	protected void protocolFailure(String msg) {
		alive = false;
		//TODO: any msg?
	}

	/**
	 * Handle stream connection.
	 *
	 * Should be called by an owner
	 * frequently to allow object
	 * to handle new arriving data for that
	 * connection.
	 */
	public void poll() throws IOException {
		receive();
	}

	public String name() {
		return "remote";
	}
}
