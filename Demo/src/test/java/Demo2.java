import java.util.Arrays;
import java.util.Scanner;

/**
 * @program: taotao
 * @description:
 * @author: 钟兴青
 * @create: 2018-11-20 20:29
 **/
public class Demo2 {

    public static void main(String[] args) {

        test();

        System.out.println("-------------------------");
        int[] a = new int[]{4,5,1,6,3,7,8,2,9};

        Arrays.sort(a);
        System.out.println(a);
        System.out.println("-------------------------");

        for (int b : a){
            System.out.println(b);
        }
    }

    static void test(){
        Scanner input = new Scanner(System.in);
        int[] num = new int[6];
        for (int i = 0; i < num.length; i++) {
            num[i] = input.nextInt();
        }
        Arrays.sort(num);
        System.out.println("输入您要插入的数：");
        int insert = input.nextInt();
        for (int i = 0; i < num.length; i++) {
            if ((insert > num[i]) && (insert < num[i - 1])) {
                num[i] = insert;
                break;
            }
        }
        for (int i = 0; i < num.length; i++) {
            System.out.println(num[i]);
        }

    }
}
