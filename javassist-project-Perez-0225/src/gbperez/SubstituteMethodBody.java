package gbperez;

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
	static String workDir = System.getProperty("user.dir");
	static String inputDir = workDir + File.separator + "classfiles";
	static String outputDir = workDir + File.separator + "output";
	
	private static Map<String, Boolean> isModified = new HashMap<String, Boolean>();
	static String appName;
	static String methodName;
	static int num;
	
	public static void main(String[] args) throws Throwable
	{
		isModified.put("baz", false);
		isModified.put("bar", false);
		isModified.put("foo", false);
		
		while(true)
		{
			UtilMenu.showMenuOptions();
			int option = UtilMenu.getOption();
			switch (option)
			{
			case 1:
				System.out.print("A user enters 3 inputs: ");
				String[] input = UtilMenu.getArguments();
				
				if (input.length != 3)
				{
					System.out.println("[WRN] Invalid input size!!");
					break;
				}
				else
				{
					appName = input[0];
					methodName = input[1];
					num = Integer.parseInt(input[2]);
					if(validMethod(appName, methodName)) {
						if (!isModified.get(methodName)) {
							SubstituteMethodBody s = new SubstituteMethodBody();
							Class<?> c = s.loadClass(appName);
							Method main = c.getDeclaredMethod("main", 
									new Class[] {String[].class}); // Invoke main
							main.invoke(null, new Object[] {args});
							
						}
						else {
							System.out.println("[WRN] Invalid input method!!");
							break;
						}
					}
					
				}
				
			}
		}
	}
	
	private static ClassPool pool;
	
	public SubstituteMethodBody() throws NotFoundException 
	{	
		pool = new ClassPool();
	    pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
	    pool.insertClassPath(inputDir); // "target" must be there.
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
					
					if(className.equals("target.ComponentApp") && methodName.equals("foo") || 
							className.equals("target.ServiceApp") && methodName.equals("bar")) 
					{
						isModified.put(methodName, true);
						String block = "";
						if (num <= 3)
						{
							
							for(int i = 1; i <= num; i++)
							{
								block += "System.out.println(\"[Inserted] " + className + "'s parm " + i + ": \" + $" + i + ");\n";
							}
							block += "$proceed($$);\n";
						}
						else 
						{
							block += "System.out.println(\"[Inserted] " + className + "'s parm 3: \" + $3);\n";
							block += "$proceed($$);\n";
						}
						call.replace(block);
					}
				}
			});
			
			cc.writeFile(outputDir);
			byte[] b = cc.toBytecode();
			return defineClass(name, b, 0, b.length);
		}  catch (NotFoundException e) {
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
	
	public static boolean validMethod(String className, String methodName)
	{
		if(className.equals("target.ComponentApp") && (methodName.equals("foo"))) {
			return true;
		} else if (className.equals("target.ComponentApp") && (methodName.equals("bar"))) {
			System.out.println("[WRN] Cannot modify a private method!!");
			return false;
		} else if (className.equals("target.ServiceApp") && (methodName.equals("baz"))) {
			System.out.println("[WRN] Cannot modify a private method!!");
			return false;
		}
		else if(className.equals("target.ServiceApp") && (methodName.equals("bar"))) {
			return true;
		}
		else {
			System.out.println("[WRN] Invalid input class!!");
			return false;
		}
	}
}
