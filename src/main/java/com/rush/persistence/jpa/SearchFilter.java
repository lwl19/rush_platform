package com.rush.persistence.jpa;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;

/**
 * @des 查询条件转换的过程类，一个对象对应一条查询规则 使用{@link DynamicSpecifications}
 *      可以转换为规则查询Predicate
 * @author: lwlgml@gmail.com
 * @date: 2016年6月13日 下午1:47:07
 */
public class SearchFilter {

	// 属性名 支持属性传递，层级使用"."分割
	public String fieldName;

	// 过滤值
	public Object value;

	// 操作类型
	public SearchOperator operator;

	public SearchFilter(String fieldName, SearchOperator operator, Object value) {
		this.fieldName = fieldName;
		this.value = value;
		this.operator = operator;
	}

	/**
	 * @Title: parse
	 * @Description: TODO
	 * @param searchParams
	 * @return
	 * @return: Map<String,SearchFilter>
	 */
	public static Map<String, SearchFilter> parse(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = Maps.newHashMap();

		for (Entry<String, Object> entry : searchParams.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			// 拆分operator与filedAttribute
			String[] names = StringUtils.split(key, "_");
			if (names.length != 2)
				throw new IllegalArgumentException(key + " is not a valid search filter name");
			String fieldName = names[1];
			SearchOperator operator = SearchOperator.valueOf(names[0]);
			if (emptyError(operator, value))
				continue;
			// 创建searchFilter
			SearchFilter filter = new SearchFilter(fieldName, operator, value);
			filters.put(key, filter);
		}
		return filters;
	}

	private static boolean emptyError(SearchOperator operator, Object value) {
		return !operator.toString().startsWith("IS") && (value == null || StringUtils.isEmpty(String.valueOf(value)));
	}

}
