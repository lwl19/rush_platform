package com.rush.persistence.jpa;

import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

/**
 * TODO
 * <p>
 * @author 作者 E-mail:<p>
 * @version 创建时间：2016年6月13日 下午2:08:19
 * <p> 描述: 动态生成Specification(Specification是spring-jpa定义的规范规则接口，用于创建查询规则Predicate)
 */
public class DynamicSpecifications {
	public static <T> Specification<T> bySearchFilter(final Collection<SearchFilter> filters, final Class<T> entityClazz) {
		Specification<T> spec = new DynamicSpecification<T>(filters);
		return spec;
	}

	/**
	 * 生成约束
	 * 
	 * @param root
	 *            查询实体根
	 * @param builder
	 *            查询创建器
	 * @param filters
	 *            查询条件
	 * @return 约束对象
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Predicate bySearchFilterToPredicate(Root<?> root, CriteriaBuilder builder, final Collection<SearchFilter> filters) {
		if (!CollectionUtils.isEmpty(filters)) {

			List<Predicate> predicates = Lists.newArrayList();
			for (SearchFilter filter : filters) {
				// nested path translate, 如Task的名为"user.name"的filedName,
				// 转换为Task.user.name属性
				String[] names = StringUtils.split(filter.fieldName, ".");
				Path expression = root.get(names[0]);
				for (int i = 1; i < names.length; i++) {
					expression = expression.get(names[i]);
				}

				// logic operator
				switch (filter.operator.toString()) {
				case "EQ":
					predicates.add(builder.equal(expression, filter.value));
					break;
				case "NEQ":
					predicates.add(builder.not(builder.equal(expression, filter.value)));
					break;
				case "LIKE":
					predicates.add(builder.like(expression, "%" + filter.value + "%"));
					break;
				case "LLIKE":
					predicates.add(builder.like(expression, filter.value + "%"));
					break;
				case "RLIKE":
					predicates.add(builder.like(expression, "%" + filter.value));
					break;
				case "GT":
					predicates.add(builder.greaterThan(expression, (Comparable) filter.value));
					break;
				case "LT":
					predicates.add(builder.lessThan(expression, (Comparable) filter.value));
					break;
				case "GTE":
					predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.value));
					break;
				case "LTE":
					predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.value));
					break;
				case "IN":
					predicates.add(builder.in(expression).value(filter.value));
					break;
				case "NOTIN":
					predicates.add(builder.not(builder.in(expression).value(filter.value)));
					break;
				case "ISNULL":
					predicates.add(builder.isNull(expression));
					break;
				case "ISNOTNULL":
					predicates.add(builder.isNotNull(expression));
					break;
				case "ISEMPTY":
					predicates.add(builder.equal(expression, ""));
					break;
				case "ISNOTEMPTY":
					predicates.add(builder.not(builder.equal(expression, "")));
					break;
				case "ISTRUE":
					predicates.add(builder.isTrue(expression));
					break;
				case "ISFALSE":
					predicates.add(builder.isFalse(expression));
					break;
				case "EQENUM":
					try {
						String[] values = filter.value.toString().split("_");
						Class clazz = Class.forName(values[0]);
						predicates.add(builder.equal(expression, Enum.valueOf(clazz, values[1])));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						throw new RuntimeException(e.getMessage());
					}

					break;
				}

			}

			// 将所有条件用 and 联合起来
			if (!predicates.isEmpty()) {
				return builder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}
		return builder.conjunction();
	}
}
