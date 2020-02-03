package classloader;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;

public class SampleLoader extends ClassLoader {
   static String WORK_DIR = System.getProperty("user.dir");
   static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
   static String TARGET_APP = "MyApp";
   private ClassPool pool;

   public static void main(String[] args) throws Throwable {
      SampleLoader loader = new SampleLoader();
      Class<?> c = loader.loadClass(TARGET_APP);
      c.getDeclaredMethod("main", new Class[] { String[].class }). //
            invoke(null, new Object[] { args });
      // First argument is null because its a static method
   }

   public SampleLoader() throws NotFoundException {
      pool = new ClassPool();
      pool.insertClassPath(INPUT_DIR); // Search MyApp.class in this path.
   }

   /* 
    * Find a specified class, and modify the bytecode.
    */
   protected Class<?> findClass(String name) throws ClassNotFoundException {
      try {
         CtClass cc = pool.get(name);
         if (name.equals("MyApp")) {
            CtField f = new CtField(CtClass.intType, "hiddenValue", cc);
            f.setModifiers(Modifier.PUBLIC);
            cc.addField(f);
         }
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
