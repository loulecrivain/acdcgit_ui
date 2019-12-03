package pacpresentations;
import javax.swing.*;

public class GenError {
	public static void show(Exception e) {
		JOptionPane.showMessageDialog(null, "Exception " + e.getClass() + " raised !", "error", JOptionPane.ERROR_MESSAGE);
		e.printStackTrace();
	}
}
