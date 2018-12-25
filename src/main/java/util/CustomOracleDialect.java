package util;

import java.sql.Types;

import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.type.StringType;  
  
public class CustomOracleDialect extends Oracle10gDialect {  
    public CustomOracleDialect () {  
        super();  
        registerHibernateType(Types.NVARCHAR, StringType.INSTANCE.getName());
    }  
}  
