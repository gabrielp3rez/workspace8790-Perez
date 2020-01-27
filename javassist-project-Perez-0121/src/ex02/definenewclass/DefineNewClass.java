package ex02.definenewclass;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class DefineNewClass {
	static String workDir = System.getProperty("user.dir");
	static String outputDir = workDir + File.separator + "output";
	static Scanner scan;
	
	public static void main(String[] args)
	{
		scan = new Scanner(System.in);
		boolean valid = false;
		String[] inputs;
		
		// Get input classes from scanner
		do {
			System.out.print("Enter three classes separated by a comma: ");
			inputs = getInputs();
			
			// Need to be three classes
			if (inputs.length == 3)
			{
				valid = true;
			}
			else
			{
				System.out.println( "[WRN] Invalid Input");
			}
		} while (!valid);
		
		List<String> common = new ArrayList<String>();

		for (int i = 0; i <= 2; i++)
		{
			if(inputs[i].startsWith("Common"))
			common.add(inputs[i]);
		}
		
		String superClassName = getSuperClassName(common, inputs);
		List<String> childClasses = getChildClasses(superClassName, inputs);
		try
		{
			ClassPool pool = ClassPool.getDefault();
			insertClassPath(pool);
			
			CtClass superClass = makeClass(pool, superClassName);
			superClass.writeFile(outputDir);
			
			CtClass childClass1 = makeClass(pool, childClasses.get(0));
			childClass1.writeFile(outputDir);
			
			CtClass childClass2 = makeClass(pool, childClasses.get(1));
			childClass2.writeFile(outputDir);
			
			childClass1.defrost();
			childClass2.defrost();
			
			childClass1.setSuperclass(superClass);
			childClass2.setSuperclass(superClass);
			
			childClass1.writeFile(outputDir);
			childClass2.writeFile(outputDir);
			
	        System.out.println("[DBG] write output to: " + outputDir);
		}
		catch( CannotCompileException | IOException | NotFoundException e )
		{
			e.printStackTrace();
		}
	}
	
	public static CtClass makeClass(ClassPool pool, String newClassName)
	{
		CtClass cc = pool.makeClass(newClassName);
		System.out.println("[DBG] make class: " + cc.getName());
	    return cc;
	}
	
	public static String[] getInputs() {
	      String input = scan.nextLine();

	      List<String> list = new ArrayList<String>();
	      String[] inputArr = input.split(",");
	      for (String iElem : inputArr) {
	         list.add(iElem.trim());
	      }
	      return list.toArray(new String[0]);
	   }
	
	/*
	 * Method to determine the class that should
	 * be the super class
	 * @param common, args
	 * @retrun the super class
	 */
	static String getSuperClassName( List<String> common, String[] inputs )
	{
		String superClass = "";
		// If only one class name is in the common list, then it should be the super class
		if(common.size() == 1)
		{
			superClass = common.get(0);
		}
		// else if there are multiple classes prefixed with common, then iterate through them
		// and determine the longest one
		else if( common.size() >= 1 )
		{
			for( String s: common)
			{
				if( s.length() > superClass.length() )
				{
					superClass = s;
				}
			}
		}
		// else, use the first argument as the super class
		else
		{
			superClass = inputs[0];
		}
		return superClass;
	}
	
	/*
	 * Method to determine the child classes
	 * @param superClass, args
	 * @return the child classes
	 */
	static List<String> getChildClasses(String superClass, String[] inputs)
	{
		List<String> childClasses = new ArrayList<String>();
		// Iterate through args array
		for(int i = 0; i <= 2; i++)
		{
			// If the argument class != super class, then add it to the child class list
			if( !inputs[i].equals(superClass) )
			{
				childClasses.add(inputs[i]);
			}
		}
		return childClasses;
	}
	
	public static void insertClassPath(ClassPool pool) throws NotFoundException 
	{
		String strClassPath = outputDir;
	    pool.insertClassPath(strClassPath);
	    System.out.println("[DBG] insert classpath: " + strClassPath);
	}
}
