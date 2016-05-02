package ru.ivansmurygin.ocp.concurency.executor;

import java.util.Calendar;
import java.util.concurrent.*;

/**
 * Created by SmuryginIM on 27.04.2016.
 */
public class ThreadPoolExecutor {

    public static void main(String[] args) {
        fixedThreadPoolExecutor();
    }

    static void fixedThreadPoolExecutor(){
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Future<String> result = executorService.submit(new SimpleBigTask());
        executorService.execute(new SimpleSmallAction());

        try {
            System.out.printf("%s :result is = %s \n", Thread.currentThread().getName(), result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        executorService.execute(new SimpleSmallAction());
        executorService.execute(new SimpleSmallAction());
        executorService.execute(new SimpleSmallAction());

        System.out.printf("%s: Is executor service shutdown? : %b\n", Thread.currentThread().getName(),
                executorService.isShutdown());
        executorService.shutdown();

    }

}

class SimpleBigTask implements Callable<String> {
    @Override
    public String call() throws Exception {
        Thread.sleep(3000);
        return Thread.currentThread().getName() + ": Task was completed after 3 seconds!";
    }
}


class SimpleSmallAction implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ": Action was performed");
    }
}

class BeeperControl {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

    public void beepForTenSeconds() {
        ScheduledFuture<?> result = scheduler.schedule(new ScheduledTask(), 1, TimeUnit.SECONDS);
        try {
            System.out.printf("%s : Task was executed with result = %s \n", Thread.currentThread().getName(), result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        scheduler.scheduleAtFixedRate(new ScheduledPeriodicalAction(), 0, 10, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(new ScheduledPeriodicalAction(), 0, 10, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(new ScheduledPeriodicalAction(), 0, 10, TimeUnit.SECONDS);

        result = scheduler.schedule(new ScheduledTask(), 1, TimeUnit.SECONDS);
        try {
            System.out.printf("%s : Task was executed with result = %s \n", Thread.currentThread().getName(), result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //scheduler.shutdown();
    }

    class ScheduledTask implements Callable<String>{

        @Override
        public String call() throws Exception {
            return Thread.currentThread().getName() + ": Scheduled task was executed";
        }
    }

    class ScheduledPeriodicalAction implements Runnable {

        @Override
        public void run() {
            System.out.printf("%s : ten second passed %s\n", Thread.currentThread().getName(), Calendar.getInstance().getTime());
        }
    }
}


