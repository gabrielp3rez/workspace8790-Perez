package ex09.substitutemethodbody;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class SubstituteMethodBody extends ClassLoader {
   static final String WORK_DIR = System.getProperty("user.dir");
   static final String INPUT_PATH = WORK_DIR + File.separator + "classfiles";

   static final String TARGET_MY_APP = "target.MyApp";
   static final String MOVE_METHOD = "move";
   static final String DRAW_METHOD = "draw";

   static String _L_ = System.lineSeparator();

   public static void main(String[] args) throws Throwable {
      SubstituteMethodBody s = new SubstituteMethodBody();
      Class<?> c = s.loadClass(TARGET_MY_APP);
      Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
      mainMethod.invoke(null, new Object[] { args });
   }

   private ClassPool pool;

   public SubstituteMethodBody() throws NotFoundException {
      pool = new ClassPool();
      pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
      pool.insertClassPath(INPUT_PATH); // "target" must be there.
      System.out.println("[DBG] Class Pathes: " + pool.toString());
   }

   /*
    * Finds a specified class. The bytecode for that class can be modified.
    */
   protected Class<?> findClass(String name) throws ClassNotFoundException {
      CtClass cc = null;
      try {
         cc = pool.get(name);
         if (!cc.getName().equals(TARGET_MY_APP)) {
            return defineClass(name, cc.toBytecode(), 0, cc.toBytecode().length);
         }

         cc.instrument(new ExprEditor() {
            public void edit(MethodCall call) throws CannotCompileException {
               String className = call.getClassName();
               String methodName = call.getMethodName();

               if (className.equals(TARGET_MY_APP) && methodName.equals(DRAW_METHOD)) {
                  System.out.println("[Edited by ClassLoader] method name: " + methodName + ", line: " + call.getLineNumber());
                  String block1 = "{" + _L_ //
                        + "System.out.println(\"Before a call to " + methodName + ".\"); " + _L_ //
                        + "$proceed($$); " + _L_ //
                        + "System.out.println(\"After a call to " + methodName + ".\"); " + _L_ //
                        + "}";
                  System.out.println("[DBG] BLOCK1: " + block1);
                  System.out.println("------------------------");
                  call.replace(block1);
               } else if (className.equals(TARGET_MY_APP) && methodName.equals(MOVE_METHOD)) {
                  System.out.println("[Edited by ClassLoader] method name: " + methodName + ", line: " + call.getLineNumber());
                  String block2 = "{" + _L_ //
                        + "System.out.println(\"\tReset param to zero.\"); " + _L_ //
                        + "$1 = 0; " + _L_ //
                        + "$proceed($$); " + _L_ //
                        + "}";
                  System.out.println("[DBG] BLOCK2: " + block2);
                  System.out.println("------------------------");
                  call.replace(block2);
               }
            }
         });
         byte[] b = cc.toBytecode();
         return defineClass(name, b, 0, b.length);
      } catch (NotFoundException e) {
         throw new ClassNotFoundException();
      } catch (IOException e) {
         throw new ClassNotFoundException();
      } catch (CannotCompileException e) {
         e.printStackTrace();
         throw new ClassNotFoundException();
      }
   }
}
