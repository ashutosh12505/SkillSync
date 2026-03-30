package com.skillsync.session.controller;

import com.skillsync.session.service.SessionService;
import com.skillsync.session.producer.SessionProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // 🔥 REQUIRED
    @MockBean
    private SessionService sessionService;

    // 🔥 REQUIRED
    @MockBean
    private RestTemplate restTemplate;

    // 🔥 THIS FIXES YOUR ERROR
    @MockBean
    private SessionProducer producer;

    @Test
    void testGetSessions() throws Exception {
        mockMvc.perform(get("/sessions"))
                .andExpect(status().isOk());
    }
}