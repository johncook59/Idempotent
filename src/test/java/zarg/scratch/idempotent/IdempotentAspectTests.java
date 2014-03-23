package zarg.scratch.idempotent;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class IdempotentAspectTests {

    @Autowired
    private Service service;

    @Test
    public void testNoAspect() throws Exception {
        long result1 = service.accessor(new ExampleRequest(1));
        long result2 = service.accessor(new ExampleRequest(1));
        assertFalse(result1 == result2); // Not supposed to be idempotent
    }

    @Test
    public void testAspectApplied() throws Exception {
        long result1 = service.mutator(new ExampleRequest(2));
        long result2 = service.mutator(new ExampleRequest(2));

        assertTrue(result1 == result2);
    }

    @Test
    public void testAspectAppliedDifferentRequests() throws Exception {
        long result1 = service.mutator(new ExampleRequest(2));
        long result2 = service.mutator(new ExampleRequest(3));

        assertFalse(result1 == result2);
    }
}
