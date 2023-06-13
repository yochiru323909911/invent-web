package hac.springBeans;

import hac.Beans.UserSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;

/**
 * This is the configurations
 */
@Configuration
public class BeanConfiguration {
    @Bean
    @SessionScope
    public UserSession sessionBeanUser () {
        return new UserSession();
    }
}
