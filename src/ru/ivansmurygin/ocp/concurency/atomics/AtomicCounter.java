package com.smurygin.ocjp.concurency.atomics;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by dfyz on 10/25/14.
 */
public class AtomicCounter {
    AtomicInteger counter;

    public AtomicCounter(Integer initialValue) {
        this.counter = new AtomicInteger(initialValue);
    }

    int increment(){
        return counter.incrementAndGet();
    }

    int addValue(int value){
        return counter.addAndGet(value);
    }

    void decrement(){
        counter.getAndDecrement();
    }


    public static void main(String[] args) {
        AtomicCounter ac = new AtomicCounter(10);

        new AtomicCounterUser("User1", ac).start();
        new AtomicCounterUser("User2", ac).start();
        new AtomicCounterUser("User3", ac).start();
    }

}

class AtomicCounterUser extends Thread {
    AtomicCounter atomicCounter;

    AtomicCounterUser(String name, AtomicCounter ac) {
        atomicCounter = ac;
        setName(name);
        setPriority(MAX_PRIORITY);
    }

    @Override
    public void run() {
        System.out.printf("Counter was incremented by %s, current value %d \n", getName(), atomicCounter.increment());
    }
}
