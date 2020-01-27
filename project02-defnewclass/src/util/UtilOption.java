package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

public class UtilOption {
   private static Scanner scanner = new Scanner(System.in);

   @Test
   public void testGetInputs() {
      System.out.println("=============================================");
      System.out.println("Example Program");

      while (true) {
         System.out.println("=============================================");
         System.out.print("Enter three classes (\"q\" to terminate)\n");
         String[] inputs = getInputs();
         for (int i = 0; i < inputs.length; i++) {
            System.out.println("[DBG] " + i + ": " + inputs[i]);
         }
      }
   }

   public static String[] getInputs() {
      String input = scanner.nextLine();
      if (input.trim().equalsIgnoreCase("q")) {
         System.err.println("Terminated.");
         System.exit(0);
      }
      List<String> list = new ArrayList<String>();
      String[] inputArr = input.split(",");
      for (String iElem : inputArr) {
         list.add(iElem.trim());
      }
      return list.toArray(new String[0]);
   }
}
