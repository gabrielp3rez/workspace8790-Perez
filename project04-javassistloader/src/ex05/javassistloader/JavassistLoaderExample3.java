package ex05.javassistloader;

import java.io.File;
import java.lang.reflect.Method;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;
import javassist.NotFoundException;
import util.UtilMenu;

public class JavassistLoaderExample3 {
   private static final String WORK_DIR = System.getProperty("user.dir");
   private static final String TARGET_POINT = "target.Point";
   private static final String TARGET_RECTANGLE = "target.Rectangle";

   public static void main(String[] args) {

      while (true) {
         UtilMenu.showMenuOptions();
         int option = UtilMenu.getOption();
         switch (option) {
         case 1:
            System.out.println("[DBG] Enter (1) the method to be modified and\n" //
                  + "(2) a method call to be inserted (e.g., getVal: move(10, 20);):");
            String[] arguments = UtilMenu.getArguments();
            analysisProcess(arguments[0], arguments[1]);
            break;
         default:
            break;
         }
      }
   }

   static void analysisProcess(String methodDecl, String methodCall) {
      try {
         ClassPool cp = ClassPool.getDefault();
         insertClassPath(cp);

         CtClass cc = cp.get(TARGET_RECTANGLE);
         cc.setSuperclass(cp.get(TARGET_POINT));
         CtMethod m1 = cc.getDeclaredMethod(methodDecl);
         String BLK1 = "\n{\n" //
               + "\t" + methodCall + "" + "\n" //
               + "\t" + "System.out.println(\"[TR] getX result : \" + getX()); " + "\n" + "}";
         System.out.println("[DBG] Block: " + BLK1);
         m1.insertBefore(BLK1);

         Loader cl = new Loader(cp);
         Class<?> c = cl.loadClass(TARGET_RECTANGLE);
         Object rect = c.newInstance();
         System.out.println("[DBG] Created a Rectangle object.");

         Class<?> rectClass = rect.getClass();
         Method m = rectClass.getDeclaredMethod("getVal", new Class[] {});
         System.out.println("[DBG] Called getDeclaredMethod.");
         Object invoker = m.invoke(rect, new Object[] {});
         System.out.println("[DBG] getVal result: " + invoker);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   static void insertClassPath(ClassPool pool) throws NotFoundException {
      String strClassPath = WORK_DIR + File.separator + "classfiles";
      pool.insertClassPath(strClassPath);
      System.out.println("[DBG] insert classpath: " + strClassPath);
   }
}
