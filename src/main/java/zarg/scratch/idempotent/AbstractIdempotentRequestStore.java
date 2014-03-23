package zarg.scratch.idempotent;

import java.io.Serializable;
import java.util.Map;

import org.aspectj.lang.Signature;
import org.springframework.util.Assert;

/**
 * Implements all the methods of IdempotentRequestStore. Derived classes must
 * implement the getRequestMap() method to return a suitable implementation of
 * Map<Key, IdempotentRequestStoreItem>.
 * 
 * @author john
 */
public abstract class AbstractIdempotentRequestStore implements IdempotentRequestStore {

    @SuppressWarnings("serial")
    public static class Key implements Serializable {
        final String signature;
        final IdempotentRequestWrapper request;

        public Key(Signature signature, IdempotentRequestWrapper request) {
            Assert.notNull(request);
            Assert.notNull(signature);
            this.signature = signature.toLongString();
            this.request = request;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime + request.hashCode();
            result = prime * result + signature.hashCode();
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof Key)) {
                return false;
            }
            Key other = (Key) obj;
            if (request == null) {
                if (other.request != null) {
                    return false;
                }
            } else if (!request.equals(other.request)) {
                return false;
            }
            if (signature == null) {
                if (other.signature != null) {
                    return false;
                }
            } else if (!signature.equals(other.signature)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return String.format("Key [signature=%s, request=%s]", signature, request);
        }
    }

    @Override
    public boolean contains(Signature signature, IdempotentRequestWrapper request) {
        Key key = new Key(signature, request);
        return getRequestMap().containsKey(key);
    }

    @Override
    public IdempotentResponseWrapper getResponse(Signature signature, IdempotentRequestWrapper request) {
        Key key = new Key(signature, request);
        return getRequestMap().containsKey(key) ? getRequestMap().get(key).getResponse() : null;
    }

    @Override
    public void store(Signature signature, IdempotentRequestWrapper request) {
        Key key = new Key(signature, request);
        getRequestMap().put(key, new IdempotentRequestStoreItem(request));
    }

    @Override
    public void setResponse(Signature signature, IdempotentRequestWrapper request,
            IdempotentResponseWrapper idempotentResponse) {
        Key key = new Key(signature, request);
        if (getRequestMap().containsKey(key)) {
            IdempotentRequestStoreItem item = getRequestMap().get(key);
            item.setResponse(idempotentResponse);
            getRequestMap().put(key, item);
        }
    }

    protected abstract Map<Key, IdempotentRequestStoreItem> getRequestMap();
}
