package au.com.mongodb.cache;

import javax.cache.event.*;
import java.io.Serializable;

public class SimpleCacheEntryListener implements CacheEntryCreatedListener<String, String>,
                                                 CacheEntryUpdatedListener<String, String>,
                                                 CacheEntryRemovedListener<String, String>,
                                                 CacheEntryExpiredListener<String, String>,
                                                 Serializable {

    private boolean created;
    private boolean updated;
    private boolean expired;
    private boolean removed;


    @Override
    public void onCreated(Iterable<CacheEntryEvent<? extends String, ? extends String>> events) throws CacheEntryListenerException {
        this.created = true;
    }

    @Override
    public void onUpdated(Iterable<CacheEntryEvent<? extends String, ? extends String>> events) throws CacheEntryListenerException {
            this.updated = true;
    }


    @Override
    public void onExpired(Iterable<CacheEntryEvent<? extends String, ? extends String>> iterable) throws CacheEntryListenerException {
        this.expired = true;
    }

    @Override
    public void onRemoved(Iterable<CacheEntryEvent<? extends String, ? extends String>> iterable) throws CacheEntryListenerException {
        this.removed = true;
    }
}
