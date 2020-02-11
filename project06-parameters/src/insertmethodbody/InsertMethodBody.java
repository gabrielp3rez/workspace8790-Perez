package insertmethodbody;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import util.UtilFile;

public class InsertMethodBody {
   static String WORK_DIR = System.getProperty("user.dir");
   static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
   static String OUTPUT_DIR = WORK_DIR + File.separator + "output";

   static String _L_ = System.lineSeparator();

   public static void main(String[] args) {
      try {
         ClassPool pool = ClassPool.getDefault();
         pool.insertClassPath(INPUT_DIR);
         CtClass cc = pool.get("target.Point");
         CtMethod m = cc.getDeclaredMethod("move");
         String block1 = "{ " + _L_ //
               + "System.out.println(\"[DBG] param1: \" + $1); " + _L_ //
               + "System.out.println(\"[DBG] param2: \" + $2); " + _L_ + //
               "}";
         System.out.println(block1);
         m.insertBefore(block1);
         cc.writeFile(OUTPUT_DIR);
         System.out.println("[DBG] write output to: " + OUTPUT_DIR);
         System.out.println("[DBG] \t" + UtilFile.getShortFileName(OUTPUT_DIR));
      } catch (NotFoundException | CannotCompileException | IOException e) {
         e.printStackTrace();
      }
   }
}
