package soft.test;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static java.math.BigDecimal.ROUND_HALF_EVEN;

public class Test2 {


    @Test
    public void test1() {
        System.out.println(0.2 + 0.7);
    }

    @Test
    public void test2() {
        BigDecimal bigDecimal1 = new BigDecimal("0.2");
        BigDecimal bigDecimal2 = new BigDecimal(0.7);
        bigDecimal1 = bigDecimal1.add(bigDecimal2);
        System.out.println(bigDecimal1);
    }
}
