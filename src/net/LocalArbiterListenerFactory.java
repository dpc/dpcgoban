
/**
 * Factory for creating LocalArbiterListeners.
 */
class LocalArbiterListenerFactory {
	public static LocalArbiterListener Create(LocalArbiter parent, int type)
	throws InvalidArgumentException {
		switch (type) {
			case LocalArbiterListener.ANY:
			case LocalArbiterListener.BLUETOOTH:
				/* try { */
					return new LocalArbiterBluetoothListener(parent);
				/*} catch (LocalArbiterBluetoothListener.CreationException e) {
					throw new InvalidArgumentException(
						"couldn't create BluetoothListener: `"
						+ e.getMessage() + "'"
						);
				}*/
			default:
				throw new InvalidArgumentException("unsupported/invalid LocalArbiterListener type");
		}
	}
}
