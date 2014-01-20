package it.cnr.ilc.cophi.action.userbean;


import it.cnr.ilc.cophi.utils.Consts;
import it.cnr.ilc.cophi.utils.Utils;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;


/**
 * Simple login bean.
 * 
 * @author itcuties
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -4497884335833238273L;

	// Simple user database :)
	private static final String[] users = {"test1:test1","test2:test2","test3:test3","test4:test4","test5:test5"};
	
//	private static final String[] users = {"simone:simone"}; //in manutenzione

	private String username;
	private String password;
	private String role;
	private boolean loggedIn;
	private String arabicFontSize = "1.5em";
	private String greekFontSize = "1.4em";

	@ManagedProperty(value="#{navigationBean}")
	private NavigationBean navigationBean;

	/**
	 * Login operation.
	 * @return
	 */
	public String doLogin() {
		// Get every user from our sample database :)
		for (String user: users) {
			String dbUsername = user.split(":")[0];
			String dbPassword = user.split(":")[1];

			// Successful login
			if (dbUsername.equals(username) && dbPassword.equals(password)) {
				loggedIn = true;
				/*
				 *  XXX non funziona il getFlash...
				 * Utils.addInfoMessageToContext("Login done", "loginMessage");
				 * FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
				 */
				//
				return navigationBean.redirectToWelcome();
			}
		}

		// Set login ERROR
		/*
		 *  XXX non funziona il getFlash...
		 * Utils.addErrorMessageToContext("Login error! Check username and password", "loginMessage");
		 * FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		 */
		//
		// To to login page
		return navigationBean.toLogin();

	}

	/**
	 * Logout operation.
	 * @return
	 */
	public String doLogout() {
		// Set the paremeter indicating that user is logged in to false
		loggedIn = false;
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

		Utils.addInfoMessageToContext("Logout done", "loginMessage");
		return navigationBean.toLogin();
	}


	public void verifyUseLogin(ComponentSystemEvent event){
		if(!loggedIn){
			//System.err.println("User is NOT logged in");
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			try {
				ec.redirect(ec.getRequestContextPath() + navigationBean.redirectToLogin());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else { 
			//System.err.println("User is logged in");
		}
	}

	// ------------------------------
	// Getters & Setters 

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getLoggedIn() {
		return loggedIn;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public void setNavigationBean(NavigationBean navigationBean) {
		this.navigationBean = navigationBean;
	}

	/**
	 * @return the arabicFontSize
	 */
	public String getArabicFontSize() {
		return arabicFontSize;
	}

	/**
	 * @param arabicFontSize the arabicFontSize to set
	 */
	public void setArabicFontSize(String arabicFontSize) {
		this.arabicFontSize = arabicFontSize;
	}

	/**
	 * @return the greekFontSize
	 */
	public String getGreekFontSize() {
		return greekFontSize;
	}

	/**
	 * @param greekFontSize the greekFontSize to set
	 */
	public void setGreekFontSize(String greekFontSize) {
		this.greekFontSize = greekFontSize;
	}

	public void decreaseArabicSize() {
		decreaseSize(Consts.ARABIC);
	}

	public void decreaseGreekSize() {
		decreaseSize(Consts.GREEK);
	}

	private void decreaseSize(int lang){
		switch (lang) {
		case Consts.GREEK:
			if (Utils.emToFloat(getGreekFontSize()) > 0.5) {
				setGreekFontSize((Utils.emToFloat(getGreekFontSize()) * 0.8 ) + "em");
			}
			break;

		case Consts.ARABIC:
			if (Utils.emToFloat(getArabicFontSize()) > 0.5) {
				setArabicFontSize((Utils.emToFloat(getArabicFontSize()) * 0.8) + "em");
			}
			break;

		default:
			break;
		}

	}
	public void increaseArabicSize() {
		increaseSize(Consts.ARABIC);
	}

	public void increaseGreekSize() {
		increaseSize(Consts.GREEK);
	}

	private void increaseSize(int lang){
		switch (lang) {
		case Consts.GREEK:
			if (Utils.emToFloat(getGreekFontSize()) < 4.0) {
				setGreekFontSize((Utils.emToFloat(getGreekFontSize()) * 1.2 ) + "em");
			}
			break;

		case Consts.ARABIC:
			if (Utils.emToFloat(getArabicFontSize()) < 4.0) {
				setArabicFontSize((Utils.emToFloat(getArabicFontSize()) * 1.2) + "em");
			}
			break;

		default:
			break;
		}

	}

	public void resetArabicSize() {
		resetSize(Consts.ARABIC);
	}

	public void resetGreekSize() {
		resetSize(Consts.GREEK);
	}

	private void resetSize (int lang){

		switch (lang) {
		case Consts.GREEK:
			setGreekFontSize("1.4em");
			break;

		case Consts.ARABIC:
			setArabicFontSize("1.5em");
			break;

		default:
			break;
		}
	}

}