package in.maven.ark.lms.auth;

import in.maven.ark.lms.common.entity.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "in.maven.ark.lms")
@EnableDiscoveryClient
@EntityScan(basePackageClasses = {User.class})
@EnableJpaRepositories(basePackages = "in.maven.ark.lms.auth.repository")
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
