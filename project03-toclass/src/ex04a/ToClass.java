package ex04a;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import target.Hello;

public class ToClass {
   private static final String METHOD_NAME = "say";
   private static final String PKG_NAME = "target" + ".";

   public static void main(String[] args) {
      process("Hello");
   }

   static void process(String clazz) {
      try {
         ClassPool cp = ClassPool.getDefault();
         CtClass cc = cp.get(PKG_NAME + clazz);

         CtMethod declaredMethod = cc.getDeclaredMethod(METHOD_NAME);
         String block = "{\n" //
               + "System.out.println(\"[TR] Calling Hello.say()\");" //
               + "\n}";
         System.out.println("[DBG] BLOCK: " + block);
         declaredMethod.insertBefore(block);
         Class<?> rClass = cc.toClass();

         Hello newInstance = (Hello) rClass.newInstance();
         newInstance.say();
         System.out.println("------------------------------------------");

         Method methodSay = rClass.getMethod("say");
         methodSay.invoke(rClass.newInstance());
         System.out.println("==========================================");
      } catch (NotFoundException | CannotCompileException | //
            InstantiationException | IllegalAccessException | //
            NoSuchMethodException | SecurityException | //
            IllegalArgumentException | InvocationTargetException e) {
         System.out.println(e.toString());
      }
   }
}
