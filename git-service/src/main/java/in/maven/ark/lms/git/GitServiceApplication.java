package in.maven.ark.lms.git;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "in.maven.ark.lms")
@EnableDiscoveryClient
@EnableFeignClients
public class GitServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GitServiceApplication.class, args);
    }
}
