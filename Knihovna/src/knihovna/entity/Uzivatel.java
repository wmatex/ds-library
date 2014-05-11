/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package knihovna.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author wmatex
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name="Uzivatel.prihlasit", 
        query="SELECT u FROM Uzivatel u WHERE u.email = :email"
            + " AND u.heslo = :heslo"),
    @NamedQuery(
        name="Uzivatel.findByEmail",
        query="SELECT u FROM Uzivatel u WHERE u.email = :email"
    ),
    @NamedQuery(
        name="Uzivatel.getAll",
        query="SELECT u FROM Uzivatel u ORDER BY u.prijmeni ASC, u.krestniJmeno ASC"
    )
})
public class Uzivatel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_uzivatel")
    private Integer idUzivatel;
    @Basic(optional = false)
    @Column(name = "krestni_jmeno")
    private String krestniJmeno;
    @Basic(optional = false)
    private String prijmeni;
    @Basic(optional = false)
    private String email;
    @Basic(optional = false)
    private String heslo;
    @Basic(optional = false)
    private short role;

    public Uzivatel() {
    }

    public Uzivatel(Integer idUzivatel) {
        this.idUzivatel = idUzivatel;
    }

    public Uzivatel(String krestniJmeno, String prijmeni, String email) {
        this.krestniJmeno = krestniJmeno;
        this.prijmeni = prijmeni;
        this.email = email;
        
    }

    public Uzivatel(Integer idUzivatel, String krestniJmeno, String prijmeni, String email, String heslo, short role) {
        this.idUzivatel = idUzivatel;
        this.krestniJmeno = krestniJmeno;
        this.prijmeni = prijmeni;
        this.email = email;
        this.heslo = heslo;
        this.role = role;
    }

    public Integer getIdUzivatel() {
        return idUzivatel;
    }

    public void setIdUzivatel(Integer idUzivatel) {
        this.idUzivatel = idUzivatel;
    }

    public String getKrestniJmeno() {
        return krestniJmeno;
    }

    public void setKrestniJmeno(String krestniJmeno) {
        this.krestniJmeno = krestniJmeno;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public void setPrijmeni(String prijmeni) {
        this.prijmeni = prijmeni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeslo() {
        return heslo;
    }

    public void setHeslo(String heslo) {
        this.heslo = heslo;
    }

    public short getRole() {
        return role;
    }

    public void setRole(short role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUzivatel != null ? idUzivatel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Uzivatel)) {
            return false;
        }
        Uzivatel other = (Uzivatel) object;
        if ((this.idUzivatel == null && other.idUzivatel != null) || (this.idUzivatel != null && !this.idUzivatel.equals(other.idUzivatel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return krestniJmeno + " " + prijmeni + " <" + email + ">";
    }
    
}
