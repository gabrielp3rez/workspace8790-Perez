package annotation;

import java.io.File;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import target.Author;
import target.Column;
import target.Row;
import target.Table;
import util.UtilMenu;

public class AnnotatedFieldExample {
	static String workDir = System.getProperty("user.dir");
	static String inputDir = workDir + File.separator + "classfiles";
	static String outputDir = workDir + File.separator + "output";
	
	
	public static void main(String[] args) 
	{
		while(true)
		{
			UtilMenu.showMenuOptions();
			int option = UtilMenu.getOption();
			switch(option)
			{
			case 1:
				System.out.println("Enter the name of a class, and the name of two annotations separated by a comma (e.g., ComponentApp,Column,Author or ServiceApp,Row,Author): ");
				String[] input = UtilMenu.getArguments();
				if(input.length != 3)
				{
					System.out.println("[WRN] Invalid input size!!");
					break;
				}
				else
				{
					String className = input[0];
					String annotation1 = input[1];
					String annotation2 = input[2];
					
					try {
						ClassPool pool = ClassPool.getDefault();
						pool.insertClassPath(inputDir);
						CtClass ct = pool.get("target." + className);
						
						CtField[] fields = ct.getDeclaredFields();
						process(fields,annotation1,annotation2);
						System.out.println();
						
					} catch (NotFoundException | ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			
			}
		}
	}
	
	/*
	 * Goes through fields in the class, and each annotation of that field
	 * and if an annotation of that field is an instance of annotation1
	 * then it calls the getAttributes method
	 * @param: fields, annotation1, annotation2
	 */
	static void process(CtField[] fields, String annotation1, String annotation2) throws ClassNotFoundException
	{
		for(int i = 0; i < fields.length; i++)
		{
			Object[] annoList = fields[i].getAnnotations();
			for(int j = 0; j < annoList.length; j++)
			{
				if(Class.forName("target." + annotation1).isInstance(annoList[j]))
				{
					getAttributes(annoList, annotation2);
					break;
				}
			}
		}
	}
	
	/*
	 * Goes through array of annotations and prints out the
	 * attributes if the annotation is an instance of annotation2
	 * @param: annotation list, annotation2
	 */
	static void getAttributes(Object[] annoList, String annotation2) throws ClassNotFoundException
	{
		for(int i = 0; i < annoList.length; i++)
		{
			if(Class.forName("target." + annotation2).isInstance(annoList[i]))
			{
				if(annotation2.equals("Author"))
				{
					Author author = (Author) annoList[i];
					System.out.println("Name: " + author.name() + ", Year: " + author.year());
				}
				if(annotation2.equals("Column"))
				{
					Column column = (Column) annoList[i];
					System.out.println("ID: " + column.id() + ", Name: " + column.name());
				}
				if(annotation2.equals("Row"))
				{
					Row row = (Row) annoList[i];
					System.out.println("Name: " + row.name() + ", ID: " + row.id());
				}
				if(annotation2.equals("Table"))
				{
					Table table = (Table) annoList[i];
					System.out.println("ID: " + table.id() + ", Name: " + table.id());
				}
			}
		}
	}
}
