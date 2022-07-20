/**
 * I declare that this code was written by me.
 * I will not copy or allow others to copy my code.
 * I understand that copying code is considered as plagiarism.
 *
 * Ahmad Dzaki Bin Osman, 21007915, 11 Jun 2022 7:17:34 pm
 */

package project_3;

/**
 * @author ahmad
 *
 */
public class Park extends JoggingSpot {
	
	private boolean seaview;

	public Park(String id, String name, String category, boolean seaview) {
		super(id, name, category);
		this.seaview = seaview;
	}

	/**
	 * @return the seaview
	 */
	public boolean isSeaview() {
		return seaview;
	}
	public String display() {
		String have = "";
		String outputPark = "";
		if(seaview) {
			have = "yes";
		}else {
			have = "no";
		}
		outputPark += String.format("%-10s %-30s %-25s %-30s\n", getId(),getName(),getCategory(), have);
		return outputPark;
	
	}


}
