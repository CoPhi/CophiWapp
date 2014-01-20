package it.cnr.ilc.cophi.model.view;

import it.cnr.ilc.cophi.model.Link;

public class LinkViewEntity {

	private Link link = null;
	private String greekPericopeText = null;
	private String arabicPericopeText = null;
	private String arabicPericopeInfo = null;
	private String greekPericopeInfo = null;
	private String greekPericopeRefId = null;
	private String arabicPericopeRefId = null;
	
	/**
	 * @return the link
	 */
	public Link getLink() {
		return link;
	}

	/**
	 * @param link the link to set
	 */
	public void setLink(Link link) {
		this.link = link;
	}

	/**
	 * @return the greekPericopeText
	 */
	public String getGreekPericopeText() {
		return greekPericopeText;
	}

	/**
	 * @param greekPericopeText the greekPericopeText to set
	 */
	public void setGreekPericopeText(String greekPericopeText) {
		this.greekPericopeText = greekPericopeText;
	}

	/**
	 * @return the arabicPericopeText
	 */
	public String getArabicPericopeText() {
		return arabicPericopeText;
	}

	/**
	 * @param arabicPericopeText the arabicPericopeText to set
	 */
	public void setArabicPericopeText(String arabicPericopeText) {
		this.arabicPericopeText = arabicPericopeText;
	}

	/**
	 * @return the arabicPericopeInfo
	 */
	public String getArabicPericopeInfo() {
		return arabicPericopeInfo;
	}

	/**
	 * @param arabicPericopeInfo the arabicPericopeInfo to set
	 */
	public void setArabicPericopeInfo(String arabicPericopeInfo) {
		this.arabicPericopeInfo = arabicPericopeInfo;
	}

	/**
	 * @return the greekPericopeInfo
	 */
	public String getGreekPericopeInfo() {
		return greekPericopeInfo;
	}

	/**
	 * @param greekPericopeInfo the greekPericopeInfo to set
	 */
	public void setGreekPericopeInfo(String greekPericopeInfo) {
		this.greekPericopeInfo = greekPericopeInfo;
	}

	/**
	 * @return the greekPericopeRefId
	 */
	public String getGreekPericopeRefId() {
		return greekPericopeRefId;
	}

	/**
	 * @param greekPericopeRefId the greekPericopeRefId to set
	 */
	public void setGreekPericopeRefId(String greekPericopeRefId) {
		this.greekPericopeRefId = greekPericopeRefId;
	}

	/**
	 * @return the arabicPericopeRefId
	 */
	public String getArabicPericopeRefId() {
		return arabicPericopeRefId;
	}

	/**
	 * @param arabicPericopeRefId the arabicPericopeRefId to set
	 */
	public void setArabicPericopeRefId(String arabicPericopeRefId) {
		this.arabicPericopeRefId = arabicPericopeRefId;
	}
}
