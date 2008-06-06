/**
 * Remote arbiter from local perspective.
 *
 * This class pretends to be arbiter, while just communicating remotely with
 * real LocalArbiter on the other side of the connection.
 */
class RemoteArbiterBluetoothTransport implements RemoteArbiterTransport {
	Parent parent;

	public RemoteArbiterBluetoothTransport(Parent parent) {
		this.parent = parent;
	}

	public void sendMsg(String s) {


	}

	public int type() {
		return BLUETOOTH;
	}
}


