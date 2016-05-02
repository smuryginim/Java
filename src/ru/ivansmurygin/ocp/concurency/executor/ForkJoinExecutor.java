package ru.ivansmurygin.ocp.concurency.executor;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Created by SmuryginIM on 02.05.2016.
 */
public class ForkJoinExecutor {

    public final static int THREADS_COUNT = 10;
    public final static long N = 1000000;

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(THREADS_COUNT);
        Long sum = forkJoinPool.invoke(new SumTask(0, N));

        System.out.printf("Result is %d", sum);
    }
}

//Example from
class SumTask extends RecursiveTask<Long> {
    long from, to;

    public SumTask(long from, long to) {
        this.from = from;
        this.to = to;
    }

    @Override
    protected Long compute() {
        //1. Decide if the task should be divided
        if (to-from < ForkJoinExecutor.N/ForkJoinExecutor.THREADS_COUNT){
            //2. Compute result locally
            long sumLocal = 0;
            for (long s = from; s <= to; s++) {
                sumLocal += s;
            }

            System.out.printf("\t Local sum digits from %d to %d is %d \n", from, to, sumLocal);
            return sumLocal;
        // 3. else fork task for two sub-tasks
        } else {
            Long mid = (from + to)/2;
            System.out.printf("Fork task for two subtask with range %d to %d and %d to %d\n", from, mid, mid, to);

            //3.1 fork first task
            SumTask sumTaskFirst = new SumTask(from, mid);
            sumTaskFirst.fork();

            // 3.2 compute second task
            SumTask sumTaskSecond = new SumTask(mid+1, to);
            Long resultSecond = sumTaskSecond.compute();

            //3.3 wait until first task will be computed and sum the result
            return sumTaskFirst.join() + resultSecond;

        }
    }
}