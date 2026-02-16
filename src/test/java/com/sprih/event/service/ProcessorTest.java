package com.sprih.event.service;


import com.sprih.event.model.Event;
import com.sprih.event.model.EventType;
import com.sprih.event.service.CallbackService;
import com.sprih.event.service.processor.EmailEventProcessor;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ProcessorTest {

    @Test
    void testEmailProcessingDelay() throws Exception {

        BlockingQueue<Event> queue = new LinkedBlockingQueue<>();

        CallbackService callbackService = mock(CallbackService.class);

        EmailEventProcessor processor =
                new EmailEventProcessor(queue, callbackService);

        Event event = new Event(
                "1",
                EventType.EMAIL,
                Map.of(),
                "http://localhost:8080/test-callback",
                Instant.now()
        );

        queue.add(event);

        Thread thread = new Thread(processor);

        long start = System.currentTimeMillis();
        thread.start();

        Thread.sleep(6000); // wait slightly more than 5s
        processor.shutdown();
        thread.join();

        long end = System.currentTimeMillis();

        verify(callbackService, times(1)).sendSuccess(event);

        assertTrue((end - start) >= 5000);
    }

    @Test
    void testRandomFailureSimulation() throws Exception {

        BlockingQueue<Event> queue = new LinkedBlockingQueue<>();

        CallbackService callbackService = mock(CallbackService.class);

        EmailEventProcessor processor =
                spy(new EmailEventProcessor(queue, callbackService));

        // Force failure
        doThrow(new RuntimeException("Simulated processing failure"))
                .when(callbackService)
                .sendSuccess(any());

        Event event = new Event(
                "1",
                EventType.EMAIL,
                Map.of(),
                "http://localhost",
                Instant.now()
        );

        queue.add(event);

        Thread thread = new Thread(processor);
        thread.start();

        Thread.sleep(6000);
        processor.shutdown();
        thread.join();

        verify(callbackService, atLeastOnce()).sendFailure(eq(event), any());
    }

    @Test
    void testGracefulShutdownCompletesRemainingQueue() throws Exception {

        BlockingQueue<Event> queue = new LinkedBlockingQueue<>();
        CallbackService callbackService = mock(CallbackService.class);

        EmailEventProcessor processor =
                new EmailEventProcessor(queue, callbackService);

        Event event1 = new Event("1", EventType.EMAIL, Map.of(), "http://localhost", Instant.now());
        Event event2 = new Event("2", EventType.EMAIL, Map.of(), "http://localhost", Instant.now());

        queue.add(event1);
        queue.add(event2);

        Thread thread = new Thread(processor);
        thread.start();

        Thread.sleep(2000); // let first start
        processor.shutdown();

        thread.join();

        verify(callbackService, times(2)).sendSuccess(any());
    }

}