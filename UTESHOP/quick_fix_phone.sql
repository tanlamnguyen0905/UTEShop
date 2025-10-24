-- Quick fix: Copy phone từ Users vào Addresses cho các địa chỉ không có phone

-- Bước 1: Xem trước sẽ update những gì
SELECT 
    a.addressID,
    a.name as address_name,
    a.phone as old_phone,
    u.phone as user_phone,
    u.username,
    CASE 
        WHEN a.phone IS NULL OR a.phone = '' THEN 'WILL UPDATE'
        ELSE 'SKIP (already has phone)'
    END as action
FROM Addresses a
INNER JOIN Users u ON a.userID = u.userID
WHERE (a.phone IS NULL OR a.phone = '');

-- Bước 2: Thực hiện update (uncomment để chạy)
/*
UPDATE a
SET a.phone = u.phone
FROM Addresses a
INNER JOIN Users u ON a.userID = u.userID
WHERE (a.phone IS NULL OR a.phone = '')
  AND u.phone IS NOT NULL
  AND u.phone != '';

-- Hiển thị kết quả
SELECT 
    addressID,
    name,
    phone,
    province
FROM Addresses
ORDER BY addressID DESC;
*/

-- Hoặc set phone mặc định cho test (uncomment để chạy)
/*
UPDATE Addresses
SET phone = '0909123456'
WHERE addressID = 1 AND (phone IS NULL OR phone = '');
*/


