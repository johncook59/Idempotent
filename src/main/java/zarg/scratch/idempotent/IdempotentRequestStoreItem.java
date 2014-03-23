package zarg.scratch.idempotent;

import java.io.Serializable;

/**
 * A container for idempotent requests and responses that are cached to ensure
 * the duplicate invocations with the same requests return the same data.
 * 
 * @author john
 */
@SuppressWarnings("serial")
public class IdempotentRequestStoreItem implements Serializable {
    private final IdempotentRequestWrapper request;
    private IdempotentResponseWrapper response = null;

    public IdempotentRequestStoreItem(IdempotentRequestWrapper request) {
        this.request = request;
    }

    public IdempotentResponseWrapper getResponse() {
        return response;
    }

    public void setResponse(IdempotentResponseWrapper response) {
        synchronized (this) {
            this.response = response;
        }
    }

    public IdempotentRequestWrapper getRequest() {
        return request;
    }

    @Override
    public String toString() {
        return String.format("IdempotentRequestStoreItem [request=%s, response=%s]", request, response);
    }
}
