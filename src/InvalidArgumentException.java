import java.lang.Exception;

/**
 * Custom exception used here and there in dpcgoban.
 *
 * Thrown when the callee things there's something wrong
 * with arguments.
 */
class InvalidArgumentException extends Exception {
	public InvalidArgumentException(String s) {
		super(s);
	}
}
