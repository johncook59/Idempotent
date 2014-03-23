package zarg.scratch.idempotent;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ExampleRequest implements Serializable {
    private final Integer value;

    public ExampleRequest(Integer value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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
        if (!(obj instanceof ExampleRequest)) {
            return false;
        }
        ExampleRequest other = (ExampleRequest) obj;
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("ExampleRequest [value=%s]", value);
    }
}
