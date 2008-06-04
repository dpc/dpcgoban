interface LocalArbiterListener {
	public final static int ANY = 0;
	public final static int BLUETOOTH = 1;

	public void start();
	public void stop();

	/**
	 * Type of LocalArbiterListener.
	 */
	public int type();
}
