package com.example.demo.src.restaurant;

import com.example.demo.src.restaurant.model.*;
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

    public int createRestaurant(PostRestaurantReq postRestaurantReq) {
        String createRestQuery = "insert into Restaurant (restaurantName, address, category, password) VALUES (?,?,?,?)";
        Object[] createRestParams = new Object[]{postRestaurantReq.getRestaurantName(), postRestaurantReq.getAddress(), postRestaurantReq.getCategory(), postRestaurantReq.getPassword()};
        this.jdbcTemplate.update(createRestQuery, createRestParams);

        String lastInsertRestQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertRestQuery, int.class);
    }

    public List<GetRestaurantRes> getRestaurants() {
        String getRestaurantsQuery = "select * from Restaurant";
        return this.jdbcTemplate.query(getRestaurantsQuery,
                (rs, rowNum) -> new GetRestaurantRes(
                        rs.getInt("restaurantIdx"),
                        rs.getString("restaurantName"),
                        rs.getString("address"),
                        rs.getString("category"))
        );
    }

    public List<GetRestaurantRes> getRestaurantByCategory(String category) {
        String getRestsByCategoryQuery = "select * from Restaurant where category=?";
        String getRestsByCategoryParams = category;
        return this.jdbcTemplate.query(getRestsByCategoryQuery,
                (rs, rowNum) -> new GetRestaurantRes(
                        rs.getInt("restaurantIdx"),
                        rs.getString("restaurantName"),
                        rs.getString("address"),
                        rs.getString("category")),
                getRestsByCategoryParams);
    }

    public List<GetRestaurantRes> getRestPage(int size) {
        String getRestsPageQuery = "select *from Restaurant order by restaurantIdx desc limit ?";
        int getRestsPageParams = size;
        return this.jdbcTemplate.query(getRestsPageQuery,
                (rs, rowNum) -> new GetRestaurantRes(
                        rs.getInt("restaurantIdx"),
                        rs.getString("restaurantName"),
                        rs.getString("address"),
                        rs.getString("category")
                ),getRestsPageParams);
    }

    public List<GetRestaurantRes> getRestBasedCursor(int cursorId, int size) {
        String getRestsBasedCursorQuery = "select * from Restaurant where restaurantIdx < ? order by restaurantIdx desc limit ?";
        return this.jdbcTemplate.query(getRestsBasedCursorQuery,
                (rs, rowNum) -> new GetRestaurantRes(
                        rs.getInt("restaurantIdx"),
                        rs.getString("restaurantName"),
                        rs.getString("address"),
                        rs.getString("category")
                ),cursorId,size);
    }

    public Restaurant getPwd(PostRestaurantLoginReq postRestaurantLoginReq) {
        String getPwdQuery = "select restaurantIdx, restaurantName, password, address, category from Restaurant where restaurantName =?";
        String getPwdParams = postRestaurantLoginReq.getRestaurantName();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs, rowNum) -> new Restaurant(
                        rs.getInt("restaurantIdx"),
                        rs.getString("restaurantName"),
                        rs.getString("password"),
                        rs.getString("address"),
                        rs.getString("category")
                ), getPwdParams);
    }

    // 메뉴추가 쿼리
    public PostMenuRes createMenu(String restaurantName, PostMenuReq postMenuReq) {
        String createMenuQuery = "insert into Menu (menuName, restaurantIdx, price) VALUES (?,?,?)";
        Object[] createRestParams = new Object[]{postMenuReq.getMenuName(), getRestaurantIdx(restaurantName), postMenuReq.getPrice()};
        this.jdbcTemplate.update(createMenuQuery, createRestParams);

        int menuIdx = this.jdbcTemplate.queryForObject("select last_insert_id() as menuIdx", int.class);

        String lastInsertRestQuery = "select menuName from Menu where menuIdx = ?";

        return this.jdbcTemplate.queryForObject(lastInsertRestQuery,
                (rs, rowNum) -> new PostMenuRes(
                        menuIdx,
                        restaurantName,
                        rs.getString("menuName")
                ), menuIdx);
    }

    // 음식점 이름으로 idx 값 가져오기
    public int getRestaurantIdx(String restaurantName) {
        String getRestaurantIdxQuery = "select restaurantIdx from Restaurant where restaurantName=?";
        return this.jdbcTemplate.queryForObject(getRestaurantIdxQuery, int.class, restaurantName);
    }

    // 메뉴제공 쿼리
    public List<GetMenuRes> getMenus(String restaurantName){
        String getMenusQuery = "select * from Menu where restaurantIdx = ?";
        int restaurantIdx = getRestaurantIdx(restaurantName);
        return this.jdbcTemplate.query(getMenusQuery,
                (rs, rowNum) -> new GetMenuRes(
                        rs.getInt("menuIdx"),
                        restaurantName,
                        rs.getString("menuName"),
                        rs.getInt("price")
                ),restaurantIdx);
    }
    // 지점 정보변경
    public PatchRestRes modifyRest(String restaurantName, PatchRestReq patchRestReq){
        String modifyQuery = "update Restaurant set restaurantName=?, address=?, category=? where restaurantName=?";
        Object[] modifyParams = new Object[]{patchRestReq.getRestaurantName(),patchRestReq.getAddress(),patchRestReq.getCategory(),restaurantName};
        this.jdbcTemplate.update(modifyQuery,modifyParams);
        return new PatchRestRes(patchRestReq.getRestaurantName(),patchRestReq.getAddress(),patchRestReq.getCategory());
    }
}
