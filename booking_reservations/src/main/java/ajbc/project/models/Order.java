package ajbc.project.models;

import java.time.LocalDate;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Order {
	private ObjectId id;
	@BsonProperty(value = "hotel_id")
	private ObjectId hotelId;
	@BsonProperty(value = "room_id")
	private ObjectId roomId;
	@BsonProperty(value = "customer_id")
	private ObjectId customerId;
	@BsonProperty(value = "start_date")
	private LocalDate startDate;
	@BsonProperty(value = "num_nights")
	private int numNights;
	@BsonProperty(value = "total_price")
	private float totalPrice;
	@BsonProperty(value = "num_people")
	private int numPeople;

	public Order(ObjectId hotelId, ObjectId roomId, ObjectId customerId, LocalDate startDate, int numNights,
			float totalPrice, int numPeople) {
		this.id = ObjectId.get();
		this.hotelId = hotelId;
		this.roomId = roomId;
		this.customerId = customerId;
		this.startDate = startDate;
		this.numNights = numNights;
		this.totalPrice = totalPrice;
		this.numPeople = numPeople;
	}

	public Order() {
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public ObjectId getHotelId() {
		return hotelId;
	}

	public ObjectId getRoomId() {
		return roomId;
	}

	public void setHotelId(ObjectId hotelId) {
		this.hotelId = hotelId;
	}

	public void setRoomId(ObjectId roomId) {
		this.roomId = roomId;
	}

	public ObjectId getCustomerId() {
		return customerId;
	}

	public void setCustomerId(ObjectId customerId) {
		this.customerId = customerId;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public int getNumNights() {
		return numNights;
	}

	public void setNumNights(int numNights) {
		this.numNights = numNights;
	}

	public float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getNumPeople() {
		return numPeople;
	}

	public void setNumPeople(int numPeople) {
		this.numPeople = numPeople;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", hotelId=" + hotelId + ", roomId=" + roomId + ", customerId=" + customerId
				+ ", startDate=" + startDate  + ", numNights=" + numNights + ", totalPrice="
				+ totalPrice + ", numPeople=" + numPeople + "]";
	}
}
