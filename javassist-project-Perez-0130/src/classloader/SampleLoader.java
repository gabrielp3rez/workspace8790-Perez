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
	static String[] inputs;
	
	public static void main(String[] args) throws Throwable
	{
		inputs = getInputs(); // Fetch input from user
		String appName = inputs[0];
		SampleLoader loader = new SampleLoader(); // constructor that inserts class path
		Class<?> c = loader.loadClass(appName); // load the class
		c.getDeclaredMethod("main", new Class[] { String[].class }). // invoke the main method of app
			invoke(null, new Object[] {inputs});
	}
	
	/*
	 * Inserts class path
	 */
	public SampleLoader() throws NotFoundException 
	{
		pool = new ClassPool();
		pool.insertClassPath(INPUT_DIR);
	}
	
	/*
	 * Get input from user
	 * @return String array if input from user
	 */
	public static String[] getInputs()
	{
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter an application class name and a field name separated by a comma (e.g., ComponentApp,f1): ");
		String[] inputs = scan.nextLine().split(",\\s*");
		scan.close();
		return inputs;
	}
	
	/*
	 * Overriden method, find a specified class, and modify the bytecode
	 */
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			CtClass cc = pool.get(name);
			if (name.equals(inputs[0]))
			{
				// Create field, set it to public, and intialize to 3.14
				CtField f = new CtField(CtClass.doubleType, inputs[1], cc);
				f.setModifiers(Modifier.PUBLIC);
				cc.addField(f, CtField.Initializer.constant(3.14));
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
