package zarg.scratch.idempotent;

import org.aspectj.lang.Signature;

/**
 * Describes the functionality required of a request store for idempotent method
 * invocations.
 * 
 * @author john
 * 
 */
public interface IdempotentRequestStore {

    /**
     * Checks the cache for an existing call for this request
     * 
     * @param signature
     *            The method called with this request
     * @param request
     * @return True if the method has already been called with this request
     */
    boolean contains(Signature signature, IdempotentRequestWrapper request);

    /**
     * Retrieves the response for the associated request. May return null if the
     * first operation has not yet completed.
     * 
     * @param signature
     *            The method called with this request
     * @param request
     * @return The result of the first operation to complete, or null if no
     *         result is yet available.
     */
    IdempotentResponseWrapper getResponse(Signature signature, IdempotentRequestWrapper request);

    /**
     * Stores the request for first call for operation on this request
     * 
     * @param signature
     *            The method called with this request
     * @param request
     */
    void store(Signature signature, IdempotentRequestWrapper request);

    /**
     * Stores the response to be returned for all subsequent calls to this
     * operation with this request
     * 
     * @param signature
     *            The method called with this request
     * @param request
     * @param idempotentResponse
     *            The result of the call to the advised method.
     */
    void setResponse(Signature signature, IdempotentRequestWrapper request, IdempotentResponseWrapper idempotentResponse);
}
