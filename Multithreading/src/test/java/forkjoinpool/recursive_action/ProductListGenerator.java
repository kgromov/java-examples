package forkjoinpool.recursive_action;

import java.util.List;

/**
 * Created by konstantin on 23.02.2019.
 */
public interface ProductListGenerator {

    List<Product> generate(int size);
}
