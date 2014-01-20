package it.cnr.ilc.cophi.model.handler;
import it.cnr.ilc.cophi.action.controller.content.CommentContentBehaviour;
import it.cnr.ilc.cophi.action.controller.content.ContextContent;
import it.cnr.ilc.cophi.action.controller.content.DocumentTextContentBehaviour;
import it.cnr.ilc.cophi.action.controller.content.LinkContentBehaviour;
import it.cnr.ilc.cophi.action.controller.content.PericopeTextContentBehaviour;
import it.cnr.ilc.cophi.action.controller.resource.ContextResource;
import it.cnr.ilc.cophi.action.controller.resource.ResourceBehaviour;
import it.cnr.ilc.cophi.action.controller.resource.XMLResourceBehaviour;
import it.cnr.ilc.cophi.model.Factory;
import it.cnr.ilc.cophi.model.Link;
import it.cnr.ilc.cophi.model.Reference;
import it.cnr.ilc.cophi.model.comment.Comment;
import it.cnr.ilc.cophi.model.text.PericopeText;
import it.cnr.ilc.cophi.model.text.RefPericopeText;
import it.cnr.ilc.cophi.model.text.RefTokenText;
import it.cnr.ilc.cophi.model.text.TokenText;
import it.cnr.ilc.cophi.model.view.LinkViewEntity;
import it.cnr.ilc.cophi.model.view.TokenViewEntity;
import it.cnr.ilc.cophi.utils.CophiSort;
import it.cnr.ilc.cophi.utils.Consts;
import it.cnr.ilc.cophi.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.sound.midi.SysexMessage;

import org.jdom2.Document;

/**
 * 
 * @author simone
 *
 */
public class EntityManager {

	private static EntityManager instance;

	CharSequence greekCharBuffer = null;
	CharSequence arabicCharBuffer = null;

	private ContextResource arabicCr = null;
	private ContextResource greekCr = null;

	private ContextResource linkCr = null;
	private ContextResource commentCr = null;

	private HashMap<String,Link<RefPericopeText>> links = null;

	private HashMap<String,Comment> comments = null;
	private HashMap<String, List<Comment>> commentsByLink = null;

	private HashMap<String,PericopeText> arabicPericopes = null;
	private HashMap<String,PericopeText> greekPericopes = null;

	private HashMap<String,TokenText> greekTokens = null;
	private HashMap<String,TokenText> arabicTokens = null;

	private Document greekPericopesDom = null;
	private Document arabicPericopesDom = null;

	private List<TokenViewEntity> listOfArabicTokenView = null;
	private List<TokenViewEntity> listOfGreekTokenView = null;

	private HashMap<String, String> arabicTokenToPericope = null;
	private HashMap<String, String> greekTokenToPericope = null;

	private HashMap<String, String> pericopeToLink = null;
	
	private String dbName = "new_GA"; 

	/**
	 * @return the dbName
	 */
	public String getDbName() {
		return dbName;
	}

	/**
	 * @param dbName the dbName to set
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * @return the arabicTokens
	 */
	public HashMap<String, TokenText> getArabicTokens() {
		return arabicTokens;
	}

	/**
	 * @param arabicTokens the arabicTokens to set
	 */
	public void setArabicTokens(HashMap<String, TokenText> arabicTokens) {
		this.arabicTokens = arabicTokens;
	}

	/**
	 * @return the arabicPericopes
	 */
	public HashMap<String, PericopeText> getArabicPericopes() {
		return arabicPericopes;
	}

	/**
	 * @param arabicPericopes the arabicPericopes to set
	 */
	public void setArabicPericopes(
			HashMap<String, PericopeText> arabicPericopes) {
		this.arabicPericopes = arabicPericopes;
	}

	/**
	 * @return the greekTokens
	 */
	public HashMap<String, TokenText> getGreekTokens() {
		return greekTokens;
	}

	/**
	 * @param greekTokens the greekTokens to set
	 */
	public void setGreekTokens(HashMap<String, TokenText> greekTokens) {
		this.greekTokens = greekTokens;
	}

	/**
	 * @return the greekPericopes
	 */
	public HashMap<String, PericopeText> getGreekPericopes() {
		return greekPericopes;
	}

