package io.pivotal.pal.tracker;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO time_entries (project_id, user_id, date, hours) " +
                            "VALUES (?, ?, ?, ?)",
                    RETURN_GENERATED_KEYS
            );

            statement.setLong(1, timeEntry.getProjectId());
            statement.setLong(2, timeEntry.getUserId());
            statement.setDate(3, Date.valueOf(timeEntry.getDate()));
            statement.setInt(4, timeEntry.getHours());

            return statement;
        }, generatedKeyHolder);
        timeEntry.setId(generatedKeyHolder.getKey().longValue());
        return timeEntry;
    }

    @Override
    public TimeEntry find(long id) {
        try {

            TimeEntry timeEntry = jdbcTemplate.queryForObject("select * from time_entries where id = ?",
                    (rs, rowNum) ->
                            new TimeEntry(
                                    rs.getLong("id"),
                                    rs.getLong("project_id"),
                                    rs.getLong("user_id"),
                                    rs.getDate("date").toLocalDate(),
                                    rs.getInt("hours")), id);
            return timeEntry;

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List list() {
        //List<TimeEntry> timeEntries = new ArrayList<>();
        List<TimeEntry> timeEntries = jdbcTemplate.query("select * from time_entries",
                (rs, rowNum) ->
                        new TimeEntry(
                                rs.getLong("id"),
                                rs.getLong("project_id"),
                                rs.getLong("user_id"),
                                rs.getDate("date").toLocalDate(),
                                rs.getInt("hours")));
        return timeEntries;
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        // 	UPDATE [table name] SET Select_priv = 'Y',Insert_priv = 'Y',Update_priv = 'Y' where [field name] = 'user';
        timeEntry.setId(id);

        jdbcTemplate.update("update time_entries set project_id = ?, user_id = ?, date = ?, hours = ? where id = ?",
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                Date.valueOf(timeEntry.getDate()),
                timeEntry.getHours(),
                timeEntry.getId());


        return timeEntry;
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("delete from time_entries where id = ?", id);
    }
}
