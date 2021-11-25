package io.github.myifeng;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.myifeng.example.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MybatisOperationLogApplicationTests {

	final MockMvc mvc;
	final ObjectMapper objectMapper;

	@Autowired
	MybatisOperationLogApplicationTests(MockMvc mvc, ObjectMapper objectMapper) {
		this.mvc = mvc;
		this.objectMapper = objectMapper;
	}

	@DisplayName("User Api")
	@Test
	void testUserApi() throws Exception {

		mvc.perform(get("/user"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("length()", equalTo(0)));

		User user = new User();
		String id = UUID.randomUUID().toString();
		user.setId(id);
		String name = "TEST_USER_" + new Random().nextInt(2);
		user.setName(name);
		mvc.perform(post("/user")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", equalTo(id)))
				.andExpect(jsonPath("$.name", equalTo(name)))
				.andDo(result -> {
					mvc.perform(get("/user"))
							.andExpect(status().isOk())
							.andExpect(jsonPath("length()", equalTo(1)))
							.andExpect(jsonPath("$.[0].id", equalTo(id)))
							.andExpect(jsonPath("$.[0].name", equalTo(name)));

					mvc.perform(put("/user/" + id)
									.contentType(MediaType.APPLICATION_JSON_VALUE)
									.content("{\"name\": \"UPDATE_NAME\"}"))
							.andExpect(status().isOk());

					mvc.perform(get("/user"))
							.andExpect(status().isOk())
							.andExpect(jsonPath("length()", equalTo(1)))
							.andExpect(jsonPath("$.[0].id", equalTo(id)))
							.andExpect(jsonPath("$.[0].name", equalTo("UPDATE_NAME")));

					mvc.perform(delete("/user/" + id))
							.andExpect(status().isOk());

					mvc.perform(get("/user"))
							.andExpect(status().isOk())
							.andExpect(jsonPath("length()", equalTo(0)));

				});

	}

}
