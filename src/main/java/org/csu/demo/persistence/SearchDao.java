package org.csu.demo.persistence;

import org.csu.demo.domain.Item;

import java.util.ArrayList;
import java.util.List;

public interface SearchDao {
    public List<Item> items = new ArrayList<>();

    public final String SEARCH_SQL = "SELECT i.id AS item_id, i.name AS item_name, p.description AS product_name, c.name AS category_name " +
            "FROM Item i " +
            "JOIN producttype p ON i.product_id = p.id " +
            "JOIN category c ON p.category_id = c.id " +
            "WHERE (c.name LIKE ? OR p.description LIKE ? OR i.name LIKE ?)";

    public List<Item> SearchItems(String keyword);
}
