package it.cnr.ilc.cophi.model;

import java.util.List;

public abstract class ReferenceSet<T extends Reference> {
	
	private String id;
	private List<T> value = null;
	private String type;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the value
	 */
	public List<T> getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(List<T> value) {
		this.value = value;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}
