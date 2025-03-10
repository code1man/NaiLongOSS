package org.csu.demo.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.csu.demo.domain.ProductType;

import java.sql.*;

@MappedTypes(ProductType.class)
@MappedJdbcTypes(JdbcType.INTEGER)
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
        return ProductType.fromIndex(index);
    }

    // 3. 通过列索引获取枚举值（查询时使用）
    @Override
    public ProductType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int index = rs.getInt(columnIndex);
        System.out.println("数据库返回的 ProductType index：" + index);
        return ProductType.fromIndex(index);
    }

    // 4. 处理存储过程返回值（查询时使用）
    @Override
    public ProductType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int index = cs.getInt(columnIndex);
        return ProductType.fromIndex(index);
    }


}
