package com.flurry.pageplugin.mybatis;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public final class ConvertUtils {

	private static final String HEX_PREFIX = "0x";

	private static final int HEX_RADIX = 16;

	private static final Class<?>[] CONSTR_ARGS = { String.class };

	private ConvertUtils() {
	}

	public static Boolean toBoolean(Object value) {
		if (value == null) 	return null;
		if (value instanceof Boolean) 	return (Boolean) value;
		if (value instanceof String) {
			return  "y".equalsIgnoreCase((String)value)
				|| "t".equalsIgnoreCase((String)value)
				|| "true".equalsIgnoreCase((String)value)
				|| "yes".equalsIgnoreCase((String)value)
				|| "1".equalsIgnoreCase((String)value);
		}		
		if (value instanceof Number) {
			return ((Number)value).intValue() == 1;
		}		
		throw new RuntimeException("The value " + value+ " can't be converted to a Boolean object");
	}

	public static Byte toByte(Object value) {
		if (value == null) return null;		
		Number n = toNumber(value, Byte.class);
		if (n instanceof Byte) {
			return (Byte) n;
		} else {
			return Byte.valueOf(n.byteValue());
		}
	}


	public static Number toNumber(Object value, Class<?> targetClazz) {		
		if (value == null) 	return null;
		if (value instanceof Number) return (Number) value;				
		String str = value.toString();	
		//16 Judge	
		if (str.startsWith(HEX_PREFIX)) {
			try {
				return new BigInteger(str.substring(HEX_PREFIX.length()), HEX_RADIX);
			} catch (NumberFormatException nex) {
				throw new RuntimeException("Could not convert " + str + " to " + targetClazz.getName()+ "! Invalid hex number.", nex);
			}
		}
		try {
			Constructor<?> constr = targetClazz.getConstructor(CONSTR_ARGS);
			return (Number) constr.newInstance(new Object[] {str});			
		} catch (InvocationTargetException itex) {
			throw new RuntimeException("Could not convert " + str + " to "+ targetClazz.getName(), itex.getTargetException());
		} catch (Exception ex) {
			throw new RuntimeException("Conversion error when trying to convert " + str+ " to " + targetClazz.getName(), ex);
		}
	}


	public static Short toShort(Object value) {
		if (value == null) 	return null;
		Number number = toNumber(value, Short.class);
		if (number instanceof Short) {
			return (Short) number;
		} else {
			return Short.valueOf(number.shortValue());
		}
	}

	public static Integer toInteger(Object value) {
		if (value == null) 	return null;
		Number number = toNumber(value, Integer.class);
		if (number instanceof Integer) {
			return (Integer) number;
		} else {
			return Integer.valueOf(number.intValue());
		}
	}

	public static Long toLong(Object value) {
		if (value == null) 	return null;
		Number number = toNumber(value, Long.class);
		if (number instanceof Long) {
			return (Long) number;
		} else {
			return Long.valueOf(number.longValue());
		}
	}

	public static Float toFloat(Object value) {
		if (value == null) 	return null;
		Number number = toNumber(value, Float.class);
		if (number instanceof Float) {
			return (Float) number;
		} else {
			return new Float(number.floatValue());
		}
	}

	public static Double toDouble(Object value) {
		if (value == null) 	return null;
		Number number = toNumber(value, Double.class);
		if (number instanceof Double) {
			return (Double) number;
		} else {
			return new Double(number.doubleValue());
		}
	}

	public static BigInteger toBigInteger(Object value) {
		if (value == null) 	return null;
		Number number = toNumber(value, BigInteger.class);
		if (number instanceof BigInteger) {
			return (BigInteger) number;
		} else {
			return BigInteger.valueOf(number.longValue());
		}
	}

	public static BigDecimal toBigDecimal(Object value) {
		if (value == null) 	return null;
		Number number = toNumber(value, BigDecimal.class);
		if (number instanceof BigDecimal) {
			return (BigDecimal) number;
		} else {
			return new BigDecimal(number.doubleValue());
		}
	}

	public static Date toDate(Object value, String format) {
		if (value == null) 	return null;
		if (value instanceof String) {
			try {
				return new SimpleDateFormat(format).parse((String) value);
			} catch (ParseException e) {
				throw new RuntimeException("The value " + value + "with format "+format+" can't be converted to a Date", e);
			}			
		} else if (value instanceof Long) {
			return new Date(((Long)value).longValue());
		} else if (value instanceof Date) { 
			return (Date) value;
		} else if (value instanceof Calendar) {
			return ((Calendar) value).getTime();
		}		
		throw new RuntimeException("The value " + value + " can't be converted to a Date");		
	} 
	

	public static boolean toBoolean(Object value, boolean defaultValue) {		
		try {			
			return toBoolean(value);			
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static byte toByte(Object value, byte defaultValue) {		
		try {
			return toByte(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static short toShort(Object value, short defaultValue) {		
		try {
			return toShort(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static int toInt(Object value, int defaultValue) {		
		try {
			return toInt(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static int toInt(Object value) throws RuntimeException {		
		if (null == value) {
			throw new RuntimeException("Impossible to convert "+value+" from "+(null == value ? "unknown" : value.getClass().getName())+" to "+int.class.getName());
		} else if (value instanceof Number) {
			return ((Number) value).intValue();
		} else if (value instanceof String) {
			try {
				return Integer.parseInt((String) value);
			} catch (NumberFormatException e) {
				throw new RuntimeException("Impossible to convert "+value+" from "+(null == value ? "unknown" : value.getClass().getName())+" to "+int.class.getName());
			}
		}	
		throw new RuntimeException("Impossible to convert "+value+" from "+(null == value ? "unknown" : value.getClass().getName())+" to "+int.class.getName());
	}

	public static long toLong(Object value, long defaultValue) {
		try {
			return toLong(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static float toFloat(Object value, float defaultValue) {
		try {
			return toFloat(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static double toDouble(Object value, double defaultValue) {
		try {
			return toDouble(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}
}
