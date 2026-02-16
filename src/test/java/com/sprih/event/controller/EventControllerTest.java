package com.sprih.event.controller;

import com.sprih.event.model.Event;
import com.sprih.event.model.EventType;
import com.sprih.event.model.dto.EventRequest;
import com.sprih.event.service.CallbackService;
import com.sprih.event.service.processor.EmailEventProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testValidEventSubmission() throws Exception {

        String payload = """
        {
          "eventType": "EMAIL",
          "payload": { "recipient": "test@test.com" },
          "callbackUrl": "http://localhost"
        }
        """;

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());
    }

    @Test
    void testInvalidEventType() throws Exception {

        String payload = """
        {
          "eventType": "INVALID",
          "payload": {},
          "callbackUrl": "http://localhost"
        }
        """;

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testMissingPayload() throws Exception {

        String payload = """
        {
          "eventType": "EMAIL",
          "callbackUrl": "http://localhost"
        }
        """;

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCallbackTriggered() throws Exception {

        BlockingQueue<Event> queue = new LinkedBlockingQueue<>();
        CallbackService callbackService = mock(CallbackService.class);

        EmailEventProcessor processor =
                new EmailEventProcessor(queue, callbackService);

        Event event = new Event("1", EventType.EMAIL, Map.of(), "http://localhost", Instant.now());

        queue.add(event);

        Thread thread = new Thread(processor);
        thread.start();

        Thread.sleep(6000);
        processor.shutdown();
        thread.join();

        verify(callbackService).sendSuccess(event);
    }
}