	/**
	 * @param greekPericopes the greekPericopes to set
	 */
	public void setGreekPericopes(
			HashMap<String, PericopeText> greekPericopes) {
		this.greekPericopes = greekPericopes;
	}

	/**
	 * @return the links
	 */
	public HashMap<String, Link<RefPericopeText>> getLinks() {
		return links;
	}

	/**
	 * @param links the links to set
	 */
	public void setLinks(HashMap<String, Link<RefPericopeText>> links) {
		this.links = links;
	}

	/**
	 * @return the greekCharBuffer
	 */
	public CharSequence getGreekCharBuffer() {
		return greekCharBuffer;
	}

	/**
	 * @param greekCharBuffer the greekCharBuffer to set
	 */
	public void setGreekCharBuffer(CharSequence greekCharBuffer) {
		this.greekCharBuffer = greekCharBuffer;
	}

	/**
	 * @return the arabicCharBuffer
	 */
	public CharSequence getArabicCharBuffer() {
		return arabicCharBuffer;
	}

	/**
	 * @param arabicCharBuffer the arabicCharBuffer to set
	 */
	public void setArabicCharBuffer(CharSequence arabicCharBuffer) {
		this.arabicCharBuffer = arabicCharBuffer;
	}

	/**
	 * @return the commentCr
	 */
	public ContextResource getCommentCr() {
		return commentCr;
	}

	/**
	 * @param commentCr the commentCr to set
	 */
	public void setCommentCr(ContextResource commentCr) {
		this.commentCr = commentCr;
	}

	/**
	 * @return the comments
	 */
	public HashMap<String, Comment> getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(HashMap<String, Comment> comments) {
		this.comments = comments;
	}

	/**
	 * @return the commentsByLink
	 */
	public HashMap<String, List<Comment>> getCommentsByLink() {
		return commentsByLink;
	}

	/**
	 * @param commentsByLink the commentsByLink to set
	 */
	public void setCommentsByLink(HashMap<String, List<Comment>> commentsByLink) {
		this.commentsByLink = commentsByLink;
	}

	/**
	 * @return the greekPericopesDom
	 */
	public Document getGreekPericopesDom() {
		return greekPericopesDom;
	}

	/**
	 * @param greekPericopesDom the greekPericopesDom to set
	 */
	public void setGreekPericopesDom(Document greekPericopesDom) {
		this.greekPericopesDom = greekPericopesDom;
	}

	/**
	 * @return the arabicPericopesDom
	 */
	public Document getArabicPericopesDom() {
		return arabicPericopesDom;
	}

	/**
	 * @param arabicPericopesDom the arabicPericopesDom to set
	 */
	public void setArabicPericopesDom(Document arabicPericopesDom) {
		this.arabicPericopesDom = arabicPericopesDom;
	}

	/**
	 * @return the listOfArabicTokenView
	 */
	public List<TokenViewEntity> getListOfArabicTokenView() {
		return listOfArabicTokenView;
	}

	/**
	 * @param listOfArabicTokenView the listOfArabicTokenView to set
	 */
	public void setListOfArabicTokenView(List<TokenViewEntity> listOfArabicTokenView) {
		this.listOfArabicTokenView = listOfArabicTokenView;
	}

	/**
	 * @return the listOfGreekTokenView
	 */
	public List<TokenViewEntity> getListOfGreekTokenView() {
		return listOfGreekTokenView;
	}

	/**
	 * @param listOfGreekTokenView the listOfGreekTokenView to set
	 */
	public void setListOfGreekTokenView(List<TokenViewEntity> listOfGreekTokenView) {
		this.listOfGreekTokenView = listOfGreekTokenView;
	}

	/**
	 * @return the arabicTokenToPericope
	 */
	public HashMap<String, String> getArabicTokenToPericope() {
		return arabicTokenToPericope;
	}

	/**
	 * @param arabicTokenToPericope the arabicTokenToPericope to set
	 */
	public void setArabicTokenToPericope(
			HashMap<String, String> arabicTokenToPericope) {
		this.arabicTokenToPericope = arabicTokenToPericope;
	}

