package ex04b.toclass;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;
import target.Hello;
import util.UtilMenu;

public class ToClass {
   private static final String PKG_NAME = "target" + ".";
   private static final String METHOD_NAME = "say";

   public static void main(String[] args) {
      while (true) {
         UtilMenu.showMenuOptions();
         switch (UtilMenu.getOption()) {
         case 1:
            System.out.println("Enter 3 inputs (e.g., HelloA,jA,iA or HelloB,jB,iB");
            String[] inputs = UtilMenu.getArguments();
            process(inputs[0], inputs[1], inputs[2]);
            break;
         default:
            break;
         }
      }
      // process("HelloA", "iA", "jA");
      // System.out.println("------------------------------------------");
      // process("HelloB", "iB", "jB");
      // System.out.println("==========================================");
   }

   static void process(String clazz, String field1, String field2) {
      try {
         ClassPool cp = ClassPool.getDefault();
         CtClass cc = cp.get(PKG_NAME + clazz);

         CtConstructor declaredConstructor = cc.getDeclaredConstructor(new CtClass[0]);
         String block1 = "{ " //
               + "System.out.println(\"[TR] " + field1 + ": \" + " + field1 + "); }";
         System.out.println("[DBG] BLOCK1: " + block1);
         declaredConstructor.insertAfter(block1);

         String block2 = "{ " //
               + "System.out.println(\"[TR] " + field2 + ": \" + " + field2 + "); }";
         System.out.println("[DBG] BLOCK2: " + block2);
         declaredConstructor.insertAfter(block2);

         CtMethod declaredMethod = cc.getDeclaredMethod(METHOD_NAME);
         String block3 = "{ " //
               + "System.out.println(\"[TR] Hello.say: \" + " + field2 + "); }";
         System.out.println("[DBG] BLOCK3: " + block3);
         declaredMethod.insertBefore(block3);

         Class<?> c = cc.toClass();
         Hello h = (Hello) c.newInstance(); // object instantiation
         h.say(); // operation
      } catch (NotFoundException | CannotCompileException | //
            InstantiationException | IllegalAccessException e) {
         System.out.println(e.toString());
      }
   }
}
