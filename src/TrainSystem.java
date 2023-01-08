import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import ecs100.UI;

public class TrainSystem {
	
	HashMap<String, Station> stationsMap = new HashMap<String, Station>();
	HashMap<String, TrainLine> trainLinesMap = new HashMap<String, TrainLine>();
	HashMap<Integer, Double> faresMap = new HashMap<Integer, Double>();
		
	public TrainSystem() {
		
		this.loadStation();
		this.loadTrainLines();
		this.loadTrainLineStation();
		this.loadTrainLineService();
		this.loadFares();
	}
		
	public void loadStation() {
		 
		try {
			Scanner scanner = new Scanner(new File(".\\Train network data\\stations.data"));		
			while(scanner.hasNext()) {				
				String line =  scanner.nextLine().trim();
				try (Scanner scanLine = new Scanner(line)) {
					while(scanLine.hasNext()) {
						String stationName = scanLine.next();
						int  fareZone = scanLine.nextInt();
						double distance = scanLine.nextDouble();
						double x = scanLine.nextDouble();
	                    double y = scanLine.nextDouble();
						Station station = new Station(stationName, fareZone, distance, x, y);
						stationsMap.put(stationName, station);						
					}	
				scanLine.close();
				}			
			}			
		scanner.close();		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
	
	public void loadTrainLines() {
		
		try {
			Scanner scanner = new Scanner(new File(".\\Train network data\\train-lines.data"));		
			while(scanner.hasNext()) {				
				String trainLineName =  scanner.next();
				TrainLine trainLine = new TrainLine(trainLineName);
				trainLinesMap.put(trainLineName,trainLine);										
			}
		scanner.close();		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
	
	public void loadTrainLineStation() {
	
		for(TrainLine trainLine: trainLinesMap.values()) {		
			
			try {
			Scanner scanner = new Scanner(new File(".\\Train network data/" + trainLine.getName()+ "-stations.data"));		
			while(scanner.hasNext()) {	
				String stationName = scanner.next();
				Station station = stationsMap.get(stationName);			
			    trainLine.addStation(station);
                station.addTrainLine(trainLine);
			}
			scanner.close();		
			} catch (FileNotFoundException e) {
			e.printStackTrace();
			}
		}
	}
	
	public void loadTrainLineService() {
		
		for(TrainLine trainLine: trainLinesMap.values()) {

			try {
				Scanner scanner = new Scanner(new File(".\\Train network data/" + trainLine.getName() + "-services.data"));//"Melling_Wellington"
				while(scanner.hasNext()) {
					TrainService trainService = new TrainService(trainLine);

					String line = scanner.nextLine();					
					Scanner scanLine = new Scanner(line);
					int startTime = scanLine.nextInt();
					trainService.addTime(startTime, true);
					while(scanLine.hasNext()) {
							int otherTime = scanLine.nextInt();
							trainService.addTime(otherTime, false);
							trainLine.addTrainService(trainService);									
					}			
					scanLine.close();	
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}	
	}
	
	public void loadFares() {

		try {
			Scanner scanner = new Scanner(new File(".\\Train network data/fares.data"));
			while (scanner.hasNext()) {
				String firstline = scanner.nextLine();
				while (scanner.hasNextLine()) {
					String linefromSecond = scanner.nextLine();
					try (Scanner scanLine = new Scanner(linefromSecond)) {
						while (scanLine.hasNext()) {
							int zone = scanLine.nextInt();
							double fare = scanLine.nextDouble();
							faresMap.put(zone, fare);
						}
						scanLine.close();
					}
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}


