package com.ljm;

import java.math.BigInteger;

/**
 * Package: com.ljm
 * Description：
 * Author: CWH
 * Date:  2020/9/4 10:39
 * Modified By:
 */
public class Test {
    private static final String []H0 = ASH256Fomula.hexString2binaryString(FinalDataUtil.getEightHashInitVal());
    private static final String []DATA = ASH256Fomula.hexString2binaryString(FinalDataUtil.getHashFinalVal());

    public static void main(String[] args) throws Exception {
        //1.准备好预处理和补位后的数据
        String initData = FileUtil.readFile("C:\\Users\\Administrator\\Desktop\\data\\data2..txt");
        String data = preprocessBinaryData(transformStringToBinary(initData));
        //2.将数据分块，每块大小为512bits
        String blocks[] = splitBlocks(data);
        //3.针对每块进行处理
        String hash[] = H0;
        for(String block : blocks){
            hash = circleSecret(block,hash);
        }
        StringBuilder code = new StringBuilder();
        for(int i=0 ; i<hash.length ; i++){
            code.append(hash[i]);
        }
        FileUtil.writeToFile(code.toString(),"C:\\Users\\Administrator\\Desktop\\data\\code2.txt");
    }

    /**
     * 将数据转换为2进制编码
     */
    public static String transformStringToBinary(String message){
        StringBuilder sb = new StringBuilder();
        for (int i=0 ; i<message.length(); i++){
            char theChar = message.charAt(i);
            int theChar_ascii=Integer.valueOf(theChar);
            sb.append(Integer.toBinaryString(theChar_ascii));
        }
        return sb.toString();
    }

    /**
     * 将2进制数据编码进行预处理并且补位
     */
    public static String preprocessBinaryData(String binaryMessage){
        Double messageLength = Double.valueOf(binaryMessage.length());
        StringBuilder sb = new StringBuilder(binaryMessage);
        if(messageLength > Math.pow(2.0,64.0)){
            //2^64表示整个二进制文本的最后64位表示文本的长度，即文本的二进制长度最大为2^64bits
            System.out.println("数据过大，无法加密!!!");
            return "数据过大，无法加密!!!";
        }else{
            //1.预处理(先补1，其他补0) 这里取模后为448是为了留出512-448 = 64位来控制数据长度
            int result = binaryMessage.length() % 512;
            sb.append("1");
            if(result != 448){
                if(result < 448){
                    int gap = 448 - result;
                    if(gap > 1){
                        for (int i=0 ; i<gap-1 ; i++){
                            sb.append("0");
                        }
                    }
                }else{
                    for (int i=0 ; i<512-(result-448)-1 ; i++){
                        sb.append("0");
                    }
                }
            }else{
                if(binaryMessage.length() == 448){
                    //若正好是448，也要补一个512bits
                    for (int i=0 ; i<511 ; i++){
                        sb.append("0");
                    }
                }
            }
            //2.补上表示数据长度的64bits
            BigInteger b = new BigInteger(String.valueOf(binaryMessage.length()));
            String dataSizeBinary = b.toString(2);
            StringBuilder stringBuilder = new StringBuilder();
            if(dataSizeBinary.length() < 64){
                int gap_1 = 64-dataSizeBinary.length();
                for (int i=0 ; i<gap_1 ; i++){
                    stringBuilder.append("0");
                }
                stringBuilder.append(dataSizeBinary);
            }
            sb.append(stringBuilder.toString());
        }
        return sb.toString();
    }

    /**
     * 传入已经处理后的数据
     * 对数据分块，块大小为512bits 由于预处理和部位让数据长度为n*512bits
     * 因此，块的数量为n
     */
    public static String[] splitBlocks(String processedBinaryMessage){
        String []blocks = new String[processedBinaryMessage.length()/512];
        int count = 0;
        int blockSize = 512;
        for(int i=0 ; i<processedBinaryMessage.length() ; i+=blockSize){
            blocks[count++] = processedBinaryMessage.substring(i,i+blockSize);
        }
        return blocks;
    }

    /**
     * 将块拓展为64个字（一个字为32bits）
     * 块中512bits先分为16*32bits，剩下48个字通过公式构建
     */
    public static String[] blockConstructToWords(String block){
        //1.先将该块拆分为前16个字  W[0]~W[15]
        String []words = new String[64];
        int count = 0;
        int wordSize = 32;
        for(int i=0 ; i<block.length() ; i+=wordSize){
            words[count++] = block.substring(i,i+wordSize);
        }
        //2.拓展其他48个bits W[16]~W[63]   公式Wt=σ1(Wt−2)+Wt−7+σ0(Wt−15)+Wt−16
        for(int j=16 ; j<64; j++){
            String s1 = ASH256Fomula.fom2(words[j-2]);
            String s2 = words[j-7];
            String s3 = ASH256Fomula.fom1(words[j-15]);
            String s4 = words[j-16];
            String sum1 = ASH256Fomula.add(s1,s2);
            String sum2 = ASH256Fomula.add(s3,s4);
            String sum3 = ASH256Fomula.add(sum1,sum2);
            String sum = sum3.substring(sum3.length()-32,sum3.length());//截取低32位
            words[j] = sum;
        }
        return words;
    }

    /**
     * 每个块和上一次Hash值产生当前新的Hash
     *
     */
    public static String[] circleSecret(String block,String hash[]){
        //3.1 将每块拓展为64*32bits 即64个字
        String []words = blockConstructToWords(block);
        //3.2进行64次循环加密
        String []now = new String[8];//保存本次加密结果
        for(int i=0 ; i<64 ; i++){
            String s1 = ASH256Fomula.fomSpecial(words[i],DATA[i],"");
            String ch_val = ASH256Fomula.fom5(hash[4],hash[5],hash[6]);
            String s2 = ASH256Fomula.fomSpecial(s1,ch_val,hash[7]);
            String e1_val = ASH256Fomula.fom4(hash[4]);
            String s3 = ASH256Fomula.fomSpecial(s2,e1_val,"");
            String ma_val = ASH256Fomula.fom6(hash[0],hash[1],hash[2]);
            String s4 = ASH256Fomula.fomSpecial(s3,ma_val,"");
            String e0_val = ASH256Fomula.fom3(hash[0]);
            String s5 = ASH256Fomula.fomSpecial(s4,e0_val,"");
            now[0] = s5;

            for(int j=1 ; j<8 ; j++){
                if(j!=4){
                    now[j] = hash[j-1];
                }else{
                    now[j] = ASH256Fomula.fomSpecial(hash[j-1],s3,"");
                }
            }
        }
        return now;
    }
}
