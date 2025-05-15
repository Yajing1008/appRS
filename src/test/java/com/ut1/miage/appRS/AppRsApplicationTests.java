package com.ut1.miage.appRS;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppRsApplicationTests {

	@Test
	void contextLoads() {
	}

	
    @Test
    void testMainMethodRunsWithoutException() {
        assertDoesNotThrow(() -> AppRsApplication.main(new String[]{}));
    }
}
