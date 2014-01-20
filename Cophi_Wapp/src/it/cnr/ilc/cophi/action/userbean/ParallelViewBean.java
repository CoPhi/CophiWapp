/**
 * 
 */
package it.cnr.ilc.cophi.action.userbean;

import it.cnr.ilc.cophi.action.management.RepositoryBean;
import it.cnr.ilc.cophi.model.Pericope;
import it.cnr.ilc.cophi.model.text.RefTokenText;
import it.cnr.ilc.cophi.model.view.LinkViewEntity;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;


/**
 * @author Angelo Del Grosso
 *
 */
@ManagedBean
@SessionScoped
public class ParallelViewBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8761677373617116582L;

	private static Logger logger = Logger.getLogger("GAlogger"); 

	@ManagedProperty(value="#{repository}")
	private RepositoryBean repositoryBean;

	/**
	 * @return the repositoryBean
	 */
	public RepositoryBean getRepositoryBean() {
		return repositoryBean;
	}

	/**
	 * @param repositoryBean the repositoryBean to set
	 */
	public void setRepositoryBean(RepositoryBean repositoryBean) {
		this.repositoryBean = repositoryBean;
	}

	/**
	 * 
	 */
	private Pericope<RefTokenText> selectedPericope;
	private String selectedContent;

	public ParallelViewBean() {
		// TODO Auto-generated constructor stub
		logger.fine("parallel");
	}


	public List<String> getArabicTokenText () {
		//System.err.println("getArabicTokenText()");
		return repositoryBean.getArabicTokens();
	}


	public List<LinkViewEntity> getLinks() {

		return repositoryBean.getLinks();
	}

	public List<LinkViewEntity> getLinks(String lang) {

		//System.err.println("getLinks() " + lang);
		return repositoryBean.getLinks(lang);
	}

	//	public ArrayList<Pericope<Reference>> getPericopesByArabic(){
	//		
	//		return (ArrayList<Pericope<Reference>>) repositoryBean.getPericopes
	//		
	//	}
	////	
	//	public ArrayList<Pericope> getPericopesByGreek(){
	//		
	//		return repositoryBean.getPericopesOrderedByGreek();
	//		
	//	}
	//
	///**
	// * @return the managerBean
	// */
	//	public ManagerBean getManagerBean() {
	//		return repositoryBean;
	//	}
	//
	///**
	// * @param managerBean the managerBean to set
	// */
	//	public void setManagerBean(ManagerBean managerBean) {
	//		this.repositoryBean = managerBean;
	//	}

	/**
	 * @return the selectedPericope
	 */
	public Pericope getSelectedPericope() {
		return selectedPericope;
	}

	/**
	 * @param selectedPericope the selectedPericope to set
	 */
	public void setSelectedPericope(Pericope selectedPericope) {
		this.selectedPericope = selectedPericope;
		logger.fine("select pericope in parallel");
	}

	/**
	 * @return the selectedContent
	 */
	public String getSelectedContent() {

		return selectedContent;
	}

	/**
	 * @param selectedContent the selectedContent to set
	 */
	public void setSelectedContent(String selectedContent) {
		this.selectedContent = selectedContent;
		logger.fine("selected content da table view");
	}


    public String logout() {
    	System.err.println("logout");
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "Home.xhtml?faces-redirect=true";
    }

}
