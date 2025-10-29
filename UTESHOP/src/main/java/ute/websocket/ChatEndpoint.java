package ute.websocket;

import com.google.gson.Gson;
import jakarta.websocket.*;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.ServerEndpointConfig;
import ute.configs.GsonConfig;
import ute.dao.impl.MessageDaoImpl;
import ute.dao.inter.MessageDao;
import ute.entities.Message;
import ute.entities.Users;


import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/ws/chat/{room}", configurator = ChatEndpoint.EndpointConfigurator.class)
public class ChatEndpoint {
	private static final Map<String, Set<Session>> ROOMS = new ConcurrentHashMap<>();
	private static final Map<Session, UserCtx> CONTEXT = new ConcurrentHashMap<>();
	private static final Gson GSON = GsonConfig.getGson();
	private static final MessageDao messageDao = new MessageDaoImpl();

	public static class EndpointConfigurator extends ServerEndpointConfig.Configurator {
		@Override
		public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
			// Lấy HttpSession từ request
			jakarta.servlet.http.HttpSession httpSession = (jakarta.servlet.http.HttpSession) request.getHttpSession();
			sec.getUserProperties().put("httpSession", httpSession);
		}
	}

	private static class UserCtx {
		String room;
		Long userId;
		String username;
		String role;
	}

	@OnOpen
	public void onOpen(Session session, @PathParam("room") String room) throws IOException {
		try {
			// Lấy HttpSession từ WebSocket session
			jakarta.servlet.http.HttpSession httpSession = 
				(jakarta.servlet.http.HttpSession) session.getUserProperties().get("httpSession");
			
			if (httpSession == null) {
				session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "No HTTP Session"));
				return;
			}
			
			// Lấy currentUser từ HttpSession
			Users currentUser = (Users) httpSession.getAttribute("currentUser");
			if (currentUser == null) {
				session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "User not logged in"));
				return;
			}
			
			// Tạo user context
			UserCtx ctx = new UserCtx();
			ctx.room = room;
			ctx.userId = currentUser.getUserID();
			ctx.username = currentUser.getUsername();
			ctx.role = currentUser.getRole();
			CONTEXT.put(session, ctx);

			ROOMS.computeIfAbsent(room, k -> ConcurrentHashMap.newKeySet()).add(session);

			// Gửi lịch sử gần nhất
			List<Message> history = messageDao.findRecentByRoom(room, 50);
			Map<String, Object> payload = new HashMap<>();
			payload.put("type", "HISTORY");
			payload.put("room", room);
			payload.put("messages", history);
			session.getBasicRemote().sendText(GSON.toJson(payload));

			// Thông báo JOIN
			broadcast(room, Map.of("type", "JOIN", "room", room, "senderId", ctx.userId, "senderName", ctx.username,
					"role", ctx.role, "timestamp", OffsetDateTime.now().toString()));

		} catch (Exception e) {
			e.printStackTrace();
			session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Error: " + e.getMessage()));
		}
	}

	@OnMessage
	public void onMessage(Session session, String text) {
		try {
			UserCtx ctx = CONTEXT.get(session);
			if (ctx == null) {
				System.out.println("No context for session");
				return;
			}
			
			@SuppressWarnings("unchecked")
			Map<String, Object> msg = GSON.fromJson(text, Map.class);
			String content = Objects.toString(msg.get("content"), "").trim();
			if (content.isEmpty()) {
				return;
			}

			System.out.println("Received message from " + ctx.username + ": " + content);

			Message m = Message.builder()
					.roomId(ctx.room)
					.senderId(ctx.userId)
					.senderName(ctx.username)
					.role(ctx.role)
					.content(content)
					.seen(false)
					.build();
			messageDao.save(m);

			broadcast(ctx.room, Map.of(
				"type", "CHAT", 
				"room", ctx.room, 
				"senderId", ctx.userId, 
				"senderName", ctx.username,
				"role", ctx.role, 
				"content", content, 
				"timestamp", OffsetDateTime.now().toString()
			));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
		UserCtx ctx = CONTEXT.remove(session);
		if (ctx != null) {
			var set = ROOMS.getOrDefault(ctx.room, Collections.emptySet());
			set.remove(session);
			broadcast(ctx.room, Map.of("type", "LEAVE", "room", ctx.room, "senderId", ctx.userId, "senderName",
					ctx.username, "role", ctx.role, "timestamp", OffsetDateTime.now().toString()));
		}
	}

	@OnError
	public void onError(Session session, Throwable thr) {
		// log error
		thr.printStackTrace();
	}

	private void broadcast(String room, Map<String, Object> payload) {
		String json = GSON.toJson(payload);
		Set<Session> sessions = ROOMS.getOrDefault(room, Collections.emptySet());
		for (Session s : sessions) {
			try {
				s.getBasicRemote().sendText(json);
			} catch (IOException ignored) {
			}
		}
	}
}
