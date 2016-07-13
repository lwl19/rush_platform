package com.rush.persistence.jpa;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author lwlgml@gmail.com
 * @version 2016年6月22日 上午11:38:13
 */
public class RepositoryTools {
	public static <T> T query(JdbcTemplate jdbcTemplate, String sql, RowMapper<T> rowMapper, Object... objs) {
		return jdbcTemplate.queryForObject(sql, objs, rowMapper);
	}

	public static void update(JdbcTemplate jdbcTemplate, String sql, Object... objs) {
		jdbcTemplate.update(sql, objs);
	}

	public static <T> List<T> queryList(JdbcTemplate jdbcTemplate, String sql, RowMapper<T> rowMapper, Object... objs) {
		return jdbcTemplate.query(sql, objs, rowMapper);
	}
}
