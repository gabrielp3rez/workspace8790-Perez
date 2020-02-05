package ex04.toclass;

import java.util.Scanner;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.Loader;
import javassist.NotFoundException;
import util.UtilMenu;

public class ToClassPerez {
	
	private static Scanner scan = new Scanner(System.in);
	
	public static void main(String[] args)
	{
		while(true)
		{
			UtilMenu.showMenuOptions(); // Show menu options
			int option = UtilMenu.getOption(); // Get menu options
			switch (option)
			{
			case 1:
				String[] inputs = getInputs(); // Get inputs
				if(inputs.length != 3) // Invalid input
				{
					System.out.println("[WRN] Invalid Input");
					break;
				}
				try {
					ClassPool cp = ClassPool.getDefault(); // Get default class pool
					CtClass cc = cp.get(inputs[0]); // Get CtClass from the input
					cc.defrost();
					CtConstructor declaredConstructor = cc.getDeclaredConstructor(new CtClass[0]);
					String block = "{\n"
							+ "\tSystem.out.println(\"" + inputs[1] + ": \" + " + inputs[1]	+ ");\n"
							+ "\tSystem.out.println(\"" + inputs[2] + ": \" + " + inputs[2] + ");\n"
							+ "}";		
					declaredConstructor.insertAfter(block); // insert block of code into constructor
					Loader c1 = new Loader(cp); // Instantiate Loader
					Class<?> c = c1.loadClass(inputs[0]); // Load class
					Object obj = c.newInstance(); // Create instance of Class which calls constructor
		 		} catch (NotFoundException | CannotCompileException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/*
	 * Returns the input from the user
	 * @return String array of the inputs from the user
	 */
	public static String[] getInputs()
	{
		System.out.print("Enter a class name and two field names separated by a comma: ");
		String[] inputs = scan.nextLine().split(",\\s*");
		return inputs;
	}
}
