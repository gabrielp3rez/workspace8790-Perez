package ex04.toclass;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;

public class ToClass {
	
	private static Scanner scan;
	private static final String PKG_NAME = "target" + ".";
	public static void main(String[] args)
	{
		scan = new Scanner(System.in);
		List<String> validClassNames = new ArrayList<String>();
		validClassNames.add("CommonComponentB");
		validClassNames.add("CommonServiceA");
		
		String className = getInput(validClassNames);
		String methodName = className;
		System.out.println(methodName);
		try {
			ClassPool cp = ClassPool.getDefault();
			CtClass cc = cp.get(PKG_NAME + className);
			
			CtConstructor declaredConstructor = cc.getDeclaredConstructor(new CtClass[0]);
		
			String block = "{\n" 
					+ "\tSystem.out.println(\"id: \" + id);\n"
					+ "\tSystem.out.println(\"year: \" + year);\n"
					+ "}";
			
			System.out.println("[DBG] BLOCK: " + block);
			declaredConstructor.insertAfter(block);
			Class<?> rClass = cc.toClass();
			Object inst = rClass.newInstance();
			
		} catch (NotFoundException | CannotCompileException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getInput(List<String> validClassNames)
	{
		boolean valid = false;
		String className = "";
		do
		{
			System.out.print("Enter a Class Name (CommonComponentB or CommonServiceA): ");
			String[] arguments = scan.nextLine().split(",\\s*");
			if (arguments.length != 1)
			{
				System.out.println("[WRN] Invalid Input");
			}
			else
			{
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
