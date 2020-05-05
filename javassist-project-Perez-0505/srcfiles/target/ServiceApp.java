package target;

public class ServiceApp {
   public void bar(int n, int m) {
      System.out.println("[DBG] bar called");
   }

   public void bar1(String n, int m) {
      System.out.println("[DBG] bar1 called");
   }
   
   public void bar2(int n, String m) {
      System.out.println("[DBG] bar2 called");
   }

   public static void main(String[] args) {
      ServiceApp f = new ServiceApp();
      f.bar(10, 20);
      f.bar1("msg1", 20);
      f.bar2(10, "msg2");
   }
}
