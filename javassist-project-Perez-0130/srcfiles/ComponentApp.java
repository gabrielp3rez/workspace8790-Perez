public class ComponentApp {
	public static void main(String[] args) throws Exception
	{
		System.out.println("Run...");
		ComponentApp localMyApp = new ComponentApp();
		localMyApp.runComponent();
		System.out.println("Field name: " + Class.forName(args[0]).getField(args[1]).getName());
		System.out.println("Field value: " + Class.forName(args[0]).getField(args[1]).get(Class.forName(args[0]).newInstance()));
		System.out.println("Done.");
	}
	public void runComponent() {
		System.out.println("Called runComponent");
	}
}
