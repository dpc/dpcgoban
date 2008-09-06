/**
 * Game protocol definitions and constans.
 */
class Protocol {
	// GameController can send:
	static final String MOVE_REQUEST = "move";
	static final String PASS_REQUEST = "pass";
	static final String HANDLE_COLOR = "handle";
	static final String UNHANDLE_COLOR = "unhandle";
	static final String PING = "ping";

	// Arbiter can send
	static final String PLACE_STONE = "stone";
	static final String CLEAR_BOARD = "clear";
	static final String GAME_INFO = "info";
	static final String PONG = "pong";
	// the move was accepted
	static final String MOVE = "move";
	static final String PASS = "pass";
}
