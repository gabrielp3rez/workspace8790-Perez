package target;

public class ComponentApp {
	int comX = 11, comY = 11; public int comZ = 11;
	String comID = "Component";
	
	public void move(int dx, int dy, String str) {
		
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("[ComponentApp] Run...");
		ComponentApp a = new ComponentApp();
		a.move(0, 0, null);
		System.out.println("[ComponentApp] Done.");
	}
}
