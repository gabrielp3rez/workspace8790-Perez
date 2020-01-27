package ex04c;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;

public class DefineClass extends ClassLoader {
   static final String _S = File.separator;
   static final String INPUT_CLASS_LOC = "bin" + _S + "target" + _S + "Hello.class";
   static final String INPUT_CLASS_NAME = "target.Hello";

   public static void main(String args[]) {
      new DefineClass().process();
   }

   void process() {
      byte bytecode[] = new byte[10000];
      int len = read(bytecode);
      Class<?> c = defineClass(INPUT_CLASS_NAME, bytecode, 0, len);

      try {
         Method methodMain = c.getMethod("main", String[].class);
         System.out.println("[DBG] Method: " + methodMain.getName());
         System.out.println("------------------------------------------");
         methodMain.invoke(null, (Object) new String[] {});
         System.out.println("------------------------------------------");

         Method m = c.getMethod("foo");
         System.out.println("[DBG] Method: " + m.getName());
         System.out.println("------------------------------------------");

         m.invoke(c.newInstance());
         System.out.println("==========================================");
      } catch (Throwable x) {
         x.printStackTrace();
      }
   }

   private int read(byte[] bytecode) {
      int len = 0;
      try {
         FileInputStream fis = new FileInputStream(INPUT_CLASS_LOC);
         int nbytes;
         do {
            nbytes = fis.read(bytecode, len, bytecode.length - len);
            if (nbytes > 0) {
               len += nbytes;
            }
         } while (nbytes > 0);
         fis.close();
      } catch (Throwable x) {
         System.err.println("Cannot find " + INPUT_CLASS_LOC);
         x.printStackTrace();
      }
      return len;
   }
}