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

	private ClassPool pool;
	
	public static void main(String[] args) throws Throwable
	{
		ClassPool cp = ClassPool.getDefault();
		cp.insertClassPath(INPUT_DIR);
		while(true)
		{
			UtilMenu.showMenuOptions();
			int option = UtilMenu.getOption();
			switch(option)
			{
			case 1:
				System.out.println("Enter an application class name, a method name, and a method parameter index separated by a comma (e.g., ComponentApp,foo,1): ");
				String[] input = UtilMenu.getArguments();
				String appName = input[0];
				String methodName = input[1];
				String index = input[2];
				
				CtClass cc = cp.get("target." + appName);
				cc.defrost();
				CtMethod m = cc.getDeclaredMethod(methodName);
				String block = "{\n"
						+ "\tSystem.out.println(\"[DBG] Param" + index + ": \" + $" + index + ");\n"
						+ "}";
				System.out.println("[DBG] Block: " + block);
				m.insertBefore(block);
				
				Loader c1 = new Loader(cp);
				Class<?> c = c1.loadClass("target." + appName);
				Method main = c.getMethod("main", String[].class);
				System.out.println("[DBG] Method: " + main.getName());
				main.invoke(null, (Object) new String[] {});
				cc.writeFile(OUTPUT_DIR);
			}
		}
	}
}
