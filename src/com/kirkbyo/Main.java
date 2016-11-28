package com.kirkbyo;

public class Main {

    public static void main(String[] args) {
        Window window = new Window();
        window.create();

        boolean words = new WordSearch().fileContainsWord("car");
        System.out.print(words);
    }
}