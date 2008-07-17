import javax.microedition.io.StreamConnection;

class RemoteController {
	protected StreamConnection stream;

	RemoteController(StreamConnection stream) {
		this.stream = stream;
	}
}
