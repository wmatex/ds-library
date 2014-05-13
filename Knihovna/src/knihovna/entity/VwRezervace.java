/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package knihovna.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedStoredProcedureQuery;
import static javax.persistence.ParameterMode.IN;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author wmatex
 */
@Entity
@Table(name = "vw_rezervace")
@NamedNativeQueries({
    @NamedNativeQuery(
        resultClass=VwRezervace.class,
        name = "VwRezervace.findByUser",
        query = "SELECT v.* FROM vw_rezervace v "
            + "WHERE v.id_uzivatel = ? "
            + "LIMIT ? OFFSET ?"
    )
})
@NamedStoredProcedureQuery(
    name = "VwRezervace.nova",
    procedureName = "vytvor_rezervaci",
    parameters = {
        @StoredProcedureParameter(mode=IN, name="_id_uzivatel", type=Integer.class),
        @StoredProcedureParameter(mode=IN, name="_id_titul", type=Integer.class),
        @StoredProcedureParameter(mode=IN, name="_interval", type=String.class)
    }
)
public class VwRezervace implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id_uzivatel")
    private Integer idUzivatel;
    @Column(name = "krestni_jmeno")
    private String krestniJmeno;
    private String prijmeni;
    private String email;
    @Id
    @Column(name = "id_titul")
    private Integer idTitul;
    private String nazev;
    private Integer poradi;
    private Boolean splnena;
    @Temporal(TemporalType.DATE)
    private Date vyprsi;
    @Column(name = "zajem_do")
    @Temporal(TemporalType.DATE)
    private Date zajemDo;

    public VwRezervace() {
    }

    public Integer getIdUzivatel() {
        return idUzivatel;
    }

    public String getKrestniJmeno() {
        return krestniJmeno;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public String getEmail() {
        return email;
    }

    public Integer getIdTitul() {
        return idTitul;
    }

    public String getNazev() {
        return nazev;
    }

    public Integer getPoradi() {
        return poradi;
    }

    public Boolean getSplnena() {
        return splnena;
    }

    public Date getVyprsi() {
        return vyprsi;
    }

    public void setVyprsi(Date vyprsi) {
        this.vyprsi = vyprsi;
    }

    public Date getZajemDo() {
        return zajemDo;
    }

    public void setZajemDo(Date zajemDo) {
        this.zajemDo = zajemDo;
    }
}
