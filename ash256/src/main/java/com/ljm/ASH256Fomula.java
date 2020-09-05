package com.ljm;

/**
 * Package: com.ljm
 * Description：ASH256算法中使用到的基本公式
 * Author: Jim
 * Date:  2020/9/4 17:52
 * Modified By:
 */
public class ASH256Fomula {

    /**
     * 公式一: σ0(x)=S7(x)⊕S18(x)⊕R3(x)
     * S7:循环右移7位
     * R3:右移3位
     *
     * word为传入的一个字
     */
    public static String fom1(String word){
        String res = basicDiffOr(basicCircleRightMove(word,7),basicCircleRightMove(word,18));
        String result = basicDiffOr(res,basicRightMove(word,3));
        return result;
    }

    /**
     * 公式二: σ1(x)=S17(x)⊕S19(x)⊕R10(x)
     *
     * word为传入的一个字
     */
    public static String fom2(String word){
        String res = basicDiffOr(basicCircleRightMove(word,17),basicCircleRightMove(word,19));
        String result = basicDiffOr(res,basicRightMove(word,10));
        return result;
    }

    /**
     * 原理图中田字格算法
     * 两个数的和如果大于2^32 结果取和%（2^32）余数
     * 两个数的和如果不大于2^32 结果取和
     */
    public static String fomSpecial(String x, String y,String z){
        Long sum = null;
        if(z.trim().equals("")){
            sum = transformToDecimalInt(add(x,y));
        }else{
            sum = transformToDecimalInt(add(add(x,y),z));
        }

        Long base = Long.parseLong("4294967296");
        Long res = null;
        if(sum > base){
            res = sum % base;
        }else{
            res = sum;
        }
        return transformToBinaryString(res);
    }


    /**
     * 公式三: Σ0(x)=S2(x)⊕S13(x)⊕S22(x)
     *
     * word为传入的一个字
     */
    public static String fom3(String word){
        String res = basicDiffOr(basicCircleRightMove(word,2),basicCircleRightMove(word,13));
        String result = basicDiffOr(res,basicCircleRightMove(word,22));
        return result;
    }

    /**
     * 公式四: Σ1(x)=S6(x)⊕S11(x)⊕S25(x)
     *
     * word为传入的一个字
     */
    public static String fom4(String word){
        String res = basicDiffOr(basicCircleRightMove(word,6),basicCircleRightMove(word,11));
        String result = basicDiffOr(res,basicCircleRightMove(word,25));
        return result;
    }

    /**
     * 公式五: Ch(x,y,z)=(x∧y)⊕(¬x∧z)
     *
     * word为传入的一个字
     */
    public static String fom5(String wordX,String wordY,String wordZ){
        String res1 = basicAnd(wordX,wordY);
        String res2 = basicAnd(basicNot(wordX),wordZ);
        return basicDiffOr(res1,res2);
    }

    /**
     * 公式六: Ma(x,y,z)=(x∧y)⊕(x∧z)⊕(y∧z)
     *
     * word为传入的一个字
     */
    public static String fom6(String wordX,String wordY,String wordZ){
        String res1 = basicAnd(wordX,wordY);
        String res2 = basicAnd(wordX,wordZ);
        String res3 = basicAnd(wordY,wordZ);
        return basicDiffOr(basicDiffOr(res1,res2),res3);
    }

    /**
     * 循环右移（基础公式）
     */
    public static String basicCircleRightMove(String x , int move){
        char[] rt=new char[x.length()];
        for(int i=0;i<x.length();i++)
            rt[i]=x.charAt(i);
        for(int i=0;i<x.length();i++) {
            if(i>x.length()-(move+1))
                rt[(i%move)]=x.charAt(i);
            else rt[i+move]=x.charAt(i);
        }
        String result = new String(rt);
        return result;
    }

    /**
     * 右移（基础公式）
     */
    public static String basicRightMove(String x , int move){
        StringBuilder sb = new StringBuilder();
        for(int i=0 ; i<move ; i++){
            sb.append('0');
        }
        String suffix = x.substring(0,x.length()-move);
        sb.append(suffix);
        return sb.toString();
    }

    /**
     * 异或（基础公式）
     */
    public static String basicDiffOr(String x , String y){
        if(x.length() != y.length()){
            return "位数不相同，无法运算!";
        }

        StringBuilder sb = new StringBuilder();
        for(int i=0 ; i<x.length() ; i++){
            if(x.charAt(i) == y.charAt(i) ){
                sb.append('0');
            }else{
                sb.append('1');
            }
        }
        return sb.toString();
    }

    /**
     * 非（基础公式）
     */
    public static String basicNot(String x){
        StringBuilder sb = new StringBuilder();
        for(int i=0 ; i<x.length() ; i++){
            if(x.charAt(i) == '1'){
                sb.append('0');
            }else{
                sb.append('1');
            }
        }
        return sb.toString();
    }

    /**
     * 与（基础公式）
     */
    public static String basicAnd(String x , String y){
        if(x.length() != y.length()){
            return "位数不相同，无法运算!";
        }

        StringBuilder sb = new StringBuilder();
        for(int i=0 ; i<x.length() ; i++){
            if(x.charAt(i) == '1' && y.charAt(i) == '1'){
                sb.append('1');
            }else{
                sb.append('0');
            }
        }
        return sb.toString();
    }

