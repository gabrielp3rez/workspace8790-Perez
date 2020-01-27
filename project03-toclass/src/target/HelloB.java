package target;

public class HelloB extends Hello {
   String jB;
   int iB;

   public HelloB() {
      System.out.println("[ORG] HelloB Constructor called");
      jB = "John";
      iB = 200;
   }

   public void say() {
      System.out.println("[ORG] HelloB.say() called");
   }
}
