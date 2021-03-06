package it.cnr.ilc.cophi.model;

public abstract class Reference {

	private String id;
	private String ref;
	private String extended;

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
	 * @return the ref
	 */
	public String getRef() {
		return ref;
	}

	/**
	 * @param ref the ref to set
	 */
	public void setRef(String ref) {
		this.ref = ref;
	}

	/**
	 * @return the extended
	 */
	public String getExtended() {
		return extended;
	}

	/**
	 * @param extended the extended to set
	 */
	public void setExtended(String ext) {
		this.extended = ext;
	}
}
