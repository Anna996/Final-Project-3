package ajbc.project.dao;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import ajbc.project.models.Address;
import ajbc.project.models.Customer;
import ajbc.project.models.Hotel;
import ajbc.project.models.HotelName;
import ajbc.project.models.Order;
import ajbc.project.models.Room;
import ajbc.project.models.RoomOrders;
import ajbc.project.utils.MyConnectionString;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Accumulators.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class BookingReservationsDao {

	private MongoClient mongoClient;
	private MongoDatabase database;
	private MongoCollection<Hotel> hotels;
	private MongoCollection<Room> rooms;
	private MongoCollection<Order> orders;
	private MongoCollection<Customer> customers;
	private MongoCollection<RoomOrders> roomOrders;

	private MongoCollection<Document> hotelDocs;
	private MongoCollection<Document> roomDocs;
	private MongoCollection<Document> orderDocs;
	private MongoCollection<Document> customerDocs;
	private MongoCollection<Document> roomOrdersDocs;

	private static final String DATABASE = "booking_reservations";
	private static final String HOTEL_COLLECTION = "Hotels";
	private static final String ROOM_COLLECTION = "Rooms";
	private static final String ORDER_COLLECTION = "Orders";
	private static final String CUSTOMER_COLLECTION = "Customers";
	private static final String ROOM_ORDERS_COLLECTION = "Room_orders";

	public BookingReservationsDao() {
		connectToDB();
		getFromDB();
	}

	private void connectToDB() {
		CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
		CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

		ConnectionString connectionString = MyConnectionString.uri();
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString)
				.serverApi(ServerApi.builder().version(ServerApiVersion.V1).build()).codecRegistry(codecRegistry)
				.build();

		mongoClient = MongoClients.create(settings);
	}

	private void getFromDB() {
		database = mongoClient.getDatabase(DATABASE);
		
		hotels = database.getCollection(HOTEL_COLLECTION, Hotel.class);
		rooms = database.getCollection(ROOM_COLLECTION, Room.class);
		orders = database.getCollection(ORDER_COLLECTION, Order.class);
		customers = database.getCollection(CUSTOMER_COLLECTION, Customer.class);
		roomOrders = database.getCollection(ROOM_ORDERS_COLLECTION, RoomOrders.class);

		hotelDocs = database.getCollection(HOTEL_COLLECTION);
		roomDocs = database.getCollection(ROOM_COLLECTION);
		orderDocs = database.getCollection(ORDER_COLLECTION);
		customerDocs = database.getCollection(CUSTOMER_COLLECTION);
		roomOrdersDocs = database.getCollection(ROOM_ORDERS_COLLECTION);
	}

	public void seed() {

		List<Room> roomList = seedRooms();
		List<ObjectId> roomIds = roomList.stream().map(room -> room.getId()).collect(Collectors.toList());

		List<RoomOrders> roomOrdersList = roomIds.stream().map(id -> new RoomOrders(id)).collect(Collectors.toList());

		List<Hotel> hotelList = seedHotels(roomIds);
		List<ObjectId> hotelIds = hotelList.stream().map(hotel -> hotel.getId()).collect(Collectors.toList());

		List<Customer> customerList = seedCustomers();
		List<ObjectId> customerIds = customerList.stream().map(customer -> customer.getId())
				.collect(Collectors.toList());

		List<Order> orderList = seedOrders(hotelIds, roomIds, customerIds, hotelList);
		List<ObjectId> orderIds = orderList.stream().map(order -> order.getId()).collect(Collectors.toList());

		hotelList.get(0).addOrder(orderIds.get(0));
		hotelList.get(0).addOrder(orderIds.get(1));
		hotelList.get(1).addOrder(orderIds.get(2));
		hotelList.get(2).addOrder(orderIds.get(3));

		customerList.get(0).addOrder(orderIds.get(0));
		customerList.get(1).addOrder(orderIds.get(1));
		customerList.get(2).addOrder(orderIds.get(2));
		customerList.get(3).addOrder(orderIds.get(3));

		roomOrdersList.get(0).addDates(orderList.get(0).getStartDate(),
				orderList.get(0).getStartDate().plusDays(orderList.get(0).getNumNights()));
		roomOrdersList.get(2).addDates(orderList.get(1).getStartDate(),
				orderList.get(1).getStartDate().plusDays(orderList.get(1).getNumNights()));
		roomOrdersList.get(4).addDates(orderList.get(2).getStartDate(),
				orderList.get(2).getStartDate().plusDays(orderList.get(2).getNumNights()));
		roomOrdersList.get(7).addDates(orderList.get(3).getStartDate(),
				orderList.get(3).getStartDate().plusDays(orderList.get(3).getNumNights()));

		hotels.insertMany(hotelList);
		rooms.insertMany(roomList);
		customers.insertMany(customerList);
		orders.insertMany(orderList);
		roomOrders.insertMany(roomOrdersList);
	}

	private List<Room> seedRooms() {
		return Arrays.asList(
				// HERMOSO - max 2 people
				new Room(10, false), new Room(12, false), new Room(14, true), new Room(16, true),

				// LINDO - max 4 people
				new Room(22, false), new Room(24, true), new Room(26, true),

				// BELLO - max 3 people
				new Room(32, false), new Room(34, true));
	}

	private List<Hotel> seedHotels(List<ObjectId> roomIds) {
		return Arrays.asList(
				new Hotel(HotelName.HERMOSO, new Address("Oxford", 52, "London", "UK"), 94.5f, roomIds.subList(0, 4),
						108.5f, 2),
				new Hotel(HotelName.LINDO, new Address("Gran הia", 135, "Madrid", "Spain"), 95f, roomIds.subList(4, 7),
						125f, 4),
				new Hotel(HotelName.BELLO, new Address("Rivoli Street", 123, "Paris", "France"), 98.8f,
						roomIds.subList(7, 9), 100f, 3));
	}

	private List<Customer> seedCustomers() {
		return Arrays.asList(new Customer(3333, "Avi", "Cohen", "Italy"), new Customer(4444, "Chen", "Levi", "Israel"),
				new Customer(5555, "Maria", "Maria", "Russia"), new Customer(6666, "Avi", "Shalom", "Israel"),
				new Customer(7777, "Gal", "Gadot", "USA"), new Customer(8888, "Natali", "Natilia", "France"),
				new Customer(9999, "Dan", "Cohen", "Israel"));
	}

	private List<Order> seedOrders(List<ObjectId> hotelIds, List<ObjectId> roomIds, List<ObjectId> customerIds,
			List<Hotel> hotelList) {
		return Arrays.asList(
				new Order(hotelIds.get(0), roomIds.get(0), customerIds.get(0), LocalDate.of(2022, 6, 25), 3,
						3 * hotelList.get(0).getPricePerNight(), 1),
				new Order(hotelIds.get(0), roomIds.get(2), customerIds.get(1), LocalDate.of(2022, 6, 29), 5,
						5 * hotelList.get(0).getPricePerNight(), 2),
				new Order(hotelIds.get(1), roomIds.get(4), customerIds.get(2), LocalDate.of(2022, 6, 23), 10,
						10 * hotelList.get(1).getPricePerNight(), 4),
				new Order(hotelIds.get(2), roomIds.get(7), customerIds.get(3), LocalDate.of(2022, 7, 25), 4,
						4 * hotelList.get(2).getPricePerNight(), 2));
	}

	public List<Order> getOrdersbyCustomerId(ObjectId customerId) {

		return orders.find(eq("customer_id", customerId)).into(new ArrayList<>());
	}

	public List<Hotel> getHotelsByCity(String cityName) {

		return hotels.find(eq("address.city", cityName)).into(new ArrayList<>());
	}

	public List<Room> getAvailableRoomsInDate(ObjectId hotelId, LocalDate date) {
		List<Room> availiableRooms = new ArrayList<Room>();
		List<ObjectId> hotelRoomsIds = hotels.find(eq("_id", hotelId)).first().getRooms();

		for (ObjectId roomId : hotelRoomsIds) {
			if (isRoomAvailable(roomId, date)) {
				availiableRooms.add(rooms.find(eq("_id", roomId)).first());
			}
		}

		return availiableRooms;
	}

	public boolean isRoomAvailable(ObjectId roomId, LocalDate date) {

		List<LocalDate> datesFromDB = new ArrayList<>();
		Bson match = match(eq("room_id", roomId));
		Bson unwind = unwind("$dates");
		AggregateIterable<Document> docs = roomOrdersDocs.aggregate(Arrays.asList(match, unwind));

		docs.forEach(doc -> datesFromDB.add(parseDate((Date) (doc.get("dates")))));

		return !datesFromDB.contains(date);
	}

	private LocalDate parseDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public void createNewOrder(ObjectId hotelId, ObjectId customerId, LocalDate startDate, int numOfNights,
			int numOfPeople) {

		int maxPeople = getMaxPeoplePerRoom(hotelId);
		if (numOfPeople > maxPeople) {
			System.out.println("The hotel Rooms can contain max [" + maxPeople + "] people per room");
			return;
		}

		List<ObjectId> availiableRooms = getAvailableRoomsInDates(hotelId, startDate, numOfNights);
		if (availiableRooms.isEmpty()) {
			System.out.println("There is now available rooms in these dates");
			return;
		}

		int index = new Random().nextInt(availiableRooms.size());
		ObjectId roomId = availiableRooms.get(index);
		Order newOrder = new Order(hotelId, roomId, customerId, startDate, numOfNights,
				numOfNights * getPricePerNight(hotelId), numOfPeople);

		// GET from DATABASE
		Hotel hotel = hotels.find(getHotelFilterId(hotelId)).first();
		Customer customer = customers.find(getCustomerFilterId(customerId)).first();
		RoomOrders roomOrder = roomOrders.find(getRoomOrdersFilterRoomId(roomId)).first();

		// UPDATE in java POJOs
		hotel.addOrder(newOrder.getId());
		customer.addOrder(newOrder.getId());
		roomOrder.addDates(startDate, startDate.plusDays(numOfNights));

		// INSERT Back to DATABASE
		hotels.findOneAndReplace(getHotelFilterId(hotelId), hotel);
		customers.findOneAndReplace(getCustomerFilterId(customerId), customer);
		roomOrders.findOneAndReplace(getRoomOrdersFilterRoomId(roomId), roomOrder);
		orders.insertOne(newOrder);

		System.out.println("New order was successfuly created");
		System.out.println(
				String.format("orderId: [%s], hotelId: [%s], roomId: [%s]", newOrder.getId(), hotelId, roomId));
	}

	private Bson getHotelFilterId(ObjectId hotelId) {
		return eq("_id", hotelId);
	}

	private Bson getCustomerFilterId(ObjectId customerId) {
		return eq("_id", customerId);
	}

	private Bson getRoomOrdersFilterRoomId(ObjectId roomId) {
		return eq("room_id", roomId);
	}

	public float getPricePerNight(ObjectId hotelId) {
		String pricePerNight = "price_per_night";
		Document doc = hotelDocs
				.aggregate(Arrays.asList(project(fields(include(pricePerNight))), match(getHotelFilterId(hotelId))))
				.first();
		double price = (double) doc.get(pricePerNight);

		return (float) price;
	}

	public int getMaxPeoplePerRoom(ObjectId hotelId) {
		String maxPeople = "max_people_per_room";
		Document doc = hotelDocs
				.aggregate(Arrays.asList(project(fields(include(maxPeople))), match(getHotelFilterId(hotelId))))
				.first();
		return (int) doc.get(maxPeople);
	}

	private List<ObjectId> getAvailableRoomsInDates(ObjectId hotelId, LocalDate startDate, int numOfNights) {

		LocalDate endDate = startDate.plusDays(numOfNights);
		if (startDate.isAfter(endDate)) {
			return null;
		}

		List<ObjectId> availiableRooms = new ArrayList<>();
		Map<ObjectId, Integer> seenRooms = new HashMap<ObjectId, Integer>();
		LocalDate date = startDate;

		while (!date.isEqual(endDate)) {

			getAvailableRoomsInDate(hotelId, date).forEach(room -> {
				ObjectId roomId = room.getId();

				seenRooms.putIfAbsent(roomId, 0);
				seenRooms.put(roomId, seenRooms.get(roomId) + 1);
			});

			date = date.plusDays(1);
		}

		seenRooms.forEach((roomId, count) -> {
			if (count == numOfNights) {
				availiableRooms.add(roomId);
			}
		});

		return availiableRooms;
	}

	public void cancelOrder(ObjectId OrderId) {
		Order deletedOrder = orders.findOneAndDelete(eq("_id", OrderId));

		if (deletedOrder == null) {
			System.out.println("There is no such order in database");
			return;
		}

		Hotel hotel = hotels.find(getHotelFilterId(deletedOrder.getHotelId())).first();
		Customer customer = customers.find(getCustomerFilterId(deletedOrder.getCustomerId())).first();
		RoomOrders roomOrder = roomOrders.find(getRoomOrdersFilterRoomId(deletedOrder.getRoomId())).first();

		hotel.removeOrder(OrderId);
		customer.removeOrder(OrderId);
		roomOrder.removeDates(deletedOrder.getStartDate(),
				deletedOrder.getStartDate().plusDays(deletedOrder.getNumNights()));

		hotels.findOneAndReplace(getHotelFilterId(deletedOrder.getHotelId()), hotel);
		customers.findOneAndReplace(getCustomerFilterId(deletedOrder.getCustomerId()), customer);
		roomOrders.findOneAndReplace(getRoomOrdersFilterRoomId(deletedOrder.getRoomId()), roomOrder);

		System.out.println("Order [" + OrderId + "] was deleted from database");
	}

	public List<Document> sortHotelsByIncome() {

		Bson leftJoin = lookup(HOTEL_COLLECTION, "hotel_id", "_id", "hotel");
		Bson groupByHotel = group("$hotel.name", sum("total_income", "$total_price"));
		Bson orderByIncome = sort(descending("total_income"));
		Bson project = project(fields(include("total_income"), excludeId(), computed("hotel_name", "$_id")));

		return orderDocs.aggregate(Arrays.asList(leftJoin, groupByHotel, orderByIncome, project))
				.into(new ArrayList<>());
	}

	public Document getSumTotalPricesFromOrders() {
		Bson group = group("", sum("sum_of_total_prices", "$total_price"));
		Bson project = project(excludeId());

		return orderDocs.aggregate(Arrays.asList(group, project)).first();
	}

	public List<Document> getMostProfitable3Months() {

		Bson groupByMonth = group(eq("$month", "$start_date"), max("profit", "$total_price"));
		Bson orderByProfit = sort(descending("profit"));
		Bson project = project(fields(excludeId(), computed("month", "$_id"),include("profit")));
		Bson limit3 = limit(3);
		
		return orderDocs.aggregate(Arrays.asList(groupByMonth, orderByProfit,project, limit3)).into(new ArrayList<>());
	}

	public void close() {
		mongoClient.close();
	}

	public void drop() {
		hotels.drop();
		rooms.drop();
		customers.drop();
		orders.drop();
		roomOrders.drop();
	}
}
