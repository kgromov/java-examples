package forkjoinpool.recursive_action;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * Created by konstantin on 23.02.2019.
 */
public class Main {
    static class UpdateProductTask {
        private int startIndex;

        public UpdateProductTask(int startIndex) {
            this.startIndex = startIndex;
        }

        public void updateProducts(List<Product> products) {
            for (int i = startIndex; i < startIndex + 10; i++) {
                Product product = products.get(i);
                product.setPrice(product.getPrice() * (1 + 0.2));
            }
        }
    }

    public static void main(String[] args) {
        long start = System.nanoTime();
        ProductListGenerator generator = new DummyProductListGenerator();
        List<Product> products = generator.generate(10_000);
        // parallelStream = 93ms, common stream - 82ms
//        products.forEach(product -> product.setPrice(product.getPrice() * (1 + 0.2)));

       /* ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i = 0; i < products.size(); i += 10) {
            int finalI = i;
            service.submit(() -> new UpdateProductTask(finalI).updateProducts(products));
        }
        service.shutdown();*/


        // ~230ms
        ProductTask task = new ProductTask(products, 0, products.size(), 0.2);
//        ProductRunnable runnable = new ProductRunnable(products, 0, products.size(), 0.2);
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.execute(task);
//        pool.execute(ProductTask.adapt(runnable));


        do {
            System.out.printf("Main: Thread Count:%d\n", pool.getActiveThreadCount());
            System.out.printf("Main: Thread Steal:%d\n", pool.getStealCount());
            System.out.printf("Main: Parallelism:%d\n", pool.getParallelism());
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!task.isDone());

        pool.shutdown();
        if (task.isCompletedNormally()) {
            System.out.println("Main: The process has completed normally.");
        }
        System.out.println(String.format("Time elapsed = %d", TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS)));

        for (Product product : products) {
            if (product.getPrice() != 12) {
                System.out.println(String.format("Product %s: %f", product.getName(), product.getPrice()));
            }
        }
        System.out.println("Main: End of the program.");
    }
}
