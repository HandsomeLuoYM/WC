import java.io.*;
import java.util.Scanner;

/**
 * @author Ming
 * @date 2020/3/10 - 18:49
 * @describe
 */
public class Wc {

    private static final String WORD_FORMAT = "[a-zA-Z]";

//    public static int

    public static void main(String[] args) throws IOException {
        //获取用户输入值
//        Scanner scanner = new Scanner(System.in);
//        String command = scanner.nextLine();
        File file = new File("D:\\idea_maven\\WC\\src\\a.c");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        int characterNumber = 0;
        String line = new String();
        while (null != (line = br.readLine())){
            characterNumber = characterNumber + line.length();
            System.out.println(line );
        }
        System.out.println(characterNumber);

//        if (file.isDirectory()){
//            String[] list = file.list();
//            for(String name:list){
//                System.out.println(name);
//            }
//        }
//        System.out.println(file.toString().length());
    }

}
