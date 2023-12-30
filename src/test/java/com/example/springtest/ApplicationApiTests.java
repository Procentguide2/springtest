package com.example.springtest;

import com.example.springtest.dto.ProductDto;
import com.example.springtest.exception.UserExistException;
import com.example.springtest.form.RecordsForm;
import com.example.springtest.form.RegisterForm;
import com.example.springtest.jwt.JwtTokenUtil;
import com.example.springtest.model.User;
import com.example.springtest.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationApiTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsService jwtDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private RegisterForm registerForm;
    private RecordsForm recordsForm;
    private ProductDto productDto1, productDto2;
    private String invalidToken;

    @BeforeEach
    void setUp() {
        registerForm = new RegisterForm("testuser", "testpass");
        productDto1 = new ProductDto(LocalDate.now(), 1111, "test 1", 20, "paid");
        productDto2 = new ProductDto(LocalDate.now(), 1111, "test 2", 20, "paid");

        recordsForm = new RecordsForm(Arrays.asList(productDto1, productDto2));

        invalidToken = "eyJhbGciOiJIUzUxMiJ9.eyJzY3AiOiJST0xFX0FVVEgiLCJzdWIiOiJ0ZXN0IiwiZXhwIjoxNzAzODAxNjEyLCJpYXQiOjE3MDM3ODM2MTJ9.QErM6KM6YLZOp97RGl36sIf9fUcSOeOwq6TmIFGK9VrlmZNs39fgz-CsdwTm9K3pbmqLQmdXK9xCtS6uIhdndQ";
    }

    @Test
    void addUserApiMustBeOk() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .post("/user/add")
                        .content(asJsonString(registerForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void authenticateUserApiMustBeOk() throws Exception, UserExistException {

        userService.saveUserFromForm(registerForm);

        mvc.perform(MockMvcRequestBuilders
                        .post("/user/authenticate")
                        .content(asJsonString(registerForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void addUserApiMustBeBadRequest() throws Exception, UserExistException {

        userService.saveUserFromForm(registerForm); //user already exists in db

        mvc.perform(MockMvcRequestBuilders
                        .post("/user/add")
                        .content(asJsonString(registerForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void authenticateUserApiMustBeBadRequest() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                        .post("/user/authenticate")
                        .content(asJsonString(registerForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    void saveRecordsMustBeOK() throws Exception, UserExistException {

        userService.saveUserFromForm(registerForm);

        final UserDetails userDetails = jwtDetailsService
                .loadUserByUsername(registerForm.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        mvc.perform(MockMvcRequestBuilders
                        .post("/products/add")
                        .content(asJsonString(recordsForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void saveRecordsWithInvalidTokenMustBeForbidden() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                        .post("/products/add")
                        .content(asJsonString(recordsForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + invalidToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void getProductsMustBeOK() throws UserExistException, Exception {

        userService.saveUserFromForm(registerForm);

        final UserDetails userDetails = jwtDetailsService
                .loadUserByUsername(registerForm.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        mvc.perform(MockMvcRequestBuilders
                        .get("/products/all")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getProductsWithInvalidTokenMustBeForbidden() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                        .get("/products/all")
                        .header("Authorization", "Bearer " + invalidToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @AfterEach
    void cleanCreatedUser() {
        Optional<User> foundUser = Optional.ofNullable(userService.findUserByUsername(registerForm.getUsername()));
        if (foundUser.isPresent()) {
            userService.deleteByUsername(registerForm.getUsername());
        }
    }


    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}