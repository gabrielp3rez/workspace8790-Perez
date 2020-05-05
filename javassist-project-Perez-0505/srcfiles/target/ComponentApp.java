package target;

public class ComponentApp {
   public void foo(int n, int m) {
      System.out.println("[DBG] foo called");
   }

   public void foo1(String n, int m) {
      System.out.println("[DBG] foo1 called");
   }

   public void foo2(int n, String m) {
      System.out.println("[DBG] foo2 called");
   }

   public static void main(String[] args) {
      ComponentApp f = new ComponentApp();
      f.foo(1, 2);
      f.foo1("str1", 2);
      f.foo2(1, "str2");
   }
}
