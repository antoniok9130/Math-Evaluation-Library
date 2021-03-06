package Math_Evaluation_Library.Miscellaneous;

import Math_Evaluation_Library.Engine.Engine;
import Math_Evaluation_Library.Search;

/**
 * Created by Antonio on 2017-07-13.
 */
public class Format {

    public static String format(String str, String... keys){
        if (!startsWith(keys, "{x}")){
            str = replaceAll(str, "{x}", Engine.var);
        }
        if (!startsWith(keys, "{y}")){
            str = replaceAll(str, "{y}", Engine.varOp);
        }
        for (int i = 0; i<keys.length; i++){
            String[] pair = keys[i].split(":");
            if (pair.length == 2) {
                pair[0] = pair[0].trim();
                pair[1] = pair[1].trim();
                if (!pair[0].equals(pair[1])) {
                    str = replaceAll(str, pair[0], pair[1]);
                }
            }
        }
        int index = str.indexOf("[");
        while (index != -1){
            int end = str.indexOf("]", index);
            str = replaceAll(str, str.substring(index, end+1), Engine.evaluateString(str.substring(index+1, end)));
            index = str.indexOf("[");
        }
        return str;
    }

    public static String replaceAll(String str, String replace, String replacement){
        String replaced = "";
        for (int i = 0; i<str.length();){
            int index = i+replace.length();
            if (index <= str.length() && str.substring(i, index).equals(replace)){
                replaced += replacement;
                i += replace.length();
            }
            else{
                replaced += str.charAt(i)+"";
                i++;
            }
        }
        return replaced;
    }

    public static boolean startsWith(String[] array, String start){
        for (String str : array){
            if (str.startsWith(start)){
                return true;
            }
        }
        return false;
    }


    public final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j]-Byte.MIN_VALUE;
            hexChars[j * 2] = hexArray[v/16];
            hexChars[j * 2 + 1] = hexArray[v%16];
        }
        return new String(hexChars);
    }
    public static byte[] hexToBytes(String hex) {
        char[] hexChars = hex.toCharArray();
        byte[] bytes = new byte[hexChars.length/2];
        for (int i = 0; i<bytes.length; ++i){
            bytes[i] = (byte)(Byte.MIN_VALUE+16* Search.linearSearch(hexArray, hexChars[2*i])+Search.linearSearch(hexArray, hexChars[2*i+1]));
        }
        return bytes;
    }
    public static String charsToHex(char[] chars) {
        StringBuilder hex = new StringBuilder();
        for ( int j = 0; j < chars.length; j++ ) {
            hex.append(String.format("%2s", Integer.toHexString(chars[j])).replace(' ', '0'));
        }
        return hex.toString();
    }

}
