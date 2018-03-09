/*
 * Created: 2017年3月21日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.framework.springboot.audit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.jasig.inspektr.audit.AspectJAuditPointRuntimeInfo;
import org.jasig.inspektr.audit.AuditActionContext;
import org.jasig.inspektr.audit.AuditPointRuntimeInfo;
import org.jasig.inspektr.audit.AuditTrailManager;
import org.jasig.inspektr.audit.annotation.Audit;
import org.jasig.inspektr.audit.annotation.Audits;
import org.jasig.inspektr.audit.spi.AuditActionResolver;
import org.jasig.inspektr.audit.spi.AuditResourceResolver;
import org.jasig.inspektr.audit.spi.support.BooleanAuditActionResolver;
import org.jasig.inspektr.audit.spi.support.DefaultAuditActionResolver;
import org.jasig.inspektr.audit.spi.support.ObjectCreationAuditActionResolver;
import org.jasig.inspektr.common.spi.ClientInfoResolver;
import org.jasig.inspektr.common.spi.PrincipalResolver;
import org.jasig.inspektr.common.web.ClientInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
public class YkyAuditTrailManagementAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(YkyAuditTrailManagementAspect.class);
	
	private final PrincipalResolver auditPrincipalResolver;

	private final Map<String, AuditActionResolver> auditActionResolvers;

	private final Map<String, AuditResourceResolver> auditResourceResolvers;

	private final List<AuditTrailManager> auditTrailManagers;

	private final String applicationCode;

	private ClientInfoResolver clientInfoResolver = new YkyClientInfoResolver();
	
	private static final String REGEX = "\\[(.*?)\\]";// 使用非贪婪模式!

	/**
	 * Constructs an AuditTrailManagementAspect with the following parameters.
	 * Also, registers some default AuditActionResolvers including the
	 * {@link DefaultAuditActionResolver}, the
	 * {@link BooleanAuditActionResolver} and the
	 * {@link ObjectCreationAuditActionResolver}.
	 *
	 * @param applicationCode
	 *            the overall code that identifies this application.
	 * @param auditablePrincipalResolver
	 *            the resolver which will locate principals.
	 * @param auditTrailManagers
	 *            the list of managers to write the audit trail out to.
	 * @param auditActionResolverMap
	 *            the map of resolvers by name provided in the annotation on the
	 *            method.
	 * @param auditResourceResolverMap
	 *            the map of resolvers by the name provided in the annotation on
	 *            the method.
	 */
	public YkyAuditTrailManagementAspect(final String applicationCode,
			final PrincipalResolver auditablePrincipalResolver, final List<AuditTrailManager> auditTrailManagers,
			final Map<String, AuditActionResolver> auditActionResolverMap,
			final Map<String, AuditResourceResolver> auditResourceResolverMap) {
		this.auditPrincipalResolver = auditablePrincipalResolver;
		this.auditTrailManagers = auditTrailManagers;
		this.applicationCode = applicationCode;
		this.auditActionResolvers = auditActionResolverMap;
		this.auditResourceResolvers = auditResourceResolverMap;

	}

	@Around(value = "@annotation(audits)", argNames = "audits")
	public Object handleAuditTrail(final ProceedingJoinPoint joinPoint, final Audits audits) throws Throwable {
		Object retVal = null;
		String currentPrincipal = null;
		final String[] actions = new String[audits.value().length];
		final String[][] auditableResources = new String[audits.value().length][];
		try {
			retVal = joinPoint.proceed();
			currentPrincipal = this.auditPrincipalResolver.resolveFrom(joinPoint, retVal);

			if (currentPrincipal != null) {
				for (int i = 0; i < audits.value().length; i++) {
					final AuditActionResolver auditActionResolver = this.auditActionResolvers
							.get(audits.value()[i].actionResolverName());
					final AuditResourceResolver auditResourceResolver = this.auditResourceResolvers
							.get(audits.value()[i].resourceResolverName());
					auditableResources[i] = auditResourceResolver.resolveFrom(joinPoint, retVal);
					actions[i] = auditActionResolver.resolveFrom(joinPoint, retVal, audits.value()[i]);
				}
			}
			return retVal;
		} catch (final Exception e) {
			currentPrincipal = this.auditPrincipalResolver.resolveFrom(joinPoint, e);

			if (currentPrincipal != null) {
				for (int i = 0; i < audits.value().length; i++) {
					auditableResources[i] = this.auditResourceResolvers.get(audits.value()[i].resourceResolverName())
							.resolveFrom(joinPoint, e);
					actions[i] = auditActionResolvers.get(audits.value()[i].actionResolverName()).resolveFrom(joinPoint,
							e, audits.value()[i]);
				}
			}
			throw e;
		} finally {
			for (int i = 0; i < audits.value().length; i++) {
				executeAuditCode(currentPrincipal, auditableResources[i], joinPoint, retVal, actions[i],
						audits.value()[i]);
			}
		}
	}

	@Around(value = "@annotation(audit)", argNames = "audit")
	public Object handleAuditTrail(final ProceedingJoinPoint joinPoint, final Audit audit) throws Throwable {
		final AuditActionResolver auditActionResolver = this.auditActionResolvers.get(audit.actionResolverName());
		final AuditResourceResolver auditResourceResolver = this.auditResourceResolvers.get(audit.resourceResolverName());

		String currentPrincipal = null;
		String[] auditResource = new String[] { null };
		String action = null;
		Object retVal = null;
		try {
			retVal = joinPoint.proceed();

			currentPrincipal = this.auditPrincipalResolver.resolveFrom(joinPoint, retVal);
			auditResource = auditResourceResolver.resolveFrom(joinPoint, retVal);
			action = auditActionResolver.resolveFrom(joinPoint, retVal, audit);

			return retVal;
		} catch (final Exception e) {
			currentPrincipal = this.auditPrincipalResolver.resolveFrom(joinPoint, e);
			auditResource = auditResourceResolver.resolveFrom(joinPoint, e);
			action = auditActionResolver.resolveFrom(joinPoint, e, audit);
			throw e;
		} finally {
			executeAuditCode(currentPrincipal, auditResource, joinPoint, retVal, action, audit);
		}
	}

	private void executeAuditCode(final String currentPrincipal, final String[] auditableResources,
			final ProceedingJoinPoint joinPoint, final Object retVal, final String action, final Audit audit) {
		final String applicationCodeTemp = (audit.applicationCode() != null && audit.applicationCode().length() > 0)
				? audit.applicationCode() : this.applicationCode;
		final ClientInfo clientInfo = this.clientInfoResolver.resolveFrom(joinPoint, retVal);
		final Date actionDate = new Date();
		final AuditPointRuntimeInfo runtimeInfo = new AspectJAuditPointRuntimeInfo(joinPoint);
		String tempAction;
		try {
			tempAction = parsingActionAnnotationsParameter(action, joinPoint, auditableResources);
		} catch (Exception e) {
			logger.error("设计日志{}解析失败,数据丢失", action, e);
			return;
		}
		final AuditActionContext auditContext = new AuditActionContext(currentPrincipal,
				Arrays.toString(auditableResources), tempAction, applicationCodeTemp, actionDate,
				clientInfo.getClientIpAddress(), clientInfo.getServerIpAddress(), runtimeInfo);
		// Finally record the audit trail info
		for (AuditTrailManager manager : auditTrailManagers) {
			manager.record(auditContext);
		}
	}
	
	private String parsingActionAnnotationsParameter(String action ,ProceedingJoinPoint joinPoint ,String[] auditableResources) throws Exception {
		Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
		Annotation[][] annotations =  method.getParameterAnnotations();
		
		//解析注解参数
		String regex = "'#(.*?)'";//使用非贪婪模式！
		Matcher matcher= Pattern.compile(regex).matcher(action);
		Map<String, String> param = new HashMap<>();
		ObjectMapper objectMapper = new ObjectMapper();
	    while(matcher.find()){  
	      String name = matcher.group(1);
	      Object val = parsingParamter(name, annotations, auditableResources, objectMapper);
	      if(null != val){
	    	  param.put("'#"+name+"'", val.toString());
	      }
	    }
	    
	    //替换原始数据
	    String actionTemp = action;
	    for(Entry<String, String> entry : param.entrySet()){
	    	String keyTemp = entry.getKey().replaceAll("\\[", "\\\\[").replaceAll("\\]", "\\\\]");
	    	actionTemp = actionTemp.replaceAll(keyTemp, entry.getValue());
	    }
		return actionTemp;
	}
	
	@SuppressWarnings("unchecked")
	private Object parsingParamter(String name ,Annotation[][] annotations , String[] auditableResources , ObjectMapper objectMapper) throws Exception{
		if (name.indexOf('.') > -1) {
			String[] nameArray = name.split("\\.");
			Object obj = getFirstInParameter(nameArray[0] , annotations , auditableResources , objectMapper);
			int idx = 0;
			do {
				idx ++;
				obj = getValInFirstObj(obj , nameArray[idx]);
			} while (idx<nameArray.length-1);
			return obj;
		}else if(name.indexOf('[') >-1){
			String nameTemp = name;
			Matcher matcher = Pattern.compile(REGEX).matcher(nameTemp);
			List<Integer> indexList = new ArrayList<>(3);
			while (matcher.find()) {
				Integer index = Integer.parseInt(matcher.group(1));
				indexList.add(index);
			}
			if (!indexList.isEmpty()) {
				nameTemp = nameTemp.substring(0, nameTemp.indexOf('['));
			}
			Object obj = getValInResources(annotations, auditableResources, nameTemp, objectMapper , List.class);
			if(!indexList.isEmpty() && obj!=null) {
				List<Object> objList = (List<Object>) obj;
				Object valObj = null;
				int j =0;
				while (j<indexList.size()) {
					if(null != valObj){
						valObj = ((List<Object>)valObj).get(indexList.get(j));
					}else{
						valObj = objList.get(indexList.get(j));
					}
					j++;
				}
				return valObj;
			} else {
				return obj;
			}
		}else{
			return getValInResources(annotations, auditableResources, name, objectMapper , String.class);
		}
	}
	
	@SuppressWarnings("unchecked")
	private Object getFirstInParameter(String name, Annotation[][] annotations , String[] auditableResources , ObjectMapper objectMapper) throws Exception{
		String nameTemp = name;
		Matcher matcher = Pattern.compile(REGEX).matcher(nameTemp);
		List<Integer> indexList = new ArrayList<>(3);
		while (matcher.find()) {
			Integer index = Integer.parseInt(matcher.group(1));
			indexList.add(index);
		}
		if (!indexList.isEmpty()) {
			nameTemp = nameTemp.substring(0, nameTemp.indexOf('['));
		}
		Object obj;
		if(!indexList.isEmpty()) {
			obj = getValInResources(annotations, auditableResources, nameTemp, objectMapper , List.class);
		}else{
			obj = getValInResources(annotations, auditableResources, nameTemp, objectMapper , null);
		}
		if(!indexList.isEmpty() && obj!=null) {
			List<Object> objList = (List<Object>) obj;
			Object valObj = null;
			int j =0;
			while (j<indexList.size()) {
				if(null != valObj){
					valObj = ((List<Object>)valObj).get(indexList.get(j));
				}else{
					valObj = objList.get(indexList.get(j));
				}
				j++;
			}
			return valObj;
		} else {
			return obj;
		}
	}
	
	@SuppressWarnings("unchecked")
	private Object getValInFirstObj(Object paramobj, String name){
		if(null == paramobj){
			return null;
		}
		if(name.indexOf('[') >-1){
			String nameTemp = name;
			Matcher matcher = Pattern.compile(REGEX).matcher(nameTemp);
			List<Integer> indexList = new ArrayList<>(3);
			while (matcher.find()) {
				
				Integer index = Integer.parseInt(matcher.group(1));
				indexList.add(index);
			}
			if (!indexList.isEmpty()) {
				nameTemp = nameTemp.substring(0, nameTemp.indexOf('['));
			}
			if(!indexList.isEmpty() && paramobj!=null) {
				List<Object> objList = (List<Object>) ((Map<String,Object>)paramobj).get(nameTemp);
				Object valObj = null;
				int j =0;
				while (j<indexList.size()) {
					if(null != valObj){
						valObj = ((List<Object>)valObj).get(indexList.get(j));
					}else{
						valObj = objList.get(indexList.get(j));
					}
					j++;
				}
				return valObj;
			} else {
				return paramobj;
			}
		}else{
			return ((Map<String, Object>)paramobj).get(name);
		}
	}
	
	<T> Object getValInResources(Annotation[][] annotations ,String[] auditableResources ,String name , ObjectMapper objectMapper , Class<T> valueType) throws Exception {
		for (int i = 0; i < annotations.length; i++) {
			if(annotations[i].length<=0){
				continue;
			}
			if (annotations[i][0] instanceof Param && name.equals(((Param) annotations[i][0]).value()) && null == valueType) {
				return getunknownObj(objectMapper , auditableResources[i]);
			} else if (annotations[i][0] instanceof Param && name.equals(((Param) annotations[i][0]).value()) && null != valueType) {
				return objectMapper.readValue(auditableResources[i], valueType);
			}
		}
		return null;
	}
	
	private Object getunknownObj(ObjectMapper objectMapper , String auditableResources){
		try {
			return objectMapper.readValue(auditableResources, Map.class);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		try {
			return objectMapper.readValue(auditableResources, String.class);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		try {
			return objectMapper.readValue(auditableResources, List.class);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	public void setClientInfoResolver(final ClientInfoResolver factory) {
		this.clientInfoResolver = factory;
	}
}