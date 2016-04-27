package com.smurygin.ocjp.concurency.semaphore;

import java.util.concurrent.Semaphore;

/**
 * Created by dfyz on 10/6/14.
 */
public class ATMRoom extends Semaphore{

    public ATMRoom(int permits) {
        super(permits);
    }

    @Override
    protected void reducePermits(int reduction) {
        super.reducePermits(reduction);
    }

    public static void main(String[] args) {
        ATMRoom machines = new ATMRoom(1);

        //this one will increment number of permits
        machines.release();
        machines.reducePermits(2);

        new Person(machines, "Ivan");
        new Person(machines, "Vsevolod");
        new Person(machines, "Aleksander");
        new Person(machines, "Maksim");

        System.out.println("exiting main");
    }

}

class Person extends Thread {
    private Semaphore machines;

    public Person(Semaphore machines, String name){
        this.machines = machines;
        this.setName(name);
        this.start();
    }

    public void run(){
        try {
            System.out.println(getName() + " waiting for access machine");
            machines.acquire();
            System.out.println(getName() + " is accessing ATM machine");
            Thread.sleep(1000);
            System.out.println(getName() + " is done using ATM machine" );
            machines.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
