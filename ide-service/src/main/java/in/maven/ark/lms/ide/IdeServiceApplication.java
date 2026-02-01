package in.maven.ark.lms.ide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "in.maven.ark.lms")
@EnableDiscoveryClient
@EnableFeignClients
public class IdeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(IdeServiceApplication.class, args);
    }
}
