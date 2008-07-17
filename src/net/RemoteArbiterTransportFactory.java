/**
 * Remote arbiter factory.
 */
class RemoteArbiterTransportFactory {

	static RemoteArbiterTransport Create(
		RemoteArbiterTransport.Parent parent,
		int type
		)
		throws InvalidArgumentException {
		switch (type) {
			case RemoteArbiterTransport.BLUETOOTH:
					return new RemoteArbiterBluetoothTransport(parent);

			default:
				throw new InvalidArgumentException(
					"invalid/unimplemented connector type"
					);
		}
	}
}
