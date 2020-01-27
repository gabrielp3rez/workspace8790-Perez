package ex03.frozenclasses;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class FrozenClass {
   static String workDir = System.getProperty("user.dir");
   static String outputDir = workDir + File.separator + "output";

   public static void main(String[] args) {
      try {
         ClassPool pool = ClassPool.getDefault();
         insertClassPath(pool);

         CtClass ccPoint2 = pool.makeClass("Point2");
         ccPoint2.writeFile(outputDir); // debugWriteFile
         System.out.println("[DBG] write output to: " + outputDir);

         CtClass ccRectangle2 = pool.makeClass("Rectangle2");
         ccRectangle2.writeFile(outputDir);
         System.out.println("[DBG] write output to: " + outputDir);

         ccRectangle2.defrost(); // modifications of the class definition will be permitted.
         ccRectangle2.setSuperclass(ccPoint2);
         ccRectangle2.writeFile(outputDir);
         System.out.println("[DBG] write output to: " + outputDir);

      } catch (NotFoundException | CannotCompileException | IOException e) {
         e.printStackTrace();
      }
   }

   static void insertClassPath(ClassPool pool) throws NotFoundException {
      String strClassPath = outputDir;
      pool.insertClassPath(strClassPath);
      System.out.println("[DBG] insert classpath: " + strClassPath);
   }
}
