package testdemo;

import java.util.Random;
import java.util.Scanner;

public class demo {


    public static void main(String[] args) {
        Random random=new Random();
        int x=random.nextInt(100)+1;
        Scanner scanner=new Scanner(System.in);
        int i=scanner.nextInt();
        System.out.println(x+i);
    }
}
