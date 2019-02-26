import ssm.pojo.Employee;

import java.io.*;
import java.util.UUID;

/**
 * @author 肥宅快乐码
 * @date 2019/2/24 - 17:07
 */
public class CreateSqlTest {

    public static void main(String args[]) {
        writeFile();
    }

    /**
     * 读入TXT文件
     */
    public static void readFile() {
        String pathname = "input.txt"; // 绝对路径或相对路径都可以，写入文件时演示相对路径,读取以上路径的input.txt文件
        //防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw;
        //不关闭文件会导致资源的泄露，读写文件都同理
        //Java7的try-with-resources可以优雅关闭文件，异常时自动关闭文件；详细解读https://stackoverflow.com/a/12665271
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String line;
            //网友推荐更加简洁的写法
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入TXT文件
     */
    public static void writeFile() {
        try {
            File writeName = new File("E://output.txt"); // 相对路径，如果没有则要建立一个新的output.txt文件
            writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
            try (FileWriter writer = new FileWriter(writeName);
                 BufferedWriter out = new BufferedWriter(writer)
            ) {
                for (int i = 0; i < 1000; i++) {
                    String  uid = UUID.randomUUID().toString().substring(0,5) + i;
                    out.write("insert into TBL_EMP(EMP_ID,EMP_NAME,GENDER,EMAIL,D_ID) VALUES(null, '"+ uid + "', 'M', '"+uid+"@qq.com', 1);\r\n");
                    out.write("insert into TBL_EMP(EMP_ID,EMP_NAME,GENDER,EMAIL,D_ID) VALUES(null, '"+ uid + "', 'M', '"+uid+"@qq.com', 2);\r\n");
                    out.write("insert into TBL_EMP(EMP_ID,EMP_NAME,GENDER,EMAIL,D_ID) VALUES(null, '"+ uid + "', 'M', '"+uid+"@qq.com', 3);\r\n");
                    out.flush(); // 把缓存区内容压入文件
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


