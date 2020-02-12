package insertmethodbody;

import java.io.File;
import java.lang.reflect.Method;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;
import util.UtilMenu;

public class InsertMethodBody {
	static String WORK_DIR = System.getProperty("user.dir");
	static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
	static String OUTPUT_DIR = WORK_DIR + File.separator + "output";

	private static ClassPool cp;
	
	public static void main(String[] args) throws Throwable
	{
		cp = ClassPool.getDefault();
		cp.insertClassPath(INPUT_DIR);
		while(true)
		{
			UtilMenu.showMenuOptions();
			int option = UtilMenu.getOption();
			switch(option)
			{
			case 1:
				System.out.println("Enter an application class name, a method name, and a method parameter index separated by a comma (e.g., ComponentApp,foo,1): ");
				String[] input = UtilMenu.getArguments(); // Grab input using UtilMenu class
				String appName = input[0];
				String methodName = input[1];
				String index = input[2];
				
				CtClass cc = cp.get("target." + appName); // Get the class from the class pool
				cc.defrost(); // Defrost the class to make modifications
				CtMethod m = cc.getDeclaredMethod(methodName); // Get the method name from the class
				// Block to insert
				String block = "{\n"
						+ "\tSystem.out.println(\"[Inserted] target." + appName + "." + methodName + "'s param " + index + ": \" + $" + index + ");\n"
						+ "}";
				System.out.println("[DBG] Block: " + block);
				m.insertBefore(block); // Insert the block into the method
				
				Loader c1 = new Loader(cp); // Create a class loader to invoke the main method of the application
				Class<?> c = c1.loadClass("target." + appName);
				Method main = c.getMethod("main", String[].class);
				main.invoke(null, (Object) new String[] {});
			}
		}
	}
}
