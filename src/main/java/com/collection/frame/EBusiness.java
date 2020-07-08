package com.collection.frame;

/**
 * 统一业务异常
 */
public class EBusiness extends RuntimeException {
	
	private static final long serialVersionUID = -5646733763129661754L;

	public EBusiness() {
		super();
	}

	public EBusiness(String message) {
		super(message);
	}
	
	public EBusiness(String message, Throwable e) {
		super(message, e);
	}
	
	public EBusiness(Throwable e) {
		super(e);
	}
	
	public EBusiness(String message, Throwable e, boolean a, boolean b) {
		super(message, e, a, b);
	}
}
