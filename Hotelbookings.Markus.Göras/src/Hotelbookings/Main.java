package Hotelbookings;

public class Main {

	public static void main(String[] args) 
	{

		Methods metod = new Methods();
		metod.start();
		metod.program();
		
		System.out.println("Have a nice day!"); // Avslutningstext

	}

	
	
}



/*

Detta hotell är ett upplevelse-hotell med ett fåtal (Men ack så spännande) rum.
Vid eventuell utbyggnad av hotellet kan nya rum snabbt och enkelt läggas till i systemet.

Programmet jobbar mot filer och mappar i eclipse.workspace (\bookings och \rooms) där den sparar bokningar och datum.
Listor används bla för att jämföra datum och skicka värden emellan olika metoder. 
I filerna sparas all data som strängar och konverteras vid behov om till andra datatyper i programmets metoder.

*/