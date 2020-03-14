import java.io.*;
import java.util.Scanner;

/**
 * @author Ming
 * @date 2020/3/10 - 18:49
 * @describe
 */
@SuppressWarnings("unused")
public class Wc {

    //拆分单词的正则表达式
    private static final String SPILT_LINE_TO_WORD_REGEX = "\\s+|\\(|\\)|,|\\.|\\:|\\{|\\}|\\-|\\+|;|\\?|\\/|\\\\|/";
    //判断单行注释的正则表达式
    private static final String SINGLE_EXPLAIN_REGEX = "\\s*(\\{|\\})?//.*";
    //判断多行注释行
    private static final String MULTIPLE_ROWS_EXPLAIN_REGEX_HEAD = "\\s*/\\*.*";
    private static final String MULTIPLE_ROWS_EXPLAIN_REGEX_TAIL = ".*\\*/";
    //空白行正则表达式
    private static final String BLANK_CODE_REGEX = "(\\{|\\})?\\s*";
    //文件路径格式正则表达式
    private static final String FILE_REGEX = "[a-zA-Z]:(\\\\([a-zA-Z0-9_]+.[a-zA-Z0-9_]{1,16}))+";


    /**
     * 递归判断文件
     * @param file 文件
     * @return 返回名字集
     */
    public static String[] judgeFile(File file){
        if (file.isDirectory()){
            File[] files = file.listFiles();
            for(File f :files){
                judgeFile(f);
            }
        }else {
            String fileName = file.getName();
            if ("c".equals(fileName.substring(fileName.lastIndexOf(".")+1,fileName.length()))){
                System.out.println(file.getName());
            }
        }
        return null;
    }

    /**
     * 统计高级数据
     * @param bufferedReader 缓存流读取器
     * @exception IOException IO流异常
     */
    public static void specialOperation(BufferedReader bufferedReader) throws IOException {

        //初始化统计数据参数
        int blankCount = 0, codeCount = 0, explainCount = 0;
        String line;
        boolean b = false;
        while (null != (line = bufferedReader.readLine())){
            if (line.matches(BLANK_CODE_REGEX)){
                blankCount++;
            }else if (line.matches(MULTIPLE_ROWS_EXPLAIN_REGEX_HEAD)){
                explainCount++;
                b = true;
            }else if (b){
                explainCount++;
                if (line.matches(MULTIPLE_ROWS_EXPLAIN_REGEX_TAIL)){
                    b = false;
                }
            }else {
                codeCount++;
            }
        }
        System.out.println("代码行数：" + codeCount);
        System.out.println("注释行数：" + explainCount);
        System.out.println("空白行行数：" + blankCount);
    }

    /**
     * 判定用户操作
     * @param split 指令拆分集
     * @throws IOException IO流异常抛出
     */
    public static void basicOperation(String[] split) throws IOException {

        //初始化统计数据参数
        int characterCount = 0, lineCount = 0, wordCount = 0;
        File file = new File(split[2]);
        String fileName = file.getName();
        if (!split[2].matches(FILE_REGEX) || !file.exists() || file.isDirectory()
                || !"c".equals(fileName.substring(fileName.lastIndexOf(".")+1,fileName.length()))){
            System.out.println("Error：该路径非C语言文件 或 不存在 或 文件错误！");
            return;
        }
        //获取本地文件的缓存流读取器，方便后面操作
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(split[2])));
        //用于暂时存储行数据
        String line = new String();
        switch (split[1]){
            case "-c" :
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
                    words = line.split(SPILT_LINE_TO_WORD_REGEX);
                    wordCount += words.length;
                }
                System.out.println("单词数："+wordCount);
                break;
            case "-a" :
                specialOperation(br);
                break;
            default:
                System.out.println("输入错误！");
        }
        br.close();
    }

    public static void main(String[] args) throws IOException {

        //获取用户输入指令
        Scanner scanner = new Scanner(System.in);
        //循环来获取用户操作
        while (true){
            //将指令集拆分
            String[] split = scanner.nextLine().split(" ");
            if("wc.exe".equals(split[0]) && 3 == split.length){
                basicOperation(split);
            }else if("wc.exe".equals(split[0]) && 4 == split.length){

            }else {
                System.out.println("Error：输入的指令错误");
            }
        }

    }

}
