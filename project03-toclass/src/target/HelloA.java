package target;

public class HelloA extends Hello {
   String jA;
   int iA;

   public HelloA() {
      System.out.println("[ORG] HelloA Constructor called");
      jA = "Smith";
      iA = 100;
   }

   public void say() {
      System.out.println("[ORG] HelloA.say() called");
   }
}
