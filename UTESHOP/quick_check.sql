-- QUICK CHECK: Kiểm tra nhanh tất cả mọi thứ

PRINT '=== QUICK CHECK: Address Phone Issue ===';
PRINT '';

-- Check 1: Cột phone có tồn tại không?
PRINT '1. Kiểm tra cột phone:';
IF EXISTS (
    SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_NAME = 'Addresses' AND COLUMN_NAME = 'phone'
)
BEGIN
    PRINT '   ✅ Cột phone TỒN TẠI';
    
    SELECT 
        '   → ' + DATA_TYPE + 
        CASE WHEN CHARACTER_MAXIMUM_LENGTH IS NOT NULL 
             THEN '(' + CAST(CHARACTER_MAXIMUM_LENGTH AS VARCHAR) + ')' 
             ELSE '' END +
        ', nullable: ' + IS_NULLABLE as info
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_NAME = 'Addresses' AND COLUMN_NAME = 'phone';
END
ELSE
BEGIN
    PRINT '   ❌ Cột phone KHÔNG TỒN TẠI!';
    PRINT '   → Fix: ALTER TABLE Addresses ADD phone NVARCHAR(20) NULL;';
END

PRINT '';

-- Check 2: Có bao nhiêu địa chỉ?
PRINT '2. Thống kê địa chỉ:';
SELECT 
    COUNT(*) as total_addresses,
    SUM(CASE WHEN phone IS NULL OR phone = '' THEN 1 ELSE 0 END) as without_phone,
    SUM(CASE WHEN phone IS NOT NULL AND phone != '' THEN 1 ELSE 0 END) as with_phone
FROM Addresses;

PRINT '';

-- Check 3: Xem 5 địa chỉ mới nhất
PRINT '3. Top 5 địa chỉ gần nhất:';
SELECT TOP 5
    addressID,
    name,
    CASE 
        WHEN phone IS NULL THEN '❌ NULL'
        WHEN phone = '' THEN '⚠️  EMPTY'
        ELSE '✅ ' + phone
    END as phone_status,
    phone as phone_value,
    province
FROM Addresses
ORDER BY addressID DESC;

PRINT '';

-- Check 4: Test INSERT
PRINT '4. Test INSERT (KHÔNG thực thi, chỉ preview):';
PRINT '   Run this to test:';
PRINT '   ';
PRINT '   DECLARE @testUserID BIGINT = (SELECT TOP 1 userID FROM Users);';
PRINT '   INSERT INTO Addresses (name, phone, province, district, ward, addressDetail, isDefault, userID)';
PRINT '   VALUES (N''Test Direct'', ''0999888777'', N''Test Province'', N''Test District'', N''Test Ward'', N''123 Test'', 0, @testUserID);';
PRINT '   ';
PRINT '   SELECT TOP 1 * FROM Addresses ORDER BY addressID DESC;';

PRINT '';

-- Check 5: Constraints
PRINT '5. Kiểm tra constraints trên cột phone:';
IF EXISTS (
    SELECT 1 
    FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS con
    INNER JOIN INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE col 
        ON con.CONSTRAINT_NAME = col.CONSTRAINT_NAME
    WHERE con.TABLE_NAME = 'Addresses' AND col.COLUMN_NAME = 'phone'
)
BEGIN
    SELECT 
        '   → ' + con.CONSTRAINT_NAME + ' (' + con.CONSTRAINT_TYPE + ')' as constraint_info
    FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS con
    INNER JOIN INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE col 
        ON con.CONSTRAINT_NAME = col.CONSTRAINT_NAME
    WHERE con.TABLE_NAME = 'Addresses' AND col.COLUMN_NAME = 'phone';
END
ELSE
BEGIN
    PRINT '   ✅ Không có constraint nào trên cột phone';
END

PRINT '';
PRINT '=== END OF QUICK CHECK ===';


