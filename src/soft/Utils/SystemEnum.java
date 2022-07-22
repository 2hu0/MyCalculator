package soft.Utils;

/**
 * 枚举进制常量
 *
 * @author 2hu0
 */

public enum SystemEnum {
    /**
     * 10进制
     */
    DEC("DEC"),
    /**
     * 16进制
     */
    HEX("HEX"),
    /**
     * 2进制
     */
    BINARY("BIN"),
    /**
     * 特殊情况
     * 保留原进制
     * 作为标记使用
     */
    ORIGINAL("original"),
    ;
    private final String name;

    SystemEnum(String name) {
        this.name = name;
    }

    public String getCurrentSystem() {
        return name;
    }

    public static String getSystemName(SystemEnum system) {
        switch (system) {
            case DEC:
                return DEC.getCurrentSystem();
            case BINARY:
                return BINARY.getCurrentSystem();
            case HEX:
                return HEX.getCurrentSystem();
            case ORIGINAL:
                return ORIGINAL.getCurrentSystem();
            default:
                break;
        }
        return "";
    }
}
