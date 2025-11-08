package com.seashell.kafka_consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DbTestRunner implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("DB 連線成功！: " + conn.getMetaData().getURL());
        } catch (Exception e) {
            System.err.println("DB 連線失敗: " + e.getMessage());
        }
    }
}
