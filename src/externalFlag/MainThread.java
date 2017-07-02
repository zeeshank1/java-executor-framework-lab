package externalFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


/*
*@author Zeeshan
*
*
*/
public class MainThread {

 private static boolean flag = false;
 private static int numOfThreads = 3;

 private static List<Callable<String>> callableThreadList = new ArrayList<Callable<String>>();
 private static List<Future<String>> resultList = new ArrayList<>();
 private static ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);

  
 public static void main(String args[]) {
  
  callableThreadList.add(new ThreadA());
  callableThreadList.add(new ThreadB());
  callableThreadList.add(new ThreadC());

  for (Callable<String> callableObject : callableThreadList) {
   Future<String> result = executor.submit(callableObject);
   resultList.add(result);
  }

  // shut down the executor service now
  executor.shutdown();
 }

 public static synchronized void changeFlag(boolean flagParam) {

  Thread currentThread = Thread.currentThread();
  // IF Started
  if (flagParam) {

   if (!currentThread.interrupted()) {
    System.out.println(currentThread.getName() + " is trying to change the flag with " + flagParam);
    flag = flagParam;
    System.out.println("flag is set by :" + currentThread.getName());

    //Terminate all other threads
    shutdownAndAwaitTermination();

   } else {
    currentThread.interrupt();
   }

   
  } else {
   if (!currentThread.interrupted())
    System.out.println(currentThread.getName() + " tried to change the flag with false ,So it is stopped");
       currentThread.interrupt();

  }
 }

 
 /**
  * Method to kill all other threads in the pool.
  *  
  * You can test this method by setting the change flag = false in Callable
  * threads, i.e., ThreadA or ThreadB or ThreadC
  * 
  */
 private static void shutdownAndAwaitTermination() {
  // Disable new tasks from being submitted
  executor.shutdown(); 
  try {
   // Wait a while for existing tasks to terminate
   if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
    
    // Cancel currently executing tasks
    executor.shutdownNow(); 
    
    // Wait a while for tasks to respond to being cancelled
    if (!executor.awaitTermination(60, TimeUnit.SECONDS))
     System.err.println("Pool did not terminate");
   }
  } catch (InterruptedException ie) {
   
   // (Re-)Cancel if current thread also interrupted
   executor.shutdownNow();
   // Preserve interrupt status
   Thread.currentThread().interrupt();
  }
 }
}
