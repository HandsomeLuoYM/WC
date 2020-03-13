import java.io.*;
import java.util.Scanner;

/**
 * @author Ming
 * @date 2020/3/10 - 18:49
 * @describe
 */
public class Wc {

    //拆分单词的正则
    private static final String SPILT_LINE_TO_WORD = "\\s+|\\(|\\)|,|\\.|\\:|\\{|\\}|\\-|\\+|;|\\?|\\/|\\\\|/";
    private static final String WORD_FORMAT = "[a-zA-Z]";

    /**
     * 判定用户操作
     * @return
     * @throws IOException
     */
    public static void basicOperation(String[] split) throws IOException {

        //初始化统计数据参数
        int characterCount = 0;
        int lineCount = 0;
        int wordCount = 0;

        //获取本地文件的缓存流读取器，方便后面操作
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(split[2])));
        //用于暂时存储行数据
        String line = new String();
        switch (split[1]){
            case "-c" :
                //计算字符长度
                while (null != (line = br.readLine())){
                    characterCount +=  line.length();
                }
                System.out.println("字符数为：" + characterCount);
                break;
            case "-l" :
                while (br.readLine() != null){
                    lineCount++;
                }
                System.out.println("行数为：" + lineCount);
                break;
            case "-w" :
                String[] words;
                while ((line = br.readLine()) != null){
                    words = line.split(SPILT_LINE_TO_WORD);
                    wordCount += words.length;
                }
                System.out.println("单词数："+wordCount);
                break;
            default:
                System.out.println("输入错误！");
        }


    }

    public static void main(String[] args) throws IOException {
        //获取用户输入指令
        Scanner scanner = new Scanner(System.in);
        //循环来获取用户操作
        while (true){
            //将指令集拆分
            String[] split = scanner.nextLine().split(" ");

            if("wc.exe".equals(split[0])){
                basicOperation(split);
            }else {
                System.out.println("Error：输入的指令错误");
            }
        }
//        if (file.isDirectory()){
//            String[] list = file.list();
//            for(String name:list){
//                System.out.println(name);
//            }
//        }
//        System.out.println(file.toString().length());
    }

}
