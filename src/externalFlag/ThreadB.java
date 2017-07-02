package externalFlag;

import java.util.concurrent.Callable;

public class ThreadB implements Callable<String> {

 @Override
 public String call() {

  MainThread.changeFlag(false);

  Thread cuurentThread = Thread.currentThread();
  if (!cuurentThread.interrupted()) {

   System.out.println("Only Thread - " + Thread.currentThread().getName() + " is running.");
   return "COMPLETED";

  }
  return null;

 }

}