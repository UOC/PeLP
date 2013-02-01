package edu.uoc.pelp.interceptor;

import com.opensymphony.xwork2.ActionInvocation;

public interface Interceptor  {

	void destroy();

	void init();

	String intercept(ActionInvocation invocation) throws Exception;
}