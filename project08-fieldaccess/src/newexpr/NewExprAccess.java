package newexpr;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

public class NewExprAccess extends ClassLoader {
   static final String WORK_DIR = System.getProperty("user.dir");
   static final String CLASS_PATH = WORK_DIR + File.separator + "classfiles";
   static final String TARGET_MY_APP2 = "target.MyAppField";
   static String _L_ = System.lineSeparator();

   public static void main(String[] args) throws Throwable {
      NewExprAccess s = new NewExprAccess();
      Class<?> c = s.loadClass(TARGET_MY_APP2);
      Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
      mainMethod.invoke(null, new Object[] { args });
   }

   private ClassPool pool;

   public NewExprAccess() throws NotFoundException {
      pool = new ClassPool();
      pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
      pool.insertClassPath(CLASS_PATH); // TARGET must be there.
   }

   /*
    * Finds a specified class. The bytecode for that class can be modified.
    */
   protected Class<?> findClass(String name) throws ClassNotFoundException {
      CtClass cc = null;
      try {
         cc = pool.get(name);
         cc.instrument(new ExprEditor() {
            public void edit(NewExpr newExpr) throws CannotCompileException {
               try {
                  String longName = newExpr.getConstructor().getLongName();
                  if (longName.startsWith("java.")) {
                     return;
                  }
               } catch (NotFoundException e) {
                  e.printStackTrace();
               }
               String log = String.format("[Edited by ClassLoader] new expr: %s, " //
                     + "line: %d, signature: %s", newExpr.getEnclosingClass().getName(), //
                     newExpr.getLineNumber(), newExpr.getSignature());
               System.out.println(log);

               String block1 = "{ " + _L_ //
                     + " $_ = $proceed($$);" + _L_ //
                     + " System.out.print(" + "\"new expr: \" + " //
                     + " $_.getClass().getName() + " + "\", \" );" + _L_ //
                     + " System.out.print($_.getClass().getDeclaredFields()[0].getName()" //
                     + " + \": \" + " + "$_.x + " + "\", \" );" + _L_ //
                     + " System.out.println($_.getClass().getDeclaredFields()[1].getName()" //
                     + " + \": \" + " + "$_.y);" + _L_ //
               // + " System.out.println($type);" + _L_
                     + "}";
               System.out.println(block1);
               newExpr.replace(block1);
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