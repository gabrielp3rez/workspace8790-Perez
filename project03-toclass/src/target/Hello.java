package target;

public class Hello {
   public void say() {
      System.out.println("[ORG] Hello.say() called");
   }

   public static void bye() {
      System.out.println("[ORG] Hello.bye() called");
   }

   public static void main(String[] args) {
      System.out.println("[ORG] Hello.main() called");
      bye();
   }

   public void foo() {
      System.out.println("[ORG] Hello.foo() called");
      say();
   }
}
