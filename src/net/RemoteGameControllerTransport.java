import java.io.IOException;

/**
 * Transport used to communicate with remote arbiter.
 */
interface RemoteGameControllerTransport {
	static final int BLUETOOTH = 1;

	/**
	 * Interface to be implemented by owner of
	 * this transport.
	 */
	public interface Parent {

		/**
		 * Called on each incoming message.
		 */
		void receiveMsg(String s);

		/**
		 * Called on creation of transport to let know who is his child.
		 */
		void registerChildTransport(RemoteGameControllerTransport t);

		/**
		 * Called to notify parent about disconnect exception.
		 *
		 * Problem with connecting will be singalized by this
		 * one too.
		 */
		void handleTransportDisconnected(
				RemoteGameControllerTransport t, String s
				);

		/**
		 * Called after connection was estabilished.
		 */
		void handleTransportConnected(
				RemoteGameControllerTransport t, String s
				);

		void handleTransportInfo(RemoteGameControllerTransport t, String s);
	}

	void sendMsg(String msg);
	void poll() throws IOException;
	int type();
}
