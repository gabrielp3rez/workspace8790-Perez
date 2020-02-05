package classloader;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;

public class SampleLoader extends ClassLoader {
	static String WORK_DIR = System.getProperty("user.dir");
	static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
	
	private ClassPool pool;
	
	public static void main(String[] args) throws Throwable
	{
		String[] inputs = getInputs();
		String appName = inputs[0];
		String fieldName = inputs[1];
		SampleLoader loader = new SampleLoader();
		Class<?> c = loader.loadClass(appName);
		c.getDeclaredMethod("main", new Class[] { String[].class }).invoke(null, new Object[] {inputs});
	}
	
	public SampleLoader() throws NotFoundException 
	{
		pool = new ClassPool();
		pool.insertClassPath(INPUT_DIR);
	}
	
	public static String[] getInputs()
	{
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter an application class name and a field name separated by a comma: ");
		String[] inputs = scan.nextLine().split(",\\s*");
		return inputs;
	}
	
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			CtClass cc = pool.get(name);
			if (name.contentEquals("ComponentApp"))
			{
				CtField f = new CtField(CtClass.doubleType, "f1", cc);
				f.setModifiers(Modifier.PUBLIC);
				cc.addField(f);
			}
			byte[] b = cc.toBytecode();
			return defineClass(name, b, 0, b.length);
		} catch (NotFoundException e) {
			throw new ClassNotFoundException();
		} catch (CannotCompileException e) {
			throw new ClassNotFoundException();
		} catch (IOException e) {
			throw new ClassNotFoundException();
		}
	}
}
