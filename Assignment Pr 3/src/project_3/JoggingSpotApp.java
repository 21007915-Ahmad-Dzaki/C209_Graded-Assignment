/**
 * I declare that this code was written by me.
 * I will not copy or allow others to copy my code.
 * I understand that copying code is considered as plagiarism.
 *
 * Ahmad Dzaki Bin Osman,21007915, 11 Jun 2022 7:08:20 pm
 */

package project_3;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;


/**
 * @author ahmad
 *
 */
public class JoggingSpotApp {

	
	private ArrayList<JoggingSpot> jsList = new ArrayList<JoggingSpot>();
	
	public static void main(String[] args) {

		JoggingSpotApp jsa = new JoggingSpotApp();
		jsa.start();
	}
	
	private void start() {
		

		String connectionString = "jdbc:mysql://localhost:3306/assignment_pr_3";
		String userid = "root";
		String password = "";
	
		DBUtil.init(connectionString, userid, password);
		loadItems();

		int option = -1;

		while (option != 3) {
			
			menu();
			option = Helper.readInt("Enter choice > ");

			if (option == 1) {
				viewAllSpots();
			} else if (option == 2) {
				viewByCat();
			}  else if (option == 3) {
				DBUtil.close();
				System.out.println("Thank you for using Jogging Spots App!");
			}
				
			}

	}
	
	private void menu() {
		Helper.line(80, "=");
		System.out.println("SINGAPORE JOGGING SPOTS APP");
		Helper.line(80, "=");
		System.out.println("1. View All Jogging Spots");
		System.out.println("2. View Jogging Spots by Category");
		System.out.println("3. Quit");
	}
	
	private void subMenu() { //submenu for option 2 
		Helper.line(80, "=");
		System.out.println("SELECT CATEGORY");
		Helper.line(80, "=");
		System.out.println("1. Park");
		System.out.println("2. Park Connector");
		System.out.println("3. Stadium");
	}
	
	private void viewAllSpots() {
		String outputPark = String.format("%-10s %-30s %-25s %-30s\n", "ID","NAME","CATEGORY", "SEAVIEW");
		String outputPC = String.format("%-10s %-30s %-25s %-30s\n", "ID","NAME","CATEGORY", "DISTANCE");
		String outputStadium = "";
		for (JoggingSpot js : jsList) {
			if(js instanceof Park) {
				Park p = (Park)js;
				outputPark += p.display();
			}
			if(js instanceof ParkConnector) {
				ParkConnector pc = (ParkConnector)js;
				outputPC += pc.display();
			}
			if(js instanceof Stadium) {
				Stadium s = (Stadium)js;
				outputStadium += s.display();
				outputStadium += "\n" + s.announceUnavailability(s.getId());
				outputStadium += "\n" ;
				
			}
		}
		Helper.line(80,"-");
		System.out.println(outputPark);
		Helper.line(80,"-");
		System.out.println(outputPC);
		Helper.line(80,"-");
		System.out.println(outputStadium);
		
	}
	
	private void viewByCat() {
		String outputPark = String.format("%-10s %-30s %-25s %-30s\n", "ID","NAME","CATEGORY", "SEAVIEW");
		String outputPC = String.format("%-10s %-30s %-25s %-30s\n", "ID","NAME","CATEGORY", "DISTANCE");
		String outputStadium = "";
		subMenu();
		int choice = Helper.readInt("Enter Jogging Spot Category > ");
		boolean pFound = false;
		boolean pcFound = false;
		boolean sFound = false;
		for (JoggingSpot js : jsList) {
			if (choice == 1) {
				if(js instanceof Park) {
					Park p = (Park)js;
					outputPark += p.display();
					pFound = true;
			}
			}
			else if (choice == 2) {
			if(js instanceof ParkConnector) {
				ParkConnector pc = (ParkConnector)js;
				outputPC += pc.display();
				pcFound = true;
			}
			}
			else if (choice == 3) {
			if(js instanceof Stadium) {
				Stadium s = (Stadium)js;
				outputStadium += s.display();
				outputStadium += "\n" + s.announceUnavailability(s.getId());
				outputStadium += "\n" ;
				sFound =true;
			}
			}
		}
		if (pFound == true) {
			Helper.line(80,"-");
			System.out.println(outputPark);
		}else if (pcFound == true) {
			Helper.line(80,"-");
			System.out.println(outputPC);
		}else if (sFound == true) {
			Helper.line(80,"-");
			System.out.println(outputStadium);
		}else {
			System.out.println("The category is not found.");
		}
	}
	// method gets data from SQL and add objects into arrayList
	private void loadItems() {
		try {
			String sql = "SELECT * FROM jogging_spot";
			ResultSet rs = DBUtil.getTable(sql);
			
			while (rs.next()) {
				String id = rs.getString("ID");
				String name = rs.getString("Name");
				String cat = rs.getString("Category");
				boolean seaview = rs.getBoolean("HasSeaview");
				double distance = rs.getDouble("DistanceKm");
				Time ct = rs.getTime("ClosingTime");
				
				if (distance == 0 && ct == null) {
					jsList.add(new Park(id, name, cat, seaview));
				}
				else if (seaview == false && ct == null) {
					jsList.add(new ParkConnector(id, name, cat, distance));
				}
				else if (seaview == false && distance == 0) {
					LocalTime closeTime = ct.toLocalTime();
					jsList.add(new Stadium(id, name, cat, closeTime));
				}
			}
			
		}catch (SQLException se) {
			se.printStackTrace();
		}
	}
}
