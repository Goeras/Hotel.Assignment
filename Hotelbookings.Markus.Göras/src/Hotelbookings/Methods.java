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
import java.util.ArrayList;
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
				System.out.println("Ange ditt bokningsnr:");
				try
				{
					int bookingNr = sc.nextInt();
					sc.nextLine(); // "Consume pga nextInt() ovanför"
					readFromFile(bookingNr);
					wait(2000);
				}
				catch(Exception e)
				{
					System.out.println("Wrong input, enter your booking number with digits.");
				}
				
				break;
			case "C": // Avboka..
				System.out.println("Ange ditt bokningsnr:");
				try
				{
					int bookingNr = sc.nextInt();
					sc.nextLine(); // "Consume pga nextInt() ovanför"
					readFromFileRoomToDelete(bookingNr);
					deleteFile(bookingNr);
				}
				catch(Exception e)
				{
					sc.nextLine(); // "Consume pga nextInt() ovanför"
					System.out.println("Wrong input, enter your booking number with digits.");
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
	{
		System.out.println("What kind of room do you want?\n"
				+ "Space-room      (S)\n"
				+ "Djungle-room    (D)\n"
				+ "Unicorn-room    (U)\n"
				+ "Cat-room        (C)\n"
				+ "Heavy Metal-room(H)\n"
				+ "Grown Ups-room  (G)\n"
				+ "Ogre-room       (O)\n"
				+ "Quit            (Q)");
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
		boolean datesAvailable = true;
		int bookingNr = randomBookingNumber(); // Skapa bokningsnummer
		List<Integer> dates = new ArrayList<>();
		int numberOfNights=0;
		System.out.println("Enter the dates you want to stay, with digits(1-31)\n"
				+ "Separate with Enter.\n"
				+ "Press any letter when finished");
		while(sc.hasNextInt())
		{
			int i = sc.nextInt();
			numberOfNights++;
			dates.add(i); // Lägger till önskade datum i ArrayList
		}
		datesAvailable = checkIfOccupied(room, dates); // Kolla om datum är ledigt..
		if (datesAvailable) // Om datum är tillgängligt..
		{	
			try 
			{
				String stringDates = arrayToString(dates);
				sc.nextLine(); // "Consume". Consumar nextLine() ifrån tidigare nextInt()..
				System.out.println("Enter your name: ");
				String name = sc.nextLine();
				writeToFile(bookingNr, name, room, numberOfNights, stringDates); // Skriver bokningsinfo till boknings-fil..
				bookRoomDates(room, dates, true); // Skriver datum till rums-fil..
				System.out.println("Booking confirmed. Your booking number is: "+bookingNr+"\n");
				wait(2000);
			}
			catch(Exception e)
			{
				System.out.println("Wrong input, use digits..");
			}
		}
		else  // Om datum är upptaget..
		{
			sc.nextLine(); // "Consumer"
			System.out.println("Dates not avilable.\n");
			wait(1000);
		}
		
	}
	
	public void printMenu() // Metod för att skriva ut huvudmeny
	{
		System.out.println("What do you want to do?\n"
				+ "Book a room       (B)\n"
				+ "View booking      (V)\n"
				+ "Cancel booking    (C)\n"
				+ "All bookings      (A)\n"
				+ "Quit              (Q)");
	}
	
	public void readFromFile(int bookNr) // Metod för att läsa boking från fil
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
		catch(Exception e)
		{
			System.out.println("writeToFile knas");
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
	
	public boolean checkIfOccupied(String room, List<Integer> dates) // Metod för att kolla om valda datum är lediga.
	{
		boolean available = true;
		try
		{
			String filepath = ".\\rooms\\"+room+".txt";
			File file = new File(filepath);
			if(!file.exists())
			{
				file.createNewFile();
			}
			BufferedReader br = new BufferedReader(new FileReader(filepath));
			String line;
			while((line = br.readLine())!=null)
			{
				try {
	                int dateFromFile = Integer.parseInt(line);
	                if (dates.contains(dateFromFile)) 
	                {
	                    available = false;
	                    break;
	                }
	            } catch (NumberFormatException e) 
				{
	                e.printStackTrace();
	            }
			}
			sc.nextLine(); //"Consume"
			br.close();
		}
		catch(IOException ioe)
		{
			System.out.println("checkIfOccupied knas");
		}
		return available;
	}
	
	public void bookRoomDates(String room, List<Integer> dates, boolean keepOldValues) // Metod för att skriva bokade datum till rums-filen
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
			catch(Exception e)
			{
				e.printStackTrace();
			}
	}
	
	public void deleteDatesFromRoomFile(List<Integer> dates, String room) // Metod för att ta bort datum från fil (Skapar ny fil med nya värden i anropad metod)
	{
		List<Integer> oldNumbers = new ArrayList<>();
		try
		{
			String filepath = ".\\rooms\\"+room+".txt";
			BufferedReader br = new BufferedReader(new FileReader(filepath));
			StringBuilder sb = new StringBuilder();
			String message = br.readLine();
			
			while(message!=null)
			{
				try
				{
					int number = Integer.parseInt(message);
					oldNumbers.add(number);
					sb.append(message+System.lineSeparator());
					message =br.readLine();
				}
				catch (NumberFormatException e) 
				{
                    e.printStackTrace();
                }
			}
			br.close();
			oldNumbers.removeAll(dates);
			
			// ta bort den gamla filen..
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
	                room = message.substring(6);
	            } else if (lineCounter == 4) {
	                datesToRemove = message;
	            }
	        }
			br.close();
			intValuesFromString(datesToRemove, room);
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
	
	public String arrayToString(List<Integer> a) // Metod för att konvertera en Lista med Integers till en String.
	{
		StringBuilder arr = new StringBuilder();
		for (int i =0; i<a.size();i++)
		{
			arr.append(a.get(i));
			arr.append(", ");
		}
		return arr.toString();
	}
	
	public void intValuesFromString(String stringWithDates, String room) // Metod för att sortera ut int-värden ifrån en String.
	{
		List<Integer> numbers = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d+"); // Regex för att plocka nummer-värden ur en sträng, lägger sedan till dessa i en ArrayList i while-loopen nedanför.
        Matcher matcher = pattern.matcher(stringWithDates);

        while (matcher.find()) 
        {
            int number = Integer.parseInt(matcher.group());
            numbers.add(number);
        }
        deleteDatesFromRoomFile(numbers, room); // Anropar metod som tar bort datum från rums-fil.
	}
	
	public void fileCounter() // Metod för att räkna antalet befintliga bokningar.
	{
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
	}
	
	public int randomBookingNumber() // Metod för att skapa ett random bokningsnummer mellan 1000-9999
	{
		Random random = new Random();
		int bookingNr = random.nextInt(9000)+1000;
		boolean uniqueNumber = bookingNumberUnique(bookingNr);
		if (!uniqueNumber)
		{
			return randomBookingNumber(); // OM bokingsnumret inte är unikt, gör om proceduren.
		}
		return bookingNr;
	}
	
}
