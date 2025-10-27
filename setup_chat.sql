-- Quick setup cho chat
USE UTEShop;
GO

-- Tạo bảng Message
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[Message]') AND type in (N'U'))
BEGIN
    CREATE TABLE [Message] (
        IdMessage BIGINT IDENTITY(1,1) PRIMARY KEY,
        RoomId NVARCHAR(100) NOT NULL,
        SenderId BIGINT NOT NULL,
        SenderName NVARCHAR(100) NOT NULL,
        Role NVARCHAR(20) NOT NULL,
        Content NVARCHAR(2000) NOT NULL,
        CreatedAt DATETIMEOFFSET DEFAULT SYSDATETIMEOFFSET(),
        Seen BIT DEFAULT 0
    );
    PRINT 'Created Message table';
END
ELSE
BEGIN
    PRINT 'Message table already exists';
END
GO

-- Index để tăng tốc query
CREATE NONCLUSTERED INDEX IX_Message_RoomId ON Message(RoomId, CreatedAt DESC);
GO

PRINT 'Setup completed!';

