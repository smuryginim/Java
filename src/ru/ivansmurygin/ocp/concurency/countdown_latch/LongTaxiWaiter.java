package com.smurygin.ocjp.concurency.countdown_latch;

import java.util.concurrent.CountDownLatch;

/**
 * Created by dfyz on 10/8/14.
 */
public class LongTaxiWaiter {

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(4);

        new Participant("Dmitry", countDownLatch);
        new Participant("Georgy", countDownLatch);
        new Participant("Tom Cruze", countDownLatch);
        new Participant("Dzek", countDownLatch);

        System.out.println("We will wait for 4 minutes...");
        for (int i = 0; i < 4; i++) {
            System.out.println(i+1);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        }
        System.out.println("countDownLatch = " + countDownLatch.toString());
    }

}

class Participant extends Thread {
    CountDownLatch latch;

    Participant(String name, CountDownLatch latch) {
        this.latch = latch;
        this.setName(name);
        this.start();
    }

    @Override
    public void run() {
        System.out.println("Participant " + getName() + "is ready and waiting");
        try {
            latch.await();
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Finally we can start!!");
    }
}
