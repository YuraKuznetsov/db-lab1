# Counter Implementation

The goal of this program is to explore various approaches to incrementing a counter in a PostgreSQL database concurrently while considering factors such as performance, concurrency control, and consistency.

Initially, we have table `user_counter` with fields `user_id`, `counter`, and `version`, as shown below.

| user_id | counter | version | 
|---------|---------|---------|
|    1    |    0    |    0    |

Then the program creates 10 threads and increments the counter 10_000 times in each thread using one of 4 different strategies. Let's take a look at the results!

### Lost Update
Increments the counter without any concurrency control, potentially leading to lost updates if multiple threads attempt to update the counter simultaneously.
**Time: 27 seconds.**

| user_id | counter | version |
|---------|---------|---------|
|    1    | 10_885  |    0    |

### In Place Update
Uses a basic synchronized block to ensure only one thread can update the counter at a time, preventing lost updates but potentially introducing contention and reducing concurrency.
**Time: 30 seconds.**

| user_id | counter | version |
|---------|---------|---------|
|    1    | 100_000 |    0    |

### Row Level Locking
Utilizes row-level locking to prevent concurrent updates to the same counter. Each thread acquires a lock on the row it intends to update, ensuring only one thread can modify the counter at a time.
**Time: 41 seconds.**

| user_id | counter | version |
|---------|---------|---------|
|    1    | 100_000 |    0    |

### Optimistic Concurrency Control
Employs optimistic concurrency control using a version number. Threads first read the current version of the counter, perform the update, and check if the version has changed before committing the update. If the version has changed, the thread retries the update.
**Time: 135 seconds.**

| user_id | counter | version |
|---------|---------|---------|
|    1    | 100_000 | 100_000 |

## Conclusion

In scenarios like ours where the database must handle thousands of concurrent requests, the "In Place Update" strategy emerges as the most suitable choice - offering a balance between preventing lost updates and maintaining faster execution compared to other implementations.
