package zarg.scratch.idempotent;

import org.apache.log4j.Logger;

public class ExampleService implements Service {
    private final Logger logger = Logger.getLogger(getClass());

    public long accessor(ExampleRequest request) {
        logger.debug("In accessor()");
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
        }
        return System.currentTimeMillis();
    }

    @Idempotent
    public long mutator(@IdempotentRequest ExampleRequest request) {
        logger.debug("In mutator()");
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
        }
        return System.currentTimeMillis();
    }
}
