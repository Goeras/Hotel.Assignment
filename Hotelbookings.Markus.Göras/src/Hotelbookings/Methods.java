package Hotelbookings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Methods {

	Scanner sc = new Scanner(System.in);

	public void program() //Huvudmeny
	{
		boolean menuContinue = true;
		
		do
		{
			printMenu();
			String choice = sc.nextLine().toUpperCase();
			switch (choice)
			{
			case "B": // Boka rum
				booking();
				break;
			case "V": // Se bokning..
				System.out.println("Enter booking number:");
				try
				{
					int bookingNr = sc.nextInt();
					sc.nextLine(); // "Consume pga nextInt() ovanför"
					readFromFile(bookingNr);
					wait(2000);
				}
				catch(InputMismatchException ime)
				{
					sc.nextLine(); // "Consume pga nextInt() ovanför"
					System.out.println("Wrong input, enter booking number with digits.\n");
				}
				
				break;
			case "C": // Avboka..
				System.out.println("Enter booking number:");
				try
				{
					int bookingNr = sc.nextInt();
					sc.nextLine(); // "Consume pga nextInt() ovanför"
					readFromFileRoomToDelete(bookingNr);
					deleteFile(bookingNr);
				}
				catch(InputMismatchException ime)
				{
					sc.nextLine(); // "Consume pga nextInt() ovanför"
					System.out.println("Wrong input, enter booking number with digits.\n");
				}
				break;
			case "Q": // Avsluta..
				menuContinue = false;
				break;
			case "A": // Skriv ut alla befintliga bokningar..
				readAllFiles();
				wait(2000);
				break;
			default:
				System.out.println("\n***Wrong input, try again***\n");
				break;
			}	
		}while(menuContinue);
	}
	
	public void booking() // Bokningsmeny
	{   // Här går det att lägga till så många rum som en switch-case och fantasin tillåter. 
		System.out.println("**Experience Rooms**\n"
				+ "Space-room       (S)\n"
				+ "Djungle-room     (D)\n"
				+ "Unicorn-room     (U)\n"
				+ "Cat-room         (C)\n"
				+ "Heavy Metal-room (H)\n"
				+ "Grown Ups-room   (G)\n"
				+ "Ogre-room        (O)\n"
				+ "Quit             (Q)");
		String choice = sc.nextLine().toUpperCase();
		switch(choice)
		{
		case "S":
			bookRoom("Space-room");
			break;
			
		case "D":
			bookRoom("Djungle-room");
			break;
			
		case "U":
			bookRoom("Unicorn-room");
			break;
			
		case "C":
			bookRoom("Cat-room");
			break;
			
		case "H":
			bookRoom("Heavy Metal-room");
			break;
			
		case "G":
			bookRoom("Grown Ups-room");
			break;
			
		case "O":
			bookRoom("Ogre-room");
			break;
			
		case "Q":
			break;
		default:
			System.out.println("Wrong input..");
			break;
		}
	}
	
	public void bookRoom(String room) // Metod för rumsbokning
	{
		List<LocalDate> totalDates = bookDates();
		boolean datesAvailable = true;
		int bookingNr = randomBookingNumber(); // Skapa bokningsnummer
		int numberOfNights=totalDates.size()-1;
		
		datesAvailable = checkIfOccupied(room, totalDates); // Kolla om datum är ledigt..
		if (datesAvailable) // Om datum är tillgängligt..
		{	
			try 
			{
				String stringDates = arrayToString(totalDates);
				String name ="";
				boolean validInput = false;
				while (!validInput)
				{
					System.out.println("Enter name of the guest: ");
					name = sc.nextLine();
					if(name.matches("^[a-zA-ZåäöÅÄÖ -]*$")) // Regex för att säkerställa att namnet innehåller bokstäver, även " " & "-" är tillåtet för tex dubbelnamn.
					{
						validInput = true;
					}
					else
						System.out.println("Use letters only.");
				}
				writeToFile(bookingNr, name, room, numberOfNights, stringDates); // Skriver bokningsinfo till boknings-fil..
				bookRoomDates(room, totalDates, true); // Skriver datum till rums-fil..
				System.out.println("Booking confirmed. Booking number is: "+bookingNr+"\n");
				wait(2000);
			}
			catch(Exception e)
			{
				System.out.println("Something went wrong in method: bookRoom()");
				e.printStackTrace();
			}
		}
		else  // Om datum är upptaget..
		{
			System.out.println("Dates not avilable for this room.\n");
			wait(1000);
		}
		
	}
	
	public void printMenu() // Metod för att skriva ut huvudmeny
	{
		System.out.println("****Choose from Menu****\n"
				+ "*Book a room        (B)*\n"
				+ "*View booking       (V)*\n"
				+ "*Cancel booking     (C)*\n"
				+ "*All bookings       (A)*\n"
				+ "*Quit               (Q)*\n"
				+ "************************");
	}
	
	public List<LocalDate> bookDates() // Metod för datum-bokning.
	{
		List<LocalDate> dates = new ArrayList<>();
		boolean validInput = false;
		LocalDate start = null;
		LocalDate end = null;
		try 
		{
			while(!validInput)
			{
				System.out.println("Enter the date for check-in (YYYY-MM-DD)");
				start = LocalDate.parse(sc.nextLine());
				System.out.println("Enter the date for check-out (YYYY-MM-DD)");
				end = LocalDate.parse(sc.nextLine());
				
				if(start.isBefore(LocalDate.now()) || end.isBefore(start)) // Kollar så att datum inte har passerad och att utcheckning inte är före inchekning.
				{
					System.out.println("Dates not valid. Make sure check-out is after check-in and entered dates has not already passed.");
				}
				else
				{
					validInput = true;
				}
			}
			while (!start.isAfter(end)) // Lägger till alla datum från in-check till ut-check i Listan.
			{
				dates.add(start);
				start = start.plusDays(1);
			}
		}
		catch (DateTimeParseException | InputMismatchException e)
		{
			System.out.println("not a valid input");
			return bookDates();
		}
		return dates;
		
	}
	
	public void readFromFile(int bookNr) // Metod för att läsa bokning från fil
	{
		try
		{
			String strBookingNr = Integer.toString(bookNr);
			String filepath = ".\\bookings\\"+strBookingNr+".txt";
			BufferedReader br = new BufferedReader(new FileReader(filepath));
			StringBuilder sb = new StringBuilder();
			String message = br.readLine();
			
			while(message!=null)
			{
				sb.append(message+System.lineSeparator());
				message =br.readLine();
			}
			String output = sb.toString();
			System.out.println("***Booking***\n"+output);
			br.close();
		}
		catch(IOException ioe)
		{
			System.out.println("Cant find a booking with that number.\n");
		}
		
	}
	
	public void writeToFile(int bookNr, String name, String roomType, int nights, String dates ) // Skriver bokning till fil
	{
		try {
		String strBookingNr = Integer.toString(bookNr);
		String strNightsNr = Integer.toString(nights);
		String message = "Booking Nr: "+strBookingNr+"\nName: "+name+"\nRoom: "+roomType+"\nBooked dates: "+dates+"\nNumber of nights: "+strNightsNr;
		String filepath = ".\\bookings\\"+strBookingNr+".txt";
		
		File file = new File(filepath);
		if(!file.exists())
		{
			file.createNewFile();
		}
		FileWriter fr = new FileWriter(file.getAbsoluteFile(),false);
		BufferedWriter bw = new BufferedWriter(fr);
		bw.write(message+System.lineSeparator());
		bw.close();
		
		}
		catch(IOException e)
		{
			System.out.println("Someting went wrong in method: writeToFile()");
		}
	}
	
	public void deleteFile(int bookNr) // Metod för att ta bort bokningsfil
	{
		String strBookingNr = Integer.toString(bookNr);
		String filepath = ".\\bookings\\"+strBookingNr+".txt";
		File file = new File(filepath);
		if(file.exists())
		{
			file.delete();
			wait(1000);
		}
	}
	
	public boolean checkIfOccupied(String room, List<LocalDate> dates) // Metod för att kolla om valda datum är lediga.
	{
		boolean available = true;
		try
		{
			String filepath = ".\\rooms\\"+room+".txt";
			File file = new File(filepath);
			
			  if(!file.exists()) { file.createNewFile(); }
			 
			BufferedReader br = new BufferedReader(new FileReader(filepath));
			String line;
			while((line = br.readLine())!=null)
			{
				try {
	                LocalDate dateFromFile = LocalDate.parse(line);
	                if (dates.contains(dateFromFile)) 
	                {
	                    available = false;
	                    break;
	                }
	            } catch (NumberFormatException nfe) 
				{
	            	System.out.println("Someting wrong in method: checkIfOccupied");
	            	nfe.printStackTrace();
	            }
			}
			br.close();
		}
		catch(IOException ioe)
		{
			System.out.println("Someting went wrong in method: checkIfOccupied");
			ioe.printStackTrace();
		}
		return available;
	}
	
	public void bookRoomDates(String room, List<LocalDate> dates, boolean keepOldValues) // Metod för att skriva bokade datum till rums-filen
	{
		boolean keepValues = keepOldValues;
		try {
				for (int i=0; i<dates.size();i++)
				{
					String message = dates.get(i).toString();
					String filepath = ".\\rooms\\"+room+".txt";
					File file = new File(filepath);
					if(!file.exists())
					{
						file.createNewFile();
					}
					FileWriter fr = new FileWriter(file.getAbsoluteFile(),keepValues);
					BufferedWriter bw = new BufferedWriter(fr);
					bw.write(message+System.lineSeparator());
					bw.close();
					keepValues = true;
			
				}
			}
			catch(IOException ioe)
			{
				System.out.println("Something went wrong when writing to file(bookRoomDates)");
				ioe.printStackTrace();
			}
	}
	
	public void deleteDatesFromRoomFile(List<LocalDate> dates, String room) // Metod för att ta bort datum från fil (Skapar ny fil med nya värden i anropad metod)
	{
		List<LocalDate> oldNumbers = new ArrayList<>(); // Lista där alla benfitliga bokade datum för gällande rum sparas.
		try
		{
			String filepath = ".\\rooms\\"+room+".txt";
			BufferedReader br = new BufferedReader(new FileReader(filepath));
			StringBuilder sb = new StringBuilder();
			String message = br.readLine();
			// Läser ut befintliga värden (datum) från fil.
			while(message!=null)
			{
				try
				{
					LocalDate date = LocalDate.parse(message);
					oldNumbers.add(date);
					sb.append(message+System.lineSeparator());
					message =br.readLine();
				}
				catch (DateTimeParseException dtpe) 
				{
					System.out.println("Something wrong in deleteDatesFromRoomFile..");
                    dtpe.printStackTrace();
                }
			}
			br.close();
			oldNumbers.removeAll(dates); // Tar bort avbokningsdatum, behåller övriga.
			
			// Tar bort den gamla filen..
			File file = new File(filepath);
			if(file.exists())
			{
				file.delete();
				System.out.println("Booking cancelled.\n");
				wait(1000);
			}
			else 
			{
				System.out.println("Cant delete the file, Give us a call..");
			}
			// Skapa en ny fil med uppdaterade datum..
			bookRoomDates(room, oldNumbers, false);
		}
		catch(IOException ioe)
		{
			System.out.println("Something wrong in deleteDatesFromRoomFile..");
			ioe.printStackTrace();
		}
	}
	
	public void readFromFileRoomToDelete(int bookingNumber) // Läser ut vilket rum som avbokningen gäller
	{
		String room ="";
		String datesToRemove = "";
		try
		{
			String strBookingNr = Integer.toString(bookingNumber);
			String filepath = ".\\bookings\\"+strBookingNr+".txt";
			BufferedReader br = new BufferedReader(new FileReader(filepath));
			
			for (int lineCounter = 1; lineCounter <= 4; lineCounter++) {
	            String message = br.readLine();
	            if (lineCounter == 3) {
	                room = message.substring(6); // Läser substrängen för att enbart fånga upp rumsnamnet från rad 3 och spara till variabel.
	            } else if (lineCounter == 4) {
	                datesToRemove = message; // Sparar hela strängen från rad 4 till variabel (Datumen sorteras sedan ur från resterande del av sträng i annan metod)
	            }
	        }
			br.close();
			dateValuesFromString(datesToRemove, room); // Anropar metod för att konvertera en Sträng till datum..
			
		}
		catch(IOException ioe)
		{
			System.out.println("Cant find your booking.. Make sure to type a correct booking number.\n");
		}
	}
	
	public void wait(int ms) // Metod för att skapa fördröjning i programmet mellan vissa utskrifter
	{
		try
		{
			Thread.sleep(ms);
		}
		catch(InterruptedException ie)
		{
			Thread.currentThread().interrupt();
		}
	}
	
	public void readAllFiles() //Skriver ut alla befintliga bokningar
	{
		fileCounter(); // Anropar metod som räknar och skriver ut antal befintliga bokningar

        String directoryPath = ".\\bookings\\";
        Path directory = Paths.get(directoryPath);
        try {
            Files.list(directory).forEach(file -> 
            {
                if (Files.isRegularFile(file)) 
                {
                    System.out.println("File: " + file.getFileName());
                    String booking = file.getFileName().toString().replace(".txt", "");
                    int bookingNr = Integer.parseInt(booking);
                    readFromFile(bookingNr); // Anropar metod och skriver ut bokningar en och en.
                }
            });
        } catch (IOException e) 
        {
        	System.out.println("Something went wrong in method: readAllFiles()");
            e.printStackTrace();
        }
	}
	
	public boolean bookingNumberUnique(int num) // Metod för att säkerställa att nytt bokningsnummer är unikt för att undvika duletter.
	{
		String strNum = Integer.toString(num);
		List<String> files = new ArrayList<>();
		String directoryPath = ".\\bookings\\";
        Path directory = Paths.get(directoryPath);
        try {
            Files.list(directory).forEach(file -> 
            {
                if (Files.isRegularFile(file)) 
                {
                    files.add(file.getFileName().toString().replace(".txt", ""));
                }
            });
        } catch (IOException e) 
        {
        	System.out.println("Something went wrong in method: bookingNumberUnique()");
            e.printStackTrace();
        }
        if (files.contains(strNum))
        {
        	return false;
        }
        else
        {
        	return true;
        }
	}
	
	public String arrayToString(List<LocalDate> a) // Metod för att konvertera en Lista med datum till en String.
	{
		StringBuilder arr = new StringBuilder();
		for (int i =0; i<a.size();i++)
		{
			arr.append(a.get(i));
			arr.append(", ");
		}
		return arr.toString();
	}
	
	public void dateValuesFromString(String stringWithDates, String room) // Metod för att konvertera en Sträng med datum till en List<LocalDate>
	{
		try
		{
			List<LocalDate> dates = new ArrayList<>();
			Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}"); // Regex för att plocka datum-värden ur en sträng, lägger sedan till dessa i en ArrayList i while-loopen nedanför.
			Matcher matcher = pattern.matcher(stringWithDates);
			while (matcher.find())
			{
				String strDate = matcher.group();
				LocalDate date = LocalDate.parse(strDate);
				dates.add(date);
			}
			deleteDatesFromRoomFile(dates, room); // Anropar metod för att ta bort bokade datum från room-file
		}
		catch (DateTimeParseException dpe)
		{
			System.out.println("Something went wrong in method: dateValueFromString()");
			dpe.printStackTrace();
		}
	}
	
	public void fileCounter() // Metod för att räkna antalet befintliga bokningar. (räknar antal filer i mappen "bookings")
	{
		try {
			int fileCounter = 0;
			String path = ".\\bookings\\";
			File directory = new File(path);
			if (directory.exists() && directory.isDirectory()) 
			{
			    File[] files = directory.listFiles();

			    if (files != null) 
			    {
			        fileCounter = 0;

			        for (File file : files) 
			        {
			            if (file.isFile()) 
			            {
			                fileCounter++;
			            }
			        }

			        System.out.println("Number of bookings: " + fileCounter);
			    } else 
			    {
			        System.err.println("Unable to list any bookings.");
			    }
			} else 
			{
			    System.err.println("The directory does not exist..");
			}
		} catch (Exception e) {
			System.out.println("Something went wrong i method: fileCounter()");
			e.printStackTrace();
		}
	}
	
	public int randomBookingNumber() // Metod för att skapa ett random bokningsnummer mellan 1000-9999
	{
		Random random = new Random();
		int bookingNr = random.nextInt(9000)+1000;
		boolean uniqueNumber = bookingNumberUnique(bookingNr); // Anropar metod för att kolla om bokningsnumret är unikt bland befintliga bokningar.
		if (!uniqueNumber)
		{
			return randomBookingNumber(); // OM bokingsnumret inte är unikt, gör om proceduren.
		}
		return bookingNr;
	}
	
	public void start() // En liten onödig startmetod bara för skojs skull.. (Kommentera bort anropet i Main om programmet skall startas frekvent för att slippa bli gråhårig)
	{
		System.out.println("Starting program..");
		wait(1000);
		System.out.print("Searching for updates..");
		wait(1000);
		System.out.print("..");
		wait(1000);
		System.out.print("..\n");
		wait(1000);
		System.out.println("Booking system ready..\n\n\n\n\n\n");
		wait(1000);
	}
	
}
