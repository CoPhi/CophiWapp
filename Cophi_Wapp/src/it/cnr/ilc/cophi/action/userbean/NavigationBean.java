package it.cnr.ilc.cophi.action.userbean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Simple navigation bean
 * @author itcuties
 *
 */
@ManagedBean
@SessionScoped
public class NavigationBean implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6846568063302596960L;

    /**
     * Redirect to login page.
     * @return Login page name.
     */
    public String redirectToLogin() {
        return "/Login.xhtml?faces-redirect=true";
    }
     
    /**
     * Go to login page.
     * @return Login page name.
     */
    public String toLogin() {
        return "/secured/Welcome.xhtml?faces-redirect=true";
    }

	/**
	 * Redirect to welcome page.
	 * @return Welcome page name.
	 */
	public String redirectToWelcome() {
		return "/Home.xhtml";
	}

	/**
	 * Go to welcome page.
	 * @return Welcome page name.
	 */
	public String toWelcome() {
		return "/secured/Welcome.xhtml?faces-redirect=true";
	}

}