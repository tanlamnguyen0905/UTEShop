-- Script để thêm cột phone vào bảng Addresses nếu chưa tồn tại

-- Kiểm tra xem cột phone đã tồn tại chưa
IF NOT EXISTS (
    SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_NAME = 'Addresses' AND COLUMN_NAME = 'phone'
)
BEGIN
    -- Thêm cột phone vào bảng Addresses
    ALTER TABLE Addresses
    ADD phone NVARCHAR(20) NULL;
    
    PRINT 'Đã thêm cột phone vào bảng Addresses';
END
ELSE
BEGIN
    PRINT 'Cột phone đã tồn tại trong bảng Addresses';
END

-- Kiểm tra kết quả
SELECT TOP 5 addressID, name, phone, province, district, ward, addressDetail, isDefault 
FROM Addresses;

