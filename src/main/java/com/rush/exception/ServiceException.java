package com.rush.exception;

/**
 * TODO
 * @author 作者 E-mail:
 * @version 创建时间：2016年6月13日 下午2:20:06
 * 描述: 继承自RuntimeException, 从由Spring管理事务的函数中抛出时会触发事务回滚.
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -6001471136221735910L;

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Class<?> clazz, String method, String message,
			Throwable cause) {
		super(
				String.format(
						"ServiceException for message: {%s},throwed by class[%s] method[%s]",
						message, clazz.getName(), method), cause);
	}

	public ServiceException(Class<?> clazz, String method, String message) {
		super(
				String.format(
						"ServiceException for message: {%s},throwed by class[%s] method[%s]",
						message, clazz.getName(), method));
	}

}
