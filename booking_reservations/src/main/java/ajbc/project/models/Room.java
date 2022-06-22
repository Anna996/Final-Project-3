package ajbc.project.models;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Room {
	private ObjectId id;
	private int number;
	@BsonProperty(value = "has_bath")
	private boolean hasBath;

	public Room(int number, boolean hasBath) {
		this.id = ObjectId.get();
		this.number = number;
		this.hasBath = hasBath;
	}

	public Room() {
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public boolean isHasBath() {
		return hasBath;
	}

	public void setHasBath(boolean hasBath) {
		this.hasBath = hasBath;
	}

	@Override
	public String toString() {
		return "Room [id=" + id + ", number=" + number + ", hasBath=" + hasBath + "]";
	}
}