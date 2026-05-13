package edu.thu.canteen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("edu.thu.canteen.domain.mapper")
public class CanteenApplication {
    public static void main(String[] args) {
        SpringApplication.run(CanteenApplication.class, args);
    }
}
