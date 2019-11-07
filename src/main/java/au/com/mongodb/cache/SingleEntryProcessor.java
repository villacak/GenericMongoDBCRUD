package au.com.mongodb.cache;

import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;
import java.io.Serializable;

public class SingleEntryProcessor implements EntryProcessor<String, String, String>, Serializable {

    @Override
    public String process(MutableEntry<String, String> mutableEntry, Object... objects) throws EntryProcessorException {
        String toReturn = null;
        if (mutableEntry.exists()) {
            final String current = mutableEntry.getValue();
            mutableEntry.setValue(current);
            toReturn = current;
        }
        return toReturn;
    }
}
