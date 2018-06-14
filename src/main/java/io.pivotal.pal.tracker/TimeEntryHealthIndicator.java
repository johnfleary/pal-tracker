package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TimeEntryHealthIndicator implements HealthIndicator {

    @Autowired
    private JdbcTimeEntryRepository jdbcTimeEntryRepository;

    @Override
    public Health health() {

        if (jdbcTimeEntryRepository.list().size() < 5) {
           return Health.up().build();
        }
        return Health.down().build();
    }
}
