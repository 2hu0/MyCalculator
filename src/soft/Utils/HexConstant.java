package soft.Utils;

/**枚举一些十六进制常量
 * @author 2hu0
 */

public enum HexConstant {
    HEX_A(10),
    HEX_B(11),
    HEX_C(12),
    HEX_D(13),
    HEX_E(14),
    HEX_F(15);
     public Integer val;
    static {
        HEX_A.val = 10;
        HEX_B.val = 11;
        HEX_C.val = 12;
        HEX_D.val = 13;
        HEX_E.val = 14;
        HEX_F.val = 15;
    }
    HexConstant(int i) {
    }
}
