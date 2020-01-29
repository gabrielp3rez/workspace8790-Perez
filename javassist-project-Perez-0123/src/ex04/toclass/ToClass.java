package ex04.toclass;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;

public class ToClass
{	
	private static Scanner scan;
	private static final String PKG_NAME = "target" + ".";
	
	public static void main(String[] args)
	{
		scan = new Scanner(System.in);
		List<String> validClassNames = new ArrayList<String>();
		validClassNames.add("CommonComponentB");
		validClassNames.add("CommonServiceA");
		
		// Get input
		String className = getInput(validClassNames);

		try {
			// Get class pool and class
			ClassPool cp = ClassPool.getDefault();
			CtClass cc = cp.get(PKG_NAME + className);
			
			// Get constructor to be modified
			CtConstructor declaredConstructor = cc.getDeclaredConstructor(new CtClass[0]);
		
			// Block to insert
			String block = "{\n" 
					+ "\tSystem.out.println(\"id: \" + id);\n"
					+ "\tSystem.out.println(\"year: \" + year);\n"
					+ "}";
			
			System.out.println("[DBG] BLOCK: " + block);
			
			// Insert block in constructor
			declaredConstructor.insertAfter(block);
			
			// Create instance of class to call constructor
			Class<?> rClass = cc.toClass();
			Object inst = rClass.newInstance();
			
		} catch (NotFoundException | CannotCompileException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Gets the input from the user
	 * @param validClassNames
	 * @return the class name
	 */
	public static String getInput(List<String> validClassNames)
	{
		boolean valid = false;
		String className = "";
		do
		{
			System.out.print("Enter a Class Name (CommonComponentB or CommonServiceA): ");
			// Get the inputs
			String[] arguments = scan.nextLine().split(",\\s*");
			
			// Should only be one input
			if (arguments.length != 1)
			{
				System.out.println("[WRN] Invalid Input");
			}
			else
			{
				// Valid class names are CommonComponentB or CommonServiceA
				if (!validClassNames.contains(arguments[0]))
				{
					System.out.println("[WRN] Invalid Input");
				}
				else
				{
					valid = true;
					className = arguments[0];
				}
			}
		
		} while(!valid);
		return className;
	}
}
