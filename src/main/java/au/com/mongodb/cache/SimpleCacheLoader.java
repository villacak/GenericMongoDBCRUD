package au.com.mongodb.cache;

import javax.cache.integration.CacheLoader;
import javax.cache.integration.CacheLoaderException;
import java.util.HashMap;
import java.util.Map;

public class SimpleCacheLoader implements CacheLoader<String, String> {

    /**
     *
     * @param key
     * @return
     * @throws CacheLoaderException
     */
    @Override
    public String load(final String key) throws CacheLoaderException {
        return null;
    }

    /**
     * Load all data into the cache
     *
     * @param keys
     * @return
     * @throws CacheLoaderException
     */
    @Override
    public Map<String, String> loadAll(Iterable<? extends String> keys) throws CacheLoaderException {
        final Map<String, String> data = new HashMap<>();
        for (String key : keys) {
            data.put(key, load(key));
        }
        return data;
    }
}
