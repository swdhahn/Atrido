package com.countgandi.com.net;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ProxySide {
	
	ProxyType type();
	
	public enum ProxyType {
		Server, Client
	}

}