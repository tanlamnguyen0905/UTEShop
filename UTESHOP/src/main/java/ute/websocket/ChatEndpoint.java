//package ute.websocket;
//
//import com.google.gson.Gson;
//import jakarta.websocket.*;
//import jakarta.websocket.server.HandshakeRequest;
//import jakarta.websocket.server.PathParam;
//import jakarta.websocket.server.ServerEndpoint;
//import jakarta.websocket.server.ServerEndpointConfig;
//import ute.dao.impl.MessageDaoImpl;
//import ute.dao.inter.MessageDao;
//import ute.entities.Message;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jws;
//import ute.utils.JwtUtil;
//
//
//import java.io.IOException;
//import java.time.OffsetDateTime;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//
//import org.hibernate.Session;
//
//@ServerEndpoint(value = "/ws/chat/{room}", configurator = ChatEndpoint.EndpointConfigurator.class)
//public class ChatEndpoint {
//	private static final Map<String, Set<Session>> ROOMS = new ConcurrentHashMap<>();
//	private static final Map<Session, UserCtx> CONTEXT = new ConcurrentHashMap<>();
//	private static final Gson GSON = new Gson();
//	private static final MessageDao messageDao = new MessageDaoImpl();
//
//	public static class EndpointConfigurator extends ServerEndpointConfig.Configurator {
//		@Override
//		public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
//			// Lấy JWT: từ cookie 'token' hoặc query ?token=
//			String token = null;
//			if (request.getHeaders().get("cookie") != null) {
//				for (String ck : request.getHeaders().get("cookie")) {
//					for (String p : ck.split(";")) {
//						String[] kv = p.trim().split("=", 2);
//						if (kv.length == 2 && kv[0].equals("token"))
//							token = kv[1];
//					}
//				}
//			}
//			if (token == null && request.getParameterMap().containsKey("token")) {
//				token = request.getParameterMap().get("token").get(0);
//			}
//			sec.getUserProperties().put("jwt", token);
//		}
//	}
//
//	private static class UserCtx {
//		String room;
//		Long userId;
//		String username;
//		String role;
//	}
//
//	@OnOpen
//	public void onOpen(Session session, @PathParam("room") String room) throws IOException {
//		String token = (String) session.getUserProperties().get("jwt");
//		if (token == null) {
//			session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "Missing JWT"));
//			return;
//		}
//		try {
//			Jws<Claims> jws = JwtUtil.parse(token);
//			Claims c = jws.getBody();
//			// TODO: kiểm tra quyền truy cập room theo business (order owner, admin,
//			// shipper...)
//			UserCtx ctx = new UserCtx();
//			ctx.room = room;
//			ctx.userId = JwtUtil.getUserId(c);
//			ctx.username = JwtUtil.getUsername(c);
//			ctx.role = JwtUtil.getRole(c);
//			CONTEXT.put(session, ctx);
//
//			ROOMS.computeIfAbsent(room, k -> ConcurrentHashMap.newKeySet()).add(session);
//
//			// Gửi lịch sử gần nhất
//			var history = messageDao.findRecentByRoom(room, 50);
//			Map<String, Object> payload = new HashMap<>();
//			payload.put("type", "HISTORY");
//			payload.put("room", room);
//			payload.put("messages", history);
//			session.getBasicRemote().sendText(GSON.toJson(payload));
//
//			// Thông báo JOIN
//			broadcast(room, Map.of("type", "JOIN", "room", room, "senderId", ctx.userId, "senderName", ctx.username,
//					"role", ctx.role, "timestamp", OffsetDateTime.now().toString()));
//
//		} catch (Exception e) {
//			session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Invalid JWT"));
//		}
//	}
//
//	@OnMessage
//	public void onMessage(Session session, String text) {
//		UserCtx ctx = CONTEXT.get(session);
//		if (ctx == null)
//			return;
//		Map<String, Object> msg = GSON.fromJson(text, Map.class);
//		String content = Objects.toString(msg.get("content"), "").trim();
//		if (content.isEmpty())
//			return;
//
//		Message m = Message.builder().roomId(ctx.room).senderId(ctx.userId).senderName(ctx.username).role(ctx.role)
//				.content(content).seen(false).build();
//		messageDao.save(m);
//
//		broadcast(ctx.room, Map.of("type", "CHAT", "room", ctx.room, "senderId", ctx.userId, "senderName", ctx.username,
//				"role", ctx.role, "content", content, "timestamp", OffsetDateTime.now().toString()));
//	}
//
//	@OnClose
//	public void onClose(Session session, CloseReason reason) {
//		UserCtx ctx = CONTEXT.remove(session);
//		if (ctx != null) {
//			var set = ROOMS.getOrDefault(ctx.room, Collections.emptySet());
//			set.remove(session);
//			broadcast(ctx.room, Map.of("type", "LEAVE", "room", ctx.room, "senderId", ctx.userId, "senderName",
//					ctx.username, "role", ctx.role, "timestamp", OffsetDateTime.now().toString()));
//		}
//	}
//
//	@OnError
//	public void onError(Session session, Throwable thr) {
//		// log
//	}
//
//	private void broadcast(String room, Map<String, Object> payload) {
//		String json = GSON.toJson(payload);
//		Set<Session> sessions = ROOMS.getOrDefault(room, Collections.emptySet());
//		for (Session s : sessions) {
//			try {
//				s.getBasicRemote().sendText(json);
//			} catch (IOException ignored) {
//			}
//		}
//	}
//}
