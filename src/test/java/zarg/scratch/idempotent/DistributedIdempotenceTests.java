package zarg.scratch.idempotent;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DistributedIdempotenceTests {

    private ClassPathXmlApplicationContext ctx1;
    private ClassPathXmlApplicationContext ctx2;

    @Before
    public void setUp() throws Exception {
        ctx1 = new ClassPathXmlApplicationContext(
                "classpath*:/zarg/scratch/idempotent/IdempotentAspectTests-context.xml");
        ctx2 = new ClassPathXmlApplicationContext(
                "classpath*:/zarg/scratch/idempotent/IdempotentAspectTests-context.xml");
    }

    @After
    public void teardown() {
        ctx1.close();
        ctx2.close();
    }

    @Test
    public void test() {
        Service instance1 = ctx1.getBean(Service.class);
        Service instance2 = ctx2.getBean(Service.class);
        long result1 = instance1.mutator(new ExampleRequest(1));
        long result2 = instance2.mutator(new ExampleRequest(1));
        assertTrue(result1 == result2);
    }
}
