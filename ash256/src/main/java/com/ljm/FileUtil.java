package com.ljm;

import java.io.*;

/**
 * Package: com.ljm
 * Description：
 * Author: CWH
 * Date:  2020/9/5 13:50
 * Modified By:
 */
public class FileUtil {
    public static void main(String[] args) throws Exception{
        String data = readFile("C:\\\\Users\\\\Administrator\\\\Desktop\\\\code.txt");
        System.out.println(data);
    }

    public static void writeToFile(String content , String path) throws IOException {
        //String path = "/Users/shaonaiyi/datas/tmp/hello.txt";
        //win系统
        //String path = "C:\\Users\\Administrator\\Desktop\\code.txt";
        File file = new File(path);
        //String content = "hello,shaonaiyi.\n";
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(content.getBytes());
        fileOutputStream.close();
    }

    public static String readFile(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        //String path = "/Users/shaonaiyi/datas/tmp/hello.txt";
        //win系统
        //String path = "c:\\hello.txt";
        FileInputStream fileInputStream = new FileInputStream(path);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        fileInputStream.close();
        return sb.toString();
    }

}
