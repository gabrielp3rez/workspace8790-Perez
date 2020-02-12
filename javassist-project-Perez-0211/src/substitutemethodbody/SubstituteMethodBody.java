package substitutemethodbody;

import java.io.File;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.NotFoundException;
import util.UtilMenu;

public class SubstituteMethodBody extends ClassLoader {
	static final String WORK_DIR = System.getProperty("user.dir");
	static final String INPUT_PATH = WORK_DIR + File.separator + "classfiles";

	public static void main(String[] args) throws Throwable 
	{
		SubstituteMethodBody s = new SubstituteMethodBody();
		
		while(true)
		{
			UtilMenu.showMenuOptions();
			int option = UtilMenu.getOption();
			switch(option)
			{
			case 1:
				System.out.println("Enter an application class name, a method name, the index of the method parameter, and the value to be assigned to the parameter separated by a comma");
				System.out.print("(e.g., ComponentApp,move,1,0): ");
				String[] input = UtilMenu.getArguments();
				String appName = input[0];
				String methodName = input[1];
			}
			
		}
	}
	
	private ClassPool pool;
	
	public SubstituteMethodBody() throws NotFoundException 
	{	
		pool = new ClassPool();
	    pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
	    pool.insertClassPath(INPUT_PATH); // "target" must be there.
	    System.out.println("[DBG] Class Pathes: " + pool.toString());
	}
	
	protected Class<?> findClass(String name) throws ClassNotFoundException 
	{
		return null;
	}
}
