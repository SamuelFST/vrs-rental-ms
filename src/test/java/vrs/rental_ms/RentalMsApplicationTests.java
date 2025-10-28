package vrs.rental_ms;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RentalMsApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	public static final ObjectMapper objectMapper = new ObjectMapper();

	public ResultActions doRequest(RequestBuilder requestBuilder) throws Exception {
		return mockMvc.perform(requestBuilder);
	}

	public static <T> T readJsonFileAndConvert(String filePath, Class<T> clazz) throws IOException {
		return objectMapper.readValue(readJsonFile(filePath), clazz);
	}

	private static String readJsonFile(String filePath) throws IOException {
		return StreamUtils.copyToString(new ClassPathResource(filePath).getInputStream(), StandardCharsets.UTF_8);
	}

}
