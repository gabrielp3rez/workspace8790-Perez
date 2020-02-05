public class ServiceApp {
	public static void main(String[] args) throws Exception
	{
		System.out.println("Run...");
		ServiceApp localMyApp = new ServiceApp();
		localMyApp.runComponent();
		System.out.println(Class.forName(args[0]).getField(args[1]).getName());
		System.out.println(/* Show the value TODO*/);
		System.out.println("Done.");
	}
	public void runComponent() {
		System.out.println("Called runComponent");
	}
}
