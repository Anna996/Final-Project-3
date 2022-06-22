package ajbc.project.models;

import java.util.ArrayList;
import java.util.List;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Customer {

	private ObjectId id;
	@BsonProperty(value = "customer_id")
	private long customerId;
	@BsonProperty(value = "first_name")
	private String firstName;
	@BsonProperty(value = "last_name")
	private String lastName;
	private String country;
	private List<ObjectId> orders;

	public Customer(long customerId, String firstName, String lastName, String country) {
		this.id = ObjectId.get();
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.country = country;
		this.orders = new ArrayList<ObjectId>();
	}

	public Customer() {
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<ObjectId> getOrders() {
		return orders;
	}

	public void setOrders(List<ObjectId> orders) {
		this.orders = orders;
	}
	
	public void addOrder(ObjectId orderId) {
		this.orders.add(orderId);
	}
	
	public void removeOrder(ObjectId orderId) {
		this.orders.remove(orderId);
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", customerId=" + customerId + ", firstName=" + firstName + ", lastName="
				+ lastName + ", country=" + country + ", orders=" + orders + "]";
	}
}
