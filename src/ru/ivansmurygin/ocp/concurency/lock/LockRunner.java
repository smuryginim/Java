package ru.ivansmurygin.ocp.concurency.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by SmuryginIM on 02.05.2016.
 */
public class LockRunner {

    public static void main(String[] args) throws InterruptedException {
        lockWithConditionExample();
    }

    class Valuator {
        public AtomicInteger status = new AtomicInteger(0);

        public void valuate() {
            int oldstatus = status.get();     	/* valid code here */
            // INSERT CODE HERE
            //int newstatus = //determine new status
        }
    }

    public static void simpleLockExample(){
        ReentrantLock reentrantLock = new ReentrantLock();
        Eater eater1 = new Eater(reentrantLock);
        Eater eater2 = new Eater(reentrantLock);
        eater1.start();
        eater2.start();

    }

    public static void lockWithConditionExample() throws InterruptedException {
        Fork fork = new Fork();
        WashMashine washMashine = new WashMashine(fork);
        EaterExtended eater1 = new EaterExtended(fork);
        EaterExtended eater2 = new EaterExtended(fork);
        washMashine.start();
        Thread.sleep(2000);
        eater1.start();
        eater2.start();
    }


    static class Eater extends Thread {
        Lock fork;

        Eater(Lock resource){
            this.fork = resource;
        }

        @Override
        public void run() {
            if (fork.tryLock()){
                doEat();
            } else {
                System.out.printf("%s: I can't eat without fork. I'll wait until fork is free \n", this.getName());
                fork.lock();
                doEat();
            }

            fork.unlock();
        }

        void doEat(){
            System.out.printf("%s: I'll use fork for 5 secs \n", this.getName());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("%s: Now you can use this fork\n", this.getName());
        }
    }

    static class EaterExtended extends Thread {
        Fork fork;

        EaterExtended(Fork resource){
            this.fork = resource;
        }

        @Override
        public void run() {
            fork.getLock().lock();
            try {
                while (!fork.isClean()){
                    System.out.printf("%s: I can't eat with dirty fork \n", this.getName());
                    try {
                        fork.getCondition().await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.printf("%s: Now fork is clean and I can eat \n", this.getName());
                doEat();
                fork.getCondition().signalAll();
            } finally {
                fork.getLock().unlock();
            }
        }

        void doEat(){
            System.out.printf("%s: I'll use fork for 5 secs \n", this.getName());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fork.setIsClean(false);
            System.out.printf("%s: Now you can use this fork, but it is dirty\n", this.getName());
        }
    }

    static class Fork {
        private volatile boolean isClean;
        private final Lock lock;
        private final Condition condition;


        public Fork() {
            this.lock = new ReentrantLock();
            this.isClean = true;
            condition = lock.newCondition();
        }

        public boolean isClean() {
            return isClean;
        }

        public void setIsClean(boolean isClean) {
            this.isClean = isClean;
        }

        public Lock getLock() {
            return lock;
        }

        public Condition getCondition() {
            return condition;
        }
    }

    static class WashMashine extends Thread {
        Fork fork;

        public WashMashine(Fork fork) {
            this.fork = fork;
            this.setName("Wash Machine");
        }

        @Override
        public void run() {
            while (true) {
                fork.getLock().lock();
                try {
                    while (fork.isClean){
                        try {
                            System.out.printf("%s: Waiting for dirty fork\n", this.getName());
                            fork.getCondition().await();
                            System.out.printf("%s: Washing fork\n", this.getName());
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        fork.setIsClean(true);
                        fork.getCondition().signalAll();
                    }
                } finally {
                    System.out.printf("%s: Fork is clean now\n", this.getName());
                    fork.getLock().unlock();
                }
            }
        }

    }

}


