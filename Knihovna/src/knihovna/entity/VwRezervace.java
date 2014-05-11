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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author wmatex
 */
@Entity
@Table(name = "vw_rezervace")
@NamedQueries({
    @NamedQuery(
            name = "VwRezervace.findByUser", 
            query = "SELECT v FROM VwRezervace v WHERE v.idUzivatel = :uzivatel")
})
public class VwRezervace implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "id_uzivatel")
    private Integer idUzivatel;
    @Column(name = "krestni_jmeno")
    private String krestniJmeno;
    private String prijmeni;
    private String email;
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
    @Id
    private Long id;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}
