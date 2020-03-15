import sun.rmi.runtime.Log;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * @author Ming
 * @date 2020/3/10 - 18:49
 * @describe 简单统计文件数据代码
 */
@SuppressWarnings("unused")
public class Wc {

    //拆分单词的正则表达式
    private static final String SPILT_LINE_TO_WORD_REGEX = "\\s+|\\(|\\)|,|\\.|\\:|\\{|\\}|\\-|\\+|;|\\?|\\/|\\\\|/";
    //匹配单词的正则表达式
    private static final String WORD_REGEX = "[\\w]+";
    //判断单行注释的正则表达式
    private static final String SINGLE_EXPLAIN_REGEX = "\\s*(\\{|\\})?//.*";
    //判断多行注释行
    private static final String MULTIPLE_ROWS_EXPLAIN_REGEX_HEAD = "\\s*/\\*.*";
    private static final String MULTIPLE_ROWS_EXPLAIN_REGEX_TAIL = ".*\\*/";
    //空白行正则表达式
    private static final String BLANK_CODE_REGEX = "(\\{|\\})?\\s*";
    //文件路径格式正则表达式
    private static final String FILE_REGEX = "[a-zA-Z]:(\\\\([a-zA-Z0-9_]+.[a-zA-Z0-9_]{1,16}))+";
    //错误日志输出
    private static final Logger log = Logger.getLogger("Wc");
    //简单封装日志输出信息，防止多次new对象，在此可用枚举进一步封装
    private static final Map<String,String> LOG_MAP = new HashMap<String, String>();

    static {
        LOG_MAP.put("pathError","Error：文件路径 错误 或 不存在!");
        LOG_MAP.put("operationError","Error：输入的指令错误!");
        LOG_MAP.put("invalidError","Error：无效指令!");
    }

    /**
     * 递归判断文件
     * @param split 递归的指令集
     * @exception  IOException 直接抛出，不做处理
     */
    @SuppressWarnings("unused")
    public static void recursionOperation(String[] split) throws IOException {
        File file = new File(split[3]);
        if (file.isDirectory()){
            //判断为文件
            File[] files = file.listFiles();
            for(File f :files){
                split[3] = f.getPath();
                recursionOperation(split);
            }
        }else {
            //判断为非文件
            String fileName = file.getName();
            //判断是否为C后缀文件
            if ("c".equals(fileName.substring(fileName.lastIndexOf(".")+1,fileName.length()))){
                String[] operation = new String[]{split[0],split[2],file.getPath()};
                System.out.println(file.getPath());
                basicOperation(operation);
                System.out.println("-------------------------------------------------------------");
            }
        }
    }

    /**
     * 统计一些高级的数据
     * @param bufferedReader 缓存流读取器
     * @exception IOException IO流异常，直接抛出，不做处理
     */
    @SuppressWarnings("unused")
    public static void specialOperation(BufferedReader bufferedReader) throws IOException {

        //初始化统计数据参数
        int blankCount = 0, codeCount = 0, explainCount = 0;
        String line;
        boolean b = false;
        while (null != (line = bufferedReader.readLine())){
            if (line.matches(BLANK_CODE_REGEX)){
                blankCount++;
            }else if (line.matches(SINGLE_EXPLAIN_REGEX)){
                explainCount++;
            }else if (line.matches(MULTIPLE_ROWS_EXPLAIN_REGEX_HEAD)){
                explainCount++;
                if (!line.matches(MULTIPLE_ROWS_EXPLAIN_REGEX_TAIL)){
                    b = true;
                }
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
        System.out.println("空白行数：" + blankCount);
        bufferedReader.close();
    }

    /**
     * 判定用户操作
     * @param split 指令拆分集
     * @throws IOException IO流异常抛出
     */
    @SuppressWarnings("unused")
    public static void basicOperation(String[] split) throws IOException {

        //初始化统计数据参数
        int characterCount = 0, lineCount = 0, wordCount = 0;
        //进一步判断输入文件路径是否正确
        File file = new File(split[2]);
        String fileName = file.getName();
        if (!split[2].matches(FILE_REGEX) || !file.exists() || file.isDirectory()
                || !"c".equals(fileName.substring(fileName.lastIndexOf(".")+1,fileName.length()))){
            log.severe(LOG_MAP.get("pathError"));
            return;
        }
        //获取本地文件的缓存流读取器，方便后面操作
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(split[2])));
        //用于暂时存储行数据
        String line;
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
                while ((line = br.readLine()) != null){
                    for (String s : line.split(SPILT_LINE_TO_WORD_REGEX)){
                        if (s.matches(WORD_REGEX)){
                            wordCount ++;
                        }
                    }
                }
                System.out.println("单词数："+wordCount);
                break;
            case "-a" :
                specialOperation(br);
                break;
            default:
                log.warning(LOG_MAP.get("invalidError"));
        }
        br.close();
    }

    /**
     * 主类
     * @param args 输入参数
     * @throws IOException IO流异常，抛出，不做处理
     */
    @SuppressWarnings("unused")
    public static void main(String[] args) throws IOException {
        System.out.println("---------------------------------------------------------");
        System.out.println("| 程序功能 :                                            |");
        System.out.println("|-c：返回文件的字段数                                   |");
        System.out.println("|-w：返回文件的词的数                                   |");
        System.out.println("|-l：返回文件的行数                                     |");
        System.out.println("|-s：递归处理目录下符合条件的文件                       |");
        System.out.println("|-a：返回文件的代码行数/空白行数/注释行数               |");
        System.out.println("---------------------------------------------------------");
        //获取用户输入指令
        Scanner scanner = new Scanner(System.in);
        //循环来获取用户操作
        while (true){
            System.out.println("---------------------------------------------------------");
            System.out.println("请输入命令（格式：wc.exe [parameter] [file_path]）:");
            //将指令集拆分，并做简单判断格式
            String[] split = scanner.nextLine().split(" ");
            if("wc.exe".equals(split[0]) && 3 == split.length){
                basicOperation(split);
            }else if("wc.exe".equals(split[0]) && 4 == split.length){
                if (split[3].matches(FILE_REGEX) && new File(split[3]).exists()){
                    recursionOperation(split);
                }else {
                    log.severe(LOG_MAP.get("pathError"));
                }
            }else {
                log.severe(LOG_MAP.get("operationError"));
            }
        }
    }

}
