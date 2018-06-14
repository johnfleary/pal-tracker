package io.pivotal.pal.tracker;

import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    private final CounterService counter;
    private final GaugeService gauge;
    private TimeEntryRepository repo = null;

    public TimeEntryController(CounterService counter, GaugeService gauge, TimeEntryRepository timeEntryRepository) {
        this.counter = counter;
        this.gauge = gauge;
        this.repo = timeEntryRepository;
    }

    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry timeEntry = repo.create(timeEntryToCreate);

        counter.increment("TimeEntry.created");
        gauge.submit("timeEntries.count", repo.list().size());

        return new ResponseEntity(timeEntry,HttpStatus.CREATED);
    }
    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") long l) {
        TimeEntry timeEntry = repo.find(l);
        if (timeEntry == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        counter.increment("TimeEntry.read");
        return new ResponseEntity(timeEntry,HttpStatus.OK);
    }
    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        counter.increment("TimeEntry.listed");
        return new ResponseEntity<>(repo.list(),HttpStatus.OK);
    }
    @PutMapping("/time-entries/{id}")
    public ResponseEntity update(@PathVariable("id") long l, @RequestBody TimeEntry expected) {
        TimeEntry timeEntry = repo.update(l,expected);
        if (timeEntry == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        counter.increment("TimeEntry.updated");
        return new ResponseEntity(timeEntry,HttpStatus.OK);
    }
    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity delete(@PathVariable("id") long l) {
        repo.delete(l);
        counter.increment("TimeEntry.deleted");
        gauge.submit("timeEntries.count", repo.list().size());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
