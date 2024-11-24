package io.github.JRojowski.currency_recruitment;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled(value = "Disabled as I force the default profile to be local, which requires connection to mySQL. This is just to ease checking.")
class CurrencyRecruitmentApplicationTests {

	@Test
	void contextLoads() {
	}

}
