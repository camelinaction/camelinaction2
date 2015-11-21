package camelinaction;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class DatabaseBean {

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setupDatabase() throws Exception {
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);

        jdbc.execute("create table partner_metric "
                + "( partner_id varchar(10), time_occurred varchar(20), status_code varchar(3), perf_time varchar(10) )");
    }

    public void dropDatabase() throws Exception {
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        jdbc.execute("drop table partner_metric");
    }

}
