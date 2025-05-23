package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OrderDetailTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static OrderDetail getOrderDetailSample1() {
        return new OrderDetail().id(1L).quantity(1);
    }

    public static OrderDetail getOrderDetailSample2() {
        return new OrderDetail().id(2L).quantity(2);
    }

    public static OrderDetail getOrderDetailRandomSampleGenerator() {
        return new OrderDetail().id(longCount.incrementAndGet()).quantity(intCount.incrementAndGet());
    }
}
