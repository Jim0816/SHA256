package com.ljm;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Package: com.ljm
 * Description：
 * Author: CWH
 * Date:  2020/9/4 11:59
 * Modified By:
 */
public class FinalDataUtil {

    /**
     *  1.准备64个质数
     * 获取自然数中前64个质数
     * 质数是指在 大于1 的自然数中，除了 1 和 它本身 以外不再有其他因数的自然数。
     */
    public static int[] getFront64PrimeNumber(){
        int[] front_64_primeNums = new int[64];
        int count = 0;
        for (int i=2 ; i<1000 ; i++){
            int flag = 0;//默认本数字没有其他因数
            for (int j=2 ; j<i ; j++){
                //因为1和本身都可以被整除，所以只需要查找2至本身-1之间的数字
                if(i%j == 0){
                    //存在其他因数，它不是质数,不需要再遍历了
                    flag = 1;
                    break;
                }
            }
            if(flag == 0){
                front_64_primeNums[count++] = i;
                if(count == 64)
                    return front_64_primeNums;//已经找到了64个质数
            }

        }
        return null;
    }

    /**
     * 获取整数powNum次方的小数部分前32位(即0.xxxxxxxxxx(32个x))
     * powNUm 表示
     */
    public static String getSquaBehindBits(int num , Double powNum){
        BigDecimal b =new BigDecimal(Math.pow(Double.valueOf(Integer.valueOf(num)),powNum));
        String []strSplits = b.toString().split("\\.");
        String res = strSplits[1].substring(0,32);
        StringBuilder sb = new StringBuilder("0.");
        sb.append(res);
        SysConvert sysConvert = SysConvert.getInstance();
        //10进制转16进制  此时sb.toString格式为0.xxxxxxxxxx(32个x)
        String result = sysConvert.Convert(10, 16, sb.toString(), 8).substring(1,9);
        return result;
    }



    /**
     * 获取8个hash初值
     */
    public static String[] getEightHashInitVal(){
        String [] res = new String[8];
        int[] primeNums = getFront64PrimeNumber();
        for (int i=0 ; i<8 ; i++){
            res[i] = getSquaBehindBits(primeNums[i],1.0/2.0);
        }
        return res;
    }

    /**
     * 获取64个常量值
     */
    public static String[] getHashFinalVal(){
        String [] res = new String[64];
        int[] primeNums = getFront64PrimeNumber();
        for (int i=0 ; i<64 ; i++){
            res[i] = getSquaBehindBits(primeNums[i],1.0/3.0);
        }
        return res;
    }


}
