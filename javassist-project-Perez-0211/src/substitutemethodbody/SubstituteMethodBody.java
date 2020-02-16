package substitutemethodbody;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import util.UtilMenu;

public class SubstituteMethodBody extends ClassLoader {
	static final String WORK_DIR = System.getProperty("user.dir");
	static final String INPUT_PATH = WORK_DIR + File.separator + "classfiles";

	static String appName; 
	static String methodName;
	static String index;
	static String val;
	
	static Map<String, Boolean> isModified = new HashMap<String, Boolean>();
	
	public static void main(String[] args) throws Throwable 
	{
		isModified.put("move", false);
		isModified.put("fill", false);
		
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
				if(input.length != 4)
				{
					System.out.println("[WRN] Invalid input size!!");
					break;
				}
				else
				{
					appName = input[0];
					methodName = input[1];
					if (isModified.get(methodName))
					{
						System.out.println("[WRN] This method " + methodName + " has been modified!!");
						break;
					}
					index = input[2];
					val = input[3];
					SubstituteMethodBody s = new SubstituteMethodBody();
					Class<?> c = s.loadClass("target." + appName);
					Method main = c.getDeclaredMethod("main", new Class[] {String[].class});
					main.invoke(null, new Object[] {args});
				}
			}
		}
	}
	
	private ClassPool pool;
	
	public SubstituteMethodBody() throws NotFoundException 
	{	
		pool = new ClassPool();
	    pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
	    pool.insertClassPath(INPUT_PATH); // "target" must be there.
	    System.out.println("[DBG] Class Paths: " + pool.toString());
	}
	
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		CtClass cc = null;

		try {
			cc = pool.get(name);
			if (!cc.getName().equals("target.ComponentApp") && !cc.getName().equals("target.ServiceApp"))
			{
				return defineClass(name, cc.toBytecode(), 0, cc.toBytecode().length);
			}			

			cc.instrument(new ExprEditor() {
				public void edit(MethodCall call) throws CannotCompileException {
					String className = call.getClassName();
					String methodName = call.getMethodName();
					
					if ((className.equals("target.ComponentApp") && methodName.equals("move")) || 
							className.equals("target.ServiceApp") && methodName.equals("fill"))
					{
						isModified.put(methodName, true);
						String block = "{\n" 
								+ "\tSystem.out.println(\"Reset Param " + index + " to " + val + ".\");\n"
								+ "\t$" + index + " = " + val + ";\n"
								+ "$proceed($$);\n"
								+ "}";
						call.replace(block);
					}
				}
			});
			byte[] b = cc.toBytecode();
			return defineClass(name, b, 0, b.length);
		} catch (NotFoundException e) {
		    throw new ClassNotFoundException();
		} catch (ClassFormatError e) {
			e.printStackTrace();
			throw new ClassNotFoundException();
		} catch (IOException e) {
			e.printStackTrace();
			throw new ClassNotFoundException();
		} catch (CannotCompileException e) {
			e.printStackTrace();
			throw new ClassNotFoundException();
		}
	}
}
