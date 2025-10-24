-- Script để kiểm tra và cập nhật số điện thoại cho địa chỉ hiện có

-- Bước 1: Kiểm tra xem cột phone đã tồn tại chưa
SELECT 
    COLUMN_NAME, 
    DATA_TYPE, 
    CHARACTER_MAXIMUM_LENGTH,
    IS_NULLABLE
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'Addresses' 
ORDER BY ORDINAL_POSITION;

-- Bước 2: Kiểm tra dữ liệu hiện tại
SELECT 
    addressID, 
    name, 
    phone,
    CASE 
        WHEN phone IS NULL THEN 'NULL'
        WHEN phone = '' THEN 'EMPTY'
        ELSE 'HAS_VALUE'
    END as phone_status,
    province, 
    district
FROM Addresses
ORDER BY addressID DESC;

-- Bước 3: Đếm số địa chỉ không có phone
SELECT 
    COUNT(*) as total_addresses,
    SUM(CASE WHEN phone IS NULL OR phone = '' THEN 1 ELSE 0 END) as missing_phone,
    SUM(CASE WHEN phone IS NOT NULL AND phone != '' THEN 1 ELSE 0 END) as has_phone
FROM Addresses;

-- Bước 4 (TÙY CHỌN): Cập nhật số điện thoại từ bảng Users cho các địa chỉ chưa có
-- CHỈ chạy nếu bạn muốn lấy phone từ user
/*
UPDATE a
SET a.phone = u.phone
FROM Addresses a
INNER JOIN Users u ON a.userID = u.userID
WHERE (a.phone IS NULL OR a.phone = '')
  AND u.phone IS NOT NULL
  AND u.phone != '';
*/

-- Bước 5 (TÙY CHỌN): Hoặc set phone mặc định cho test
-- CHỈ chạy nếu bạn muốn test với số điện thoại giả
/*
UPDATE Addresses
SET phone = '0123456789'
WHERE phone IS NULL OR phone = '';
*/

-- Kiểm tra lại sau khi update
SELECT 
    addressID, 
    name, 
    phone,
    province
FROM Addresses
ORDER BY addressID DESC;


