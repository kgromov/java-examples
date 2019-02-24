package forkjoinpool.recursive_action;

import java.util.List;
import java.util.concurrent.RecursiveAction;

/**
 * Created by konstantin on 23.02.2019.
 */
public class ProductTask extends RecursiveAction {
    private List<Product> products;
    private int first;
    private int last;
    private double increment;

    public ProductTask(List<Product> products, int first, int last, double increment) {
        this.products = products;
        this.first = first;
        this.last = last;
        this.increment = increment;
    }

    @Override
    protected void compute() {
        if (last - first < Product.DEFAULT_PRODUTCR_SIZE) {
            updatePrices();
        } else {
            int middle = (last + first) / 2;
            System.out.printf("ProductTask: Pending tasks:%s\n", getQueuedTaskCount());
            ProductTask t1 = new ProductTask(products, first, middle + 1, increment);
            ProductTask t2 = new ProductTask(products, middle + 1, last, increment);
            invokeAll(t1, t2);
        }
    }

    private void updatePrices() {
        for (int i = first; i < last; i++) {
            Product product = products.get(i);
            product.setPrice(product.getPrice() * (1 + increment));
        }
    }
}
