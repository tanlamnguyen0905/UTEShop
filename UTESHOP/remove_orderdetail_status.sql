-- XÓA CỘT STATUS KHÔNG CẦN THIẾT TỪ BẢNG OrderDetail
-- OrderDetail không cần status riêng vì nó phụ thuộc vào Orders.orderStatus

USE UTESHOP;
GO

-- Kiểm tra và xóa cột status
IF EXISTS (
    SELECT * 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_NAME = 'OrderDetail' 
    AND COLUMN_NAME = 'status'
)
BEGIN
    PRINT 'Đang xóa cột status từ bảng OrderDetail...';
    ALTER TABLE OrderDetail DROP COLUMN status;
    PRINT '✓ Đã xóa cột status thành công!';
END
ELSE
BEGIN
    PRINT '✓ Cột status không tồn tại (đã xóa trước đó)';
END
GO

-- Kiểm tra kết quả
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'OrderDetail'
ORDER BY ORDINAL_POSITION;
GO

