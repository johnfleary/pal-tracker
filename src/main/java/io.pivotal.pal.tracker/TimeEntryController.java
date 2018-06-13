package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    private TimeEntryRepository repo = null;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.repo = timeEntryRepository;
    }
    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry timeEntry = repo.create(timeEntryToCreate);
        return new ResponseEntity(timeEntry,HttpStatus.CREATED);
    }
    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") long l) {
        TimeEntry timeEntry = repo.find(l);
        if (timeEntry == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(timeEntry,HttpStatus.OK);
    }
    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        List list = repo.list();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
    @PutMapping("/time-entries/{id}")
    public ResponseEntity update(@PathVariable("id") long l, @RequestBody TimeEntry expected) {
        TimeEntry timeEntry = repo.update(l,expected);
        if (timeEntry == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(timeEntry,HttpStatus.OK);
    }
    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity delete(@PathVariable("id") long l) {
        repo.delete(l);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
