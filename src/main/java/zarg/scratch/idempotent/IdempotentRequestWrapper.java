package zarg.scratch.idempotent;

import java.io.Serializable;

/**
 * Wraps the request to a method annotated as idempotent. This wrapped request
 * is cached so that subsequent invocations are seen as duplicates and so return
 * the same result.
 * 
 * @author john
 */
@SuppressWarnings("serial")
public class IdempotentRequestWrapper implements Serializable {
    private final Object request;

    public IdempotentRequestWrapper(Object request) {
        this.request = request;
    }

    public Object getRequest() {
        return request;
    }

    @Override
    public int hashCode() {
        return request == null ? 0 : request.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return request == null ? false : request.equals(obj);
    }

    @Override
    public String toString() {
        return String.format("IdempotentRequestWrapper [request=%s]", request);
    }
}
