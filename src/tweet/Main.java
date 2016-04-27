package tweet;

import javax.swing.JOptionPane;

import fiftyone.mobile.detection.Match;
import fiftyone.mobile.detection.Provider;
import fiftyone.mobile.detection.factories.StreamFactory;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String creepinUser = JOptionPane.showInputDialog("Who We Creepin?");
		boolean limitedCreeping = (JOptionPane.showConfirmDialog(null, "Do We Want to Impose a Limit of 2000?") == JOptionPane.OK_OPTION);
		System.out.println((limitedCreeping ? "Limited" : "No Limits"));
		@SuppressWarnings("unused")
		test NoIdea = new test(creepinUser, limitedCreeping);
	}

}
