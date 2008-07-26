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
	protected int messageOutCounter = 0;
	protected int messageInCounter = 0;
	protected boolean alive = true;

	RemoteGameController(StreamConnection stream) {
		this.stream = stream;
	}

	public void placeStone(int x, int y, int c) {
	}

	/**
	 * Send the data through connection.
	 *
	 * Append a out counter number and
	 * write to StreamConnection.
	 */
	protected void send(String s) throws IOException {
		DataOutputStream out =
			stream.openDataOutputStream();
		out.writeUTF(s);
	}
	
	/**
	 * Receive from stream.
	 *
	 * Non-blocking function that should
	 * handle incoming acks and commands.
	 */
	protected void receive() throws IOException {
		DataInputStream datain =
			stream.openDataInputStream();
		byte buf[] = new byte[100];
		datain.read(buf);
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
		//send();
	}

	public String name() {
		return "remote";
	}
}
