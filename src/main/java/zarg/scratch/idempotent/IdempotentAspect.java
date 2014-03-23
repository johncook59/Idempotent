package zarg.scratch.idempotent;

import java.lang.annotation.Annotation;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * An aspect, used along with the IdempotentRequest annotation, that assures
 * idempotent semantics on suitably annotated methods.
 * 
 * @author john
 * 
 */
@Aspect
public class IdempotentAspect {
    private final Logger logger = Logger.getLogger(getClass());

    private static final long DEFAULT_MAX_WAIT = 60 * 1000;
    private static final long DEFAULT_SLEEP_TIME = 1 * 1000;

    private long maxWait = DEFAULT_MAX_WAIT;
    private long sleepTime = DEFAULT_SLEEP_TIME;

    private IdempotentRequestStore idempotentRequestStore;

    /**
     * Around advice to ensure that a method is called only once and will always
     * return the same result for all subsequent calls
     * 
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("@annotation(zarg.scratch.idempotent.Idempotent)")
    public Object executeIdempotentCall(ProceedingJoinPoint pjp) throws Throwable {
        logger.debug("Starting idempotent checks...");

        Signature signature = pjp.getSignature();
        IdempotentRequestWrapper request = findIdempotentRequestArg(pjp);

        if (request != null) {
            if (idempotentRequestStore.contains(signature, request)) {
                // This is not the first call so just return the original result
                return retrieveResponse(signature, request);
            } else {
                logger.debug("Storing first request " + request);
                idempotentRequestStore.store(signature, request);
            }
        }

        Object result = pjp.proceed();

        if (request != null) {
            logger.debug("Caching response for request " + request);
            idempotentRequestStore.setResponse(signature, request, new IdempotentResponseWrapper(result));
        }

        logger.debug("Returning result");
        return result;
    }

    private Object retrieveResponse(Signature signature, IdempotentRequestWrapper request) {
        long startTime = System.currentTimeMillis();
        while (true) {
            IdempotentResponseWrapper response = idempotentRequestStore.getResponse(signature, request);
            if (response != null) {
                logger.debug("Returning a cached response for request " + request);
                return response.getResult();
            } else {
                if ((System.currentTimeMillis() - startTime) > maxWait) {
                    throw new IdempotentTimeoutException(signature, request);
                }

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    return null;
                }
            }
        }
    }

    private IdempotentRequestWrapper findIdempotentRequestArg(ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        if (args.length == 0) {
            return new IdempotentRequestWrapper(null);
        } else {
            try {
                MethodSignature signature = (MethodSignature) pjp.getSignature();
                String methodName = signature.getMethod().getName();
                Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
                Annotation[][] annotations = pjp.getTarget().getClass().getMethod(methodName, parameterTypes)
                        .getParameterAnnotations();
                for (int i = 0; i < args.length; i++) {
                    for (Annotation annotation : annotations[i]) {
                        if (annotation instanceof IdempotentRequest) {
                            return new IdempotentRequestWrapper(args[i]);
                        }
                    }
                }
            } catch (NoSuchMethodException | SecurityException e) {
                throw new IllegalStateException("Idempotent method not found", e);
            }
        }
        return null;
    }

    /**
     * Sets the time, in milliseconds, to wait for a previous call to a method
     * to return a result. Defaults to 60 seconds.
     * 
     * @param maxWait
     */
    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }

    /**
     * Sets the time, in milliseconds, to sleep between each check to see if a
     * result is ready. Defaults to 1 second.
     * 
     * @param sleepTime
     */
    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    /**
     * Sets the cache implementation that holds the requests and responses for
     * idempotent calls.
     * 
     * @param idempotentRequestStore
     */
    public void setIdempotentRequestStore(IdempotentRequestStore idempotentRequestStore) {
        this.idempotentRequestStore = idempotentRequestStore;
    }
}
