/**
 * Abstract listener for LocalArbister.
 *
 * Implementations will allow incoming GameControllers'
 * connections.
 */
interface LocalArbiterListener {
	public final static int ANY = 0;
	public final static int BLUETOOTH = 1;

	public void start();
	public void stop();

	/**
	 * Interface to be implemented by owner of
	 * this listener.
	 */
	public interface Parent extends RemoteGameController.Parent {
		/**
		 * Called to notify parent about disconnect exception.
		 *
		 * Problem with connecting will be singalized by this
		 * one too.
		 */
		void handleControllerDisconnected(RemoteGameController c);

		/**
		 * Called after connection was estabilished.
		 */
		void handleControllerTransportConnected(RemoteGameControllerTransport c);

		void handleListenerUp(LocalArbiterListener l);
		void handleListenerDown(LocalArbiterListener l, String s);
		void handleListenerInfo(LocalArbiterListener t, String s);
	}

	/**
	 * Type of LocalArbiterListener.
	 */
	public int type();
}
