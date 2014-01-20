package it.cnr.ilc.cophi.action.userbean;


import it.cnr.ilc.cophi.action.management.RepositoryBean;
import it.cnr.ilc.cophi.model.Link;
import it.cnr.ilc.cophi.model.text.RefPericopeText;
import it.cnr.ilc.cophi.model.view.LinkViewEntity;
import it.cnr.ilc.cophi.model.view.TokenViewEntity;
import it.cnr.ilc.cophi.utils.Consts;
import it.cnr.ilc.cophi.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.event.data.PageEvent;

@ManagedBean
@SessionScoped
public class PericopeEditorController implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8195576909186725358L;
	//	private String arabicText = null;
	//	private String greekText = null;


	@ManagedProperty(value="#{repository}")
	private RepositoryBean repositoryBean;


	private HashMap<String, TokenViewEntity> selectedGreekTokenView = null;
	private HashMap<String, TokenViewEntity> selectedArabicTokenView = null;

	private TokenViewEntity leftArabicBoudary = null;
	private TokenViewEntity rightArabicBoudary = null;

	private TokenViewEntity leftGreekBoudary = null;
	private TokenViewEntity rightGreekBoudary = null;

	private LinkViewEntity selectedViewLink = null;

	private int paginatorPage = 0;
	private int rowsPerPage = 20;

	/**
	 * @return the selectedViewLink
	 */
	public LinkViewEntity getSelectedViewLink() {
		//System.err.println("getSelectedViewLink()");
		return selectedViewLink;
	}

	/**
	 * @param selectedViewLink the selectedViewLink to set
	 */
	public void setSelectedViewLink(LinkViewEntity selectedViewLink) {
		clearSelectedTokenView();
		//System.err.println("setSelectedViewLink()");

		this.selectedViewLink = selectedViewLink;
	}

	/**
	 * @return the selectedGreekTokenView
	 */
	public HashMap<String, TokenViewEntity> getSelectedGreekTokenView() {
		return selectedGreekTokenView;
	}

	/**
	 * @param selectedGreekTokenView the selectedGreekTokenView to set
	 */
	public void setSelectedGreekTokenView(
			HashMap<String, TokenViewEntity> selectedGreekTokenView) {
		this.selectedGreekTokenView = selectedGreekTokenView;
	}

	/**
	 * @return the selectedArabicTokenView
	 */
	public HashMap<String, TokenViewEntity> getSelectedArabicTokenView() {
		return selectedArabicTokenView;
	}

	/**
	 * @param selectedArabicTokenView the selectedArabicTokenView to set
	 */
	public void setSelectedArabicTokenView(
			HashMap<String, TokenViewEntity> selectedArabicTokenView) {
		this.selectedArabicTokenView = selectedArabicTokenView;
	}	

	/**
	 * @return the arabicText
	 */
	public List<TokenViewEntity> getArabicTokenViewList() {

		/* Spostato la creazione dello style da jsf a java
		 * styleClass="link_nodecor bk_#{pericopeEditorController.isArabicTokenViewToList(w)?'7':w.color eq -1?9:w.pericopeId eq pericopeEditorController.selectedArabicPericopeId?'arfocused':w.color%4} arabic"
		 */

		List<TokenViewEntity> lotve = new ArrayList<TokenViewEntity>();
		String selectedPericope = getSelectedArabicPericopeId();
		if (selectedPericope.contains(".")) {
			int pageOfSelPer = Integer.parseInt(selectedPericope.substring(0, selectedPericope.indexOf(".")));
			for (TokenViewEntity tok : repositoryBean.getArabicTokenViewList()) {

				if ((pageOfSelPer - 2 < tok.getHisPage() ) && ( tok.getHisPage() < pageOfSelPer + 2)) {
					String style = null;
					if (isArabicTokenViewToList(tok)) {
						style = "7";
					} else if (tok.getColor() == -1) {
						style = "9";
					} else if (tok.getPericopeId().equals(selectedPericope)){
						style = "arfocused";
					} else {
						style = Integer.toString(tok.getColor()%4);
					}
					tok.setStyle(style);
					lotve.add(tok);
				}
			}
		}

		return lotve;

		//	return repositoryBean.getArabicTokenViewList();
	}

	/**
	 * @param arabicText the arabicText to set
	 */
	public void setArabicTokenViewList(String arabicText) {

		//System.err.println("Arabic: " + arabicText);
	}

	/**

	/**
	 * @return the greekText
	 */
	public List<TokenViewEntity> getGreekTokenViewList() {

		//forse migliora calcolando qua il colore
		/*
		 * styleClass="link_nodecor bk_#{pericopeEditorController.isGreekTokenViewToList(w)?'7'
		 * :w.color eq -1?9:w.pericopeId eq pericopeEditorController.selectedGreekPericopeId?'grfocused':w.color%4} greek">
		 */

		// styleClass="link_nodecor bk_#{pericopeEditorController.isGreekTokenViewToList(w)?'7':w.color eq -1?9:w.pericopeId eq pericopeEditorController.selectedGreekPericopeId?'grfocused':w.color%4} greek"


		List<TokenViewEntity> lotve = new ArrayList<TokenViewEntity>();
		String selectedPericope = getSelectedGreekPericopeId();
		if (selectedPericope.contains(".")) {
			selectedPericope = selectedPericope.substring(0, selectedPericope.indexOf("."));
		}
		int pericopeNumb = Integer.parseInt(selectedPericope);

		for (TokenViewEntity tok : repositoryBean.getGreekTokenViewList()) {
			if ((pericopeNumb -20 < tok.getHisPage() ) && ( tok.getHisPage()< pericopeNumb + 20)) {

				String style = null;
				if (isGreekTokenViewToList(tok)) {
					style = "7";
				} else if (tok.getColor() == -1) {
					style = "9";
				} else if (tok.getPericopeId().equals(getSelectedGreekPericopeId())){
					style = "grfocused";
				} else {
					style = Integer.toString(tok.getColor()%4);
				}
				tok.setStyle(style);
				lotve.add(tok);
			}
		}

		return lotve;

		//	return repositoryBean.getGreekTokenViewList();
		//return "<span class=\"pericope\" rel=\"12\" id=\"2\"><span class=\"token\" rel=\"12\" id=\"1\">angelo</span><span class=\"token\">&nbsp;&nbsp;&nbsp;</span><span class=\"token\" rel=\"12\" id=\"2\">mario</span></span>";
		//return repositoryBean.getGreekPericopeDomAsString();
	}

	/**
	 * @param greekText the greekText to set
	 */
	public void setGreekTokenViewList(String greekText) {
		//System.err.println("Greek: " + greekText);
	}

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

	public boolean isGreekTokenViewToList(TokenViewEntity tve){

		boolean ret = false;
		if (null != getSelectedGreekTokenView()) {
			ret = getSelectedGreekTokenView().containsKey(Integer.toString(tve.getFrom()));
		}
		return ret;

	}
	public boolean isArabicTokenViewToList(TokenViewEntity tve){

		boolean ret = false;
		if (null != getSelectedArabicTokenView()) {
			ret = getSelectedArabicTokenView().containsKey(Integer.toString(tve.getFrom()));
		}
		return ret;

	}
	public void setGreekTokenViewToList(TokenViewEntity tve){

		if (null == getSelectedGreekTokenView()) {
			setSelectedGreekTokenView(new HashMap<String, TokenViewEntity>());
		}
		if (!selectedGreekTokenView.containsKey(Integer.toString(tve.getFrom()))){
			selectedGreekTokenView.put(Integer.toString(tve.getFrom()), tve);
		} else {
			selectedGreekTokenView.remove(Integer.toString(tve.getFrom()));
		}
	}

	public void setArabicTokenViewToList(TokenViewEntity tve){

		if (null == getSelectedArabicTokenView()) {
			setSelectedArabicTokenView(new HashMap<String, TokenViewEntity>());
		}
		if (!selectedArabicTokenView.containsKey(Integer.toString(tve.getFrom()))){
			selectedArabicTokenView.put(Integer.toString(tve.getFrom()), tve);
		} else {
			selectedArabicTokenView.remove(Integer.toString(tve.getFrom()));
		}
	}

	/**
	 * @return the leftArabicBoudary
	 */
	public TokenViewEntity getLeftArabicBoudary() {
		return leftArabicBoudary;
	}

	/**
	 * @param leftArabicBoudary the leftArabicBoudary to set
	 */
	public void setLeftArabicBoudary(TokenViewEntity leftArabicBoudary) {
		this.leftArabicBoudary = leftArabicBoudary;
	}

	/**
	 * @return the rightArabicBoudary
	 */
	public TokenViewEntity getRightArabicBoudary() {
		return rightArabicBoudary;
	}

	/**
	 * @param rightArabicBoudary the rightArabicBoudary to set
	 */
	public void setRightArabicBoudary(TokenViewEntity rightArabicBoudary) {
		this.rightArabicBoudary = rightArabicBoudary;
	}

	/**
	 * @return the leftGreekBoudary
	 */
	public TokenViewEntity getLeftGreekBoudary() {
		return leftGreekBoudary;
	}

	/**
	 * @param leftGreekBoudary the leftGreekBoudary to set
	 */
	public void setLeftGreekBoudary(TokenViewEntity leftGreekBoudary) {
		this.leftGreekBoudary = leftGreekBoudary;
	}

	/**
	 * @return the rightGreekBoudary
	 */
	public TokenViewEntity getRightGreekBoudary() {
		return rightGreekBoudary;
	}

	/**
	 * @param rightGreekBoudary the rightGreekBoudary to set
	 */
	public void setRightGreekBoudary(TokenViewEntity rightGreekBoudary) {
		this.rightGreekBoudary = rightGreekBoudary;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSelectedGreekPericopeId() {

		return ((Link<RefPericopeText>)(getSelectedViewLink().getLink())).getValue().get(Consts.GREEK).getRef();
	}

	public String getSelectedArabicPericopeId() {
		return ((Link<RefPericopeText>)(getSelectedViewLink().getLink())).getValue().get(Consts.ARABIC).getRef();
	}


	/**
	 * @return the rowsPerPage
	 */
	public int getRowsPerPage() {
		return rowsPerPage;
	}

	/**
	 * @param rowsPerPage the rowsPerPage to set
	 */
	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}

	public int getPaginatorPage() {

		return paginatorPage;
	}

	public void setPaginatorPage(PageEvent event) {

		int id = event.getPage();
		paginatorPage = id;
	}


	private List<String> getSelectedTokenViewId (int langId) {

		List<String> clickedTokenViewId = null;
		HashMap<String,TokenViewEntity> tveHM = null;
		switch (langId) {
		case Consts.GREEK:
			tveHM = getSelectedGreekTokenView();
			break;
		case Consts.ARABIC:
			tveHM = getSelectedArabicTokenView();
			break;

		default:
			break;
		}
		if (null != tveHM) {
			clickedTokenViewId = new ArrayList<String>();
			for (TokenViewEntity token : tveHM.values()) {
				clickedTokenViewId.add(token.getId());
			}
		}
		return clickedTokenViewId;
	}
	private List<String> getSelectedPericopeId (String excludePericopeId, int langId) {

		List<String> clickedPericopeId = null;
		HashMap<String,TokenViewEntity> tveHM = null;
		switch (langId) {
		case Consts.GREEK:
			tveHM = getSelectedGreekTokenView();
			break;
		case Consts.ARABIC:
			tveHM = getSelectedArabicTokenView();
			break;

		default:
			break;
		}
		if (null != tveHM) {
			clickedPericopeId = new ArrayList<String>();
			for (TokenViewEntity token : tveHM.values()) {
				String id = token.getPericopeId();
				if (!id.equals(excludePericopeId)) {
					if (!clickedPericopeId.contains(token.getPericopeId())) {
						clickedPericopeId.add(token.getPericopeId());
					}
				}
			}
		}
		return clickedPericopeId;
	}

	/*	private List<TokenViewEntity> orderAndCheckTokenView (HashMap<String, TokenViewEntity> hm) {
		List<TokenViewEntity> list = null;
		List<TokenViewEntity> newList = null;
		if (null != hm) {
			list = new ArrayList<TokenViewEntity>(hm.values());
			Collections.sort(list,CophiSort.TOKENVIEW_FROM_ORDER);
			Iterator iterator = list.iterator(); 
			TokenViewEntity prec = null;
			TokenViewEntity tokenViewEntity = null;
			while (iterator.hasNext()){
				if ( null != prec){
					tokenViewEntity = (TokenViewEntity) iterator.next();
					if (tokenViewEntity.getPrec().equals(prec)) {
						newList.add(prec);
					}
				}
			}
		}
		return list;
	}*/

	private void clearSelectedGreekTokenView() {
		if (null != selectedGreekTokenView) {
			selectedGreekTokenView.clear();
			selectedGreekTokenView = null;
		}

	}
	public void clearSelectedArabicTokenView() {
		if (null != selectedArabicTokenView) { 
			selectedArabicTokenView.clear();
			selectedArabicTokenView = null;
		}
	}

	public void clearSelectedTokenView() {
		clearSelectedGreekTokenView();
		clearSelectedArabicTokenView();
	}


	/* Azioni dei bottoni */

	public void addGreekClick() {

		List<String> clickedTokenId = getSelectedTokenViewId(Consts.GREEK);
		List<String> clickedPericopeId = getSelectedPericopeId(getSelectedGreekPericopeId(), Consts.GREEK);

		repositoryBean.addGreekClick(clickedTokenId, getSelectedGreekPericopeId(), clickedPericopeId);
		repositoryBean.reloadAfterPericopeModification();
		clearSelectedGreekTokenView();
	}

	public void removeGreekClick() {
		List<String> clickedTokenId = getSelectedTokenViewId(Consts.GREEK);
		List<String> clickedPericopeId = getSelectedPericopeId(null, Consts.GREEK);

		repositoryBean.removeGreekClick(clickedTokenId, null, clickedPericopeId);
		repositoryBean.reloadAfterPericopeModification();
		clearSelectedGreekTokenView();
	}

	public void shiftGreekClick() {

		List<String> clickedTokenId = getSelectedTokenViewId(Consts.GREEK);
		List<String> clickedPericopeId = getSelectedPericopeId(getSelectedGreekPericopeId(), Consts.GREEK);
		//se clicked non e' la focused => e' come la add to focused seguita da una remove from clicked
		//se clicked e' la focused => e' come la add to before/after seguita da una remove from focued/clicked
		repositoryBean.shiftGreekClick(clickedTokenId, getSelectedGreekPericopeId(), clickedPericopeId);
		repositoryBean.reloadAfterPericopeModification();
		clearSelectedGreekTokenView();
	}


	public void addArabicClick() {

		List<String> clickedTokenId = getSelectedTokenViewId(Consts.ARABIC);
		List<String> clickedPericopeId = getSelectedPericopeId(getSelectedArabicPericopeId(), Consts.ARABIC);

		repositoryBean.addArabicClick(clickedTokenId, getSelectedArabicPericopeId(), clickedPericopeId);
		repositoryBean.reloadAfterPericopeModification();
		clearSelectedArabicTokenView();
	}

	public void removeArabicClick() {
		List<String> clickedTokenId = getSelectedTokenViewId(Consts.ARABIC);
		List<String> clickedPericopeId = getSelectedPericopeId(null, Consts.ARABIC);

		repositoryBean.removeArabicClick(clickedTokenId, null, clickedPericopeId);
		//	repositoryBean.reloadAfterPericopeModification();
		//	clearSelectedArabicTokenView();
	}

	public void shiftArabicClick() {



	}


	public void dblClickOnToken() {
		System.err.println("dblClick");
		Utils.addInfoMessageToContext("dblClick", "onDebug");
	}
	public void clickOnToken(ActionEvent event) {
		System.err.println("click");
		Utils.addInfoMessageToContext("click", "onDebug");
	}


}
