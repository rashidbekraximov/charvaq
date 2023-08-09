package uz.cluster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextListener;
import uz.cluster.configuration.OpenApiProperties;


@EnableConfigurationProperties({
        OpenApiProperties.class,
})

@EnableScheduling
@Configuration
@EnableJpaRepositories(value = {"uz.cluster.*"})
@ComponentScan(value = {"uz.cluster.*", "uz.cluster.configuration", "uz.cluster.util"})
@EntityScan(basePackages = {"uz.cluster.entity.forms", "uz.cluster.entity.auth", "uz.cluster.model", "uz.cluster.entity.references.model", "uz.cluster.db.composite_keys", "uz.cluster.services.form_services", "uz.cluster.services"})
@SpringBootApplication(scanBasePackages = {"uz.cluster.util", "uz.cluster.entity.references.model", "uz.cluster.configuration", "uz.cluster.entity.forms"})
public class ClusterApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ClusterApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ClusterApplication.class, args);
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
