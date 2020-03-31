package target;

public class ServiceApp {
	private void baz(int p1, int p2, int p3) {
		System.out.println("[DBG] baz called");
	}
	
	public void bar(int p1, int p2, int p3) {
		System.out.println("[DBG] bar called");
	}
	
	public static void main(String[] args) {
		ServiceApp f = new ServiceApp();
		f.bar(10, 20, 30);
	}
}
