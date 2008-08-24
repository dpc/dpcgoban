import java.io.*;

/**
 * Transport used to communicate with remote arbiter.
 */
interface RemoteArbiterTransport {
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
		 * Called to notify parent about disconnect exception.
		 *
		 * Problem with connecting will be singalized by this
		 * one too.
		 */
		void handleTransportDisconnected(RemoteArbiterTransport t, String s);

		/**
		 * Called after connection was estabilished.
		 */
		void handleTransportConnected(RemoteArbiterTransport t, String s);

		void handleTransportInfo(RemoteArbiterTransport t, String s);

		boolean shouldSentNewPing();
		void sentNewPing();
		boolean isLastPongValid();
	}

	void sendMsg(String msg);
	int type();

	public void start();
	public void stop();
}
