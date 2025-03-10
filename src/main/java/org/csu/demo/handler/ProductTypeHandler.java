package org.csu.demo.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.csu.demo.domain.ProductType;

import java.sql.*;

public class ProductTypeHandler extends BaseTypeHandler<ProductType> {

    // 1. 设置参数（插入时使用）
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ProductType parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getIndex()); // 存储枚举的 index
    }

    // 2. 通过列名获取枚举值（查询时使用）
    @Override
    public ProductType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int index = rs.getInt(columnName);
        return getProductTypeByIndex(index);
    }

    // 3. 通过列索引获取枚举值（查询时使用）
    @Override
    public ProductType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int index = rs.getInt(columnIndex);
        return getProductTypeByIndex(index);
    }

    // 4. 处理存储过程返回值（查询时使用）
    @Override
    public ProductType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int index = cs.getInt(columnIndex);
        return getProductTypeByIndex(index);
    }

    // 辅助方法：根据 index 获取枚举值
    private ProductType getProductTypeByIndex(int index) {
        for (ProductType type : ProductType.values()) {
            if (type.getIndex() == index) {
                return type;
            }
        }
        return null; // 未匹配到返回 null
    }
}
