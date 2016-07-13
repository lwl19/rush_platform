package com.rush.persistence.jpa;

import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

/**
 * TODO
 * @author 作者 E-mail: lwlgml@gmail.com
 * @version 创建时间：2016年6月13日 下午2:06:34
 * 描述:
 */
public class DynamicSpecification<T> implements Specification<T> {

	final Collection<SearchFilter> filters;

	public DynamicSpecification(Collection<SearchFilter> filters) {
		this.filters = filters;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) {
		return DynamicSpecifications.bySearchFilterToPredicate(root, cb,
				filters);
	}

}
