package org.brainacad.db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.brainacad.model.MenuItemInfo;
import org.brainacad.model.OrderInfo;
import org.brainacad.model.CustomerInfo;
import org.brainacad.model.StaffInfo;
import org.brainacad.model.StaffHireInfo;
import org.brainacad.model.DrinkOrderCustomerInfo;
import org.brainacad.model.CustomerOrderTotalInfo;
import org.brainacad.model.WorkScheduleInfo;

public class CoffeeShopRepository {
    private final Connection conn;

    public CoffeeShopRepository(Connection conn) {
        this.conn = conn;
    }

    public int insertMenuItem(String category, String nameEn, String nameUk, BigDecimal price) throws SQLException {
        String sql = "INSERT INTO menu_items (category, name_en, name_uk, price) VALUES (?::menu_category, ?, ?, ?) ON CONFLICT DO NOTHING";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            ps.setString(2, nameEn);
            ps.setString(3, nameUk);
            ps.setBigDecimal(4, price);
            return ps.executeUpdate();
        }
    }

    public int insertStaff(String fullName, String phone, String address, String roleCode) throws SQLException {
        String sql = "INSERT INTO staff (full_name, phone, address, role_id) " +
                "VALUES (?, ?, ?, (SELECT id FROM staff_roles WHERE code = ?)) " +
                "ON CONFLICT (phone) DO NOTHING";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, phone);
            ps.setString(3, address);
            ps.setString(4, roleCode);
            return ps.executeUpdate();
        }
    }

    public boolean roleExists(String roleCode) throws SQLException {
        String sql = "SELECT 1 FROM staff_roles WHERE code = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roleCode);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public int insertCustomer(String fullName, LocalDate birthDate, String phone, String address, BigDecimal discountPercent) throws SQLException {
        String sql = "INSERT INTO customers (full_name, birth_date, phone, address, discount_percent) " +
                "VALUES (?, ?, ?, ?, ?) ON CONFLICT (phone) DO NOTHING";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setObject(2, birthDate);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.setBigDecimal(5, discountPercent);
            return ps.executeUpdate();
        }
    }

    public long createOrder(long customerId, long staffId, String comments) throws SQLException {
        String sql = "INSERT INTO orders (customer_id, staff_id, status, discount_percent, comments) VALUES (?, ?, 'NEW', 0, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, customerId);
            ps.setLong(2, staffId);
            ps.setString(3, comments);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        throw new IllegalStateException("Failed to create order");
    }

    public void insertOrderItem(long orderId, long menuItemId, int quantity, BigDecimal unitPrice) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, menu_item_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            ps.setLong(2, menuItemId);
            ps.setInt(3, quantity);
            ps.setBigDecimal(4, unitPrice);
            ps.executeUpdate();
        }
    }

    public void addSchedulesForDate(LocalDate shiftDate, List<Long> staffIds) throws SQLException {
        String sql = "INSERT INTO work_schedule (staff_id, shift_date, shift_start, shift_end) VALUES (?, ?, '09:00', '17:00') ON CONFLICT DO NOTHING";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Long staffId : staffIds) {
                ps.setLong(1, staffId);
                ps.setObject(2, shiftDate);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public int updateMenuPrice(String nameEn, BigDecimal newPrice) throws SQLException {
        String sql = "UPDATE menu_items SET price = ? WHERE category = 'DRINK' AND name_en = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, newPrice);
            ps.setString(2, nameEn);
            return ps.executeUpdate();
        }
    }

    public int updateStaffAddress(String phone, String roleCode, String newAddress) throws SQLException {
        String sql = "UPDATE staff SET address = ? WHERE phone = ? AND role_id = (SELECT id FROM staff_roles WHERE code = ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newAddress);
            ps.setString(2, phone);
            ps.setString(3, roleCode);
            return ps.executeUpdate();
        }
    }

    public int updateStaffPhone(String currentPhone, String roleCode, String newPhone) throws SQLException {
        String sql = "UPDATE staff SET phone = ? " +
                "WHERE phone = ? " +
                "  AND role_id = (SELECT id FROM staff_roles WHERE code = ?) " +
                "  AND NOT EXISTS (SELECT 1 FROM staff WHERE phone = ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPhone);
            ps.setString(2, currentPhone);
            ps.setString(3, roleCode);
            ps.setString(4, newPhone);
            return ps.executeUpdate();
        }
    }

    public int updateCustomerDiscount(String customerPhone, BigDecimal newDiscountPercent) throws SQLException {
        String sql = "UPDATE customers SET discount_percent = ? WHERE phone = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, newDiscountPercent);
            ps.setString(2, customerPhone);
            return ps.executeUpdate();
        }
    }

    public int renameMenuItem(String category, String currentNameEn, String newNameEn, String newNameUk) throws SQLException {
        String sql = "UPDATE menu_items SET name_en = ?, name_uk = ? WHERE category = ?::menu_category AND name_en = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newNameEn);
            ps.setString(2, newNameUk);
            ps.setString(3, category);
            ps.setString(4, currentNameEn);
            return ps.executeUpdate();
        }
    }

    public Long findIdByPhone(String table, String phone) throws SQLException {
        String sql = "SELECT id FROM " + table + " WHERE phone = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
        }
        return null;
    }

    public Long findMenuItemIdByName(String nameEn) throws SQLException {
        String sql = "SELECT id FROM menu_items WHERE name_en = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nameEn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
        }
        return null;
    }

    public BigDecimal getMenuItemPrice(long menuItemId) throws SQLException {
        String sql = "SELECT price FROM menu_items WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, menuItemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("price");
                }
            }
        }
        throw new IllegalStateException("Price missing for item id " + menuItemId);
    }

    public BigDecimal findMinDiscount() throws SQLException {
        return querySingleDecimal("SELECT MIN(discount_percent) AS val FROM customers");
    }

    public BigDecimal findMaxDiscount() throws SQLException {
        return querySingleDecimal("SELECT MAX(discount_percent) AS val FROM customers");
    }

    public BigDecimal findAvgDiscount() throws SQLException {
        return querySingleDecimal("SELECT ROUND(AVG(discount_percent), 2) AS val FROM customers");
    }

    public List<CustomerInfo> findCustomersWithDiscount(BigDecimal discount) throws SQLException {
        String sql = "SELECT full_name, phone, birth_date, address, discount_percent FROM customers WHERE discount_percent = ? ORDER BY full_name";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, discount);
            try (ResultSet rs = ps.executeQuery()) {
                return readCustomers(rs);
            }
        }
    }

    public List<CustomerInfo> findYoungestCustomers() throws SQLException {
        String sql = "SELECT full_name, phone, birth_date, address, discount_percent FROM customers " +
                "WHERE birth_date = (SELECT MAX(birth_date) FROM customers) ORDER BY full_name";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return readCustomers(rs);
        }
    }

    public List<CustomerInfo> findOldestCustomers() throws SQLException {
        String sql = "SELECT full_name, phone, birth_date, address, discount_percent FROM customers " +
                "WHERE birth_date = (SELECT MIN(birth_date) FROM customers) ORDER BY full_name";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return readCustomers(rs);
        }
    }

    public List<CustomerInfo> findBirthdayCustomers(LocalDate date) throws SQLException {
        String sql = "SELECT full_name, phone, birth_date, address, discount_percent FROM customers " +
                "WHERE EXTRACT(MONTH FROM birth_date) = ? AND EXTRACT(DAY FROM birth_date) = ? " +
                "ORDER BY full_name";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, date.getMonthValue());
            ps.setInt(2, date.getDayOfMonth());
            try (ResultSet rs = ps.executeQuery()) {
                return readCustomers(rs);
            }
        }
    }

    public List<CustomerInfo> findCustomersWithoutAddress() throws SQLException {
        String sql = "SELECT full_name, phone, birth_date, address, discount_percent FROM customers " +
                "WHERE address IS NULL OR TRIM(address) = '' ORDER BY full_name";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return readCustomers(rs);
        }
    }

    public int clearOrdersStaffByPhone(String phone) throws SQLException {
        String sql = "UPDATE orders SET staff_id = NULL WHERE staff_id = (SELECT id FROM staff WHERE phone = ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            return ps.executeUpdate();
        }
    }

    public int clearOrdersCustomerByPhone(String phone) throws SQLException {
        String sql = "UPDATE orders SET customer_id = NULL WHERE customer_id = (SELECT id FROM customers WHERE phone = ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            return ps.executeUpdate();
        }
    }

    public int deleteMenuItemByNameAndCategory(String category, String nameEn) throws SQLException {
        String sql = "DELETE FROM menu_items WHERE category = ?::menu_category AND name_en = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            ps.setString(2, nameEn);
            return ps.executeUpdate();
        }
    }

    public int deleteStaffByPhoneAndRole(String phone, String roleCode) throws SQLException {
        String sql = "DELETE FROM staff WHERE phone = ? AND role_id = (SELECT id FROM staff_roles WHERE code = ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            ps.setString(2, roleCode);
            return ps.executeUpdate();
        }
    }

    public int deleteCustomerByPhone(String phone) throws SQLException {
        String sql = "DELETE FROM customers WHERE phone = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            return ps.executeUpdate();
        }
    }

    public int deleteOrderItemsByDessertName(String dessertName) throws SQLException {
        String sql = "DELETE FROM order_items WHERE menu_item_id IN (" +
                "SELECT id FROM menu_items WHERE category = 'DESSERT' AND name_en = ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dessertName);
            return ps.executeUpdate();
        }
    }

    public int deleteLatestOrderForCustomer(String customerPhone) throws SQLException {
        String sql = "WITH target AS (" +
                "  SELECT o.id FROM orders o " +
                "  JOIN customers c ON c.id = o.customer_id " +
                "  WHERE c.phone = ? " +
                "  ORDER BY o.id DESC " +
                "  LIMIT 1" +
                ")" +
                "DELETE FROM orders o WHERE o.id IN (SELECT id FROM target)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customerPhone);
            return ps.executeUpdate();
        }
    }

    public List<MenuItemInfo> findMenuItemsByCategory(String category) throws SQLException {
        String sql = "SELECT category, name_en, name_uk, price FROM menu_items WHERE category = ?::menu_category ORDER BY name_en";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                List<MenuItemInfo> list = new java.util.ArrayList<>();
                while (rs.next()) {
                    list.add(new MenuItemInfo(
                            rs.getString("category"),
                            rs.getString("name_en"),
                            rs.getString("name_uk"),
                            rs.getBigDecimal("price")
                    ));
                }
                return list;
            }
        }
    }

    public List<StaffInfo> findStaffByRole(String roleCode) throws SQLException {
        String sql = "SELECT full_name, phone, address, active FROM staff " +
                "WHERE role_id = (SELECT id FROM staff_roles WHERE code = ?) ORDER BY full_name";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roleCode);
            try (ResultSet rs = ps.executeQuery()) {
                List<StaffInfo> list = new java.util.ArrayList<>();
                while (rs.next()) {
                    list.add(new StaffInfo(
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getString("address"),
                            rs.getBoolean("active")
                    ));
                }
                return list;
            }
        }
    }

    public List<OrderInfo> findOrdersByDessertName(String dessertName) throws SQLException {
        String sql = "SELECT DISTINCT o.id, o.status, o.created_at, c.phone AS customer_phone, s.phone AS staff_phone, o.comments " +
                "FROM orders o " +
                "JOIN order_items oi ON oi.order_id = o.id " +
                "JOIN menu_items mi ON mi.id = oi.menu_item_id " +
                "LEFT JOIN customers c ON c.id = o.customer_id " +
                "LEFT JOIN staff s ON s.id = o.staff_id " +
                "WHERE mi.category = 'DESSERT' AND mi.name_en = ? " +
                "ORDER BY o.id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dessertName);
            try (ResultSet rs = ps.executeQuery()) {
                return readOrderInfo(rs);
            }
        }
    }

    public List<OrderInfo> findOrdersByWaiterPhone(String waiterPhone) throws SQLException {
        String sql = "SELECT o.id, o.status, o.created_at, c.phone AS customer_phone, s.phone AS staff_phone, o.comments " +
                "FROM orders o " +
                "LEFT JOIN customers c ON c.id = o.customer_id " +
                "LEFT JOIN staff s ON s.id = o.staff_id " +
                "WHERE s.phone = ? " +
                "ORDER BY o.id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, waiterPhone);
            try (ResultSet rs = ps.executeQuery()) {
                return readOrderInfo(rs);
            }
        }
    }

    public List<OrderInfo> findOrdersByCustomerPhone(String customerPhone) throws SQLException {
        String sql = "SELECT o.id, o.status, o.created_at, c.phone AS customer_phone, s.phone AS staff_phone, o.comments " +
                "FROM orders o " +
                "LEFT JOIN customers c ON c.id = o.customer_id " +
                "LEFT JOIN staff s ON s.id = o.staff_id " +
                "WHERE c.phone = ? " +
                "ORDER BY o.id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customerPhone);
            try (ResultSet rs = ps.executeQuery()) {
                return readOrderInfo(rs);
            }
        }
    }

    public List<OrderInfo> findOrdersOnDate(LocalDate date) throws SQLException {
        String sql = "SELECT o.id, o.status, o.created_at, c.phone AS customer_phone, s.phone AS staff_phone, o.comments " +
                "FROM orders o " +
                "LEFT JOIN customers c ON c.id = o.customer_id " +
                "LEFT JOIN staff s ON s.id = o.staff_id " +
                "WHERE DATE(o.created_at) = ? " +
                "ORDER BY o.id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, date);
            try (ResultSet rs = ps.executeQuery()) {
                return readOrderInfo(rs);
            }
        }
    }

    public List<OrderInfo> findOrdersInRange(LocalDateTime from, LocalDateTime to) throws SQLException {
        String sql = "SELECT o.id, o.status, o.created_at, c.phone AS customer_phone, s.phone AS staff_phone, o.comments " +
                "FROM orders o " +
                "LEFT JOIN customers c ON c.id = o.customer_id " +
                "LEFT JOIN staff s ON s.id = o.staff_id " +
                "WHERE o.created_at >= ? AND o.created_at <= ? " +
                "ORDER BY o.id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, from);
            ps.setObject(2, to);
            try (ResultSet rs = ps.executeQuery()) {
                return readOrderInfo(rs);
            }
        }
    }

    public List<DrinkOrderCustomerInfo> findDrinkOrdersWithBaristaOnDate(LocalDate date) throws SQLException {
        String sql = "SELECT DISTINCT o.id, o.created_at, c.full_name AS customer_name, c.phone AS customer_phone, " +
                "s.full_name AS barista_name, s.phone AS barista_phone " +
                "FROM orders o " +
                "JOIN order_items oi ON oi.order_id = o.id " +
                "JOIN menu_items mi ON mi.id = oi.menu_item_id AND mi.category = 'DRINK' " +
                "LEFT JOIN customers c ON c.id = o.customer_id " +
                "LEFT JOIN staff s ON s.id = o.staff_id " +
                "LEFT JOIN staff_roles sr ON sr.id = s.role_id " +
                "WHERE DATE(o.created_at) = ? AND sr.code = 'BARISTA' " +
                "ORDER BY o.created_at, o.id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, date);
            try (ResultSet rs = ps.executeQuery()) {
                List<DrinkOrderCustomerInfo> list = new java.util.ArrayList<>();
                while (rs.next()) {
                    list.add(new DrinkOrderCustomerInfo(
                            rs.getLong("id"),
                            rs.getString("created_at"),
                            rs.getString("customer_name"),
                            rs.getString("customer_phone"),
                            rs.getString("barista_name"),
                            rs.getString("barista_phone")
                    ));
                }
                return list;
            }
        }
    }

    public BigDecimal findAverageOrderTotalOnDate(LocalDate date) throws SQLException {
        String sql = "SELECT ROUND(AVG(total_after_discount), 2) AS val FROM order_totals WHERE DATE(created_at) = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, date);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("val");
                }
            }
        }
        return null;
    }

    public BigDecimal findMaxOrderTotalOnDate(LocalDate date) throws SQLException {
        String sql = "SELECT MAX(total_after_discount) AS val FROM order_totals WHERE DATE(created_at) = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, date);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("val");
                }
            }
        }
        return null;
    }

    public List<CustomerOrderTotalInfo> findCustomersWithMaxOrderOnDate(LocalDate date) throws SQLException {
        String sql = "WITH day_orders AS (" +
                "   SELECT order_id, customer_id, total_after_discount FROM order_totals WHERE DATE(created_at) = ?" +
                "), max_total AS (" +
                "   SELECT MAX(total_after_discount) AS max_val FROM day_orders" +
                ") " +
                "SELECT d.order_id, c.full_name, c.phone, d.total_after_discount " +
                "FROM day_orders d " +
                "JOIN max_total mt ON d.total_after_discount = mt.max_val " +
                "LEFT JOIN customers c ON c.id = d.customer_id " +
                "ORDER BY c.full_name NULLS LAST, d.order_id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, date);
            try (ResultSet rs = ps.executeQuery()) {
                List<CustomerOrderTotalInfo> list = new java.util.ArrayList<>();
                while (rs.next()) {
                    list.add(new CustomerOrderTotalInfo(
                            rs.getLong("order_id"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getBigDecimal("total_after_discount")
                    ));
                }
                return list;
            }
        }
    }

    public int countDessertOrdersOnDate(LocalDate date) throws SQLException {
        return countOrdersByCategoryOnDate("DESSERT", date);
    }

    public int countDrinkOrdersOnDate(LocalDate date) throws SQLException {
        return countOrdersByCategoryOnDate("DRINK", date);
    }

    private int countOrdersByCategoryOnDate(String category, LocalDate date) throws SQLException {
        String sql = "SELECT COUNT(DISTINCT o.id) AS cnt " +
                "FROM orders o " +
                "JOIN order_items oi ON oi.order_id = o.id " +
                "JOIN menu_items mi ON mi.id = oi.menu_item_id " +
                "WHERE mi.category = ?::menu_category AND DATE(o.created_at) = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            ps.setObject(2, date);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt");
                }
            }
        }
        return 0;
    }

    public List<MenuItemInfo> findDrinksWithTwoNames() throws SQLException {
        String condition = "category = 'DRINK' AND " + nameEnPresent() + " AND " + nameUkPresent();
        return queryMenuItemsWithCategory(condition);
    }

    public List<MenuItemInfo> findDrinksWithSingleName() throws SQLException {
        String condition = "category = 'DRINK' AND (" +
                "(" + nameEnPresent() + " AND NOT " + nameUkPresent() + ") OR (" +
                nameUkPresent() + " AND NOT " + nameEnPresent() + "))";
        return queryMenuItemsWithCategory(condition);
    }

    public List<MenuItemInfo> findDessertsWithTwoNames() throws SQLException {
        String condition = "category = 'DESSERT' AND " + nameEnPresent() + " AND " + nameUkPresent();
        return queryMenuItemsWithCategory(condition);
    }

    public List<MenuItemInfo> findDessertsWithSingleName() throws SQLException {
        String condition = "category = 'DESSERT' AND (" +
                "(" + nameEnPresent() + " AND NOT " + nameUkPresent() + ") OR (" +
                nameUkPresent() + " AND NOT " + nameEnPresent() + "))";
        return queryMenuItemsWithCategory(condition);
    }

    public List<MenuItemInfo> findItemsWithSingleName() throws SQLException {
        String condition = "(" +
                "(" + nameEnPresent() + " AND NOT " + nameUkPresent() + ") OR (" +
                nameUkPresent() + " AND NOT " + nameEnPresent() + "))";
        return queryMenuItemsWithCategory(condition);
    }

    public List<MenuItemInfo> findItemsWithTwoNames() throws SQLException {
        String condition = nameEnPresent() + " AND " + nameUkPresent();
        return queryMenuItemsWithCategory(condition);
    }

    public List<MenuItemInfo> findItemsWithSameName() throws SQLException {
        String condition = nameEnPresent() + " AND " + nameUkPresent() + " AND LOWER(TRIM(name_en)) = LOWER(TRIM(name_uk))";
        return queryMenuItemsWithCategory(condition);
    }

    public BigDecimal findMinDrinkPrice() throws SQLException {
        return querySingleDecimal("SELECT MIN(price) AS val FROM menu_items WHERE category = 'DRINK'");
    }

    public BigDecimal findMinDessertPrice() throws SQLException {
        return querySingleDecimal("SELECT MIN(price) AS val FROM menu_items WHERE category = 'DESSERT'");
    }

    public List<MenuItemInfo> findDrinksWithMinPrice() throws SQLException {
        String condition = "category = 'DRINK' AND price = (SELECT MIN(price) FROM menu_items WHERE category = 'DRINK')";
        return queryMenuItemsWithCategory(condition);
    }

    public List<MenuItemInfo> findDessertsWithMinPrice() throws SQLException {
        String condition = "category = 'DESSERT' AND price = (SELECT MIN(price) FROM menu_items WHERE category = 'DESSERT')";
        return queryMenuItemsWithCategory(condition);
    }

    public BigDecimal findMaxDrinkPrice() throws SQLException {
        return querySingleDecimal("SELECT MAX(price) AS val FROM menu_items WHERE category = 'DRINK'");
    }

    public BigDecimal findMaxDessertPrice() throws SQLException {
        return querySingleDecimal("SELECT MAX(price) AS val FROM menu_items WHERE category = 'DESSERT'");
    }

    public BigDecimal findAvgDrinkPrice() throws SQLException {
        return querySingleDecimal("SELECT ROUND(AVG(price), 2) AS val FROM menu_items WHERE category = 'DRINK'");
    }

    public BigDecimal findAvgDessertPrice() throws SQLException {
        return querySingleDecimal("SELECT ROUND(AVG(price), 2) AS val FROM menu_items WHERE category = 'DESSERT'");
    }

    public BigDecimal findAvgDrinkAndDessertPrice() throws SQLException {
        return querySingleDecimal("SELECT ROUND(AVG(price), 2) AS val FROM menu_items WHERE category IN ('DRINK','DESSERT')");
    }

    public List<MenuItemInfo> findDrinksWithMaxPrice() throws SQLException {
        String condition = "category = 'DRINK' AND price = (SELECT MAX(price) FROM menu_items WHERE category = 'DRINK')";
        return queryMenuItemsWithCategory(condition);
    }

    public List<MenuItemInfo> findDessertsWithMaxPrice() throws SQLException {
        String condition = "category = 'DESSERT' AND price = (SELECT MAX(price) FROM menu_items WHERE category = 'DESSERT')";
        return queryMenuItemsWithCategory(condition);
    }

    public int countStaffByRole(String roleCode) throws SQLException {
        String sql = "SELECT COUNT(*) AS cnt FROM staff s JOIN staff_roles sr ON sr.id = s.role_id WHERE sr.code = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roleCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt");
                }
            }
        }
        return 0;
    }

    public int countAllStaff() throws SQLException {
        String sql = "SELECT COUNT(*) AS cnt FROM staff";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("cnt");
            }
        }
        return 0;
    }

    public List<StaffHireInfo> findLatestHiredStaff() throws SQLException {
        return findStaffByHiredDate("DESC");
    }

    public List<StaffHireInfo> findLongestWorkingStaff() throws SQLException {
        return findStaffByHiredDate("ASC");
    }

    public List<WorkScheduleInfo> findBaristaScheduleForWeek(LocalDate weekStart, String baristaPhone) throws SQLException {
        return findSchedule(weekStart, baristaPhone, true, false);
    }

    public List<WorkScheduleInfo> findAllBaristaScheduleForWeek(LocalDate weekStart) throws SQLException {
        return findSchedule(weekStart, null, true, true);
    }

    public List<WorkScheduleInfo> findAllStaffScheduleForWeek(LocalDate weekStart) throws SQLException {
        return findSchedule(weekStart, null, false, true);
    }

    private List<WorkScheduleInfo> findSchedule(LocalDate weekStart, String phone, boolean onlyBaristas, boolean allowNullPhone) throws SQLException {
        LocalDate weekEnd = weekStart.plusDays(6);
        StringBuilder sql = new StringBuilder(
                "SELECT s.full_name, s.phone, sr.code AS role_code, ws.shift_date, ws.shift_start, ws.shift_end " +
                        "FROM work_schedule ws " +
                        "JOIN staff s ON s.id = ws.staff_id " +
                        "JOIN staff_roles sr ON sr.id = s.role_id " +
                        "WHERE ws.shift_date BETWEEN ? AND ?"
        );
        if (onlyBaristas) {
            sql.append(" AND sr.code = 'BARISTA'");
        }
        if (!allowNullPhone && phone != null) {
            sql.append(" AND s.phone = ?");
        }
        sql.append(" ORDER BY ws.shift_date, ws.shift_start, s.full_name");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            ps.setObject(1, weekStart);
            ps.setObject(2, weekEnd);
            int idx = 3;
            if (!allowNullPhone && phone != null) {
                ps.setString(idx, phone);
            }
            try (ResultSet rs = ps.executeQuery()) {
                List<WorkScheduleInfo> list = new java.util.ArrayList<>();
                while (rs.next()) {
                    list.add(new WorkScheduleInfo(
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getString("role_code"),
                            rs.getDate("shift_date").toLocalDate(),
                            rs.getObject("shift_start", LocalTime.class),
                            rs.getObject("shift_end", LocalTime.class)
                    ));
                }
                return list;
            }
        }
    }

    public List<StaffInfo> findStaffWithoutPhone() throws SQLException {
        String sql = "SELECT full_name, phone, address, active FROM staff WHERE phone IS NULL OR TRIM(phone) = '' ORDER BY full_name";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<StaffInfo> list = new java.util.ArrayList<>();
            while (rs.next()) {
                list.add(new StaffInfo(
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getBoolean("active")
                ));
            }
            return list;
        }
    }

    public List<String> findUniqueStaffFirstNames() throws SQLException {
        String sql = "SELECT DISTINCT INITCAP(TRIM(split_part(full_name, ' ', 1))) AS first_name " +
                "FROM staff " +
                "WHERE TRIM(full_name) <> '' " +
                "ORDER BY first_name";
        return queryDistinctString(sql, "first_name");
    }

    public List<String> findUniqueStaffLastNames() throws SQLException {
        String sql = "SELECT DISTINCT regexp_replace(full_name, '.*\\s+', '') AS last_name " +
                "FROM staff " +
                "WHERE TRIM(full_name) <> '' " +
                "ORDER BY last_name";
        return queryDistinctString(sql, "last_name");
    }

    private List<OrderInfo> readOrderInfo(ResultSet rs) throws SQLException {
        List<OrderInfo> list = new java.util.ArrayList<>();
        while (rs.next()) {
            list.add(new OrderInfo(
                    rs.getLong("id"),
                    rs.getString("status"),
                    rs.getString("created_at"),
                    rs.getString("customer_phone"),
                    rs.getString("staff_phone"),
                    rs.getString("comments")
            ));
        }
        return list;
    }

    private List<CustomerInfo> readCustomers(ResultSet rs) throws SQLException {
        List<CustomerInfo> list = new java.util.ArrayList<>();
        while (rs.next()) {
            list.add(new CustomerInfo(
                    rs.getString("full_name"),
                    rs.getString("phone"),
                    rs.getDate("birth_date").toLocalDate(),
                    rs.getString("address"),
                    rs.getBigDecimal("discount_percent")
            ));
        }
        return list;
    }

    private List<MenuItemInfo> queryMenuItemsWithCategory(String condition) throws SQLException {
        String sql = "SELECT category, name_en, name_uk, price FROM menu_items WHERE " + condition + " ORDER BY category, name_en";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<MenuItemInfo> list = new java.util.ArrayList<>();
            while (rs.next()) {
                list.add(new MenuItemInfo(
                        rs.getString("category"),
                        rs.getString("name_en"),
                        rs.getString("name_uk"),
                        rs.getBigDecimal("price")
                ));
            }
            return list;
        }
    }

    private String nameEnPresent() {
        return "NULLIF(TRIM(name_en), '') IS NOT NULL";
    }

    private String nameUkPresent() {
        return "NULLIF(TRIM(name_uk), '') IS NOT NULL";
    }

    private List<String> queryDistinctString(String sql, String column) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<String> list = new java.util.ArrayList<>();
            while (rs.next()) {
                list.add(rs.getString(column));
            }
            return list;
        }
    }

    private List<StaffHireInfo> findStaffByHiredDate(String order) throws SQLException {
        String sql = "WITH target AS (" +
                "  SELECT hired_at FROM staff ORDER BY hired_at " + order + " LIMIT 1" +
                ") " +
                "SELECT s.full_name, s.phone, s.address, s.active, sr.code AS role_code, s.hired_at " +
                "FROM staff s " +
                "JOIN staff_roles sr ON sr.id = s.role_id " +
                "WHERE s.hired_at = (SELECT hired_at FROM target) " +
                "ORDER BY s.full_name";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<StaffHireInfo> list = new java.util.ArrayList<>();
            while (rs.next()) {
                list.add(new StaffHireInfo(
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getBoolean("active"),
                        rs.getString("role_code"),
                        rs.getDate("hired_at").toLocalDate()
                ));
            }
            return list;
        }
    }

    private BigDecimal querySingleDecimal(String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getBigDecimal("val");
            }
        }
        return null;
    }

}
