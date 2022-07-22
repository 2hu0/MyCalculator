package soft.service;
class Operation {
    private static int OR = 1;
    private static int XOR = 2;
    private static int AND = 3;
    private static int SHF = 4;
    private static int ADD = 5;
    private static int SUB = 5;
    private static int MUL = 6;
    private static int DIV = 6;

    //写一个方法，返回对应的优先级数字
    public static int getValue(String operation) {
        int result = 0;
        switch (operation) {
            case "+":
                result = ADD;
                break;
            case "-":
                result = SUB;
                break;
            case "*":
                result = MUL;
                break;
            case "/":
                result = DIV;
                break;
            case "^":
                result = XOR;
                break;
            case "&":
                result = AND;
                break;
            case "|":
                result = OR;
                break;
            case "@":
                result = SHF;
                break;
            default:
                break;
        }
        return result;
    }

}