import java.util.Vector;
import java.io.IOException;

/**
 * Singleton class for LocalArbiterListener poller.
 *
 * This class is singleton-thread that will record
 * all the new connection and handle them in loop
 * to let them read incoming data.
 */
class LocalArbiterPoller
	implements Runnable {

	static private LocalArbiterPoller instance;
	protected Thread thread;

	protected Vector remoteGameControllerTransports = new Vector();
	protected boolean finished = false;

	/**
	 * Private constructor.
	 *
	 * This is singleton - use only public static methods.
	 */
	private LocalArbiterPoller() {
		thread = new Thread(this);
	}

	/**
	 * Initialize instance if neccessary.
	 */
	protected static void MakeSureInstanceReady() {
		if (instance == null) {
			instance = new LocalArbiterPoller();
			instance.start();
		}
	}

	/**
	 * Register new GameController in poller instance.
	 */
	public static void RegisterNewRemoteGameControllerTransport(
			RemoteGameControllerTransport gct
			) {
		MakeSureInstanceReady();
		instance.registerNewRemoteGameControllerTransport(gct);
	}

	/**
	 * Register new GameController in poller.
	 */
	protected void registerNewRemoteGameControllerTransport(
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
	protected void start() {
		finished = false;
		thread.start();
	}

	/**
	 * Stop poller thread.
	 */
	protected void stop() {
		try {
			finished = true;
			thread.join();
		} catch (InterruptedException ie) {}
	}

	/**
	 * Stop poller instance.
	 *
	 * (and mark it for deletion)
	 */
	public static void Stop() {
		if (instance != null) {
			instance.stop();
			instance = null;
		}
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