    /**
     * 二进制字符串加法   (存在bug)
     */
    public static String binaryStringAddtion(String x , String y){
        //进位标记
        int length = x.length();
        int [] carryFlag = new int[length];
        char [] result = new char[length];

        for (int i=0 ; i < length ; i++){
            carryFlag[i] = 0;//默认没有进位
        }

        for (int j=0 ; j < length ; j++){
            int x_val = x.charAt(j)=='1'?1:0;
            int y_val = y.charAt(j)=='1'?1:0;
            int sum = x_val + y_val + carryFlag[length-1-j];
            if(sum > 1 && j < length-1)
                carryFlag[length-1-(j+1)] = 1;//进位
            int value = sum > 1 ? sum -2 : sum;
            result[length-1-j] = String.valueOf(value).charAt(0);
        }
        String res = new String(result);
        return res;
    }

    /**
     * 二进制字符串加法   (来源于CSDN)
     */
    public static String add(String b1, String b2) {
        int len1 = b1.length();
        int len2 = b2.length();
        String s1 = b1;
        String s2 = b2;
        StringBuffer sb1 = new StringBuffer();
        //先将位数较少的二进制高位补零
        if(len1 > len2) {
            for(int i = 0; i < (len1 - len2); i++) {
                sb1.append(0);
            }
            sb1.append(b2);
            s1 = b1;
            s2 = sb1.toString();
        } else if(len1 < len2) {
            for(int j = 0; j < (len2 - len1); j++) {
                sb1.append(0);
            }
            sb1.append(b1);
            s1 = sb1.toString();
            s2 = b2;
        }
        //进位
        int flag = 0;
        StringBuffer sb2 = new StringBuffer();
        for(int z = s1.length() - 1; z >= 0; z--) {
            //字符’0’的对应的ASCII十进制是48
            //分情况判断
            if((s1.charAt(z) - 48) == 0 && (s2.charAt(z) - 48) == 0) {
                sb2.append(flag);
                flag = 0;
                continue;
            }
            if(((s1.charAt(z) - 48) == 0 && (s2.charAt(z) - 48) == 1 && flag == 0) || ((s1.charAt(z) - 48) == 1 && (s2.charAt(z) - 48) == 0 && flag == 0)) {
                sb2.append(1);
                flag = 0;
                continue;
            }
            if(((s1.charAt(z) - 48) == 0 && (s2.charAt(z) - 48) == 1 && flag == 1) || ((s1.charAt(z) - 48) == 1 && (s2.charAt(z) - 48) == 0 && flag == 1)) {
                sb2.append(0);
                flag = 1;
                continue;
            }
            if((s1.charAt(z) - 48) == 1 && (s2.charAt(z) - 48) == 1 && flag == 0) {
                sb2.append(0);
                flag = 1;
                continue;
            }
            if((s1.charAt(z) - 48) == 1 && (s2.charAt(z) - 48) == 1 && flag == 1) {
                sb2.append(1);
                flag = 1;
            }

        }
        if(flag == 1) {
            sb2.append(flag);
        }
        //倒置
        sb2.reverse();
        return sb2.toString();

    }

    /**
     * 16进制字符串转2进制字符串
     */
    public static String[] hexString2binaryString(String []hexStrings) {
        String [] result = new String[hexStrings.length];
        for (int j=0 ; j<hexStrings.length ; j++){
            String hexString = hexStrings[j];
            if (hexString == null || hexString.length() % 2 != 0)
                return null;
            String bString = "", tmp;
            for (int i = 0; i < hexString.length(); i++) {
                tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
                bString += tmp.substring(tmp.length() - 4);
            }
            result[j] = bString;
        }
        return result;
    }

    /**
     *十进制转二进制字符串（32bits）
     */
    public static String transformToBinaryString(Long num){
        StringBuilder sb = new StringBuilder();
        String binaryStringNum = Long.toBinaryString(num);
        if(binaryStringNum.length() < 32){
            for(int i=0 ; i<32-binaryStringNum.length() ; i++){
                sb.append('0');
            }
            sb.append(binaryStringNum);
        }else{
            sb.append(binaryStringNum.substring(binaryStringNum.length()-32,binaryStringNum.length()));
        }
        String result = sb.toString();
        return result;
    }

    /**
     *二制转字符串转十进制
     */
    public static Long transformToDecimalInt(String binaryString){
        Long result = Long.parseLong(binaryString, 2);
        return result;
    }

    /**
     *二制转字符串转十六进制字符串
     */
    public static String binaryStrToHexStr(String binaryStr){
        StringBuilder sb = new StringBuilder();
        for(int i=0 ; i<binaryStr.length() ; i+=4){
            String fourBinaryChars = binaryStr.substring(i,i+4);
            int decimalNum = Integer.parseInt(fourBinaryChars,2);
            String hexChar = Integer.toHexString(decimalNum);
            sb.append(hexChar);
        }
        sb.reverse();
        return sb.toString();
    }
}
