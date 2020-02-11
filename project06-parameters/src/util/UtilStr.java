package util;

public class UtilStr {
   public static void print(String str) {
      final int COLUMN_SIZE = 80;
      int cnt = 0;
      char[] words = str.toCharArray();
      for (int i = 0; i < words.length; i++) {
         System.out.print(words[i]);
         if (cnt++ > COLUMN_SIZE) {
            System.out.println();
            cnt = 0;
         }
      }
      System.out.println();
   }
}
