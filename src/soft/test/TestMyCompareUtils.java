package soft.test;

import soft.Utils.MyCompareUtils;
import org.junit.Test;

public class TestMyCompareUtils {
    @Test
    public void Test(){
        MyCompareUtils utils = new MyCompareUtils();
        boolean res = utils.compare("+", "+", "-", "*", "/");
        System.out.println(res);
    }
}
