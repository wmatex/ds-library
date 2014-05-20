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
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;

/**
 *
 * @author wmatex
 */
@Entity
@NamedNativeQueries({
    @NamedNativeQuery(
        name="VwTitul.searchForTitul",
        query="SELECT t.* FROM vw_titul t "
            + "INNER JOIN titul f ON t.id_titul = f.id_titul "
            + "WHERE f.fulltext @@ to_tsquery('simple', ?) "
            + "ORDER BY ts_rank(f.fulltext, to_tsquery('simple', ?)) DESC "
            + "LIMIT ? OFFSET ?",
        resultClass=VwTitul.class
    ),
    @NamedNativeQuery(
        name="VwTitul.getGenres",
        query="SELECT nazev FROM zanr ORDER BY id_zanr",
        resultSetMapping="genre-mapping"
    ),
    @NamedNativeQuery(
        name="VwTitul.deleteJoins",
        query="DELETE FROM titul_autor WHERE id_titul = ?"
    ),
    @NamedNativeQuery(
        name="VwTitul.getGenreId",
        query="SELECT id_zanr FROM zanr WHERE nazev = ? LIMIT 1",
        resultSetMapping="genre-id-mapping"
    )
})
@SqlResultSetMappings({
    @SqlResultSetMapping(name="genre-mapping",
        classes={
            @ConstructorResult(targetClass=String.class, columns={
                @ColumnResult(name="nazev", type=String.class)
            })
        }
    ),
    @SqlResultSetMapping(name="genre-id-mapping",
        classes={
            @ConstructorResult(targetClass=Integer.class, columns={
                @ColumnResult(name="id_zanr", type=Integer.class)
            })
        }
    )
})
@Table(name = "vw_titul")
public class VwTitul implements Serializable {
    private static final long serialVersionUID = 1L;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_titul")
    @Id
    private Integer idTitul;
    private String nazev;
    @Column(name = "rok_vydani")
    private Short rokVydani;
    @Column(name = "vypujcni_doba_dny")
    private Short vypujcniDobaDny;
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

    public VwTitul(String nazev, Short rokVydani, Short vypujcniDobaDny, BigDecimal cena, String zanr) {
        this.nazev = nazev;
        this.rokVydani = rokVydani;
        this.vypujcniDobaDny = vypujcniDobaDny;
        this.cena = cena;
        this.zanr = zanr;
    }

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

    public Collection<Autor> getAutors() {
        return autors;
    }
}
