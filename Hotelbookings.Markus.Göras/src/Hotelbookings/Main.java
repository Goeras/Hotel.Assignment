package Hotelbookings;

public class Main {

	public static void main(String[] args) 
	{

		Methods metod = new Methods();
		System.out.println("Welcome to Wigells Fantastic, Unique and Extra Ordinary Hotel Rooms!\n"); // Välkomsttext
		
		metod.wait(1000);
		metod.program();
		
		System.out.println("Have a nice day!"); // Avslutningstext

	}

	
	
}



/*

Detta hotell är ett upplevelse-hotell med ett fåtal (Men ack så spännande) rum.

Programmet jobbar mot filer och mappar i eclipse.workspace (\bookings och \rooms) där den sparar bokningar och lediga datum 
(Här bokar man bara datum innevarande månad, tider längre fram än så får man helt enkelt inte boka på detta oerhört exklusiva hotell.)

*/