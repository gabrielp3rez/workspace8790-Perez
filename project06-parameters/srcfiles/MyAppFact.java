package target;

public class MyAppFact {
   public int fact(int n) {
      if (n <= 1)
         return n;
      else
         return n * fact(n - 1);
   }

   public void initiate() {
      int result = fact(5);
      System.out.println("[MyAppFact] result: " + result);
   }

   public static void main(String[] args) {
      MyAppFact f = new MyAppFact();
      f.initiate();
   }
}
