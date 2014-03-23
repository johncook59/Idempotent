package zarg.scratch.idempotent;

public interface Service {

    long accessor(ExampleRequest request);

    long mutator(ExampleRequest request);
}
