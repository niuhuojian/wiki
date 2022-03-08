package demo;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WikiApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    void contextLoads() {
        Environment environment = applicationContext.getBean(Environment.class);
        String mysql = environment.getProperty("spring.datasource.password");
        System.out.println(stringEncryptor.encrypt(mysql));
        System.out.println(stringEncryptor.encrypt("Rootroot1!"));
    }

}
