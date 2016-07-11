package com.skplanet.bisportal.testsupport;

public class Test {
    static int cnt = 4;
    static int arr[] = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    static int[] result = new int[cnt];
    public static void main(String[] args) {
        cases(0);
    }

    public static void cases(int n) {
        if(n == cnt) {
            for(int k = 0 ; k < result.length ; k++){
                System.out.print(result[k]);
            }
            System.out.println();
            return;
        }
        for(int i = 0 ; i < cnt ; i++){
            result[n] = arr[i];
            cases(n+1);
        }
    }
}