class Protocol {
	// GameController can send:
	static final String MOVE_REQUEST = "move";
	static final String HANDLE_COLOR = "handle";
	static final String UNHANDLE_COLOR = "unhandle";

	// Arbiter can send
	static final String PLACE_STONE = "stone";
	static final String CLEAR_BOARD = "clear";
	static final String GAME_INFO = "info";
}
