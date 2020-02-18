package newfield;

import java.io.File;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import target.Author;
import target.Column;
import target.Table;

public class AnnotatedFieldExample3 {
   static String workDir = System.getProperty("user.dir");
   static String inputDir = workDir + File.separator + "classfiles";
   static String outputDir = workDir + File.separator + "output";

   public static void main(String[] args) {
      try {
         ClassPool pool = ClassPool.getDefault();
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
      for (int i = 0; i < annoList.length; i++) {
         if (annoList[i] instanceof Table) {
            Table table = (Table) annoList[0];
            System.out.println("Table: " + table.name() + ", ID: " + table.id());
         } else if (annoList[i] instanceof Column) {
            Column column = (Column) annoList[i];
            System.out.println("Column: " + column.name() + ", ID: " + column.id());
         } else if (annoList[i] instanceof Author) {
            Author author = (Author) annoList[i];
            System.out.println("Name: " + author.name() + ", Year: " + author.year());
         }
      }
   }
}
