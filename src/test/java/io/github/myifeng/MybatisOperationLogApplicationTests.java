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

import static org.hamcrest.Matchers.*;
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
		final String saveName = "INSERT_USER_" + new Random().nextInt(3);
		user.setName(saveName);
		mvc.perform(post("/user")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", notNullValue()))
//				.andExpect(jsonPath("$.createTime", notNullValue()))
//				.andExpect(jsonPath("$.updateTime", nullValue()))
				.andExpect(jsonPath("$.name", equalTo(saveName)))
				.andDo(result -> {
					String content = result.getResponse().getContentAsString();
					User savedUser = objectMapper.readValue(content, User.class);
					String id = savedUser.getId();
					mvc.perform(get("/user"))
							.andExpect(status().isOk())
							.andExpect(jsonPath("length()", equalTo(1)))
							.andExpect(jsonPath("$.[0].id", equalTo(id)))
							.andExpect(jsonPath("$.[0].name", equalTo(saveName)));

					final String updateName = "UPDATE_USER_" + new Random().nextInt(3);
					User update = new User();
					update.setName(updateName);
					mvc.perform(put("/user/" + id)
									.contentType(MediaType.APPLICATION_JSON_VALUE)
									.content(objectMapper.writeValueAsString(update)))
							.andExpect(status().isOk());

					mvc.perform(get("/user"))
							.andExpect(status().isOk())
							.andExpect(jsonPath("length()", equalTo(1)))
							.andExpect(jsonPath("$.[0].id", equalTo(id)))
//							.andExpect(jsonPath("$.[0].updateTime", notNullValue()))
							.andExpect(jsonPath("$.[0].name", equalTo(updateName)));

					mvc.perform(delete("/user/" + id))
							.andExpect(status().isOk());

					mvc.perform(get("/user"))
							.andExpect(status().isOk())
							.andExpect(jsonPath("length()", equalTo(0)));

				});

	}

}
