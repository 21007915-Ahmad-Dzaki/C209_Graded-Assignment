/**
 * I declare that this code was written by me.
 * I will not copy or allow others to copy my code.
 * I understand that copying code is considered as plagiarism.
 *
 * Ahmad Dzaki Bin Osman, 21007915, 26 Jun 2022 2:28:16 pm
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
public class JSAppTestPart2 {
	private ArrayList<JoggingSpot> jsList = new ArrayList<JoggingSpot>();
	
	public static void main(String[] args) {

		JSAppTestPart2 jsa = new JSAppTestPart2();
		jsa.start();
	}
	
	private void start() {


		String connectionString = "jdbc:mysql://localhost:3306/assignment_pr_3";
		String userid = "root";
		String password = "";
	
		DBUtil.init(connectionString, userid, password);
		loadItems();

		int option = -1;

		while (option != 5) {
			
			menu();
			option = Helper.readInt("Enter choice > ");

			if (option == 1) {
				viewAllSpots();
			} else if (option == 2) {
				viewByCat();
			} else if (option == 3) {
				addJoggingSpot();
			} else if (option == 4) {
				deleteJoggingSpot();
			} else if (option == 5) {
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
		System.out.println("3. Add Jogging Spot");
		System.out.println("4. Delete Jogging Spot");
		System.out.println("4. Quit");
	}
	
	private void menuCat() {
		Helper.line(80, "=");
		System.out.println("SELECT CATEGORY");
		Helper.line(80, "=");
		System.out.println("1. Park");
		System.out.println("2. Park Connector");
		System.out.println("3. Stadium");
	}
	
	private void viewAllSpots() {
		String outputPark = String.format("%-10s %-30s %-30s %-30s\n", "ID","NAME","CATEGORY", "SEAVIEW");
		String outputPC = String.format("%-10s %-30s %-30s %-30s\n", "ID","NAME","CATEGORY", "DISTANCE");
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
		String outputPark = String.format("%-10s %-30s %-30s %-30s\n", "ID","NAME","CATEGORY", "SEAVIEW");
		String outputPC = String.format("%-10s %-30s %-30s %-30s\n", "ID","NAME","CATEGORY", "DISTANCE");
		String outputStadium = "";
		menuCat();
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
	private void addJoggingSpot() {
		
		String sql = "";
		//get the last used ID to generate a new ID for new INSERT
		String lastID = jsList.get(jsList.size()-1).getId();;
		int hasSeaview = 0;
		int rowsAffected = 0;
		
		//used to increment the ID then add back together with the letter J
		String letterID = lastID.substring(0,1);
		int numID = Integer.parseInt(lastID.substring(1));
		numID++;
		String newID = letterID + numID;

		
		System.out.println("ADDING JOGGING SPOT");
		Helper.line(40, "-");
		String name = Helper.readString("Enter name > ");
		String cat = Helper.readStringRegEx("Enter Category > ", "([p|P]ark)|([p|P]ark\\s[c|C]onnector)|([s|S]tadium)");
		// making the First letter to be capital
		String firstLetter = cat.substring(0,1).toUpperCase();
		String rmgLetter = cat.substring(1).toLowerCase();
		cat = firstLetter + rmgLetter;
		
		if (cat.toLowerCase().equals("park")) {
			boolean seaview = Helper.readBoolean("Does the park have a seaview? (y/n) > ");
			if (seaview) {
				hasSeaview = 1;
			}
			else {
				hasSeaview = 0;
			}
			sql = "INSERT INTO jogging_spot(ID, Name, Category, HasSeaview, DistanceKm, ClosingTime) " 
					+ "VALUES ('" + newID + "' ,'"+ name + "', '" + cat + "', '"+ hasSeaview +"'," + null+ "," + null + ")";
			rowsAffected = DBUtil.execSQL(sql);
		}
		else if (cat.toLowerCase().equals("park connector")) {
			double distance = Helper.readDouble("Enter total distance for park connector > ");
			sql = "INSERT INTO jogging_spot(ID, Name, Category, HasSeaview, DistanceKm, ClosingTime) " 
					+ "VALUES ('" + newID + "' ,'"+ name + "', '" + cat + "', "+ null +", '" + distance + "',"+ null + ")";
			rowsAffected = DBUtil.execSQL(sql);
		}
		else if (cat.toLowerCase().equals("stadium")) {
			String ct = Helper.readString("Enter closing time for stadium (hh:mm:ss) > ");
			LocalTime closeTime = LocalTime.parse(ct);
			sql = "INSERT INTO jogging_spot(ID, Name, Category, HasSeaview, DistanceKm, ClosingTime) " 
					+ "VALUES ('" + newID + "' ,'" + name + "', '" + cat + "'," + null + "," + null + ",'" + closeTime + "'" + ")";
			rowsAffected = DBUtil.execSQL(sql);
			
		}
		if (rowsAffected == 1) {
			System.out.println("Jogging Spot added!");
		} else {
			System.out.println("Insert failed!");
		}
	}
	private void deleteJoggingSpot() {
		
		System.out.println("DELETING JOGGING SPOT");
		Helper.line(40, "-");
		
		String name = Helper.readString("Enter name >");
		
		String sql = "DELETE from jogging_spot WHERE Name = '" + name + "'";
		int rowsAffected = DBUtil.execSQL(sql);
		
		if (rowsAffected == 1) {
			System.out.println("Jogging Spot deleted!");
		} else {
			System.out.println("Delete Failed!");
		}
	}
//	private JoggingSpot findJS() {
//		
//	}
	
}
