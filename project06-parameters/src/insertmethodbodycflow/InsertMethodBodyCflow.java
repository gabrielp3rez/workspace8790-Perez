package insertmethodbodycflow;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import util.UtilStr;

public class InsertMethodBodyCflow extends ClassLoader {
   static final String WORK_DIR = System.getProperty("user.dir");
   static String SEP = File.separator;
   static final String INPUT_PATH = WORK_DIR + SEP + "classfiles";
   static final String OUTPUT_PATH = WORK_DIR + SEP + "output";

   static final String FACT_METHOD = "fact";
   static final String TARGET_MYAPP = "target.MyAppFact";

   static String _L_ = System.lineSeparator();

   private ClassPool pool;

   public static void main(String[] args) throws Throwable {
      ClassPool defaultPool = ClassPool.getDefault();
      defaultPool.insertClassPath(INPUT_PATH);
      CtClass cc = defaultPool.get(TARGET_MYAPP);
      CtMethod m = cc.getDeclaredMethod(FACT_METHOD);
      m.useCflow(FACT_METHOD);
      String block1 = "{" + _L_ //
            + "if ($cflow(fact) == 0)" + _L_ + //
            "System.out.println(\"[MyAppFact Inserted] fact \" + $1);" + _L_ //
            + "}";
      System.out.println("[DBG] Insert:");
      System.out.println(block1);
      System.out.println("------------------------------------------");
      m.insertBefore(block1);
      cc.writeFile(OUTPUT_PATH);

      InsertMethodBodyCflow s = new InsertMethodBodyCflow();
      Class<?> c = s.loadClass(TARGET_MYAPP);
      Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
      mainMethod.invoke(null, new Object[] { args });
   }

   public InsertMethodBodyCflow() throws NotFoundException {
      pool = new ClassPool();
      pool.insertClassPath(OUTPUT_PATH); // TARGET must be there.
      UtilStr.print("[CLASS-LOADER] CLASS_PATH: " + INPUT_PATH);
      System.out.println("------------------------------------------");
   }

   protected Class<?> findClass(String name) throws ClassNotFoundException {
      CtClass cc = null;
      try {
         cc = pool.get(name);
         byte[] b = cc.toBytecode();
         return defineClass(name, b, 0, b.length);
      } catch (NotFoundException e) {
         throw new ClassNotFoundException();
      } catch (IOException e) {
         throw new ClassNotFoundException();
      } catch (CannotCompileException e) {
         throw new ClassNotFoundException();
      }
   }
}
