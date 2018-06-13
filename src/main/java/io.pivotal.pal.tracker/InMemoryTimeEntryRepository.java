package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Map repo = new HashMap();
    private long counter = 0;

    public TimeEntry create(TimeEntry timeEntry) {

        counter = counter+1;
        timeEntry.setId(counter);
        repo.put(counter, timeEntry);
        return timeEntry;
    }

    public TimeEntry find(long id) {
        return (TimeEntry)this.repo.get(id);
    }

    public List list() {
        return new ArrayList(repo.values());
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        timeEntry.setId(id);
        TimeEntry replacedEntry = (TimeEntry)repo.replace(id, timeEntry);
        if (replacedEntry == null){
            return null;
        }
        return timeEntry;
    }

    public void delete(long id) {
        repo.remove(id);
    }
}
