package com.sct.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModelFactory {
	private static Logger log = LogManager.getLogger(ModelFactory.class);
	private static ModelFactory mf = null;

	private ModelFactory() {
	}

	public static synchronized ModelFactory getFactory() {
		if (mf == null) {
			mf = new ModelFactory();
		}
		return mf;
	}

	public Object get(Class<?> inter, Class<?> clazz) {
		if (!clazz.isAnnotationPresent(Log.class)) {
			throw new RuntimeException("Class not annotated with @Log");
		}
		Object o = null;
		Constructor<?> c;
		try {
			c = clazz.getConstructor();
			o = Proxy.newProxyInstance(inter.getClassLoader(), new Class[] { inter }, new LogProxy(c.newInstance()));
		} catch (Exception e) {
			log.error("Method threw exception: " + e);
			for (StackTraceElement s : e.getStackTrace()) {
				log.warn(s);
			}
			throw new RuntimeException(e);
		}
		return o;
	}
}
