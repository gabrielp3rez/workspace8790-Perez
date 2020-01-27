package gbperez;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import target.*;

public class SetSuperclass {
	static final String SEP = File.separator;
	static String workDir = System.getProperty("user.dir");
	static String outputDir = workDir + SEP + "output";
	
	public static void main(String[] args) 
	{
		// List of class arguments that contain the 'Common'
		List<String> common = new ArrayList<String>();
		
		if(args.length == 3)
		{
			try
			{
				// Get the ClassPool
				ClassPool pool = ClassPool.getDefault();
				
				// If the arguments contain 'Common' then add to the list
				if(args[0].startsWith("Common"))
					common.add(args[0]);
				if(args[1].startsWith("Common"))
					common.add(args[1]);
				if(args[2].startsWith("Common"))
					common.add(args[2]);
				
				// Call getSuperClassName and getChildClasses methods
				String superClass = getSuperClassName(common, args);
				List<String> childClasses = getChildClasses(superClass, args);
				
				// Insert Class Paths for the child classes
				insertClassPathRunTimeClass(pool, childClasses);
				
				// Fetch the child classes and set their super classes
				CtClass cc1 = pool.get("target." + childClasses.get(0) );
				CtClass cc2 = pool.get("target." + childClasses.get(1) );
				setSuperclass(cc1, "target." + superClass, pool);
				setSuperclass(cc2, "target." + superClass, pool);
				
				// Write the class files to the output directory
				cc1.writeFile(outputDir);
				cc2.writeFile(outputDir);
				System.out.println( "[DBG] write output to: " + outputDir);
			}
			catch (NotFoundException | ClassNotFoundException | CannotCompileException | IOException e)
			{
				e.printStackTrace();
			}
		}
		// Invalid Number of Arguments
		else
		{
			System.out.println( "Invalid Arguments" );
		}
	}
	
	/*
	 * Inserts the class paths for the list of child classes
	 * @param pool, childClasses
	 */
	static void insertClassPathRunTimeClass(ClassPool pool, List<String> childClasses) throws NotFoundException, ClassNotFoundException
	{
		// Create ClassClassPath objects
		ClassClassPath classPath1 = new ClassClassPath(Class.forName("target." + childClasses.get(0)));
		ClassClassPath classPath2 = new ClassClassPath(Class.forName("target." + childClasses.get(1)));
		// Insert the class paths into the Class Pool
		pool.insertClassPath(classPath1);
		pool.insertClassPath(classPath2);
		System.out.println( "[DBG] insert class path: " + classPath1.toString() );
		System.out.println( "[DBG] insert class path: " + classPath2.toString() );
	}
	
	/*
	 * Set the superclass using the javassist API
	 * @param curClass, superClass, pool
	 */
	static void setSuperclass(CtClass curClass, String superClass, ClassPool pool) throws NotFoundException, CannotCompileException, NotFoundException
	{
		curClass.setSuperclass(pool.get(superClass));
	    System.out.println("[DBG] set superclass: " + curClass.getSuperclass().getName() + //
	            ", subclass: " + curClass.getName());
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
}
