package zarg.scratch.idempotent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Add to a method argument that represents the idempotent request. The
 * annotated object must implement hashCode() and equals() that compare by
 * value, so that two separate instances can be compared by value. The annotated
 * class must also implement Serializable or Hazelcast's DataSerializable if a
 * distributed cache is being used.
 * 
 * @author john
 * 
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface IdempotentRequest {
}