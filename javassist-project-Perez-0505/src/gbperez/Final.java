package gbperez;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;
import util.UtilMenu;

public class Final {
	static final String WORK_DIR = System.getProperty("user.dir");
	static final String CLASS_PATH = WORK_DIR + File.separator + "classfiles";
	static final String OUTPUT_DIR = WORK_DIR + File.separator + "output";
	static String _L_ = System.lineSeparator();
	
	private static ClassPool cp;
	
	public static void main(String[] args) throws Throwable
	{
		cp = ClassPool.getDefault();
		cp.insertClassPath(CLASS_PATH);
		while(true)
		{
			UtilMenu.showMenuOptions();
			int option = UtilMenu.getOption();
			switch(option)
			{
			case 1:
				System.out.println("Enter an application class name, a method name or wildcard (*), and a method parameter index or wildcard (*): ");
				String[] input = UtilMenu.getArguments();
				String appName = input[0];
				String methodName = input[1];
				String index = input[2];
				
				CtClass cc = cp.get("target." + appName);
				cc.defrost();
				
				if(methodName.equals("*") && index.equals("*"))
				{
					CtMethod[] methods = cc.getDeclaredMethods();
					for(int i = 0; i < methods.length; i++)
					{
						String block = "";
						for(int j = 1; j <= methods[i].getParameterTypes().length; j++)
						{
							if(!methods[i].getParameterTypes()[j-1].getClass().getName().equals("javassist.CtArray"))
							{
								block += "{\n"
										+ "\tSystem.out.println(\"[Inserted] target." + appName + "." + methods[i].getName() + "'s param " + j + ": \" + $" + j + ");\n"
										+ "}\n";
							}
						}
						System.out.print(block);
						methods[i].insertBefore(block);
					}
					
					cc.writeFile(OUTPUT_DIR);
					Loader c = new Loader(cp);
					Class<?> claz = c.loadClass("target." + appName);
					Method main = claz.getMethod("main", String[].class);
					main.invoke(null, (Object) new String[] {});
				}
				else if(!methodName.equals("*") && !index.equals("*"))
				{
					CtMethod m = cc.getDeclaredMethod(methodName);
					
					if(!(Integer.parseInt(index) > m.getParameterTypes().length))
					{
						String block = "{\n"
								+ "\tSystem.out.println(\"[Inserted] target." + appName + "." + methodName + "'s param " + index + ": \" + $" + index + ");\n" 
								+ "}";
						System.out.println(block);
						m.insertBefore(block);
						
						cc.writeFile(OUTPUT_DIR);
						Loader c = new Loader(cp);
						Class<?> claz = c.loadClass("target." + appName);
						Method main = claz.getMethod("main", String[].class);
						main.invoke(null, (Object) new String[] {});
					}
					else
					{
						System.out.println("[WRN] " + methodName + " does not have that many parameters");
					}
				}
				else
				{
					System.out.println("[WRN] Please give two wildcards, or a method name and an index");
				}
			}
		}
	}
}
