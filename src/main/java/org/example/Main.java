package org.example;

import org.example.incrementer.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        // Choose one of incrementer implementations
        CounterIncrementer incrementer = new LostUpdateIncrementer();
//        CounterIncrementer incrementer = new InPlaceIncrementer();
//        CounterIncrementer incrementer = new RowLevelLockingIncrementer();
//        CounterIncrementer incrementer = new OptimisticConcurrencyControlIncrementer();

        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        long startTime = System.nanoTime();
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(incrementer);
        }
        shutdownExecutor(executorService);
        long endTime = System.nanoTime();

        printTime(startTime, endTime);
    }

    private static void shutdownExecutor(ExecutorService executorService) {
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printTime(long startTime, long endTime) {
        long elapsedTime = endTime - startTime;
        double elapsedTimeInSeconds = (double) elapsedTime / 1_000_000_000.0;
        System.out.println("Elapsed Time: " + elapsedTimeInSeconds + " seconds");
    }
}
