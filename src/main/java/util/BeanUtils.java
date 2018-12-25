package util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 這個util class主要是用來處理bean的相關資訊.這個bean的method都是safe base的，也就是說當
 * 屬性不存在時，並不會丟出{@link NullPointerException}.
 * @author Kent <KentChu@dsc.com.tw>
 *
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger	logger	= Logger.getLogger(BeanUtils.class);

	static {
		org.apache.commons.beanutils.ConvertUtils.deregister(Double.class);
		org.apache.commons.beanutils.ConvertUtils.deregister(Long.class);
		org.apache.commons.beanutils.ConvertUtils.deregister(Short.class);
		org.apache.commons.beanutils.ConvertUtils.deregister(Integer.class);
		org.apache.commons.beanutils.ConvertUtils.deregister(Float.class);
	}
	
	public static List<String> expandProperties(Class clazz, String prefix, String[] excludes)
			throws IntrospectionException {
		BeanInfo info;
		info = Introspector.getBeanInfo(clazz);

		PropertyDescriptor[] pds = info.getPropertyDescriptors();
		List<String> results = new ArrayList<String>();
		for (PropertyDescriptor pd : pds) {
			Class<?> type = pd.getPropertyType();

			if (!ArrayUtils.contains(excludes, pd.getName())) {

				String property;
				if (StringUtils.isNotBlank(prefix)) {
					property = prefix + "." + pd.getName();
				} else {
					property = pd.getName();
				}

				if (!type.isPrimitive() && !type.getPackage().getName().startsWith("java.")) {
					/* *********************************************************
					 * NOTE: 如果class包含自己，小心不要造成無窮迴圈，所以這個method不採遞迴
					 * 方式展開，只展到第二層。
					 * *********************************************************  
					 */
					PropertyDescriptor[] npds = getPropertyDescriptors(type);
					for (PropertyDescriptor npd : npds) {

						if (!ArrayUtils.contains(excludes, npd.getName())) {
							if (logger.isDebugEnabled()) {
								logger.debug("add property [" + property + "." + npd.getName() + "]");
							}
							results.add(property + "." + npd.getName());
						}
					}
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("add property [" + property + "]");
					}
					results.add(property);
				}
			}

		}
		return results;

	}

	public static void fill(Object bean) {
		int base = 0;
		boolean postFix = false;
		fillWith(bean, base, postFix);
	}

	public static void fill(Object bean, int base) {
		boolean postFix = true;
		fillWith(bean, base, postFix);
	}

	public static PropertyDescriptor getPropertyDescriptor(Class clazz, String property) {
		PropertyDescriptor result = null;
		if (isNestProperty(property)) {
			String propName = StringUtils.substringBefore(property, ".");
			PropertyDescriptor pd2 = org.springframework.beans.BeanUtils.getPropertyDescriptor(clazz, propName);

			result = getPropertyDescriptor(pd2.getPropertyType(), StringUtils.substringAfter(property, propName + "."));
		} else {
			result = org.springframework.beans.BeanUtils.getPropertyDescriptor(clazz, property);
		}

		return result;
	}

	public static Object getPropertyValue(Object object, String property)  {
//		Object value = null;
//		if (isNestProperty(property)) {
//			String propName = StringUtils.substringBefore(property, ".");
//			PropertyDescriptor pd = getPropertyDescriptor(object.getClass(), propName);
//			if (pd == null)
//				return null;
//			try {
//				Object o = null;
//				o = pd.getReadMethod().invoke(object, null);
//				if (o != null) {
//					value = getPropertyValue(o, StringUtils.substringAfter(property, propName + "."));
//				}
//			} catch (IllegalArgumentException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			} catch (InvocationTargetException e) {
//				e.printStackTrace();
//			}
//		} else {
//			PropertyDescriptor pd = getPropertyDescriptor(object.getClass(), property);
//			if (pd == null)
//				return null;
//			try {
//				value = pd.getReadMethod().invoke(object, null);
//			} catch (IllegalArgumentException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			} catch (InvocationTargetException e) {
//				e.printStackTrace();
//			}
//		}
		try {
			if (isNestProperty(property)) {
				try {
					return PropertyUtils.getNestedProperty(object, property);
				} catch (NestedNullException e) {
					return null;
				}				
			} else {
			return PropertyUtils.getProperty(object,property);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
		//return value;
	}

	/**
	 * Bean的屬性是否有宣告指定的Annontaion.
	 * @param bean 目標bean
	 * @param property 屬性名稱
	 * @param annotation 要測試的annotation class
	 * @return true 屬性有宣告annotation. false 屬性沒有宣告annotation.
	 */
	public static boolean isAnnotationPresent(Object bean, String property, Class annotation) {
		PropertyDescriptor pd = getPropertyDescriptor(bean.getClass(), property);
		if (pd != null) {
			Method readMethod = pd.getReadMethod();
			return readMethod.isAnnotationPresent(annotation);
		} else {
			return false;

		}
	}

	public static boolean isNestProperty(String property) {
		return property.indexOf(".") != -1;
	}

	public static boolean isNumberType(Class<?> type) {
		boolean isNumberType;
		if (type.isPrimitive()) {
			Class newType = ClassUtils.primitiveToWrapper(type);
			isNumberType = Number.class.isAssignableFrom(newType);
		} else {
			isNumberType = Number.class.isAssignableFrom(type);
		}
		return isNumberType;
	}

	public static boolean isPropertyExist(Class clazz, String property) {
		return getPropertyDescriptor(clazz, property) != null ? true : false;
	}

	public static void setPropertyValue(Object object, String property, Object value) {
		if (isNestProperty(property)) {
//			String propName = StringUtils.substringBeforeLast(property, ".");
//			String p2 = StringUtils.substringBefore(propName, ".");
//			Object obj = getPropertyValue(object, p2);
//			if (obj != null) {
//				setPropertyValue(obj, StringUtils.substringAfter(property, p2 + "."), value);
//			}
			try {
				if (PropertyUtils.getProperty(object, getParent(property)) ==null) {
					return;
				}
				PropertyUtils.setNestedProperty(object, property, value);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} 
		} else {

			try {
				PropertyUtils.setProperty(object, property, value);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
	}

	private static void fillWith(Object bean, int base, boolean postFix) {
		PropertyDescriptor[] pds = getPropertyDescriptors(bean.getClass());
		try {
			for (PropertyDescriptor pd : pds) {
				Class<?> propertyType = pd.getPropertyType();
				String propertyName = pd.getName();
				if (isNumberType(propertyType)) {
					if (Double.class.equals(propertyType)) {
						PropertyUtils.setProperty(bean, propertyName, ((double) 0 + base));
					} else if (Long.class.equals(propertyType)) {
						PropertyUtils.setProperty(bean, propertyName, ((long) 0 + base));
					} else {
						PropertyUtils.setProperty(bean, propertyName, 0 + base);
					}
					if (logger.isDebugEnabled()) {
						logger.debug("set " + propertyName + " to " + (0 + base));
					}
				} else if (Date.class.isAssignableFrom(propertyType)) {
					PropertyUtils.setProperty(bean, propertyName, new Date());

					if (logger.isDebugEnabled()) {
						logger.debug("set " + propertyName + " to " + new Date());
					}
				} else if (Class.class.equals(propertyType)) {
					// do nothing
				} else if (String.class.equals(propertyType)) {
					if (postFix) {
						PropertyUtils.setProperty(bean, propertyName, propertyName + base);
						if (logger.isDebugEnabled()) {
							logger.debug("set " + propertyName + " to " + propertyName + base);
						}
					} else {
						PropertyUtils.setProperty(bean, propertyName, propertyName);
						if (logger.isDebugEnabled()) {
							logger.debug("set " + propertyName + " to " + propertyName);
						}
					}
				} else {
					Object nest = propertyType.newInstance();
					fillWith(nest, base, postFix);
					PropertyUtils.setProperty(bean, propertyName, nest);
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}

	}

	// Utils不應該被實體化，所以將constructor設成private 
	private BeanUtils() {
		super();
	}

	/**
	 * 取得bean裡有宣告anno類型annotation的屬性.
	 * @param bean The bean.
	 * @param anno The annotation class. 
	 * @return 有宣告anno類型annotation的屬性陣列.
	 */
	public static String[] getPropertiesByAnnotation(Object bean, Class anno) {
		return getPropertiesByAnnotation(bean.getClass(), anno);
	}

	/**
	 * 取得bean裡有宣告anno類型annotation的屬性.
	 * @param bean The bean.
	 * @param anno The annotation class. 
	 * @return 有宣告anno類型annotation的屬性陣列.
	 */
	public static String[] getPropertiesByAnnotation(Class c, Class anno) {
		PropertyDescriptor[] pds = getPropertyDescriptors(c);
		List<String> results = new ArrayList<String>();
		for (PropertyDescriptor pd : pds) {
			if (pd.getReadMethod().isAnnotationPresent(anno)) {
				results.add(pd.getName());
			}
		}
		return results.toArray(new String[results.size()]);
	}

	public static void initialNestProperty(Object object, String nestProperty) {
		if (!isNestProperty(nestProperty))
			throw new IllegalArgumentException("property [" + nestProperty + "] is not nest property");
		if (getPropertyValue(object, getParent(nestProperty)) == null) {
			Class type = getPropertyDescriptor(object.getClass(), getParent(nestProperty)).getPropertyType();
			setPropertyValue(object, getParent(nestProperty), instantiateClass(type));
		}
	}

	private static String getParent(String nestProperty) {
		return StringUtils.substringBefore(nestProperty, ".");
	}

}
