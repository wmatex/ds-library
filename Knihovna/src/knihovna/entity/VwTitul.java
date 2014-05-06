/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package knihovna.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author wmatex
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name="searchForTitul",
        query="SELECT t FROM VwTitul t "
            + "WHERE t.nazev LIKE :nazev "
    )
})
@Table(name = "vw_titul")
public class VwTitul implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "id_titul")
    @Id
    private Integer idTitul;
    private String nazev;
    @Column(name = "rok_vydani")
    private Short rokVydani;
    @Column(name = "vypujcni_doba_dny")
    private Short vypujcniDobaDny;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private BigDecimal cena;
    private String zanr;
    @Column(name = "volne_vytisky")
    private Integer volneVytisky;

    @ManyToMany
    @JoinTable(
        name="titul_autor",
        joinColumns=@JoinColumn(name="id_titul"),
        inverseJoinColumns=@JoinColumn(name="id_autor")
    )
    private Collection<Autor> autors;

    public VwTitul() {
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

    public Short getRokVydani() {
        return rokVydani;
    }

    public void setRokVydani(Short rokVydani) {
        this.rokVydani = rokVydani;
    }

    public Short getVypujcniDobaDny() {
        return vypujcniDobaDny;
    }

    public void setVypujcniDobaDny(Short vypujcniDobaDny) {
        this.vypujcniDobaDny = vypujcniDobaDny;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }

    public String getZanr() {
        return zanr;
    }

    public void setZanr(String zanr) {
        this.zanr = zanr;
    }

    public Integer getVolneVytisky() {
        return volneVytisky;
    }
}
