package util;

import java.io.Serializable;
import java.util.Iterator;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.Property;
//import org.hibernate.validator.PropertyConstraint;
//import org.hibernate.validator.Validator;

/**
 * 檢查字串是否為空空字串.
 * <pre>
 * 以下的值為空字串：
 * 		null, ""
 * 以下的值'不'為空字串：
 * 		" ","abc"," abc"," abc "
 * </pre>
 * @author Kent <KentChu@dsc.com.tw>
 *
 */
public class NotBlankValidator {
	public boolean isValid(Object value) {
		return value != null && value.toString().length() != 0;
	}



	public void apply(Property property) {
		Iterator<Column> iter = (Iterator<Column>) property.getColumnIterator();
		while ( iter.hasNext() ) {
			iter.next().setNullable( false );
		}
	}

//	public void initialize(NotBlank parameters) {
//	}
}
