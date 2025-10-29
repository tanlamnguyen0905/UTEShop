<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} - UTESHOP</title>
    <style>
        .chat-container {
            max-width: 900px;
            margin: 50px auto;
            background: white;
            border-radius: 20px;
            box-shadow: 0 5px 30px rgba(0,0,0,0.1);
            overflow: hidden;
            height: 600px;
            display: flex;
            flex-direction: column;
        }
        
        .chat-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 20px;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
        
        .chat-header h4 {
            margin: 0;
            font-weight: 600;
        }
        
        .status-badge {
            background: rgba(255,255,255,0.2);
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 14px;
        }
        
        .status-badge.online {
            background: #10b981;
        }
        
        .chat-messages {
            flex: 1;
            overflow-y: auto;
            padding: 20px;
            background: #f8fafc;
        }
        
        .message {
            margin-bottom: 15px;
            display: flex;
            align-items: flex-start;
            animation: fadeIn 0.3s ease-in;
        }
        
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }
        
        .message.sent {
            flex-direction: row-reverse;
        }
        
        .message-content {
            max-width: 70%;
            padding: 12px 18px;
            border-radius: 18px;
            position: relative;
        }
        
        .message.received .message-content {
            background: white;
            color: #1f2937;
            border-bottom-left-radius: 4px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.05);
        }
        
        .message.sent .message-content {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-bottom-right-radius: 4px;
        }
        
        .message-meta {
            font-size: 11px;
            margin-top: 5px;
            opacity: 0.7;
        }
        
        .message.sent .message-meta {
            text-align: right;
        }
        
        .sender-name {
            font-weight: 600;
            margin-bottom: 3px;
            font-size: 13px;
        }
        
        .chat-input {
            padding: 20px;
            background: white;
            border-top: 1px solid #e5e7eb;
        }
        
        .input-group {
            display: flex;
            gap: 10px;
        }
        
        .chat-input textarea {
            flex: 1;
            border: 2px solid #e5e7eb;
            border-radius: 25px;
            padding: 12px 20px;
            resize: none;
            font-size: 15px;
            outline: none;
            transition: all 0.3s;
        }
        
        .chat-input textarea:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        
        .attach-btn {
            background: #f3f4f6;
            color: #6b7280;
            border: none;
            border-radius: 50%;
            width: 50px;
            height: 50px;
            cursor: pointer;
            transition: all 0.2s;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 18px;
        }
        
        .attach-btn:hover {
            background: #e5e7eb;
            color: #374151;
        }
        
        .send-btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 50%;
            width: 50px;
            height: 50px;
            cursor: pointer;
            transition: transform 0.2s;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .send-btn:hover {
            transform: scale(1.05);
        }
        
        .send-btn:active {
            transform: scale(0.95);
        }
        
        .message-image {
            max-width: 250px;
            max-height: 250px;
            border-radius: 12px;
            margin-top: 8px;
            cursor: pointer;
            transition: transform 0.3s;
            display: block;
        }
        
        .message-image:hover {
            transform: scale(1.02);
        }
        
        .system-message {
            text-align: center;
            color: #6b7280;
            font-size: 13px;
            margin: 10px 0;
            font-style: italic;
        }
        
        .typing-indicator {
            display: none;
            padding: 10px;
            font-style: italic;
            color: #6b7280;
            font-size: 13px;
        }
        
        .typing-indicator.show {
            display: block;
        }
        
        .role-badge {
            display: inline-block;
            padding: 2px 8px;
            border-radius: 10px;
            font-size: 11px;
            font-weight: 600;
            margin-left: 5px;
        }
        
        .role-badge.admin {
            background: #fef3c7;
            color: #92400e;
        }
        
        .role-badge.manager {
            background: #fce7f3;
            color: #831843;
        }
        
        .role-badge.shipper {
            background: #dbeafe;
            color: #1e40af;
        }
        
        .role-badge.user {
            background: #e5e7eb;
            color: #374151;
        }
    </style>
