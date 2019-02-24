package forkjoinpool.recursive_action;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by konstantin on 23.02.2019.
 */
public class DummyProductListGenerator implements ProductListGenerator {
    @Override
    public List<Product> generate(int size) {
        return IntStream.range(0, size).boxed()
                .map(i ->
                {
                    Product product = new Product();
                    product.setName("Product_"+ i);
                    product.setPrice(i);
                    return product;
                })
                .collect(Collectors.toList());

    }
}
