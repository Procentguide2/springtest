package com.example.springtest;

import com.example.springtest.exception.UserExistException;
import com.example.springtest.form.RegisterForm;
import com.example.springtest.model.User;
import com.example.springtest.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationApiTests {


	@Autowired
	private MockMvc mvc;

	@Autowired
	private UserService userService;

	private RegisterForm registerForm;

	@BeforeEach
	void setUp() {
		registerForm = new RegisterForm("testuser","testpass");
	}

	@Test
	void addUserApiMustBeOk() throws Exception {
		mvc.perform( MockMvcRequestBuilders
						.post("/user/add")
						.content(asJsonString(registerForm))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void authenticateUserApiMustBeOk() throws Exception, UserExistException {

		userService.saveUserFromForm(registerForm);

		mvc.perform( MockMvcRequestBuilders
						.post("/user/authenticate")
						.content(asJsonString(registerForm))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void addUserApiMustBeBadRequest() throws Exception, UserExistException {

		userService.saveUserFromForm(registerForm); //user already exists in db

		mvc.perform( MockMvcRequestBuilders
						.post("/user/add")
						.content(asJsonString(registerForm))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void authenticateUserApiMustBeBadRequest() throws Exception {

		mvc.perform( MockMvcRequestBuilders
						.post("/user/authenticate")
						.content(asJsonString(registerForm))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}

	@AfterEach
	void cleanCreatedUser(){
		Optional<User> foundUser = Optional.ofNullable(userService.findUserByUsername(registerForm.getUsername()));
		if (foundUser.isPresent()){
			userService.deleteByUsername(registerForm.getUsername());
		}
	}


	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

}
}