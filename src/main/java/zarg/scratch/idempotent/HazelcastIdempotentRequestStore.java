package zarg.scratch.idempotent;

import java.util.Map;

import com.hazelcast.core.HazelcastInstance;

/**
 * An implementation of the idempotent request store that uses a distributed
 * hash map from Hazelcast to hold requests. This map will automatically evict
 * entries according the Hazelcast configuration.
 * 
 * @author john
 */
public class HazelcastIdempotentRequestStore extends AbstractIdempotentRequestStore {

    public static final String DEFAULT_NAME = "idempotentRequestStore";
    private String name = DEFAULT_NAME;
    private HazelcastInstance hz;

    @Override
    protected Map<Key, IdempotentRequestStoreItem> getRequestMap() {
        return hz.getMap(name);
    }

    public void setHazelcast(HazelcastInstance hz) {
        this.hz = hz;
    }

    public void setName(String name) {
        this.name = name;
    }
}
