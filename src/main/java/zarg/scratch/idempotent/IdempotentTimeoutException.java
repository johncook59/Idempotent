package zarg.scratch.idempotent;

import org.aspectj.lang.Signature;

/**
 * Thrown when waiting for an idempotent method invocation to return a result,
 * and the wait timeout expires.
 * 
 * @author john
 * 
 */
@SuppressWarnings("serial")
public class IdempotentTimeoutException extends RuntimeException {

    public IdempotentTimeoutException(Signature signature, Object request) {
        super(String.format("Timed-out waiting for an idempotent response %s", request));
    }
}
