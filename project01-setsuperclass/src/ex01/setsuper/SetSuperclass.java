package ex01.setsuper;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import target.Rectangle;

public class SetSuperclass {
   static final String SEP = File.separator;
   static String workDir = System.getProperty("user.dir");
   static String outputDir = workDir + SEP + "output";

   public static void main(String[] args) {
      try {
         ClassPool pool = ClassPool.getDefault();

         boolean useRuntimeClass = true;
         if (useRuntimeClass) {
            insertClassPathRunTimeClass(pool);
         } else {
            insertClassPath(pool);
         }

         CtClass cc = pool.get("target.Rectangle");
         setSuperclass(cc, "target.Point", pool);

         cc.writeFile(outputDir);
         System.out.println("[DBG] write output to: " + outputDir);
      } catch (NotFoundException | CannotCompileException | IOException e) {
         e.printStackTrace();
      }
   }

   static void insertClassPathRunTimeClass(ClassPool pool) throws NotFoundException {
      ClassClassPath classPath = new ClassClassPath(new Rectangle().getClass());
      pool.insertClassPath(classPath);
      System.out.println("[DBG] insert classpath: " + classPath.toString());
   }

   static void insertClassPath(ClassPool pool) throws NotFoundException {
      String strClassPath = workDir + SEP + "bin"; // eclipse compile dir
      // String strClassPath = workDir + SEP + "classfiles"; // separate dir
      pool.insertClassPath(strClassPath);
      System.out.println("[DBG] insert classpath: " + strClassPath);
   }

   static void setSuperclass(CtClass curClass, String superClass, ClassPool pool) throws NotFoundException, CannotCompileException {
      curClass.setSuperclass(pool.get(superClass));
      System.out.println("[DBG] set superclass: " + curClass.getSuperclass().getName() + //
            ", subclass: " + curClass.getName());
   }
}
