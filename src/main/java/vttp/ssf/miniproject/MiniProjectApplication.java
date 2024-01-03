package vttp.ssf.miniproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;

import vttp.ssf.miniproject.repository.UserRepository;

@SpringBootApplication
public class MiniProjectApplication implements CommandLineRunner {

	@Autowired
	UserRepository userRepo;

	@Autowired@Qualifier("myredis")
    private RedisTemplate<String,String>template;

	public static void main(String[] args) {
		SpringApplication.run(MiniProjectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("\nApplication started running...");
	}

}