	/**
	 * @return the greekTokenToPericope
	 */
	public HashMap<String, String> getGreekTokenToPericope() {
		return greekTokenToPericope;
	}

	/**
	 * @param greekTokenToPericope the greekTokenToPericope to set
	 */
	public void setGreekTokenToPericope(HashMap<String, String> greekTokenToPericope) {
		this.greekTokenToPericope = greekTokenToPericope;
	}


	/**
	 * @return the pericopeToLink
	 */
	public HashMap<String, String> getPericopeToLink() {
		return pericopeToLink;
	}

	/**
	 * @param pericopeToLink the pericopeToLink to set
	 */
	public void setPericopeToLink(HashMap<String, String> pericopeToLink) {
		this.pericopeToLink = pericopeToLink;
	}

	/**
	 * @return the tokens
	 */


	private EntityManager () {
		init();
	}

	public static EntityManager getInstance() {
		if (instance == null) {
			synchronized(EntityManager.class) {
				if (instance == null) {
					instance = new EntityManager();
				}
			}
		}

		return instance;
	}

	private void init() {

		String root_path = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/");

		try {
			greekCharBuffer = Utils.fromFile (root_path + "resources/greek.txt");
			arabicCharBuffer = Utils.fromFile (root_path + "resources/arabic.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setArabicCr(createContextResouce(new XMLResourceBehaviour(), getDbName()  + "/doc/bada/"));
		setGreekCr(createContextResouce(new XMLResourceBehaviour(), getDbName() +  "/doc/plot/"));
		setLinkCr(createContextResouce(new XMLResourceBehaviour(), getDbName() +  "/link"));

		setCommentCr(createContextResouce(new XMLResourceBehaviour(), getDbName() +  "/comment"));

		setArabicTokens(retriveAllArabicToken()); 
		setGreekTokens(retriveAllGreekToken());

		setArabicPericopes(retrieveArabicPericope());
		setGreekPericopes(retrieveGreekPericope());

		setLinks(retrieveAllLink(getArabicPericopes(), getGreekPericopes()));

		setComments(retrieveAllComments());

		setCommentsByLink(Utils.commentsGroupByLink(getComments()));


		//	loadPericopesAsDselectom();

		setArabicTokenToPericope(Utils.createToken2PericopeHM(getArabicPericopes()));
		setGreekTokenToPericope(Utils.createToken2PericopeHM(getGreekPericopes()));
		setPericopeToLink(Utils.createPericope2LinkHM(getLinks()));

		setListOfArabicTokenView(Utils.createTokenViewList (getArabicTokens(), getArabicPericopes(), getLinks(), getArabicTokenToPericope(), getPericopeToLink(), Consts.ARABIC, getArabicCharBuffer()));
		setListOfGreekTokenView(Utils.createTokenViewList (getGreekTokens(), getGreekPericopes(), getLinks(), getGreekTokenToPericope(), getPericopeToLink(), Consts.GREEK, getGreekCharBuffer()));


	}

	public void reloadAfterPericopeModification() {

		setArabicCr(createContextResouce(new XMLResourceBehaviour(), getDbName() + "/doc/bada/"));
		setGreekCr(createContextResouce(new XMLResourceBehaviour(), getDbName() + "/doc/plot/"));

		setArabicPericopes(retrieveArabicPericope());
		setGreekPericopes(retrieveGreekPericope());
		setLinks(retrieveAllLink(getArabicPericopes(), getGreekPericopes()));


		setArabicTokenToPericope(Utils.createToken2PericopeHM(getArabicPericopes()));
		setGreekTokenToPericope(Utils.createToken2PericopeHM(getGreekPericopes()));
		setPericopeToLink(Utils.createPericope2LinkHM(getLinks()));

		setListOfArabicTokenView(Utils.createTokenViewList (getArabicTokens(), getArabicPericopes(), getLinks(), getArabicTokenToPericope(), getPericopeToLink(), Consts.ARABIC, getArabicCharBuffer()));
		setListOfGreekTokenView(Utils.createTokenViewList (getGreekTokens(), getGreekPericopes(), getLinks(), getGreekTokenToPericope(), getPericopeToLink(), Consts.GREEK, getGreekCharBuffer()));
	}

	public void saveAllPericopes() {

		savePericopes(new ArrayList<PericopeText>(getArabicPericopes().values()), "badaoui", getArabicCr(), getDbName() + "/doc/bada/", "pericopes.xml");
		savePericopes(new ArrayList<PericopeText>(getGreekPericopes().values()), "plotino", getGreekCr(), getDbName() + "/doc/plot/", "pericopes.xml");
	}


	private ContextResource createContextResouce(ResourceBehaviour rb, String dbPath) {
		ContextResource cr = Factory.getInstanceContextResource();
		cr.setResourceBehaviourType(rb);
		cr.loadResources(dbPath);
		return cr;
	}


	//tokens
	public HashMap<String,TokenText> retriveAllArabicToken () {

		return retrieveAllToken(getArabicCr()); 
	}

	public HashMap<String,TokenText> retriveAllGreekToken () {

		return retrieveAllToken(getGreekCr()); 
	}

	private HashMap<String,TokenText> retrieveAllToken (ContextResource cr) {

		ContextContent<TokenText> cc = Factory.getContextContentInstance("TokenText");
		cc.setContentType(new DocumentTextContentBehaviour(cr.retrieveContent("tokens.xml")));
		return cc.getValue();
	}

	//pericopes
	public HashMap<String,PericopeText> retrieveArabicPericope () {
		return retrieveAllPericope (getArabicCr(), getArabicTokens());
	}

	public HashMap<String,PericopeText> retrieveGreekPericope () {
		return retrieveAllPericope (getGreekCr(), getGreekTokens());
	}


	private HashMap<String,PericopeText> retrieveAllPericope (ContextResource cr, HashMap<String, TokenText> tokens) {

		ContextContent<PericopeText> cc = Factory.getContextContentInstance("PericopeText");
		PericopeTextContentBehaviour ptcb = new PericopeTextContentBehaviour(cr.retrieveContent("pericopes.xml"));
		ptcb.setTokens(tokens);
		cc.setContentType(ptcb);

		return cc.getValue();
	}

	//links
	public HashMap<String,Link<RefPericopeText>> retrieveAllLink (HashMap<String, PericopeText> pericopesAr, HashMap<String, PericopeText> pericopesGr) {

		ContextContent<Link<RefPericopeText>> cc = Factory.getContextContentInstance("Link");
		LinkContentBehaviour lcb = new LinkContentBehaviour(getLinkCr().retrieveContent("links.xml"));
		HashMap<String, PericopeText> pericopes = new HashMap<String, PericopeText>();
		pericopes.putAll(pericopesGr);
		pericopes.putAll(pericopesAr);
		lcb.setPericopes(pericopes);
		cc.setContentType(lcb);

		return cc.getValue();
	}

	private HashMap<String, Comment> retrieveAllComments() {
		ContextContent<Comment> cc = Factory.getContextContentInstance("Comment");
		CommentContentBehaviour ccb = new CommentContentBehaviour();
		ccb.setResources(getCommentCr().getResources());
		cc.setContentType(ccb);

		return cc.getValue();
	}


	public void retriveAllComment () {

	}

	/**
	 * @return the arabicCr
	 */
	public ContextResource getArabicCr() {
		return arabicCr;
	}

	/**
	 * @param arabicCr the arabicCr to set
	 */
	public void setArabicCr(ContextResource arabicCr) {
		this.arabicCr = arabicCr;
	}

	/**
	 * @return the greekCr
	 */
	public ContextResource getGreekCr() {
		return greekCr;
	}

	/**
	 * @param greekCr the greekCr to set
	 */
	public void setGreekCr(ContextResource greekCr) {
		this.greekCr = greekCr;
	}

	public Link<Reference> getLink(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the linkCr
	 */
	public ContextResource getLinkCr() {
		return linkCr;
	}

	/**
	 * @param linkCr the linkCr to set
	 */
	public void setLinkCr(ContextResource linkCr) {
		this.linkCr = linkCr;
	}

	public List<String> getArabicTokensAsList() {

		ArrayList<String> al = new ArrayList<String>();

		for (TokenText tt : getArabicTokens().values()) {
			al.add(tt.getText(getArabicCharBuffer()));
		}

		return al;
	}

	public List<String> getGreekTokensAsList() {

		ArrayList<String> al = new ArrayList<String>();

		for (TokenText tt : getGreekTokens().values()) {
			al.add(tt.getText(getGreekCharBuffer()));
		}

		return al;
	}

	public List<LinkViewEntity> getLinksAsList(int langId) {

		List<LinkViewEntity> lvel = new ArrayList<LinkViewEntity>();
		for (Link<RefPericopeText> link : getLinks().values()) {

			LinkViewEntity lve = new LinkViewEntity();
			lve.setLink(link);

			RefPericopeText grRpt = link.getValue().get(0);
			PericopeText grPericope = (PericopeText) grRpt.getPericope();
			if (null != grPericope) {

				lve.setGreekPericopeInfo(grPericope.getInfo());
				lve.setGreekPericopeText(grPericope.getText(greekCharBuffer));
				lve.setGreekPericopeRefId(grRpt.getRef());
			} else {
				System.err.println("Pericope greca non trovata in links.xml! " + grRpt.getId() + " ref " + grRpt.getRef());
			}
			RefPericopeText arRpt = link.getValue().get(1);
			PericopeText arPericope = (PericopeText) arRpt.getPericope();
			if (null != arPericope) {
				lve.setArabicPericopeInfo(arPericope.getInfo());
				lve.setArabicPericopeText(arPericope.getText(arabicCharBuffer));
				lve.setArabicPericopeRefId(arRpt.getRef());
			} else {
				System.err.println("Pericope araba non trovata in links.xml! " + arRpt.getId() + " ref " + arRpt.getRef());
			}

			lvel.add(lve);
		}
		switch (langId) {
		case Consts.GREEK:
			Collections.sort(lvel, CophiSort.GREEKLINKVIEW_FROM_ORDER);
			break;
		case Consts.ARABIC:
			Collections.sort(lvel, CophiSort.ARABICLINKVIEW_FROM_ORDER);
			break;

		default:
			break;
		}

		return lvel;
	}

	public List<Comment> getLinkComments(String linkId) {

		return getCommentsByLink().get(linkId);
	}

	public List<Comment> getOrderedLinkComments(String linkId) {

		List<Comment> unsorted = getLinkComments(linkId);
		if (null != unsorted) {
			Collections.sort(unsorted, CophiSort.COMMENT_ID_ORDER);
		}
		return unsorted;
	}

	public boolean saveComment(Comment comment) {
		// TODO Auto-generated method stub
		ContextContent<Comment> cc = Factory.getContextContentInstance("Comment");
		CommentContentBehaviour ccb = new CommentContentBehaviour();
		cc.setContentType(ccb);
		cc.contentToJDOM(comment);
		ContextResource crc = getCommentCr();
		return crc.store(cc.getDocument(), getDbName() + "/comment", Utils.createFilename(comment));

	}

	public boolean deleteComment(Comment comment) {
		// TODO Auto-generated method stub

		ContextResource crc = getCommentCr();
		return crc.remove(getDbName() + "/comment", comment.getXmlFileName());


	}
		
	
	public void reloadAllComments() {
		setCommentCr(createContextResouce(new XMLResourceBehaviour(), getDbName() + "/comment"));
		setComments(retrieveAllComments());
		setCommentsByLink(Utils.commentsGroupByLink(getComments()));
	}

	/*
	public void loadPericopesAsDom () {

		setGreekPericopesDom(Utils.createPericopeDom (getLinks(), Consts.GREEK, getGreekCharBuffer()));
		setArabicPericopesDom(Utils.createPericopeDom (getLinks(), Consts.ARABIC, getArabicCharBuffer())); 
	}
	 */
	public String pericopeDomAsString (int langId) {

		String ret = null;
		switch (langId) {
		case Consts.GREEK:
			ret = Utils.JDOMtoString(getGreekPericopesDom());
			break;

		case Consts.ARABIC:
			ret = Utils.JDOMtoString(getArabicPericopesDom());
			break;

		default:
			break;
		}
		return ret;
	}


	public void handlePericope (List<String> tokensId, String focusedPericopeId, List<String> listOfPericopesId, int action, int langId){

		switch (action) {
		case Consts.ADDTOPERICOPE:
			removeFromPericopes (tokensId, listOfPericopesId, langId);
			addToPericope(tokensId, focusedPericopeId, langId);
			break;

		case Consts.REMFROMPERICOPE:
			//listOfPericopesId.add(focusedPericopeId);
			removeFromPericopes (tokensId, listOfPericopesId, langId);
			break;

		case Consts.SHIFTTOPERICOPE:
			removeFromPericope (tokensId, focusedPericopeId, langId);
			addToPericope(tokensId, listOfPericopesId.get(0), langId);
			break;
		default:
			break;
		}

	}

	private List<PericopeText> getPericopesByIDAndLang (List<String> ids, int langId) {

		List<PericopeText> lopt = new ArrayList<PericopeText>();
		for (String id : ids) {
			lopt.add(getPericopeByIDAndLang (id, langId));
		}

		return lopt;
	}



	private PericopeText getPericopeByIDAndLang (String id, int langId) {

		PericopeText pericope = null;

		switch (langId) {
		case Consts.GREEK:
			pericope = getGreekPericopes().get(id);
			break;
		case Consts.ARABIC:
			pericope = getArabicPericopes().get(id);
			break;

		default:
			break;
		}

		return pericope;
	}

	private HashMap<String, Reference> convertReferenceListToHashMap (List<Reference> list) {


		HashMap<String,Reference> map = new HashMap<String, Reference>();
		for (Reference i : list) {
			map.put(i.getRef(), i);
		}

		return map;

	}
	private void removeFromPericopes(List<String> tokensId,
			List<String> listOfPericopesId, int langId) {

		if (null != listOfPericopesId)  {
			List<PericopeText> lopt = getPericopesByIDAndLang(listOfPericopesId, langId);

			for (PericopeText pericope : lopt) {
				if (null != pericope)
					pericope.setValue(removeFromPericope(tokensId, pericope.getId(), langId));
			}
		}
	}

	private List<Reference> removeFromPericope(List<String> tokensId, String pericopesId, int langId) {

		PericopeText pericope = getPericopeByIDAndLang(pericopesId, langId);
		List<Reference> newList = new ArrayList<Reference>();
		for (Reference ref : pericope.getValue()) {

			if (!tokensId.contains(ref.getRef())) {
				newList.add(ref);

			} else {
				//.err.println("removeFromPericope(): da eliminare!!!");
			}
		}
		return newList;

	}

	private TokenText getTokenByIDAndLang (String tokenId, int langId) {

		TokenText ret = null;
		switch (langId) {
		case Consts.GREEK:
			ret = getGreekTokens().get(tokenId);
			break;
		case Consts.ARABIC:
			ret = getArabicTokens().get(tokenId);
			break;

		default:
			break;
		}
		return ret;
	}

	private void addToPericope(List<String> tokensId, String focusedPericopeId, int langId) {

		PericopeText pericope = getPericopeByIDAndLang(focusedPericopeId, langId);

		List<Reference> tokens = pericope.getValue();

		HashMap<String,Reference> map = convertReferenceListToHashMap(tokens);

		for (String id : tokensId) {
			if(!map.containsKey(id)){
				RefTokenText ref = new RefTokenText();
				ref.setId("ref_" + id);
				ref.setTok(getTokenByIDAndLang(id, langId));
				ref.setRef(id);
				tokens.add(ref);
			}
		}

		Collections.sort(tokens, CophiSort.TOKENREF_FROM_ORDER);
		pericope.setValue(tokens); 
	}


	public boolean savePericopes(List<PericopeText> lor, String work, ContextResource cr, String collection, String filename) {
		// TODO Auto-generated method stub
		ContextContent<PericopeText> cc = Factory.getContextContentInstance("PericopeText");
		PericopeTextContentBehaviour pcb = new PericopeTextContentBehaviour();
		cc.setContentType(pcb);
		cc.contentToSequence(lor, work);

		return cr.store(cc.getDocument(), collection, filename);

	}



}
