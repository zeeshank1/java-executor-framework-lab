import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;

public class ExecutorExample {

    public static void main(String[] args) {
        // Create a thread pool with 5 threads
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // List to hold the results of tasks
        List<Future<Integer>> results = new ArrayList<>();

        // Submit 10 tasks to the executor
        for (int i = 0; i < 10; i++) {
            // Use a local copy of the loop variable to avoid concurrency issues
            final int taskNumber = i;

            // Create a Callable task
            Callable<Integer> task = () -> {
                // Simulate some work
                Thread.sleep(500); 
                return taskNumber * 2; 
            };

            // Submit the task and store the Future object
            results.add(executor.submit(task));
        }

        // Process the results
        for (Future<Integer> future : results) {
            try {
                System.out.println("Result: " + future.get());
            } catch (InterruptedException e) {
                System.err.println("Task interrupted: " + e.getMessage());
                Thread.currentThread().interrupt(); // Restore the interrupted status
            } catch (ExecutionException e) {
                System.err.println("Task threw an exception: " + e.getCause());
            }
        }

        // Shutdown the executor
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                System.err.println("Executor did not terminate in the specified time.");
                executor.shutdownNow(); // Forcefully shutdown remaining tasks
            }
        } catch (InterruptedException e) {
            System.err.println("Shutdown interrupted: " + e.getMessage());
            executor.shutdownNow(); // Forcefully shutdown remaining tasks
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }

        System.out.println("All tasks completed.");
    }
}
