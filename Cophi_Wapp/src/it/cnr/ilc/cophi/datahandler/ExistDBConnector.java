/**
 * 
 */
package it.cnr.ilc.cophi.datahandler;

import it.cnr.ilc.cophi.utils.MessageProvider;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import org.exist.xmldb.EXistResource;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

/**
 * @author simone
 *
 */
public class ExistDBConnector {

	// Default values
	
	/*
	private String dbDriver = MessageProvider.getValue("config", "db_driver");
	private String dbServerName = MessageProvider.getValue("config","db_server");
	private String dbServerPort = MessageProvider.getValue("config","db_port");
	private String dbServerProtocol = MessageProvider.getValue("config","db_protocol");
	private String dbRootName = MessageProvider.getValue("config","db_root_name");
	private String dbLogin = MessageProvider.getValue("config","db_login");
	private String dbPassword = MessageProvider.getValue("config","db_password");
	 */

	
	private String dbDriver = "org.exist.xmldb.DatabaseImpl";
	private String dbServerName = "localhost";
	private String dbServerPort = "8085";
	private String dbServerProtocol = "exist/xmlrpc/db";
	private String dbRootName = //"ga";
	private String dbLogin = //"login_name";
	private String dbPassword = //"password";

	private HashMap<String, Collection> connectionsCache = new HashMap<String, Collection>();


	private static ExistDBConnector instance = null;

	private ExistDBConnector ()	{

	};


	/**
	 * @return the dbDriver
	 */
	public String getDbDriver() {
		return dbDriver;
	}

	/**
	 * @param dbDriver the dbDriver to set
	 */
	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}

	/**
	 * @return the dbServerName
	 */
	public String getDbServerName() {
		return dbServerName;
	}

	/**
	 * @param dbServerName the dbServerName to set
	 */
	public void setDbServerName(String dbServerName) {
		this.dbServerName = dbServerName;
	}

	/**
	 * @return the dbServerPort
	 */
	public String getDbServerPort() {
		return dbServerPort;
	}

	/**
	 * @param dbServerPort the dbServerPort to set
	 */
	public void setDbServerPort(String dbServerPort) {
		this.dbServerPort = dbServerPort;
	}

	/**
	 * @return the dbRootName
	 */
	public String getDbRootName() {
		return dbRootName;
	}

	/**
	 * @param dbRootName the dbRootName to set
	 */
	public void setDbRootName(String dbRootName) {
		this.dbRootName = dbRootName;
	}

	/**
	 * @return the dbLogin
	 */
	public String getDbLogin() {
		return dbLogin;
	}

	/**
	 * @param dbLogin the dbLogin to set
	 */
	public void setDbLogin(String dbLogin) {
		this.dbLogin = dbLogin;
	}

	/**
	 * @return the dbPassword
	 */
	public String getDbPassword() {
		return dbPassword;
	}

	/**
	 * @param dbPassword the dbPassword to set
	 */
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public static ExistDBConnector getInstance() {

		if (null == instance) {
			synchronized (ExistDBConnector.class) {
				if (null == instance) {
					instance = new ExistDBConnector();
				}				
			}
		}
		return instance;
	}

	private Database getDatabaseInstance () {

		Class<?> c;
		Database db = null;
		try {
			c = Class.forName(dbDriver);
			db = (Database)c.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} 

		return db;
	}

	private Collection getNewCollection (String dbName) throws XMLDBException  {

		Collection resourceCollection = null;
		if (null != dbName) {
			Database db = getDatabaseInstance();
			DatabaseManager.registerDatabase(db);
			String URL = "xmldb:exist://" + dbServerName + ":" + dbServerPort + "/" + dbServerProtocol + "/" + dbRootName + "/" + dbName;
   			resourceCollection = DatabaseManager.getCollection(URL, dbLogin, dbPassword);
		}

		return resourceCollection;
	}

	private Collection getCollection (String dbName) {

		Collection resourceCollection = null;
		try {
			if (! connectionsCache.containsKey(dbName)) {
				resourceCollection = getNewCollection(dbName);
			} else {
				resourceCollection = connectionsCache.get(dbName);
				if (! resourceCollection.isOpen()) {
					resourceCollection = getNewCollection(dbName);
				}
			}
			storeConnection (dbName, resourceCollection);
		} catch (XMLDBException ex) {
			ex.printStackTrace();
		}
		return resourceCollection;
	}

	private void storeConnection (String dbName, Collection resourceCollection) throws XMLDBException {

		if (connectionsCache != null) {
			if (null != resourceCollection) {
				connectionsCache.put(dbName, resourceCollection);
			} else {
				System.err.println("Error in storeConnection("+ dbName + ", "+ resourceCollection +")");
				throw new XMLDBException();
			}
		} else {
			System.err.println("Error in storeConnection("+ dbName + ", "+ resourceCollection +"): connections hash map is null!");
		}
	}

	public Collection connect (String dbName) {

		return getCollection(dbName);

	}

	public boolean save (String dbName, String xmlContent, String filename) {

		boolean ret = false;
		XMLResource xr = null;
		Collection col = getCollection(dbName);
		try {
			xr = (XMLResource) col.createResource(filename, "XMLResource");
			xr.setContent(xmlContent);
			col.storeResource(xr);
			ret = true;

		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(null != xr) {
			try { 
				((EXistResource)xr).freeResources(); 
			} 
			catch(XMLDBException xe) {xe.printStackTrace();}
		}

		return ret;
	}

	public boolean save (String dbName, XMLResource xr, String filename) {

		boolean ret = false;
		Collection col = getCollection(dbName);

		File f = new File(filename);
		try {
			xr.setContent(f);
			col.storeResource(xr);
			ret = true;
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(null != xr) {
			try { 
				((EXistResource)xr).freeResources(); 
			} 
			catch(XMLDBException xe) {xe.printStackTrace();}
		}
		return ret;
	}

	public boolean remove (String dbName, XMLResource xr) {

		boolean ret = false;
		Collection col = getCollection(dbName);

		try {
			col.removeResource(xr);
			ret = true;
		} catch (XMLDBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if(null != xr) {
			try { 
				((EXistResource)xr).freeResources(); 
			} 
			catch(XMLDBException xe) {xe.printStackTrace();}
		}
		return ret;
	}

	/**
	 * Remove all open connection
	 */
	public void disconnectAll () {

		Iterator<String> it = connectionsCache.keySet().iterator();
		while (it.hasNext()) {
			String dbName = it.next();
			disconnect (dbName);
		}
	}


	/**
	 * Close and remove the connection to <i>dbName</i> collection
	 * @param dbName
	 */
	public void disconnect (String dbName) {

		if (connectionsCache.containsKey(dbName)) {
			Collection resourceCollection = connectionsCache.get(dbName);
			try {
				if (resourceCollection.isOpen()) {
					resourceCollection.close();
					connectionsCache.remove(dbName);
				}
			} catch (XMLDBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
