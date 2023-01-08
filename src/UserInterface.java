import java.util.Map.Entry;
import ecs100.UI;

public class UserInterface {

	TrainSystem trainSystem = new TrainSystem();
	private Station selected;
	
	public UserInterface() {

		UI.addButton("List all Stations", this::listAllStation);
		UI.addButton("List all TrainLines", this::listAllTrainLine);
		UI.addButton("Print Ticket Price", this::printTicketPrice);	
		UI.addButton("List Lines By Station", this::listLineByStation);
		UI.addButton("List Stations By TrainLine", this::listStationsByLine);
		UI.addButton("List TrainLines By Two Stations", this::printTrainLineFromOneStationToDestination);
		UI.addButton("Find Next TrainService", this::findNextTrainService);
		UI.addButton("Find Next TrainService Infomation", this::printNextTrainServiceInfo);						
		UI.addButton("Quit System", this::quit);
	
		UI.drawImage(".\\Train network data\\system-map.png",0,0);
		UI.setMouseMotionListener(this::doMouse);
	}

	public void listAllStation() {
		
		UI.clearText();
		UI.println("There are " + trainSystem.stationsMap.size() + " stations in Wellington.");		
		for (Entry<String, Station> station : trainSystem.stationsMap.entrySet()) {
			UI.println("|________________________________|");
			UI.println("| " + padding(station.getKey(), 30) + " |");
		}
		UI.println("**********************************");
	}

	public void listAllTrainLine() {
		
		UI.clearText();
		UI.println("There are " + trainSystem.trainLinesMap.size() + " lines in Wellington.");		
		for (String trainLine : trainSystem.trainLinesMap.keySet()) {
			UI.println("|________________________________|");
			UI.println("| " + padding(trainLine, 30) + " |");
			
		}
		UI.println("**********************************");
	}

	// List the train lines that go through a given station
	public void listLineByStation() {
		
		UI.clearText();
		String askStation = askStationEx("Select station name:");//measure1: choose station by click mouse
		//String askStation = askStation("Select station name:");//measure2: input station name manually;
		Station askStationObj = trainSystem.stationsMap.get(askStation);// get askStation's object

		for (TrainLine trainLine : trainSystem.trainLinesMap.values()) {// go through trainlineMap to get trainlineObject
																		 
			if (trainLine.getStations().contains(askStationObj)) {
				UI.println("|________________________________|");
				UI.println("| " + padding(trainLine.getName(), 30) + " |");
			}
		}
		UI.println("**********************************");
	}

	// List the stations along a given train line
	public void listStationsByLine() {
		
		UI.clearText();
		UI.println("Input 1: Wellington <==> Johnsonville");
		UI.println("Input 2: Wellington <==> Melling");
		UI.println("Input 3: Wellington <==> Waikanae");
		UI.println("Input 4: Wellington <==> Masterton");
		UI.println("Input 5: Wellington <==> Upper-Hutt");

		int askTrainLine = 0;
		String trainLineName = null;
		boolean flag = true;
		askTrainLine = UI.askInt("Choose Train Line");

		while (flag) {
			switch (askTrainLine) {
			case 1: {
				trainLineName = "Wellington_Johnsonville";
				flag = false;
				break;
			}
			case 2: {
				trainLineName = "Wellington_Melling";
				flag = false;
				break;
			}
			case 3: {
				trainLineName = "Wellington_Waikanae";
				flag = false;
				break;
			}
			case 4: {
				trainLineName = "Wellington_Masterton";
				flag = false;
				break;
			}
			case 5: {
				trainLineName = "Wellington_Upper-Hutt";
				flag = false;
				break;
			}
			default:
				UI.println("Invalid number: " + askTrainLine);
				askTrainLine = UI.askInt("Choose Train Line");
			}
			for (String trainLine : trainSystem.trainLinesMap.keySet()) {// go through all trainlines' keySet which are names of trainline
																			
				if (trainLine.equals(trainLineName)) {// check the name the user chooses equals or not
					TrainLine trainLineObj = trainSystem.trainLinesMap.get(trainLineName);// get this trainline name's Object
																					
					for (Station eachStation : trainLineObj.getStations()) {
						UI.println("|________________________________|");
						UI.println("| " + padding(eachStation.getName(), 30) + " |");
					}
				}
			}
		}
		UI.println("**********************************");
	}

