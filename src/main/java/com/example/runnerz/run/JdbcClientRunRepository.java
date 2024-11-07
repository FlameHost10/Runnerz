package com.example.runnerz.run;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class JdbcClientRunRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcClientRunRepository.class);
    private final JdbcClient jdbcClient;

    public JdbcClientRunRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Run> findAll(){
        return jdbcClient.sql("select * from Run")
                .query(Run.class)
                .list();
    }

    public Optional<Run> findById(int id){
        return jdbcClient.sql("select * from Run where id=?")
                .param(id)
                .query(Run.class)
                .optional();
    }

    public void create(Run run){
        var update = jdbcClient.sql("insert into run(id, title, started_on, completed_on, miles, location) values (?,?,?,?,?,?)")
                .params(run.id(), run.title(), run.startedOn(), run.completedOn(), run.miles(), run.location().toString())
                .update();

        Assert.state(update == 1, "Failed to create run " + run.title());
    }

    public void update(Run run, int id){
        var update = jdbcClient.sql("update run set title=?, started_on=?, completed_on=?, miles=?, location=? where id=?")
                .params(run.title(), run.startedOn(), run.completedOn(), run.miles(), run.location().toString(), id)
                .update();

        Assert.state(update == 1, "Failed to update run " + run.title());
    }

    public void deleteById(int id){
        var update = jdbcClient.sql("delete from run where id=?")
                .param(id)
                .update();

        Assert.state(update == 1, "Failed to delete run " + id);
    }


    public int count(){
        return jdbcClient.sql("select * from run")
                .query(Run.class)
                .list()
                .size();
    }

    public void saveAll(List<Run> runs){
        runs.forEach(this::create);
    }

    public List<Run> findByLocation(String location){
        return jdbcClient.sql("select * from run where location=?")
                .param(location)
                .query(Run.class)
                .list();
    }
}