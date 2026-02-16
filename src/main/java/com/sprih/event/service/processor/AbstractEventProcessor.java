package com.sprih.event.service.processor;

import com.sprih.event.model.Event;
import com.sprih.event.model.EventType;
import com.sprih.event.service.CallbackService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public abstract class AbstractEventProcessor implements Runnable {

    protected final BlockingQueue<Event> queue;
    protected final CallbackService callbackService;

    protected volatile boolean running = true;
    protected final Random random = new Random();

    protected AbstractEventProcessor(
            BlockingQueue<Event> queue,
            CallbackService callbackService
    ) {
        this.queue = queue;
        this.callbackService = callbackService;
    }

    protected abstract int processingTimeSeconds();
    protected abstract EventType eventType();

    @Override
    public void run() {
        while (running || !queue.isEmpty()) {
            try {
                Event event = queue.poll(1, TimeUnit.SECONDS);
                if (event == null) continue;

                processEvent(event);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void processEvent(Event event) {
        try {
            // Simulate processing delay
            Thread.sleep(processingTimeSeconds() * 1000L);

            // Simulate 10% random failure
            if (random.nextInt(10) == 0) {
                throw new RuntimeException("Simulated processing failure");
            }

            // Success callback
            callbackService.sendSuccess(event);

        } catch (Exception ex) {

            // Failure callback
            callbackService.sendFailure(event, ex.getMessage());
        }
    }


    public void shutdown() {
        running = false;
    }
}
