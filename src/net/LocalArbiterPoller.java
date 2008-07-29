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

	protected Vector remoteGameControllers = new Vector();
	protected boolean finished = false;

	interface Parent {
	}

	protected Parent parent;

	/**
	 * Private constructor.
	 *
	 * This is singleton - use only public static methods.
	 */
	private LocalArbiterPoller(Parent parent) {
		this.parent = parent;
		thread = new Thread(this);
	}

	/**
	 * Initialize instance if neccessary.
	 */
	protected static void MakeSureInstanceReady(Parent parent) {
		if (instance == null) {
			instance = new LocalArbiterPoller(parent);
			instance.start();
		}
	}

	/**
	 * Register new GameController in poller instance.
	 */
	public static void RegisterNewRemoteGameController(
			Parent parent,
			RemoteGameController gc
			) {
		MakeSureInstanceReady(parent);
		instance.registerNewRemoteGameController(gc);
	}

	/**
	 * Register new GameController in poller.
	 */
	protected void registerNewRemoteGameController(RemoteGameController gc) {
		synchronized (remoteGameControllers) {
			for (int i = 0; i < remoteGameControllers.size(); ++i) {
				if (remoteGameControllers.elementAt(i) == null) {
					remoteGameControllers.setElementAt(gc, i);
					return;
				}
			}
			remoteGameControllers.addElement(gc);
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
		synchronized (remoteGameControllers) {
			for (int i = 0; i < remoteGameControllers.size(); ++i) {
				if (remoteGameControllers.elementAt(i) != null) {
					RemoteGameController gc = (RemoteGameController) 
						remoteGameControllers.elementAt(i);
					try {
						gc.poll();
					} catch (IOException e) {
						// TODO: some message?
						// TODO: unregister anywere?
						remoteGameControllers.setElementAt(null, i);
					}
				}
			}
		}
	}

}
