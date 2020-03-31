package gbperez;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import util.UtilMenu;

public class Midterm extends ClassLoader{
	static final String WORK_DIR = System.getProperty("user.dir");
	static final String CLASS_PATH = WORK_DIR + File.separator + "classfiles";
	static String _L_ = System.lineSeparator();

	private static String className;
	private static int count;
	
	public static void main(String[] args) throws Throwable
	{
		while(true)
		{
			UtilMenu.showMenuOptions();
			int option = UtilMenu.getOption();
			switch(option)
			{
			case 1:
				System.out.println("Enter an application class name, and the number of fields to be analyzed, separated by a comma (e.g., target.ComponentApp,1 or target.ServiceApp,100): ");
				String[] input = UtilMenu.getArguments();
				if(input.length != 2)
				{
					System.out.println("[WRN] Invalid input size!!");
					break;
				}
				
				className = input[0];
				count = Integer.parseInt(input[1]);
				
				Midterm m = new Midterm();
				Class<?> c = m.loadClass(className);
				Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
				mainMethod.invoke(null, new Object[] { args });
			}
		}
	}
	
	private ClassPool pool;
	
	public Midterm() throws NotFoundException {
		pool = new ClassPool();
		pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
		pool.insertClassPath(CLASS_PATH);
	}
	
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		CtClass cc = null;
		try {
			cc = pool.get(name);
			cc.instrument(new ExprEditor() {
				public void edit(NewExpr newExpr) throws CannotCompileException {
					try {
						String longName = newExpr.getConstructor().getLongName();
						if(longName.startsWith("java.")) {
							return;
						}
					} catch (NotFoundException e)
					{
						e.printStackTrace();
					}
					
					CtField fields[] = newExpr.getEnclosingClass().getDeclaredFields();
					
					String block = "{ " + _L_;
					block += "  $_ = $proceed($$);" + _L_;
		            block += "  {\n" + _L_; 
		            for(int i = 0; i < count && i < fields.length; i++)
		            {
		            	if(fields[i].getModifiers() != Modifier.PUBLIC)
		            	{
		            		try {
		            		block +=
		   					     "\tString cName = $_.getClass().getName();" + _L_
		   					     + "\tString fName = $_.getClass().getDeclaredFields()[" + i + "].getName();" + _L_
		   					     + "\tString fieldFullName = cName + \".\" + fName;" + _L_
		   					  + "\t" + fields[i].getType().getName() + " fieldValue = $_." + fields[i].getName() + ";" + _L_;
		            	
		            		
		            		block += "\tSystem.out.println(\" [Instrument] Field Name: \" + fieldFullName);" + _L_;
			            	block += "\tSystem.out.println(\" [Instrument] Field Type: " + fields[i].getType().getName() + "\");" +_L_;
			            	block += "\tSystem.out.println(\" [Instrument] Field Value: \" + fieldValue);" + _L_;
			            	block += "\tSystem.out.println(\" [Instrument] Modified Line : " + newExpr.getLineNumber() + "\");" +_L_;
		            		} catch (NotFoundException e)
		            		{
		            			e.printStackTrace();
		            		}
		            	}
		            	else {
		            		System.out.println("[WRN] Unable to show a public member field: " + fields[i].getName());
		            	}
		            	
		            }
		            block += "  }" + _L_;
		            block += "}";
		            //System.out.println(block);
		            newExpr.replace(block);
				}
			});
			byte[] b = cc.toBytecode();
	        return defineClass(name, b, 0, b.length);
			
		} catch (NotFoundException e) {
	         throw new ClassNotFoundException();
	    } catch (IOException e) {
	         throw new ClassNotFoundException();
	    } catch (CannotCompileException e) {
	         e.printStackTrace();
	         throw new ClassNotFoundException();
	    }
	}
}
