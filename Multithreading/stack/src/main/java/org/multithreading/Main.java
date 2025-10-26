package org.multithreading;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        int x = 5;
        int y = 10;
        Integer result = add(x, y);
    }

    public static Integer add(Integer a, Integer b) {
        int sum = a + b;
        return sum;
    }
}