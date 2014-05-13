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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author wmatex
 */
@Entity
@Table(name = "vw_vypujcka")
@NamedQueries({
    @NamedQuery(
        name="Vypujcka.findByUser",
        query="SELECT v FROM VwVypujcka v WHERE v.idUzivatel = :uzivatel"
            + " AND v.jeVraceno = false"
    ),
    @NamedQuery(
        name="Vypujcka.findByVytisk",
        query="SELECT v FROM VwVypujcka v WHERE v.idVytisk = :id"
            + " AND v.jeVraceno = false"
    )
})
public class VwVypujcka implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "id_uzivatel")
    private Integer idUzivatel;
    @Column(name = "krestni_jmeno")
    private String krestniJmeno;
    private String prijmeni;
    private String email;
    @Column(name = "id_titul")
    private Integer idTitul;
    @Column(name = "id_vytisk")
    private Integer idVytisk;
    private String nazev;
    @Column(name = "id_vypujcka")
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator="vypujcka_generator")
    @SequenceGenerator(name="vypujcka_generator", sequenceName="seq_vypujcka", allocationSize=1, initialValue=1)
    private Integer idVypujcka;
    @Column(name = "datum_pujceni")
    @Temporal(TemporalType.DATE)
    private Date datumPujceni;
    @Column(name = "datum_vraceni")
    @Temporal(TemporalType.DATE)
    private Date datumVraceni;
    @Column(name = "je_vraceno")
    private Boolean jeVraceno;

    public VwVypujcka() {
    }

    public VwVypujcka(Integer idVytisk, Integer idUzivatel) {
        this.idVytisk = idVytisk;
        this.idUzivatel = idUzivatel;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getIdTitul() {
        return idTitul;
    }

    public Integer getIdVytisk() {
        return idVytisk;
    }

    public String getNazev() {
        return nazev;
    }

    public Integer getIdVypujcka() {
        return idVypujcka;
    }


    public Date getDatumPujceni() {
        return datumPujceni;
    }

    public Date getDatumVraceni() {
        return datumVraceni;
    }

    public void setDatumVraceni(Date datumVraceni) {
        this.datumVraceni = datumVraceni;
    }

    public Boolean getJeVraceno() {
        return jeVraceno;
    }

    public void setJeVraceno(Boolean jeVraceno) {
        this.jeVraceno = jeVraceno;
    }

    @Override
    public String toString() {
        return krestniJmeno + " " + prijmeni + " <" + email + ">: " + nazev + ", pujceno od: "
            + datumPujceni + ", do: " + datumVraceni + ", vraceno? " + jeVraceno;
    }

    
    
}
