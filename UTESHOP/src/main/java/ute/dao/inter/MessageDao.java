package ute.dao.inter;

import java.util.List;

import ute.entities.Message;

public interface MessageDao {
    Message save(Message m);
    List<Message> findRecentByRoom(String roomId, int limit);
}
