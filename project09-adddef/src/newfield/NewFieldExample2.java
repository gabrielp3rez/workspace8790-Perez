package newfield;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

public class NewFieldExample2 {
   static String workDir   = System.getProperty("user.dir");
   static String inputDir  = workDir + File.separator + "classfiles";
   static String outputDir = workDir + File.separator + "output";

   public static void main(String[] args) {
      try {
         ClassPool pool = ClassPool.getDefault();
         pool.insertClassPath(inputDir);
         CtClass pointClass = pool.get("target.Point");

         CtField f = new CtField(CtClass.intType, "z", pointClass);
         pointClass.addField(f, "0"); // an initial value of the added field.

         pointClass.writeFile(outputDir);
         System.out.println("[DBG] write output to: " + outputDir);
      } catch (NotFoundException | CannotCompileException | IOException e) {
         e.printStackTrace();
      }
   }
}
