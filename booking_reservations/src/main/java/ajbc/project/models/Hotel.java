package ajbc.project.models;

import java.util.ArrayList;
import java.util.List;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Hotel {

	private ObjectId id;
	private HotelName name;
	private Address address;
	private float rank;
	private List<ObjectId> rooms; 
	@BsonProperty(value = "price_per_night")
	private float pricePerNight;
	private List<ObjectId> orders; 
	@BsonProperty(value = "max_people_per_room")
	private int maxPeoplePerRoom;

	public Hotel(HotelName name, Address address, float rank, List<ObjectId> rooms, float pricePerNight, int maxPeoplePerRoom) {
		this.id = ObjectId.get();
		this.name = name;
		this.address = address;
		this.rank = rank;
		this.rooms = rooms;
		this.pricePerNight = pricePerNight;
		this.orders = new ArrayList<ObjectId>();
		this.maxPeoplePerRoom = maxPeoplePerRoom;
	}

	public Hotel() {
	}

	public int getMaxPeoplePerRoom() {
		return maxPeoplePerRoom;
	}

	public void setMaxPeoplePerRoom(int maxPeoplePerRoom) {
		this.maxPeoplePerRoom = maxPeoplePerRoom;
	}

	public void setRooms(List<ObjectId> rooms) {
		this.rooms = rooms;
	}

	public void setOrders(List<ObjectId> orders) {
		this.orders = orders;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public HotelName getName() {
		return name;
	}

	public void setName(HotelName name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public float getRank() {
		return rank;
	}

	public void setRank(float rank) {
		this.rank = rank;
	}

	public float getPricePerNight() {
		return pricePerNight;
	}

	public void setPricePerNight(float pricePerNight) {
		this.pricePerNight = pricePerNight;
	}
	
	public void addOrder(ObjectId orderId) {
		this.orders.add(orderId);
	}
	
	public void removeOrder(ObjectId orderId) {
		this.orders.remove(orderId);
	}
	
	public List<ObjectId> getRooms() {
		return rooms;
	}

	public List<ObjectId> getOrders() {
		return orders;
	}

	@Override
	public String toString() {
		return "Hotel [id=" + id + ", name=" + name + ", address=" + address + ", rank=" + rank + ", rooms=" + rooms
				+ ", pricePerNight=" + pricePerNight + ", orders=" + orders + ", maxPeoplePerRoom=" + maxPeoplePerRoom
				+ "]";
	}

}