	// Print the name of a train line that goes from a station to a destination
	// station
	public void printTrainLineFromOneStationToDestination() {
		
		UI.clearText();
		String askStation = askStationEx("Select station name:");//measure1: choose station by click mouse
		String askDestination = askStationEx("Select destination name:");
		
//		String askStation = askStation("Input station name:");//measure2: input station name manually;
//		String askDestination = askStation("Input destination name:");
		Station askStationObj = trainSystem.stationsMap.get(askStation);
		Station askDestinationObj = trainSystem.stationsMap.get(askDestination);

		boolean notFound = true;
		for (TrainLine trainLine : trainSystem.trainLinesMap.values()) {

			if (trainLine.getStations().contains(askStationObj)
					&& trainLine.getStations().contains(askDestinationObj)) {// check both two station Object are included in same trainline

				int stationIndex = trainLine.getStationIndex(askStationObj);
				int destinationIndex = trainLine.getStationIndex(askDestinationObj);					

				if (stationIndex < destinationIndex) {
					UI.println("|________________________________|");
					UI.println("| " + padding(trainLine.getName(), 30) + " |");
					notFound = false;
				}
			}
		}
		if (notFound) {
			UI.println("There is no Train Line connection the two Train Stations directly!");
		}

		UI.println("******************************************************************");
	}

	// Find the next train service for each line at a station immediately after a user specified time
	public void findNextTrainService() {
		
		UI.clearText();
		String askStation = askStationEx("Select station name:");//measure1: choose station by click mouse
	//	String askStation = askStation("Input station name:");//measure2: input station name manually;
		int askTime = askTime();

		Station askStationObj = trainSystem.stationsMap.get(askStation);

		for (TrainLine trainLine : trainSystem.trainLinesMap.values()) {
			if (trainLine.getStations().contains(askStationObj)) {
				int stationIndex = trainLine.getStationIndex(askStationObj);
				for (TrainService trainService : trainLine.getTrainServices()) {
					int time = trainService.getTimes().get(stationIndex);
					if (time > askTime) {
						UI.println("|_________________________________________________|");
						UI.println("| Next Service of: " + padding(trainLine.getName(), 30)+ " |");
						UI.println("| Time: " + time);
						break;
					}
				}
			}
		}
		UI.println("**************************************************");
	}

	// Find the trip between two given stations (on the same line), immediately after a user specified time.
	public void printNextTrainServiceInfo() {
		
		UI.clearText();
		String askStation = askStationEx("Select station name:");//measure1: choose station by click mouse
		String askDestination = askStationEx("Select destination name:");//measure2: input station name manually;
		
//		String askStation = askStation("Input station name:");
//		String askDestination = askStation("Input destination name:");
		
		int askTime = askTime();

		Station askStationObj = trainSystem.stationsMap.get(askStation);
		Station askDestinationObj = trainSystem.stationsMap.get(askDestination);

		boolean notFound = true;
		
		for (TrainLine trainLine : trainSystem.trainLinesMap.values()) {

			if (trainLine.getStations().contains(askStationObj)
					&& trainLine.getStations().contains(askDestinationObj)) {

				int stationIndex = trainLine.getStationIndex(askStationObj);
				int destinationIndex = trainLine.getStationIndex(askDestinationObj);

				if (stationIndex < destinationIndex) {				
					for (TrainService trainService : trainLine.getTrainServices()) {
						int startTime = trainService.getTimes().get(stationIndex);
						int arriveTime = trainService.getTimes().get(destinationIndex);
						if (startTime > askTime) {
							UI.println("|______________________________________________________|");
							UI.println("| Next Service of: " + padding(trainLine.getName(), 35) + " |");			
							UI.println("| Start Time: " + trainService.getStart());
							UI.println("| Arrive Time: " + arriveTime);							
							int fareZoneNumber = Math.abs(askDestinationObj.getZone()-askStationObj.getZone());
							UI.println("| The number of fare zones the trip goes through is: " + fareZoneNumber);				
							break;
						}
					}													
					notFound = false;
				}
			}
		}
		if (notFound) {
			UI.println("There is no Train Line connection the two Train Stations directly!");
		}
		UI.println("********************************************************");
	}
	
