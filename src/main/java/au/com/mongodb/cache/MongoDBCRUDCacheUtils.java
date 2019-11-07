package au.com.mongodb.cache;

import au.com.mongodb.constants.Constant;
import au.com.mongodb.utils.ReadProperties;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MongoDBCRUDCacheUtils {


    private Cache<String, String> cacheToReturn;
    private String cacheName;


    /**
     * Constructor that set the cache name default or other cache via cache name
     *
     * @param cacheName
     */
    public MongoDBCRUDCacheUtils(final String cacheName) {
        if (cacheName == null
                || cacheName.length() == 0
                || cacheName.equalsIgnoreCase(Constant.CACHE_NAME_DEFAULT)
                || cacheName.equalsIgnoreCase(Constant.DEFAULT)) {
            this.cacheName = Constant.CACHE_NAME_DEFAULT;
        } else {
            this.cacheName = cacheName;
        }

        setCacheByName();
    }

    /**
     * Return the CacheManager
     *
     * @return
     */
    private CacheManager getCacheManager() {
        final CachingProvider cp = Caching.getCachingProvider();
        final CacheManager cm = cp.getCacheManager();
        return cm;
    }

    /**
     * Return the cache from the name or the default if cacheName is null
     *
     * @return
     */
    private void setCacheByName() {
        final CacheManager cm = getCacheManager();
        cacheToReturn = cm.getCache(cacheName);
    }


    /**
     * Return the cache
     *
     * @return
     */
    public Cache<String, String> getCache() {
        return cacheToReturn;
    }


    /**
     * Return the cached value from a key if exist, if not returns null
     *
     * @param key
     * @return
     */
    public String getCachedValueFromKey(String key) {
        String valueToReturn = null;
        if (key != null && key.length() > 0) {
            valueToReturn = cacheToReturn.get(key);
        }
        return valueToReturn;
    }


    /**
     * Return all keys from cache
     *
     * @return
     */
    public Set<String> getAllCachedKeys() {
        final Set<String> keysToReturn = new HashSet<>();
        for (Iterator<Cache.Entry<String, String>> it = cacheToReturn.iterator(); it.hasNext(); ) {
            final Cache.Entry<String, String> tempentry = it.next();
            keysToReturn.add(tempentry.getKey());
        }
        return keysToReturn;
    }

    
    /**
     * Load all properties into a cache
     */
    public void loadBasicCache() {
        final CacheManager cm = getCacheManager();
        final SimpleCacheLoader simpleCacheLoader = new SimpleCacheLoader();
        final FactoryBuilder.SingletonFactory singletonFactory = new FactoryBuilder.SingletonFactory(simpleCacheLoader);
        final MutableConfiguration<String, String> config = new MutableConfiguration<String, String>()
                .setReadThrough(true)
                .setCacheLoaderFactory(singletonFactory);
        final Cache<String, String> cache = cm.createCache(Constant.CACHE_NAME_DEFAULT, config);

        final ReadProperties rp = new ReadProperties();
        final Map<String, String> propertiesMap = rp.getAllPropertiesMap();
        final Set<String> keys = propertiesMap.keySet();
        for(String key: keys) {
            final String value = propertiesMap.get(key);
            cache.put(key, value);
        }
        cm.close();
    }
}
