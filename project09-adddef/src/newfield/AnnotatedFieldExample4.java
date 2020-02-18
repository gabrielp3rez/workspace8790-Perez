package newfield;

import java.io.File;
import java.lang.reflect.Proxy;
import java.util.Iterator;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationImpl;
import javassist.bytecode.annotation.MemberValue;

public class AnnotatedFieldExample4 {
   static String workDir = System.getProperty("user.dir");
   static String inputDir = workDir + File.separator + "classfiles";
   static String outputDir = workDir + File.separator + "output";
   static ClassPool pool;

   public static void main(String[] args) {
      try {
         pool = ClassPool.getDefault();
         pool.insertClassPath(inputDir);

         CtClass ct = pool.get("target.AnnotatedPoint");
         CtField cf = ct.getField("x");

         process(ct.getAnnotations());
         System.out.println();

         process(cf.getAnnotations());
      } catch (NotFoundException | ClassNotFoundException e) {
         e.printStackTrace();
      }
   }

   static void process(Object[] annoList) {
      final String TARGET_TABLE = "target.Table";
      final String TARGET_COLUMN = "target.Column";
      final String TARGET_AUTHOR = "target.Author";

      for (int i = 0; i < annoList.length; i++) {
         Annotation annotation = getAnnotation(annoList[i]);
         if (annotation.getTypeName().equals(TARGET_TABLE)) {
            showAnnotation(annotation);
         }
         else if (annotation.getTypeName().equals(TARGET_COLUMN)) {
            showAnnotation(annotation);
         }
         else if (annotation.getTypeName().equals(TARGET_AUTHOR)) {
            showAnnotation(annotation);
         }
      }
   }

   static Annotation getAnnotation(Object obj) {
      // Get the underlying type of a proxy object in java
      AnnotationImpl annotationImpl = //
            (AnnotationImpl) Proxy.getInvocationHandler(obj);
      return annotationImpl.getAnnotation();
   }

   static void showAnnotation(Annotation annotation) {
      Iterator<?> iterator = annotation.getMemberNames().iterator();
      while (iterator.hasNext()) {
         Object keyObj = (Object) iterator.next();
         MemberValue value = annotation.getMemberValue(keyObj.toString());
         System.out.println("[DBG] " + keyObj + ": " + value);
      }

   }
}
