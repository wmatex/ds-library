/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package knihovna.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@NamedQueries({
})
public class Titul implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_titul")
    private Integer idTitul;
    @Basic(optional = false)
    private String nazev;
    @Basic(optional = false)
    @Column(name = "rok_vydani")
    private short rokVydani;
    @Basic(optional = false)
    @Column(name = "vypujcni_doba_dny")
    private short vypujcniDobaDny;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    private BigDecimal cena;
    @JoinColumn(name = "id_zanr", referencedColumnName = "id_zanr")
    @ManyToOne(optional = false)
    private Zanr idZanr;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTitul")
    private Collection<Vytisk> vytiskCollection;

    public Titul() {
    }

    public Titul(Integer idTitul) {
        this.idTitul = idTitul;
    }

    public Titul(Integer idTitul, String nazev, short rokVydani, short vypujcniDobaDny, BigDecimal cena) {
        this.idTitul = idTitul;
        this.nazev = nazev;
        this.rokVydani = rokVydani;
        this.vypujcniDobaDny = vypujcniDobaDny;
        this.cena = cena;
    }

    public Integer getIdTitul() {
        return idTitul;
    }

    public void setIdTitul(Integer idTitul) {
        this.idTitul = idTitul;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public short getRokVydani() {
        return rokVydani;
    }

    public void setRokVydani(short rokVydani) {
        this.rokVydani = rokVydani;
    }

    public short getVypujcniDobaDny() {
        return vypujcniDobaDny;
    }

    public void setVypujcniDobaDny(short vypujcniDobaDny) {
        this.vypujcniDobaDny = vypujcniDobaDny;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }

    public Zanr getIdZanr() {
        return idZanr;
    }

    public void setIdZanr(Zanr idZanr) {
        this.idZanr = idZanr;
    }

    @XmlTransient
    public Collection<Vytisk> getVytiskCollection() {
        return vytiskCollection;
    }

    public void setVytiskCollection(Collection<Vytisk> vytiskCollection) {
        this.vytiskCollection = vytiskCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTitul != null ? idTitul.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Titul)) {
            return false;
        }
        Titul other = (Titul) object;
        if ((this.idTitul == null && other.idTitul != null) || (this.idTitul != null && !this.idTitul.equals(other.idTitul))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "knihovna.entity.Titul[ idTitul=" + idTitul + " ]";
    }
    
}