	public void printTicketPrice() {
		int zoneDiff = 0;
		UI.clearText();
		String askStation = askStationEx("Select station name:");//measure1: choose station by click mouse
		String askDestination = askStationEx("Select destination name:");
		
//		String askStation = askStation("Input station name:");//measure2: input station name manually;
//		String askDestination = askStation("Input destination name:");
		Station askStationObj = trainSystem.stationsMap.get(askStation);
		Station askDestinationObj = trainSystem.stationsMap.get(askDestination);
	//	d = (double) Math.round(d * 100) / 100;
		double distanceDiff = Math.abs(askDestinationObj.getDistance()-askStationObj.getDistance());
		double d = (double)Math.round(distanceDiff*100)/100;
		
		boolean notFound = true;
		for (TrainLine trainLine : trainSystem.trainLinesMap.values()) {

			if (trainLine.getStations().contains(askStationObj)
					&& trainLine.getStations().contains(askDestinationObj)) {// check both two station Object are included in same trainline								
				notFound = false;
				zoneDiff = Math.abs(askDestinationObj.getZone()-askStationObj.getZone());				
			}
		}
		
		for(int zone:trainSystem.faresMap.keySet()) {
			if(zoneDiff == 0) {
				UI.println("| The two stations are in same zone");
				UI.println("| The distance from " + askStation + " to " + askDestination + " is " + d + " km!");
				UI.println("| Ticket Price is: 2.5");	
				break;
			}else if(zoneDiff == zone) {
				UI.println("| The number of fare zones the trip goes through: " + zoneDiff + " zones!");
				UI.println("| The distance from " + askStation + " to " + askDestination + " is " + d + " km!");
				UI.println("| Ticket Price is: " + trainSystem.faresMap.get(zone));	
				break;
			}
		}
		if (notFound) {
			UI.println("There is no Train Line connection the two Train Stations directly");
		}
		UI.println("****************************************************");	
	}
		
	private void quit() {
		UI.quit();
	}
	
	private Station doMouse(String action, double x, double y) {
		
		if(action.equals("clicked")) {
			for(Station station : trainSystem.stationsMap.values()) {				
				if(station.within(x, y)) {
				selected = station;		
				break;
				}
			}
		}
		return selected;
	}
	
	public String askStationEx(String question) {
		selected = null;
		UI.print(question);
		while (selected == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		UI.println(selected.getName());

		return selected.getName();
	}

	public String askStation(String question) {
		String askStation = UI.askString(question);

		if (trainSystem.stationsMap.containsKey(askStation)) {
			return askStation;
		} else {
			UI.println("Invalid Station, input Correct Station!");
			return askStation(question);
		}
	}

	public int askTime() {
		int askTime = UI.askInt("Input you time (24-hour, eg: 1425 for 2:45pm)");

		if (askTime >= 0 && askTime < 2400) {
			return askTime;
		} else {
			UI.println("Invalid Time, input Correct Time!");
			return askTime();
		}
	}
	
	private String padding(String input, int len) {
        return String.format("%-" + len + "s", input);
    }

	public static void main(String[] args) {
		new UserInterface();
	}

}
