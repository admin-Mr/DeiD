package util.dao;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

@SuppressWarnings("rawtypes")
public class MultiSessionFactory implements FactoryBean {
    private DataSource dataSource;
    private Map<DataSource, SessionFactory> sessionFactories = new HashMap<DataSource, SessionFactory>();
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        System.out.println("^^^^^^^^^^" + dataSource);
    }
    
    public void setSessionFactories(Map<DataSource, SessionFactory> sessionFactories) {
        this.sessionFactories = sessionFactories;
        System.out.println("^^^^^^^^^^" + sessionFactories);
    }
    
    public Object getObject() throws Exception {
        Assert.notNull(dataSource);
        Object object = sessionFactories.get(dataSource);
        return object;
    }

    public Class getObjectType() {
        return SessionFactory.class;
    }

    public boolean isSingleton() {
        return false;
    }

}
