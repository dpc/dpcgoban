import java.lang.Exception;
import java.io.IOException;
import javax.bluetooth.BluetoothStateException;

import javax.bluetooth.LocalDevice;
import javax.bluetooth.DiscoveryAgent;

import javax.microedition.io.StreamConnectionNotifier;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

class LocalArbiterBluetoothListener implements Runnable, LocalArbiterListener {

	private boolean closed = false;

	StreamConnectionNotifier notifier;
	LocalArbiter parent;

	Thread thread = null;

	public LocalArbiterBluetoothListener(LocalArbiter parent) {
		this.parent = parent;

		thread = new Thread(this);
	}

	public void start() {
		thread.start();
		closed = false;
	}

	public void stop() {
		closed = true;
		try {
			thread.join();
		} catch (InterruptedException ie) {}
	}

	public void initListener()
	throws IOException
	{
		LocalDevice local = null;

		try {
			local = LocalDevice.getLocalDevice();

			if (!local.setDiscoverable(DiscoveryAgent.GIAC)) {
				throw new IOException("couldn't setDiscoverable");
			}

			notifier = (StreamConnectionNotifier) Connector.open(
					"btspp://localhost:" + CommonBluetooth.MAGIC_UUID
					);

		} catch (BluetoothStateException e) {
			closed = true;
		} catch (IOException e) {
		}
		parent.handleListenerUp();
	}

	public int type() {
		return LocalArbiterListener.BLUETOOTH;
	}

	public void run() {
		try {
			parent.handleListenerInfo(
					this,
					"starting up server socket..."
					);
			initListener();
			parent.handleListenerUp(this);
			while (!closed) {
				StreamConnection conn = notifier.acceptAndOpen();
				parent.handleControllerConnected(new RemoteController(conn));
			}
		} catch (IOException e) {
			closed = true;
			parent.handleListenerDown(
					this,
					e.getMessage()
					);
			return;
		}
		parent.handleListenerDown(
				this,
				"finished normally"
				);
	}
}
