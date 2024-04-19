package tdd.tp.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tdd.tp.exception.ISBNFormatException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ISBNServiceTests {
	static ISBNService isbnService;

	@BeforeAll
	static void contextLoads() {
		isbnService = ISBNService.getSession();
	}

	/* Section ISBN 10 */
	@Test
	void isbn10Valid() {
		try {
			assertFalse(isbnService.ValidatorISBN("2080421204"));
		} catch (ISBNFormatException e) {
			fail();
		}
	}

	@Test
	void isbn10Invalid() {
		try {
			assertFalse(isbnService.ValidatorISBN("2080421203"));
		} catch (ISBNFormatException e) {
			fail();
		}
	}

	@Test
	void isbn10CharNumberInvalid() {
		assertThrows(ISBNFormatException.class, () -> isbnService.ValidatorISBN("20804212"));
		assertThrows(ISBNFormatException.class, () -> isbnService.ValidatorISBN("20804212456"));
	}

	@Test
	void isbn10CharNumberValid() {
		try {
			assertInstanceOf(Boolean.class, isbnService.ValidatorISBN("123456789X"));
		} catch (ISBNFormatException e) {
			fail();
		}
	}

	@Test
	void isbn10CharContentValid() {
		try {
			assertInstanceOf(Boolean.class, isbnService.ValidatorISBN("2080421204"));
		} catch (ISBNFormatException e) {
			fail();
		}
	}

	@Test
	void isbn10CharContentValidWithX() {
		try {
			assertInstanceOf(Boolean.class, isbnService.ValidatorISBN("208042120X"));
		} catch (ISBNFormatException e) {
			fail(e.getMessage());
		}
	}

	@Test
	void isbn10CharContentInvalid() {
		assertThrows(ISBNFormatException.class, () -> isbnService.ValidatorISBN("208A421205"));
	}

	/* Section ISBN 13 */
	@Test
	void isbn13Valid() {
		try {
			assertFalse(isbnService.ValidatorISBN("9782369021704"));
		} catch (ISBNFormatException e) {
			fail();
		}
	}

	@Test
	void isbn13Invalid() {
		try {
			assertFalse(isbnService.ValidatorISBN("9782369021705"));
		} catch (ISBNFormatException e) {
			fail();
		}
	}

	@Test
	void isbn13CharNumberInvalid() {
		assertThrows(ISBNFormatException.class, () -> isbnService.ValidatorISBN("978236902170"));
		assertThrows(ISBNFormatException.class, () -> isbnService.ValidatorISBN("97823690217048"));
	}

	@Test
	void isbn13CharNumberValid() {
		try {
			assertInstanceOf(Boolean.class, isbnService.ValidatorISBN("9782369021704"));
		} catch (ISBNFormatException e) {
			fail();
		}
	}

	@Test
	void isbn13CharContentValid() {
		try {
			assertInstanceOf(Boolean.class, isbnService.ValidatorISBN("9782369021704"));
		} catch (ISBNFormatException e) {
			fail();
		}
	}

	@Test
	void isbn13CharContentInvalid() {
		assertThrows(ISBNFormatException.class, () -> isbnService.ValidatorISBN("978236901170X"));
	}
}
