package zarg.scratch.idempotent;

import java.io.Serializable;

/**
 * Wraps the value returned from a method annotated as idempotent. This wrapped
 * result is cached for subsequent invocations to return the same result.
 * 
 * @author john
 */
@SuppressWarnings("serial")
public class IdempotentResponseWrapper implements Serializable {

    private final Object result;

    public IdempotentResponseWrapper(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }
}
