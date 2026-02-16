📩 Event Notification System

A Java-based asynchronous Event Notification System built using Spring Boot that processes EMAIL, SMS, and PUSH events using separate FIFO queues and sends completion status to a callback URL.

---

🚀 Running the Application (Docker Compose)

🔹 Prerequisites

- Docker installed

- Docker Compose installed
---

🔹 Build and Start the Application

From the project root directory, run:
```shell
docker compose up --build
```

This will:

1. Build the application using a multi-stage Docker build

2. Start the container

3. Expose the API on port 8080

---

🔹 API Endpoint

Once started, the API will be available at:
```
http://localhost:8080/api/events
```

---


🔹 Example Request
```shell
curl -X POST http://localhost:8080/api/events \
-H "Content-Type: application/json" \
-d '{
"eventType": "EMAIL",
"payload": {
"recipient": "user@example.com",
"message": "Welcome!"
},
"callbackUrl": "http://localhost:8081/test-callback"
}'
```

---


🔹 Stop the Application
```shell
docker compose down
```

The container is configured with:

```yaml
stop_grace_period: 60s
```

to allow graceful shutdown and completion of queued events.


---


🧠 Assumptions & Notes

1️⃣ Processing Simulation

- EMAIL events simulate 5 seconds processing time.

- SMS events simulate 3 seconds processing time.

- PUSH events simulate 2 seconds processing time.

- 10% of events fail randomly to simulate real-world behavior.


---


2️⃣ FIFO Guarantee

- Each event type has a dedicated `LinkedBlockingQueue`.

- Each queue is processed by a dedicated thread.

- FIFO order is guaranteed per event type.

3️⃣ Callback URL

- The system sends a POST request to the provided `callbackUrl` upon completion or failure.

- For local testing, use a reachable URL such as:
    
```
http://host.docker.internal:8081/test-callback
```
when running inside Docker.

---


4️⃣ Graceful Shutdown

On container stop:

1. The system stops accepting new events.
2. Remaining queued events are processed.
3. Threads are terminated cleanly.
4. Application exits gracefully.


---


5️⃣ In-Memory Queues

- All queues are in-memory.

- No database persistence is used.

- Restarting the application clears all queued events.


---


6️⃣ Java Version

- Built using Java 17

- Spring Boot 3

---


📦 Summary

This implementation satisfies the assignment requirements:

- Separate queues per event type

- FIFO processing

- Asynchronous processing

- Callback handling

- Random failure simulation

- Graceful shutdown

- Docker Compose support