package com.skillsync.user.controller;

import com.skillsync.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // 🔥 MOCK REPOSITORY INSTEAD OF SERVICE
    @MockBean
    private UserRepository userRepository;

    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }
}