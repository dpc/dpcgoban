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
	}

	void sendMsg(String msg);
	int type();
}
