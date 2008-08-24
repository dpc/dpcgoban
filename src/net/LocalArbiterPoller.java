import java.util.Vector;
import java.io.IOException;

/**
 * Singleton class for LocalArbiterListener poller.
 *
 * This class is singleton-thread that will record
 * all the new connection and handle them in loop
 * to let them read incoming data.
 *
 * LocalArbiter instance is the owner/parent of instances of this
 * class.
 */
class LocalArbiterPoller
	implements Runnable {

	protected Thread thread;

	protected Vector remoteGameControllerTransports = new Vector();
	protected boolean finished = false;

	/**
	 * Private constructor.
	 *
	 * This is singleton - use only public static methods.
	 */
	public LocalArbiterPoller() {
		thread = new Thread(this);
	}

	/**
	 * Register new GameController in poller.
	 */
	public void registerNewRemoteGameControllerTransport(
			RemoteGameControllerTransport gct
			) {
		synchronized (remoteGameControllerTransports) {
			for (int i = 0; i < remoteGameControllerTransports.size(); ++i) {
				if (remoteGameControllerTransports.elementAt(i) == null) {
					remoteGameControllerTransports.setElementAt(gct, i);
					return;
				}
			}
			remoteGameControllerTransports.addElement(gct);
		}
	}

	/**
	 * Start poller thread.
	 */
	public void start() {
		finished = false;
		thread.start();
	}

	/**
	 * Stop poller thread.
	 */
	public void stop() {
		try {
			finished = true;
			thread.join();
		} catch (InterruptedException ie) {}
	}

	/**
	 * Thread loop.
	 */
	public void run() {
		while (!finished) {
			loopThroghGameControllers();
		}
	}

	/**
	 * Poll all the registered objects.
	 */
	protected void loopThroghGameControllers() {
		synchronized (remoteGameControllerTransports) {
			for (int i = 0; i < remoteGameControllerTransports.size(); ++i) {
				if (remoteGameControllerTransports.elementAt(i) != null) {
					RemoteGameControllerTransport gc =
						(RemoteGameControllerTransport)
						remoteGameControllerTransports.elementAt(i);
					try {
						gc.poll();
					} catch (IOException e) {
						// TODO: unregister anywere?
						remoteGameControllerTransports.setElementAt(null, i);
					}
				}
			}
		}
	}

}
