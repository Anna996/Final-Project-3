package ajbc.project.runner;

import java.time.LocalDate;
import java.util.List;

import org.bson.Document;
import org.bson.json.JsonWriterSettings;
import org.bson.types.ObjectId;

import ajbc.project.dao.BookingReservationsDao;
import ajbc.project.models.Room;
import ajbc.project.utils.LoggerConfiguration;

public class Runner {
	private static BookingReservationsDao reservationsDao;

	public static void main(String[] args) {
		LoggerConfiguration.onlyErrors();

		reservationsDao = new BookingReservationsDao();
		System.out.println("Connected to database");

//		reservationsDao.drop();
//		reservationsDao.seed();

		handleQuestion1();
		handleQuestion2(); 
		handleQuestion3(); 
		handleQuestion4(); 
		handleQuestion5(); 
		handleQuestion6(); 
		handleQuestion7(); 
		handleQuestion8();

		reservationsDao.close();
	}

	public static void printDocument(Document document) {
		System.out.println(document.toJson(JsonWriterSettings.builder().indent(true).build()));
	}

	public static void handleQuestion1() {
		String customerId = "62b3004e99d8d66a6c288cd4";
		System.out.println("Orders of customer [" + customerId + "] :");
		reservationsDao.getOrdersbyCustomerId(new ObjectId(customerId)).forEach(System.out::println);
	}

	public static void handleQuestion2() {
		String cityName = "London";
		System.out.println("Hotels in [" + cityName + "] :");
		reservationsDao.getHotelsByCity(cityName).forEach(System.out::println);
	}

	public static void handleQuestion3() {
		String hotelId = "62b3004e99d8d66a6c288cd1";
		LocalDate date = LocalDate.of(2022, 06, 30);

		System.out.println("Available Rooms At[" + hotelId + "] In Date [" + date + "]:");
		List<Room> availiableRooms = reservationsDao.getAvailableRoomsInDate(new ObjectId(hotelId), date);
		if (availiableRooms.isEmpty()) {
			System.out.println("None");
		} else {
			availiableRooms.forEach(System.out::println);
		}
	}

	public static void handleQuestion4() {
		ObjectId hotelId = new ObjectId("62b3004e99d8d66a6c288cd1");
		ObjectId customerId = new ObjectId("62b3004e99d8d66a6c288cd8");
		LocalDate startDate = LocalDate.of(2022, 8, 10);
		int numOfNights = 5, numOfPeople = 2;

		reservationsDao.createNewOrder(hotelId, customerId, startDate, numOfNights, numOfPeople);
	}

	public static void handleQuestion5() {
		String orderId = "62b35f63a1b92a1c0696d1c8";
		reservationsDao.cancelOrder(new ObjectId(orderId));
	}

	public static void handleQuestion6() {
		System.out.println("Hotels sorted by total income:");
		reservationsDao.sortHotelsByIncome().forEach(doc -> printDocument(doc));
	}

	public static void handleQuestion7() {
		System.out.println("The sum of total prices of all orders:");
		printDocument(reservationsDao.getSumTotalPricesFromOrders());
	}

	public static void handleQuestion8() {
		System.out.println("Most profitable 3 months:");
		reservationsDao.getMostProfitable3Months().forEach(doc -> printDocument(doc));
	}
}
