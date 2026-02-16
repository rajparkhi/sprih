package com.sprih.event.service.processor;

import com.sprih.event.model.Event;
import com.sprih.event.model.EventType;
import com.sprih.event.service.CallbackService;

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

                Thread.sleep(processingTimeSeconds() * 1000L);

                if (random.nextInt(10) == 0) {
                    throw new RuntimeException("Simulated processing failure");
                }

                callbackService.sendSuccess(event);
            } catch (Exception e) {
                callbackService.sendFailure(eventType(), e.getMessage());
            }
        }
    }

    public void shutdown() {
        running = false;
    }
}
