package es.bytescolab.ms_leagues;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsLeaguesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsLeaguesApplication.class, args);
    }

}
