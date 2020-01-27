package ex02.definenewclass;

import java.io.File; 
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;

public class DefineNewClass {
   static String workDir = System.getProperty("user.dir");
   static String outputDir = workDir + File.separator + "output";

   public static void main(String[] args) {
      try {
         ClassPool pool = ClassPool.getDefault();

         CtClass cc = makeClass(pool, "Point2");
         cc.writeFile(outputDir);
         System.out.println("[DBG] write output to: " + outputDir);

         CtClass ccInterface = makeInterface(pool, "IPoint");
         ccInterface.writeFile(outputDir);
         System.out.println("[DBG] write output to: " + outputDir);
      } catch (CannotCompileException | IOException e) {
         e.printStackTrace();
      }
   }

   static CtClass makeClass(ClassPool pool, String newClassName) {
      CtClass cc = pool.makeClass(newClassName);
      System.out.println("[DBG] make class: " + cc.getName());
      return cc;
   }

   private static CtClass makeInterface(ClassPool pool, String newInterfaceName) {
      CtClass cc = pool.makeInterface(newInterfaceName);
      System.out.println("[DBG] make interface: " + cc.getName());
      return cc;
   }
}
