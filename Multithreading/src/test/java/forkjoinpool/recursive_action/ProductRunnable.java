package forkjoinpool.recursive_action;

import java.util.List;

import static java.util.concurrent.ForkJoinTask.getQueuedTaskCount;

/**
 * Created by konstantin on 23.02.2019.
 */
public class ProductRunnable implements Runnable {
    private List<Product> products;
    private int first;
    private int last;
    private double increment;

    public ProductRunnable(List<Product> products, int first, int last, double increment) {
        this.products = products;
        this.first = first;
        this.last = last;
        this.increment = increment;
    }

    @Override
    public void run() {
        if (last - first < Product.DEFAULT_PRODUTCR_SIZE) {
            updatePrices();
        } else {
            int middle = (last + first) / 2;
            System.out.printf("ProductTask: Pending tasks:%s\n", getQueuedTaskCount());
            ProductRunnable t1 = new ProductRunnable(products, first, middle + 1, increment);
            ProductRunnable t2 = new ProductRunnable(products, middle + 1, last, increment);
            t1.run();
            t2.run();
        }
    }

    private void updatePrices() {
        for (int i = first; i < last; i++) {
            Product product = products.get(i);
            product.setPrice(product.getPrice() * (1 + increment));
        }
    }
}
