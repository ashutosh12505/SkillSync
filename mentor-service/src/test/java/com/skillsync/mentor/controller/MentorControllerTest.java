package com.skillsync.mentor.controller;

import com.skillsync.mentor.service.MentorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MentorController.class)
class MentorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // 🔥 ADD THIS
    @MockBean
    private MentorService mentorService;

    // 🔥 AND THIS (since you use it)
    @MockBean
    private RestTemplate restTemplate;

    @Test
    void testGetMentors() throws Exception {
        mockMvc.perform(get("/mentors"))
                .andExpect(status().isOk());
    }
}