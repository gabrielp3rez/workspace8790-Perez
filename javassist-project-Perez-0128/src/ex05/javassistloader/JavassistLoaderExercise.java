package ex05.javassistloader;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;
import javassist.NotFoundException;
import util.UtilMenu;

public class JavassistLoaderExercise {
	private static final String WORK_DIR = System.getProperty("user.dir");
	
	private static final Scanner scan = new Scanner(System.in);
	private static Map<String, Boolean> isModified; // Map to track if a method has been modified

	
	public static void main(String[] args)
	{
		List<String> common = new ArrayList<String>();
		
		// Init the isModified map
		isModified = new HashMap<String,Boolean>();
		isModified.put("add", false);
		isModified.put("remove", false);
		
		// Init valid class names
		List<String> validClassNames = new ArrayList<String>();
		validClassNames.add("Point");
		validClassNames.add("Rectangle");
		validClassNames.add("Circle");
		
		while (true)
		{
			// Get menu options
			UtilMenu.showMenuOptions();
			int option = UtilMenu.getOption();
			switch (option)
			{
				case 1:
					// Get methods and classes
					String[] methods = getMethods();
					if(methods == null)
					{
						break;
					}
					String[] classes = getClasses();
					// Should be 3 classes
					if (classes.length != 3)
					{
						System.out.println("[WRN] Invalid Input Size!");
						break;
					}
					for(String className: classes)
					{
						if(className.startsWith("Common"))
						{
							common.add(className);
						}
					}
					String superClassName = getSuperClassName(common,classes);
					List<String> childClasses = getChildClasses(superClassName, classes);
					analysisProcess(methods, superClassName, childClasses);
			}
			
		}
	}
	
	/*
	 * Get the usage, increment, and getter methods
	 * @return array of method names
	 */
	public static String[] getMethods()
	{
		boolean valid = false;
		String[] methods;
		do
		{
			System.out.print("Enter a usage method, an increment method, and a getter method (e.g., add,incX,getX): ");
			// Split methods by a ','
			methods = scan.nextLine().split(",\\s*");
			
			// Usage methods should be add or remove
			if(methods[0].equals("add") || methods[0].equals("remove"))
			{
				// Check hash map to see if the method has been modified
				if(!isModified(methods[0]))
				{
					// If it hasn't been modified, set modified to true
					isModified.put(methods[0], true);
					// Increment methods should be incX or incY
					if(methods[1].equals("incX") || methods[1].equals("incY"))
					{
						// Getter methods should be getX or getY
						if(methods[2].equals("getX") || methods[2].equals("getY"))
						{
							valid = true;
						}
						else
						{
							System.out.println("[WRN] Invalid Getter Method");
						}
					}
					else
					{
						System.out.println("[WRN] Invalid Increment Method");
					}
				}
				else
				{
					System.out.println("[WRN] This method " + methods[0] + " has been modified");
					return null;
				}
			}
			else
			{
				System.out.println("[WRN] Invalid Usage Method");
			}
		} while(!valid);
		return methods;
	}
	
	/*
	 * Returns the class names
	 * @return array of class names
	 */
	public static String[] getClasses()
	{		
		// Split the input
		System.out.print("Enter 3 Class Names (e.g., Point,Circle,Rectangle): ");
		String[] classNames = scan.nextLine().split(",\\s*");
		
		return classNames;
	}
	
	/*
	 * Returns the value of the key value pair of the isModified map
	 * @return boolean value
	 */
	public static boolean isModified(String method)
	{
		return isModified.get(method);
	}
	
	/*
	 * Method to determine the class that should
	 * be the super class
	 * @param common, args
	 * @retrun the super class
	 */
	static String getSuperClassName( List<String> common, String[] args )
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
			superClass = args[0];
		}
		return superClass;
	}
	
	/*
	 * Method to determine the child classes
	 * @param superClass, args
	 * @return the child classes
	 */
	static List<String> getChildClasses(String superClass, String[] args)
	{
		List<String> childClasses = new ArrayList<String>();
		// Iterate through args array
		for(int i = 0; i <= 2; i++)
		{
			// If the argument class != super class, then add it to the child class list
			if( !args[i].equals(superClass) )
			{
				childClasses.add(args[i]);
			}
		}
		return childClasses;
	}
	
	/*
	 * Does the processing for inserting the class path,
	 * setting the super class, inserting the block into the method,
	 * and invoking that method
	 * @param methods, superClassName, childClasses
	 */
	public static void analysisProcess(String[] methods, String superClassName, List<String> childClasses)
	{
		try {
			// Get class pool and insert class path
			ClassPool cp = ClassPool.getDefault();
			insertClassPath(cp);
			
			// Get child classes
			CtClass childClassName1 = cp.get("target." + childClasses.get(0));
			CtClass childClassName2 = cp.get("target." + childClasses.get(1));
			
			// Defrost and set the super class
			childClassName1.defrost();
			childClassName2.defrost();
			childClassName1.setSuperclass(cp.get("target." + superClassName));
			childClassName2.setSuperclass(cp.get("target." + superClassName));
			
			// Grab the methods
			String usage = methods[0];
			String incr = methods[1];
			String getter = methods[2];
			char var = incr.charAt(incr.length() - 1);
			
			// Get the methods from the class objects
			CtMethod methodChildClass1 = childClassName1.getDeclaredMethod(usage);
			CtMethod methodChildClass2 = childClassName2.getDeclaredMethod(usage);
			
			// Block to insert
			String block = "\n{\n" 
					+ "\t" + incr + "();" + "\n"
					+ "\t" + "System.out.println(\"" + var + ": \" + " + getter + "());\n}";
			
			System.out.println("[DBG] Block: " + block);
			
			// Insert block into the methods
			methodChildClass1.insertBefore(block);
			methodChildClass2.insertBefore(block);
					
			// Invoke the methods
			callMethod(cp, childClasses.get(0), usage);
			callMethod(cp, childClasses.get(1), usage);
		
		} catch (NotFoundException | CannotCompileException | SecurityException | IllegalArgumentException | ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Method to invoke the usage method
	 * @param ClassPool cp, String className, String usage
	 */
	public static void callMethod(ClassPool cp, String className, String usage) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException
	{
		Loader c = new Loader(cp); // Instantiate a Loader object
		Class<?> child = c.loadClass("target." + className); // Load the class
		Object childInst = child.newInstance(); // Create instance of the child class
		System.out.println("[DBG] Created a " + className + " object");
		Class<?> childClass = childInst.getClass(); // Get class
		Method m1 = childClass.getDeclaredMethod(usage, new Class[] {}); // Get the usage method
		System.out.println("[DBG] Called getDeclaredMethod.");
		Object invoker = m1.invoke(childInst, new Object[] {}); // Invoke the usage method
		System.out.println("[DBG] " + usage + " result: " + invoker);
	}
	
	/*
	 * Inserts the class path into the Class Pool
	 * @param ClassPool pool
	 */
	public static void insertClassPath(ClassPool pool) throws NotFoundException
	{
		String strClassPath = WORK_DIR + File.separator + "classfiles";
		pool.insertClassPath(strClassPath);
	    System.out.println("[DBG] insert classpath: " + strClassPath);

	}
}
