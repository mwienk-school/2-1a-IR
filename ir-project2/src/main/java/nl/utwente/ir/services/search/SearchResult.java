package nl.utwente.ir.services.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

/**
 * The helper class for the view, should not contain any real logic
 * 
 * @author mark
 *
 */
public class SearchResult {
	
	private String idnummer;
	private String titel;
	private String tekst;
	private String atu_omschrijving;
	
	private Map<String, String[]> facets;

	public SearchResult(String idnummer) {
		this.idnummer = idnummer;
		facets = new HashMap<String,String[]>();
	}
	
	public String getIdnummer() {
		return idnummer;
	}
	
	public String getTitel() {
		return titel;
	}
	
	public void setTitel(String titel) {
		this.titel = titel;
	}
	
	public String getTekst() {
		return tekst;
	}
	
	public String getShortTekst() {
		if(tekst.length() > 204) return tekst.substring(0, 200) + "...";
		return tekst;
	}
	
	public void setTekst(String tekst) {
		this.tekst = tekst.replace("\n", "<br />");
	}

	// Facets
	public Map<String, String[]> getFacets() {
		return this.facets;
	}
	
	/**
	 * Set a custom facet
	 * @param key
	 * @param value
	 */
	public void setFacet(String key, String value) {
		this.facets.put(key, new String[]{value});
	}
	
	/**
	 * Get a single valued facet
	 * @param key
	 * @return
	 */
	public String getSingleFacet(String key) {
		return this.facets.get(key)[0];
	}
	
	public String getTaal() {
		return getSingleFacet("taal");
	}

	public void setTaal(String taal) {
		this.setFacet("taal", taal);
	}
	
	public String getType() {
		return getSingleFacet("type");
	}
	
	public void setType(String type) {
		this.setFacet("type", type);
	}
	
	public void setATUOmschrijving(String omschrijving) {
		this.atu_omschrijving = omschrijving;
	}
	
	public String getATUOmschrijving() {
		return atu_omschrijving;
	}

	public String getLiterair() {
		return getSingleFacet("literair");
	}

	public void setLiterair(String literair) {
		this.setFacet("literair", literair);
	}

	public String getExtreem() {
		return getSingleFacet("extreem");
	}

	public void setExtreem(String extreem) {
		this.setFacet("extreem", extreem);
	}
	
	public boolean isExtreem() {
		return getSingleFacet("extreem").equalsIgnoreCase("ja");
	}

	public String getSubgenre() {
		return getSingleFacet("subgenre");
	}

	public void setSubgenre(String subgenre) {
		this.setFacet("subgenre", subgenre);
	}

	public String getRegio() {
		return getSingleFacet("regio");
	}

	public void setRegio(String regio) {
		this.setFacet("regio", regio);
	}

	public String getDatering() {
		return getSingleFacet("datering");
	}

	public void setDatering(String datering) {
		this.setFacet("datering", datering);
	}

	public String getTrefwoorden() {
		for(String str : this.facets.get("trefwoorden")) {
			System.err.println(str);
		}
		return StringUtils.arrayToCommaDelimitedString(this.facets.get("trefwoorden"));
	}

	public void setTrefwoorden(List<String> trefwoorden) {
		this.facets.put("trefwoorden", StringUtils.toStringArray(trefwoorden));
	}

}
