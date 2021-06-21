package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing(modifyOnCreate = true)//false로하면 업데이트 날짜를 null로 처음에 생성.
@SpringBootApplication
//@EnableJpaRepositories(basePackages = "study.datajpa.repository") //boot가 알아서 넣어주기때문에 선언할 필요가 없다.
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

	@Bean//BaseEntity에서 session에서 가져온 loginId같은 정보를 AuditorAware로 담고 이를 이용하면 BaseEntity에서 createBy, lastModifiedBy에 값으로 사용한다.
	public AuditorAware<String> auditorProvider(){
		return () -> Optional.of(UUID.randomUUID().toString());
	}
}
