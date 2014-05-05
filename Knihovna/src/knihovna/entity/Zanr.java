/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package knihovna.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author wmatex
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Zanr.findAll", query = "SELECT z FROM Zanr z"),
    @NamedQuery(name = "Zanr.findByIdZanr", query = "SELECT z FROM Zanr z WHERE z.idZanr = :idZanr"),
    @NamedQuery(name = "Zanr.findByNazev", query = "SELECT z FROM Zanr z WHERE z.nazev = :nazev")})
public class Zanr implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_zanr")
    private Integer idZanr;
    @Basic(optional = false)
    private String nazev;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idZanr")
    private Collection<Titul> titulCollection;

    public Zanr() {
    }

    public Zanr(Integer idZanr) {
        this.idZanr = idZanr;
    }

    public Zanr(Integer idZanr, String nazev) {
        this.idZanr = idZanr;
        this.nazev = nazev;
    }

    public Integer getIdZanr() {
        return idZanr;
    }

    public void setIdZanr(Integer idZanr) {
        this.idZanr = idZanr;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    @XmlTransient
    public Collection<Titul> getTitulCollection() {
        return titulCollection;
    }

    public void setTitulCollection(Collection<Titul> titulCollection) {
        this.titulCollection = titulCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idZanr != null ? idZanr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Zanr)) {
            return false;
        }
        Zanr other = (Zanr) object;
        if ((this.idZanr == null && other.idZanr != null) || (this.idZanr != null && !this.idZanr.equals(other.idZanr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "knihovna.entity.Zanr[ idZanr=" + idZanr + " ]";
    }
    
}
