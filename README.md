Idempotent
==========

The @Idempotent and @IdempotentRequest annotations allow methods to be decorated with an AOP aspect that will ensure that when a method is invoked several times with the same request it retrns the result as the first invocation.

public class ExampleService implements Service {
    private final Logger logger = Logger.getLogger(getClass());

    public long accessor(ExampleRequest request) {
        return System.currentTimeMillis();
    }

    @Idempotent
    public long mutator(@IdempotentRequest ExampleRequest request) {
        return System.currentTimeMillis();
    }
}
