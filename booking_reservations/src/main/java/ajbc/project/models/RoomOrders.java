package ajbc.project.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class RoomOrders {
	private ObjectId id;
	@BsonProperty(value = "room_id")
	private ObjectId roomId;
	private List<LocalDate> dates;

	public RoomOrders(ObjectId roomId) {
		this.id = ObjectId.get();
		this.roomId = roomId;
		this.dates = new ArrayList<LocalDate>();
	}

	public RoomOrders() {
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public ObjectId getRoomId() {
		return roomId;
	}

	public void setRoomId(ObjectId roomId) {
		this.roomId = roomId;
	}

	public List<LocalDate> getDates() {
		return dates;
	}

	public void setDates(List<LocalDate> dates) {
		this.dates = dates;
	}

	public void addDates(LocalDate startDate, LocalDate endDate) {

		if (startDate.isAfter(endDate)) {
			return;
		}

		LocalDate date = startDate;

		while (!date.isEqual(endDate)) {
			dates.add(date);
			date = date.plusDays(1);
		}
	}
	
	public void removeDates(LocalDate startDate, LocalDate endDate) {

		if (startDate.isAfter(endDate)) {
			return;
		}

		LocalDate date = startDate;

		while (!date.isEqual(endDate)) {
			dates.remove(date);
			date = date.plusDays(1);
		}
	}

	@Override
	public String toString() {
		return "RoomOrders [id=" + id + ", roomId=" + roomId + ", dates=" + dates + "]";
	}
}
