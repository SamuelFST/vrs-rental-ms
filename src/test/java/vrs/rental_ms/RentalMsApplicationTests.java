package vrs.rental_ms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.StreamUtils;
import vrs.rental_ms.repository.RentalRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RentalMsApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	public MongoTemplate mongoTemplate;

	@MockitoBean
	public RentalRepository rentalRepository;

	public static final ObjectMapper objectMapper = startObjectMapper();

	@Test
	void contextLoadsWithSuccess(ApplicationContext context) {
		assertNotNull(context);
	}

	public ResultActions doRequest(RequestBuilder requestBuilder) throws Exception {
		return mockMvc.perform(requestBuilder);
	}

	public static <T> T readJsonFileAndConvert(String filePath, Class<T> clazz) throws IOException {
		return objectMapper.readValue(readJsonFile(filePath), clazz);
	}

	private static String readJsonFile(String filePath) throws IOException {
		return StreamUtils.copyToString(new ClassPathResource(filePath).getInputStream(), StandardCharsets.UTF_8);
	}

	private static ObjectMapper startObjectMapper() {
		var objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		return objectMapper;
	}

}
