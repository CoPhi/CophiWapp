package it.cnr.ilc.cophi.action.management;

import it.cnr.ilc.cophi.model.Link;
import it.cnr.ilc.cophi.model.Reference;
import it.cnr.ilc.cophi.model.comment.Comment;
import it.cnr.ilc.cophi.model.handler.EntityManager;
import it.cnr.ilc.cophi.model.view.LinkViewEntity;
import it.cnr.ilc.cophi.model.view.TokenViewEntity;
import it.cnr.ilc.cophi.utils.Consts;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name="repository", eager=true)
@ApplicationScoped
public class RepositoryBean {
	EntityManager em = null;


	public RepositoryBean() {

		init();
	}

	void init(){

		em = EntityManager.getInstance();
	}


	public List<String> getArabicTokens() {

		return em.getArabicTokensAsList();
	}

	public List<String> getGreekTokens() {

		return em.getGreekTokensAsList();
	}

	//	public List<Pericope<Reference>> getPericopes() {
	//		return em.retriveAllPericopeAsList();
	//	}

	//	public List<String> getPericopesText(String lang) {
	//		
	//		List<Pericope<Reference>> lop = getPericopes();
	//		for (Iterator it = lop.iterator(); it.hasNext();) {
	//			Pericope<Reference> pericope = (Pericope<Reference>) it.next();
	//			List<Reference> tokens = pericope.getValue();
	//			for (Iterator it2 = tokens.iterator(); it2.hasNext();) {
	//				RefTokenText refTokenText = (RefTokenText) it2.next();
	//				System.err.println(refTokenText.getTok().getFrom() +  "-" + refTokenText.getTok().getTo());
	//			}
	//		}
	//		return null;
	//	}

	public List<LinkViewEntity> getLinks() {

		return em.getLinksAsList(Consts.ARABIC);
	}

	public List<LinkViewEntity> getLinks(String lang) {

		return em.getLinksAsList("arabic".equals(lang)?Consts.ARABIC:Consts.GREEK);
	}

	public Link<Reference> getLink (String id) {

		return em.getLink(id);
	}

	public List<Comment> getCommentsByLinkId (String linkId) {
		return em.getLinkComments (linkId);
	}

	public List<Comment> reloadAllComments (String linkId) {
		em.reloadAllComments();
		return getCommentsByLinkId(linkId);
	}

	public List<Comment> getOrderedCommentsByLinkId (String linkId) {
		return em.getOrderedLinkComments (linkId);
	}

	public boolean saveComment(Comment comment) {

		return em.saveComment(comment);
	}

	public boolean deleteComment(Comment comment) {

		return em.deleteComment(comment);
	}

	//	private HashMap<String,Link<Reference>> loadLinks() {
	//		return em.retriveAllLink();
	//	}

	public String getGreekPericopeDomAsString (){
		return em.pericopeDomAsString(Consts.GREEK);
	}

	public String getArabicPericopeDomAsString (){
		return ""; /*em.pericopeDomAsString(Consts.ARABIC)*/
	}

	public List<TokenViewEntity> getArabicTokenViewList() {
		return em.getListOfArabicTokenView();
	}

	public List<TokenViewEntity> getGreekTokenViewList() {
		return em.getListOfGreekTokenView();
	}

	public void addGreekClick(List<String> tokensId, String focusedPericopeId, List<String> listOfPericopesId) {

		em.handlePericope (tokensId, focusedPericopeId, listOfPericopesId, Consts.ADDTOPERICOPE, Consts.GREEK);
		em.saveAllPericopes();
	}

	public void removeGreekClick(List<String> tokensId, String focusedPericopeId, List<String> listOfPericopesId) {

		em.handlePericope (tokensId, focusedPericopeId,  listOfPericopesId, Consts.REMFROMPERICOPE, Consts.GREEK);
		em.saveAllPericopes();
	}

	public void shiftGreekClick(List<String> tokensId, String focusedPericopeId, List<String> listOfPericopesId) {

	//	em.handlePericope (tokensId, focusedPericopeId, listOfPericopesId, Consts.SHIFTTOPERICOPE, Consts.GREEK);
		//em.saveAllPericopes();
	}

	public void addArabicClick(List<String> tokensId, String focusedPericopeId, List<String> listOfPericopesId) {

		em.handlePericope (tokensId, focusedPericopeId, listOfPericopesId, Consts.ADDTOPERICOPE, Consts.ARABIC);
		em.saveAllPericopes();
	}

	public void removeArabicClick(List<String> tokensId, String focusedPericopeId, List<String> listOfPericopesId) {

		em.handlePericope (tokensId, focusedPericopeId,  listOfPericopesId, Consts.REMFROMPERICOPE, Consts.ARABIC);
		em.saveAllPericopes();
	}

	public void shiftArabicClick(List<String> tokensId, String focusedPericopeId, List<String> listOfPericopesId) {

		em.handlePericope (tokensId, focusedPericopeId, listOfPericopesId, Consts.SHIFTTOPERICOPE, Consts.ARABIC);
		em.saveAllPericopes();
	}

	public void reloadAfterPericopeModification() {
		em.reloadAfterPericopeModification();
	}

	public String getPericopeTextById(String pericopeId, int langId) {

		String text = null;
		switch (langId) {
		case Consts.GREEK:
			text = em.getGreekPericopes().get(pericopeId).getText(em.getGreekCharBuffer());
			break;

		case Consts.ARABIC:
			text = em.getArabicPericopes().get(pericopeId).getText(em.getArabicCharBuffer());
			break;

		default:
			break;
		}
		return text;
	}

}
