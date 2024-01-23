package edu.sockets.config;

import com.zaxxer.hikari.HikariDataSource;
import edu.sockets.app.CustomEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:db.properties")
@ComponentScan("edu.sockets")
public class SocketsApplicationConfig {
    @Value("${db.url}")
    private String url;
    @Value("${db.driver.name}")
    private String driverName;
    @Value("${db.user}")
    private String user;
    @Value("${db.password}")
    private String password;
    private final CustomEnvironment environment;

    public SocketsApplicationConfig(CustomEnvironment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource hikariDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setDriverClassName(driverName);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public Integer port(){
        return Integer.parseInt(environment.getPort());
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
