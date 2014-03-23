package zarg.scratch.idempotent;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Add to methods that need to be idempotent.
 * 
 * @author john
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {
}