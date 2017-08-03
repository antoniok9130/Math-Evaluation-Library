package Math_Evaluation_Library.Engine;

import Math_Evaluation_Library.Constants.Constants;
import Math_Evaluation_Library.Miscellaneous.*;
import Math_Evaluation_Library.Objects.Function;
import Math_Evaluation_Library.Objects._Number_;
import Math_Evaluation_Library.Search;
import Math_Evaluation_Library.Sort;
import Math_Evaluation_Library.Trigonometry.Trig;
import Math_Evaluation_Library.UnitConversion._UnitConversion_;

import static java.lang.Double.NaN;

import java.util.*;

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
/**

 @author Antonio's Laptop
 */
public class Engine {

    public static String var = "x", varOp = "y";
    public static String error = "Error";
    static boolean debug = false, sorted = false;
    static double ans = 1;
    static List<String> variables = new ArrayList<>();
    static List<Double> values = new ArrayList<>();
    protected static String[] multiParamFunctions = {"max", "min", "randint", "random", "elasd", "nint", "dxn", "dx", "riemann", "sum", "product", "gcd", "lcm", "heron", "newton", "ED", "c_law", "avg", "var", "stndv", "logab", "unit"};
    protected static int[] numParameters =          { -1,    -1,    2,         2,        4,       3,      3,     2,    -1,        3,     3,         -1,     -1,   -1,      2,        -1,   3,       -1,    -1,    -1,      -1,      3};
    protected static int max_fn_string = 7;

    //private static List<String> unitConversion = new ArrayList<>(Arrays.asList("m/s->km/h"));
    public static void sortFunctions() {
        Sort.quicksort(multiParamFunctions, numParameters);
        Sort.quicksort(order);
        Sort.quicksort(operators, precedence, singleOperator, associability);
        Sort.quicksort(implicit);
        Sort.quicksort(checkImplicit);
        sorted = true;
    }