</head>
<body class="bg-light">
    <div class="container">
        <div class="chat-container">
            <div class="chat-header">
                <div>
                    <h4><i class="fas fa-headset"></i> ${pageTitle}</h4>
                    <small>Chúng tôi sẽ phản hồi trong thời gian sớm nhất</small>
                </div>
                <div class="status-badge" id="statusBadge">
                    <i class="fas fa-circle"></i> Đang kết nối...
                </div>
            </div>
            
            <div class="chat-messages" id="chatMessages">
                <div class="system-message">
                    <i class="fas fa-info-circle"></i> Bắt đầu cuộc trò chuyện
                </div>
            </div>
            
            <div class="typing-indicator" id="typingIndicator">
                <i class="fas fa-ellipsis-h"></i> Đang nhập...
            </div>
            
            <div class="chat-input">
                <!-- Image Preview -->
                <div id="imagePreview" style="display: none; padding: 10px; border-bottom: 1px solid #e5e7eb;">
                    <div style="position: relative; display: inline-block;">
                        <img id="previewImg" src="" alt="Preview" style="max-width: 120px; max-height: 120px; border-radius: 8px; border: 2px solid #e5e7eb;">
                        <button type="button" id="removeImageBtn" style="position: absolute; top: -8px; right: -8px; background: #ef4444; color: white; border: none; border-radius: 50%; width: 24px; height: 24px; cursor: pointer; display: flex; align-items: center; justify-content: center; font-size: 14px;">
                            <i class="fas fa-times"></i>
                        </button>
                    </div>
                </div>
                
                <div class="input-group">
                    <input type="file" id="imageInput" accept="image/*" style="display: none;">
                    <button class="attach-btn" id="attachBtn" title="Đính kèm ảnh">
                        <i class="fas fa-paperclip"></i>
                    </button>
                    <textarea 
                        id="messageInput" 
                        placeholder="Nhập tin nhắn của bạn..." 
                        rows="1"></textarea>
                    <button class="send-btn" id="sendBtn">
                        <i class="fas fa-paper-plane"></i>
                    </button>
                </div>
            </div>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            console.log('Chat page loaded');
            console.log('Room ID from server: ${roomId}');
            console.log('User ID from server: ${sessionScope.currentUser.userID}');
            
            const roomId = '${roomId}';
            let ws = null;
            const currentUserId = ${empty sessionScope.currentUser.userID ? 0 : sessionScope.currentUser.userID};
            let reconnectAttempts = 0;
            const maxReconnectAttempts = 5;

            if (!roomId || roomId === '') {
                alert('Lỗi: Room ID không hợp lệ!');
                console.error('Invalid roomId:', roomId);
                return;
            }

            if (!currentUserId || currentUserId === 0) {
                alert('Lỗi: User ID không hợp lệ!');
                console.error('Invalid currentUserId:', currentUserId);
                return;
            }

            console.log('Initializing chat with roomId:', roomId, 'userId:', currentUserId);

            function connect() {
                const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
                const wsUrl = protocol + '//' + window.location.host + '${pageContext.request.contextPath}/ws/chat/' + roomId;
                
                console.log('Connecting to WebSocket:', wsUrl);
            
                try {
                    ws = new WebSocket(wsUrl);
                    
                    ws.onopen = function() {
                        console.log('✅ WebSocket connected successfully');
                        document.getElementById('statusBadge').innerHTML = '<i class="fas fa-circle"></i> Trực tuyến';
                        document.getElementById('statusBadge').classList.add('online');
                        reconnectAttempts = 0;
                    };
                } catch (error) {
                    console.error('❌ Error creating WebSocket:', error);
                    alert('Không thể kết nối WebSocket: ' + error.message);
                    return;
                }
            
                ws.onmessage = function(event) {
                    console.log('📩 Received message:', event.data);
                    const data = JSON.parse(event.data);
                    console.log('📩 Parsed data:', data);
                
                switch(data.type) {
                    case 'HISTORY':
                        displayHistory(data.messages);
                        break;
                    case 'CHAT':
                        displayMessage(data);
                        break;
                    case 'JOIN':
                        displaySystemMessage(data.senderName + ' đã tham gia');
                        break;
                    case 'LEAVE':
                        displaySystemMessage(data.senderName + ' đã rời khỏi');
                        break;
                    }
                };
                
                ws.onerror = function(error) {
                    console.error('❌ WebSocket error:', error);
                };
                
                ws.onclose = function(event) {
                    console.log('🔌 WebSocket disconnected. Code:', event.code, 'Reason:', event.reason);
                    document.getElementById('statusBadge').innerHTML = '<i class="fas fa-circle"></i> Đã ngắt kết nối';
                    document.getElementById('statusBadge').classList.remove('online');
                    
                    // Attempt to reconnect
                    if (reconnectAttempts < maxReconnectAttempts) {
                        reconnectAttempts++;
                        console.log('🔄 Reconnecting... Attempt', reconnectAttempts);
                        setTimeout(connect, 3000 * reconnectAttempts);
                    } else {
                        console.error('❌ Max reconnect attempts reached');
                    }
                };
            }

        function displayHistory(messages) {
            if (!messages || messages.length === 0) return;
            
            // Reverse to show oldest first
            messages.reverse().forEach(msg => {
                displayMessage({
                    senderId: msg.senderId,
                    senderName: msg.senderName,
                    role: msg.role,
                    content: msg.content,
                    imageUrl: msg.imageUrl,
                    timestamp: msg.createdAt
                }, false);
            });
            
            scrollToBottom();
        }

        function displayMessage(data, scroll = true) {
            const messagesDiv = document.getElementById('chatMessages');
            const isSent = data.senderId == currentUserId;
            
            const messageDiv = document.createElement('div');
            messageDiv.className = 'message ' + (isSent ? 'sent' : 'received');
            
            const roleClass = data.role ? data.role.toLowerCase() : 'user';
            const roleBadge = data.role ? '<span class="role-badge ' + roleClass + '">' + data.role + '</span>' : '';
            
            // Build content with text and/or image
            let contentHtml = '';
            if (data.content) {
                contentHtml += '<div>' + escapeHtml(data.content) + '</div>';
            }
            if (data.imageUrl) {
                const imgSrc = '${pageContext.request.contextPath}/image?fname=' + encodeURIComponent(data.imageUrl);
                contentHtml += '<img src="' + imgSrc + '" class="message-image" alt="Image" onclick="window.open(\'' + imgSrc + '\', \'_blank\')">';
            }
            
            messageDiv.innerHTML = 
                '<div class="message-content">' +
                    (!isSent ? '<div class="sender-name">' + escapeHtml(data.senderName) + roleBadge + '</div>' : '') +
                    contentHtml +
                    '<div class="message-meta">' + formatTime(data.timestamp) + '</div>' +
                '</div>';
            
            messagesDiv.appendChild(messageDiv);
            
            if (scroll) {
                scrollToBottom();
            }
        }

        function displaySystemMessage(text) {
            const messagesDiv = document.getElementById('chatMessages');
            const msgDiv = document.createElement('div');
            msgDiv.className = 'system-message';
            msgDiv.innerHTML = '<i class="fas fa-info-circle"></i> ' + text;
            messagesDiv.appendChild(msgDiv);
            scrollToBottom();
        }

        let uploadedImageUrl = null;
        
        function sendMessage() {
            const input = document.getElementById('messageInput');
            const content = input.value.trim();
            
            console.log('Send message clicked, content:', content, 'imageUrl:', uploadedImageUrl);
            console.log('WebSocket state:', ws ? ws.readyState : 'null');
            
            // Phải có content hoặc imageUrl
            if (!content && !uploadedImageUrl) {
                alert('Vui lòng nhập tin nhắn hoặc chọn ảnh!');
                return;
            }
            
            if (!ws || ws.readyState !== WebSocket.OPEN) {
                alert('Kết nối WebSocket chưa sẵn sàng. Vui lòng đợi hoặc refresh trang!');
                console.error('WebSocket not ready. State:', ws ? ws.readyState : 'null');
                return;
            }
            
            try {
                const message = {
                    content: content || '',
                    imageUrl: uploadedImageUrl || ''
                };
                ws.send(JSON.stringify(message));
                input.value = '';
                input.style.height = 'auto';
                
                // Clear image preview
                uploadedImageUrl = null;
                document.getElementById('imagePreview').style.display = 'none';
                document.getElementById('imageInput').value = '';
                
                console.log('Message sent successfully');
            } catch (error) {
                console.error('Error sending message:', error);
                alert('Lỗi khi gửi tin nhắn: ' + error.message);
            }
        }

        function handleKeyPress(event) {
            if (event.key === 'Enter' && !event.shiftKey) {
                event.preventDefault();
                sendMessage();
            }
        }

        function scrollToBottom() {
            const messagesDiv = document.getElementById('chatMessages');
            messagesDiv.scrollTop = messagesDiv.scrollHeight;
        }

        function formatTime(timestamp) {
            if (!timestamp) return '';
            const date = new Date(timestamp);
            return date.toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' });
        }

        function escapeHtml(text) {
            const div = document.createElement('div');
            div.textContent = text;
            return div.innerHTML;
        }

        // Get DOM elements
        const messageInput = document.getElementById('messageInput');
        const sendBtn = document.getElementById('sendBtn');
        const attachBtn = document.getElementById('attachBtn');
        const imageInput = document.getElementById('imageInput');
        const imagePreview = document.getElementById('imagePreview');
        const previewImg = document.getElementById('previewImg');
        const removeImageBtn = document.getElementById('removeImageBtn');

        // Auto-resize textarea
        messageInput.addEventListener('input', function() {
            this.style.height = 'auto';
            this.style.height = Math.min(this.scrollHeight, 150) + 'px';
        });

        // Attach button
        attachBtn.addEventListener('click', function() {
            imageInput.click();
        });

        // File input change
        imageInput.addEventListener('change', async function(e) {
            const file = e.target.files[0];
            if (!file) return;

            // Validate file type
            if (!file.type.startsWith('image/')) {
                alert('Vui lòng chọn file ảnh!');
                return;
            }

            // Validate file size (max 10MB)
            if (file.size > 10 * 1024 * 1024) {
                alert('Kích thước ảnh tối đa 10MB!');
                return;
            }

            // Preview image
            const reader = new FileReader();
            reader.onload = function(e) {
                previewImg.src = e.target.result;
                imagePreview.style.display = 'block';
            };
            reader.readAsDataURL(file);

            // Upload image
            const formData = new FormData();
            formData.append('image', file);

            try {
                const response = await fetch('${pageContext.request.contextPath}/chat/upload-image', {
                    method: 'POST',
                    body: formData
                });

                const result = await response.json();
                if (result.success) {
                    uploadedImageUrl = result.imageUrl;
                    console.log('Image uploaded:', uploadedImageUrl);
                } else {
                    alert(result.message || 'Lỗi khi upload ảnh!');
                    imagePreview.style.display = 'none';
                    imageInput.value = '';
                }
            } catch (error) {
                console.error('Upload error:', error);
                alert('Lỗi khi upload ảnh!');
                imagePreview.style.display = 'none';
                imageInput.value = '';
            }
        });

        // Remove image button
        removeImageBtn.addEventListener('click', function() {
            uploadedImageUrl = null;
            imagePreview.style.display = 'none';
            imageInput.value = '';
        });

        // Add event listeners
        sendBtn.addEventListener('click', function(e) {
            e.preventDefault();
            sendMessage();
        });

        messageInput.addEventListener('keydown', function(e) {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                sendMessage();
            }
        });

        // Connect WebSocket
        connect();
        
        }); // End DOMContentLoaded
    </script>
</body>
</html>

