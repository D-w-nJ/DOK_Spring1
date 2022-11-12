package com.example.demo.src.restaurant;

import com.example.demo.src.restaurant.model.GetRestaurantRes;
import com.example.demo.src.restaurant.model.PostRestaurantReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class RestaurantDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createRestaurant(PostRestaurantReq postRestaurantReq){
        String createRestQuery = "insert into Restaurant (restaurantName, address, category) VALUES (?,?,?)";
        Object[] createRestParams = new Object[]{postRestaurantReq.getRestaurantName(),postRestaurantReq.getAddress(),postRestaurantReq.getCategory()};
        this.jdbcTemplate.update(createRestQuery,createRestParams);

        String lastInsertRestQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertRestQuery,int.class);
    }

    public List<GetRestaurantRes> getRestaurants(){
        String getRestaurantsQuery = "select * from Restaurant";
        return this.jdbcTemplate.query(getRestaurantsQuery,
                (rs,rowNum)->new GetRestaurantRes(
                        rs.getInt("restaurantIdx"),
                        rs.getString("restaurantName"),
                        rs.getString("address"),
                        rs.getString("category"))
        );
    }

    public List<GetRestaurantRes> getRestaurantByCategory(String category){
        String getRestsByCategoryQuery = "select * from Restaurant where category=?";
        String getRestsByCategoryParams = category;
        return this.jdbcTemplate.query(getRestsByCategoryQuery,
                (rs,rowNum)-> new GetRestaurantRes(
                        rs.getInt("restaurantIdx"),
                        rs.getString("restaurantName"),
                        rs.getString("address"),
                        rs.getString("category")),
                getRestsByCategoryParams);
    }
}
