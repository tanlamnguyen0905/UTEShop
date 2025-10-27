package ute.dao.inter;

import java.util.List;
import java.util.Map;

import ute.entities.Message;

public interface MessageDao {
    Message save(Message m);
    List<Message> findRecentByRoom(String roomId, int limit);
    List<Map<String, Object>> findAllRoomsWithLastMessage();
}