    static char[] implicit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ')', 'π', 'η', 'ϕ', 'γ', var(), varOp()};
    static char[] checkImplicit = {'(', var(), varOp(), 'π', 'η', 'ϕ', 'γ', '√'};

    public static String fixSyntax(String function) {
        if (function.length() == 0){
            return "";
        }
        if (!sorted){
            sortFunctions();
        }
        error = "Math Error";
        // Find abs brackets
        List<Integer> absBracketIndices = Search.getIndices(function, "|");
        if (absBracketIndices.size()%2 != 0) {
            error = "Absolute Value Bracket Error";
            return error;
        }
        for (int a = 0; a < absBracketIndices.size(); a += 2) {
            int open = absBracketIndices.get(a) + 5 * a / 2;
            int close = absBracketIndices.get(a + 1) + 5 * a / 2;
            function = function.substring(0, open) + "(abs(" + function.substring(open + 1, close) + "))" + function.substring(close + 1);
        }
        function = replaceVariables(function);
        function = Search.replace(function, " ", "");
        function = Search.replace(function, "P", "npr");
        function = Search.replace(function, "C", "ncr");
        function = Search.replace(function, "ED", "dist");
        function = Search.replace(function, "E", "*10^");
        function = Search.replace(function, "Σ", "sum");
        function = Search.replace(function, "Π", "product");
        function = function.toLowerCase();
        function = Search.replace(function, "npr", "P");
        function = Search.replace(function, "ncr", "C");
        function = Search.replace(function, "_r", "ʳ");
        function = Search.replace(function, "÷", "/");
        function = Search.replace(function, "×", "*");
        function = Search.replace(function, "**", "^");
        function = Search.replace(function, "coslaw", "c_law");
        function = Search.replace(function, "ave", "avg");
        function = Search.replace(function, "mean", "avg");
        function = Search.replace(function, "sec", "scnt");
        function = Search.replace(function, "aexp", "axp");
        function = Search.replace(function, "exp", "axp");
        function = Search.replace(function, "1n1p", "lp");
        function = Search.replace(function, "1og1p", "lp");
        function = Search.replace(function, "log(", "logab(");
        function = Search.replace(function, "deg", "dg");
        function = Search.replace(function, "ceil", "up");
        function = Search.replace(function, "nderiv", "dx");
        function = Search.replace(function, "deriv", "diff");
        function = Search.replace(function, "riemann", "riman");
        function = Search.replace(function, "elasy", "elasd");
        function = Search.replace(function, "elasx", "elasd");
        function = Search.replace(function, "elasd", "lasd");
        function = Search.replace(function, "prime", "nconst");
        function = Search.replace(function, "heron", "hron");
        function = Search.replace(function, "newton", "nwton");
        function = Search.replace(function, "fibsum", "smfib");
        function = Search.replace(function, "sumfib", "smfib");
        function = Search.replace(function, "variance", "var");
        function = Search.replace(function, "stdev", "stndv");
        function = Search.replace(function, "stddev", "stndv");
        function = Search.replace(function, "stndev", "stndv");
        function = Search.replace(function, "gcf", "gcd");
        function = Search.replace(function, "len", "strln");
        function = Search.replace(function, "strlen", "strln");
        function = Search.replace(function, "len", "strln");
        function = Search.replace(function, "count", "strln");
        function = Search.replace(function, "randomf", _Random_.randomf());
        function = Search.replace(function, "randomint", "randint");
        function = Search.replace(function, "ans", String.valueOf(ans));
        function = Search.replace(function, "pi", "π");
        function = Search.replace(function, "e_m", "γ");
        function = Search.replace(function, "e", "η");
        function = Search.replace(function, "gr", "ϕ");
        function = Search.replace(function, "->", "→");
        //Implicit Multiplication
        try {
            for (int a = 1; a < function.length(); a++) {
                char f = function.charAt(a);
                char f1 = function.charAt(a-1);
                boolean implicitContainssf1 = implicitContains(f1);
                for (char c : checkImplicit) {
                    if (f == c && implicitContainssf1) {
                        function = function.substring(0, a) + "*" + function.substring(a);
                        f = function.charAt(a);
                    }
                }
                if (a > 1 && Scripts.isSuperScript(f1) && checkImplicitContains(f)) {
                    char f2 = function.charAt(a-2);
                    if (implicitContains(f2)){
                        function = function.substring(0, a) + "*" + function.substring(a);
                        f = function.charAt(a);
                    }
                }
                if (f1 == ')' && (f != ')' && implicitContains(f))) {
                    function = function.substring(0, a) + "*" + function.substring(a);
                    f = function.charAt(a);
                }
                else if (a+1 < function.length()){
                    char f2 = function.charAt(a+1);
                    if (f1 == '(' && f == '-' && !_Number_.isNumber(f2)) {
                        function = function.substring(0, a) + "1*" + function.substring(a);
                        f = function.charAt(a);
                    }
                    else if (f == '-' && operatorContains(f1) && !_Number_.isNumber(f2)) {
                        function = function.substring(0, a+1) + "1*" + function.substring(a+1);
                    }
                }
            }
        }catch (StringIndexOutOfBoundsException e){
            error = "Invalid Input Error - Found while fixing implicit multiplication";      return error;
        }
        function = Search.replace(function, "intmax", "2147483647");
        function = Search.replace(function, "intmin", "-2147483648");
        function = Search.replace(function, "hparg", "graph");
        function = Search.replace(function, "dg", "deg");
        function = Search.replace(function, "scnt", "sec");
        function = Search.replace(function, "axp", "aexp");
        function = Search.replace(function, "up", "ceil");
        function = Search.replace(function, "/sec", "cos");
        function = Search.replace(function, "/csc", "sin");
        function = Search.replace(function, "/cot", "tan");
        function = Search.replace(function, "acos", "arccos");
        function = Search.replace(function, "asin", "arcsin");
        function = Search.replace(function, "atan", "arctan");
        function = Search.replace(function, "asec", "arcsec");
        function = Search.replace(function, "acsc", "arccsc");
        function = Search.replace(function, "acot", "arccot");
        function = Search.replace(function, "diff", "deriv");
        function = Search.replace(function, "riman", "riemann");
        function = Search.replace(function, "max\\*", "max");
        function = Search.replace(function, "sqrt", "√");
        function = Search.replace(function, "lasd", "elasd");
        function = Search.replace(function, "nconst", "prime");
        function = Search.replace(function, "hron", "heron");
        function = Search.replace(function, "nwton", "newton");
        function = Search.replace(function, "dist", "ED");
        for (int a = 1; a<function.length(); a++){
            if (implicitContains(function.charAt(a-1))){
                if (a+3 <= function.length() && function.substring(a, a + 3).equals("ans") && implicitContains(function.charAt(a-1))){
                    function = function.substring(0, a) + "*" + function.substring(a);
                    a += 4;
                    continue;
                }
                int index = -1;
                for (int b = 2; b <= max_order_string && a+b <= function.length(); b++){
                    index = orderIndex(function.substring(a, a+b));
                    if (index != -1){
                        function = function.substring(0, a) + "*" + function.substring(a);
                        a += b+1;
                        break;
                    }
                }
            }
            for (int b = 0; b < order.length; b++) {
                try {
                    if ((function.substring(a, a + order[b].length()).equals(order[b]) || function.substring(a, a + 3).equals("ans")) && implicitContains(function.charAt(a-1))) {
                        function = function.substring(0, a) + "*" + order[b] + function.substring(a + order[b].length());
                        a += order[b].length()+1;
                        break;
                    }
                } catch (StringIndexOutOfBoundsException e) {}
            }
            for (int b = 0; b < multiParamFunctions.length; b++) {
                try {
                    if (function.substring(a, a+ multiParamFunctions[b].length()).equals(multiParamFunctions[b]) && implicitContains(function.charAt(a-1))) {
                        function = function.substring(0, a) + "*" + multiParamFunctions[b] + function.substring(a + multiParamFunctions[b].length());
                    }
                } catch (StringIndexOutOfBoundsException e) {
                }
            }
        }
        int numLbracket = 0;
        int numRbracket = 0;
//        if (!validFunction(function) || function.length() == 0){
//            error = "Invalid Input Error - Non valid function";
//            return error;
//        }
        for (int a = 0; a < function.length(); a++) {
            char c = function.charAt(a);
            if (c == '(') {
                numLbracket++;
            } else if (c == ')') {
                numRbracket++;
            }
        }
        while (numRbracket < numLbracket){
            function += ")";
            numRbracket++;
        }
        return function;
    }

    protected static String replaceVariables(String function){
        // Replace all variables
        for (int a = 0; a < variables.size(); a++) {
            function = Search.replace(function, variables.get(a), "("+values.get(a)+")");
        }
        return function;
    }

    public static boolean validFunction(String function){
        for (int a = 0; a < function.length(); a++) {
            char c = function.charAt(a);
            if (c != var() && c != '(' && c != ')' && c != '[' && c != ']' && c != '{' && c != '}' && c != ',' && c != '.'
                    && operatorIndex(c) == -1 && !Scripts.isSuperScript(c) && !Scripts.isSubScript(c) && !implicitContains(c)) {
                try {
                    int index = -1;
                    for (int b = max_fn_string; b>=2; b--){
                        if (a+b <= function.length()){
                            if (b<=max_order_string){
                                index = orderIndex(function.substring(a, a+b));
                                if (index != -1){
                                    if (function.endsWith(order[index])){
                                        function += var;
                                    }
                                    else if (operatorContains(function.charAt(a + order[index].length()))){
                                        function = function.substring(0, a + order[index].length())+var+function.substring(a + order[index].length());
                                        a++;
                                    }
                                    a += b;
                                    break;
                                }
                            }
                            index = multiParamFunctionNamesIndex(function.substring(a, a+b));
                            if (index != -1){
                                a += b;
                                break;
                            }
                        }
                    }
                    if (index == -1){
                        return false;
                    }
                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {}
            }
            else if (c == '['){
                a = function.indexOf("]", a);
            }
        }
        return true;
    }

    public static String getError() {
        return error;
    }

    public static double evaluate(String function, double num) {
        function = Search.replace(function, "max", "up");
        function = Search.replace(function, var, "(" + MathRound.roundf(num, 17) + ")");
        function = Search.replace(function, "up", "max");
        return evaluate(function);
    }
    public static double evaluate(String function, double x, double y) {
        function = Search.replace(function, "max", "up");
        function = Search.replace(function, var, "(" + MathRound.roundf(x, 16) + ")");
        function = Search.replace(function, varOp, "(" + MathRound.roundf(y, 16) + ")");
        function = Search.replace(function, "up", "max");
        return evaluate(function);
    }

    public static double evaluate(String function) {
        try {
            return _Number_.getNumber(function);
        } catch (NumberFormatException e){}
        String format = toPostfix(function);
        if (function.toLowerCase().contains("error")) {
            System.out.println(error);
            return NaN;
        }
//        System.out.println(function + "\n" + format + "\n");
        List<String> outputs = new ArrayList<>(Arrays.asList(format.split(" ")));
        String evaluated = evaluate(outputs);
        try{
            double solution = _Number_.getNumber(evaluated);
            ans = solution;
            return solution;
        }catch(NumberFormatException e){}
        return NaN;
    }

    public static String evaluateString(String function){
        function = function.trim();
        if (function.length() == 0){
            return "";
        }
        double evaluated = evaluate(function);
        if (_Number_.isNumber(evaluated)){
            return _Number_.removeEnding0(evaluated);
        }
        //Check text cases
        int index = function.indexOf("(");
        if (index != -1){
            String eqn = function.substring(0, index);
            index = TextSolutionEngine.indexOf(eqn);
            if (index != -1){
                return TextSolutionEngine.solve("=("+eqn+", "+function.substring(eqn.length()+1));
            }
        }
        return "NaN";
    }

    public static String evaluate (List<String> list){
        return NumericalEngine.evaluate(list);
    }


//    public static double getUnitConversion(String conversion, double unit){
//        if (conversion.equals("m/s->km/h")){
//            return unit*3.6;
//        }
//        return unit;
//    }

    public static String[] order = {"cbrt", "arcsin", "sinh", "sin", "arccos", "cosh", "cos",
            "arctan", "tanh", "tan", "arccsc", "csch", "csc", "arcsec", "sech", "sec", "arccot", "coth", "cot", "ln", "lp", "log", "aexp",
            "abs", "rad", "deg", "floor", "ceil", "prime", "fib", "smfib", "bin", "tobin", "strln"};
    protected static int max_order_string = 6;
    static char[] operators = {'√', '^', '°', 'ʳ', '/', '%', '*', '!', 'P', 'C', '-', '+'};
    static int[] precedence = { 4,   4,   4,   4,   3,   3,   3,   3,   3,   3,   2,   2};
    public static boolean[] singleOperator = {true, false, false, false, false, false, false, false, false, false, false, false};
    static boolean[] associability = {false, false, false, false, true, true, true, true, true, true, true, true};

    public static String toPostfix(String infixFunction) {
        infixFunction = fixSyntax(infixFunction);
        if (infixFunction.toLowerCase().contains("error")) {
            return error;
        }
        try {
            if (infixFunction.charAt(0) == '-' && !_Number_.isNumber(infixFunction.charAt(1))) {
                infixFunction = "-1*" + infixFunction.substring(1);
            }
        }catch (StringIndexOutOfBoundsException e){}
        Stack<String> output = new Stack<>();
        Stack<String> stack = new Stack<>();
        String token = "";
        for (int a = 0; a < infixFunction.length(); a++) {
            char c = infixFunction.charAt(a);
            try {
                if (c == '.') {
                    token += ".";
                } else if (c == '-' && (a == 0 || implicitIndex(infixFunction.charAt(a - 1)) == -1)) {
                    token += "-";
                } else if (c == ',') {
                    if (token.length() < 1) {
                        error = "Syntax Error";
                        return error;
                    } else {
                        output.push(token);
                        token = "";
                    }
                } else if (c == var() || c == varOp() || Constants.isConstant(c)) {
                    if (token.length() > 0){
                        if (token.equals("-")){
                            output.push(token+(c == 'η' ? 'e' : c));
                        }
                        else{
                            error = "Constant Character Token Error:    Token: "+token+"  char: "+c;
                            return error;
                        }
                        token = "";
                    }
                    else{
                        output.push((c == 'η' ? "e" : String.valueOf(c)));
                    }
                } else{
                    String superScript = "";
                    for (int b = a; b<infixFunction.length(); b++){
                        int scriptIndex = Scripts.getSuperScriptIndex(infixFunction.charAt(b));
                        if (scriptIndex != -1){
                            superScript += Scripts.regularScripts[scriptIndex];
                        }
                        else{
                            break;
                        }
                    }
                    if (superScript.length() > 0){
                        infixFunction = infixFunction.substring(0, a)+"^"+superScript+infixFunction.substring(a+superScript.length());
                        c = infixFunction.charAt(a);
                    }

                    int read = Integer.parseInt(String.valueOf(c));
                    token += read;
                }
            } catch (NumberFormatException e) {
                if (token.length() > 0) {
                    output.push(token);
                    token = "";
                }
                try {
                    boolean parsed = false;
                    if (c == '(') {
                        stack.push("(");
                        if (infixFunction.indexOf(")", a + 1) == -1) {
                            error = "Bracket Count Error:  Missing ')' Bracket";
                            return error;
                        }
                        parsed = true;
                    } else if (c == ')') {
                        if (stack.contains("(")) {
                            while (!stack.isEmpty()) {
                                String str = stack.pop();
                                if (str.equals("(")) {
                                    break;
                                }
                                output.push(str);
                            }
                        } else {
                            error = "Bracket Count Error:  Missing '(' Bracket";
                            return error;
                        }
                        parsed = true;
                    } else if (c == '{') {
                        int end = infixFunction.indexOf("}", a);
                        if (end != -1){
                            output.push(infixFunction.substring(a, end+1));
                            a = end+1;
                        }
                        else {
                            return "Variable Syntax Error";
                        }
                    } else if (c == '[') {
                        int unitIndex = infixFunction.indexOf("→", a);
                        if (unitIndex != -1){
                            int close = infixFunction.indexOf("]", a);
                            if (unitIndex < close){
                                String[] parameters = _UnitConversion_.isUnitConversion(infixFunction.substring(a+1, close), unitIndex);
                                if (parameters != null) {
                                    for (String param : parameters){
                                        output.push(param);
                                    }
                                    output.push("unit");
                                    a = close;
                                    parsed = true;
                                }
                                else{
                                    error = "Invalid Input Error - Invalid Unit Conversion:  "+infixFunction.substring(a+1, close);
                                    return error;
                                }
                            }
                        }
                        if (!parsed){
                            int comma1 = infixFunction.indexOf(",", a);
                            int close = infixFunction.indexOf("]", a);
                            if (comma1 == -1 || close == -1) {
                                error = "Syntax Error - Evaluate";
                                return error;
                            }
                            int comma2 = infixFunction.indexOf(",", comma1 + 1);
                            if (comma2 == -1) {
                                output.push(infixFunction.substring(a + 1, comma1));
                                output.push("" + evaluate(infixFunction.substring(comma1 + 1, close)));
                                output.push("eval");
                            } else {
                                output.push(infixFunction.substring(a + 1, comma1));
                                output.push("" + evaluate(infixFunction.substring(comma1 + 1, comma2)));
                                output.push("" + evaluate(infixFunction.substring(comma2 + 1, close)));
                                output.push("evalint");
                            }
                            a = close;
                            parsed = true;
                        }
                    }
                    if (!parsed) {
                        int fnIndex = -1;
                        for (int b = 2; b<=max_fn_string && a+b <= infixFunction.length(); b++){
                            fnIndex = multiParamFunctionNamesIndex(infixFunction.substring(a, a+b));
                            if (fnIndex != -1){
                                int lb = infixFunction.indexOf("(", a);
                                try{
                                    int rb = lb+Search.getIndices(infixFunction.substring(lb), ")").get(0);
                                    String[] parameters = infixFunction.substring(lb+1, a+rb).split(",");
                                    for (int i = 0; i < parameters.length; i++) {
                                        output.push(parameters[i].trim());
                                    }
                                    if (numParameters[fnIndex] == -1){
                                        output.push(parameters.length+"");
                                    }
                                    else if (numParameters[fnIndex] != parameters.length) {
                                        error = "Syntax Error";
                                        return error;
                                    }
                                    output.push(multiParamFunctions[fnIndex]);
                                    a = rb;
                                    parsed = true;
                                    break;
                                }catch(IndexOutOfBoundsException parameterLengthError){
                                    error = "Syntax Error";
                                    return error;
                                }
                            }
                        }
                        if (!parsed) {
                            int o1 = -1;
                            if (a+1 <= infixFunction.length()){
                                o1 = operatorIndex(c);
                            }
                            if (o1 != -1){
                                while (!stack.isEmpty() && operatorIndex(stack.peek()) == -1 && !stack.peek().equals("(")) {
                                    output.push(stack.pop());
                                }
                                if (stack.isEmpty() || operatorIndex(stack.peek()) == -1) {
                                    stack.push(String.valueOf(operators[o1]));
                                } else {
                                    while (true) {
                                        if (stack.empty()) {
                                            stack.push(String.valueOf(operators[o1]));
                                            break;
                                        }
                                        int o2 = operatorIndex(stack.peek());
                                        if (o2 >= 0 && ((associability[o1] && precedence[o1] <= precedence[o2]) || (!associability[o1] && precedence[o1] < precedence[o2]))) {
                                            output.push(stack.pop());
                                        } else {
                                            stack.push(String.valueOf(operators[o1]));
                                            break;
                                        }
                                    }
                                }
                            }
                            else{
                                for (int b = max_order_string; b>=2; b--){
                                    if (a+b <= infixFunction.length()){
                                        String mathf = infixFunction.substring(a, a+b);
                                        int orderIndex = orderIndex(mathf);
                                        if (orderIndex != -1){
                                            String superScript = "";
                                            for (int i = a + mathf.length(); i<infixFunction.length(); i++){
                                                int index = Scripts.getSuperScriptNumIndex(infixFunction.charAt(i));
                                                if (index != -1){
                                                    superScript += Scripts.regularScriptNums[index];
                                                }
                                                else{
                                                    break;
                                                }
                                            }
                                            if (stack.empty() || stack.peek().equals("(")
                                                    || (stack.size() > 0 && (orderIndex(stack.peek()) >= orderIndex) || operatorIndex(stack.peek()) != -1)) {
                                                if (superScript.length() > 0) {
                                                    stack.push("^");
                                                    stack.push(superScript);
                                                }
                                                stack.push(mathf);
                                                a += mathf.length()+superScript.length() - 1;
                                            } else if (stack.size() > 0 && orderIndex(stack.peek()) < orderIndex) {
                                                while (!stack.isEmpty()) {
                                                    String str = stack.peek();
                                                    if (orderIndex(stack.peek()) < orderIndex) {
                                                        output.push(stack.pop());
                                                    } else {
                                                        output.push(stack.pop());
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (NumberFormatException | StringIndexOutOfBoundsException e2) {
                    error = "Invalid Input Error 2";
                    return error;
                }
            }
        }
        if (token.length() > 0) {
            output.push(token);
            token = "";
        }
        while (!stack.empty()) {
            output.push(stack.pop());
        }
        for (int a = 0; a < output.size(); a++) {
            token += output.get(a) + " ";
        }
        return token.trim();
    }

    public static boolean orderContains(String item){
        return Search.contains(order, item);
    }
    public static int orderIndex(String item){
        return Search.binarySearch(order, item);
    }
    public static boolean startsWithOrder(String item){
        for (String function : order){
            if (item.startsWith(function)){
                return true;
            }
        }
        return false;
    }

    public static int operatorIndex(String item){
        return (item.length() == 1 ? Search.binarySearch(operators, item.charAt(0)) : -1);
    }
    public static int operatorIndex(char item){
        return Search.binarySearch(operators, item);
    }
    public static boolean operatorContains(String item){
        return (item.length() == 1 ? Search.contains(operators, item.charAt(0)) : false);
    }
    public static boolean operatorContains(char item){
        return Search.contains(operators, item);
    }

    public static int implicitIndex(char item){
        return Search.binarySearch(implicit, item);
    }
    public static boolean implicitContains(char item){
        return Search.contains(implicit, item);
    }

    public static int checkImplicitIndex(char item){
        return Search.binarySearch(checkImplicit, item);
    }
    public static boolean checkImplicitContains(String item){
        return Search.contains(checkImplicit, item.charAt(0));
    }
    public static boolean checkImplicitContains(char item){
        return Search.contains(checkImplicit, item);
    }

    public static boolean isMultiParamFunction(String item){
        return Search.contains(multiParamFunctions, item);
    }
    public static int multiParamFunctionNamesIndex(String item){
        return Search.binarySearch(multiParamFunctions, item);
    }

    public static char var(){
        return var.charAt(0);
    }
    public static char varOp(){
        return varOp.charAt(0);
    }

    public static List<String> getVariables() {
        return variables;
    }
    public static List<Double> getValues() {
        return values;
    }

    public boolean setVariable(String variable) {
        if (!variable.equalsIgnoreCase("e") && !variable.equalsIgnoreCase("π") && variable.length() == 1) {
            this.var = variable;
            return true;
        }
        return false;
    }

}