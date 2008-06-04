import java.lang.Exception;
import java.io.IOException;
import javax.bluetooth.BluetoothStateException;

import javax.bluetooth.LocalDevice;
import javax.bluetooth.DiscoveryAgent;

import javax.microedition.io.StreamConnectionNotifier;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

class LocalArbiterBluetoothListener implements Runnable, LocalArbiterListener {
	public class CreationException extends Exception {
		public CreationException(String s) {
			super("could not create LocalArbiterBluetoothListener: '" + s + "'");
		}
	}

	private final String MAGIC_UUID = new String("26b4d249fb8844d5a756fc265a11f7a3");

	private boolean closed = false;

	StreamConnectionNotifier notifier;
	LocalArbiter parent;

	Thread thread = null;

	public LocalArbiterBluetoothListener(LocalArbiter parent) throws CreationException {
		this.parent = parent;
		LocalDevice local = null;

		try {
			local = LocalDevice.getLocalDevice();

			if (!local.setDiscoverable(DiscoveryAgent.GIAC)) {
				throw new CreationException("couldn't setDiscoverable");
			}

			notifier =
				(StreamConnectionNotifier) Connector.open("btspp://localhost:"
					+ MAGIC_UUID);

		} catch (BluetoothStateException e) {
			throw new CreationException("Bluetooth Error : `" + e.getMessage() + "'");
		} catch (IOException e) {
			throw new CreationException("IOError Error : `" + e.getMessage() + "'");
		}

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

	public int type() {
		return LocalArbiterListener.BLUETOOTH;
	}

	public void run() {
		try {
			while (!closed) {
				StreamConnection conn = notifier.acceptAndOpen();
			}
		} catch (IOException e) {
			//XXX:TODO: ?
		}
	}
}
