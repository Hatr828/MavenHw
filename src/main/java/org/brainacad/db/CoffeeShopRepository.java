package org.brainacad.db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.brainacad.model.MenuItemInfo;
import org.brainacad.model.OrderInfo;
import org.brainacad.model.StaffInfo;

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
        String sql = "SELECT name_en, name_uk, price FROM menu_items WHERE category = ?::menu_category ORDER BY name_en";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                List<MenuItemInfo> list = new java.util.ArrayList<>();
                while (rs.next()) {
                    list.add(new MenuItemInfo(
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

}
