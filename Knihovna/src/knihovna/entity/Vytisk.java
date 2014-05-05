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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author wmatex
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vytisk.findAll", query = "SELECT v FROM Vytisk v"),
    @NamedQuery(name = "Vytisk.findByIdVytisk", query = "SELECT v FROM Vytisk v WHERE v.idVytisk = :idVytisk"),
    @NamedQuery(name = "Vytisk.findByCarKod", query = "SELECT v FROM Vytisk v WHERE v.carKod = :carKod")})
public class Vytisk implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_vytisk")
    private Integer idVytisk;
    @Basic(optional = false)
    @Column(name = "car_kod")
    private String carKod;
    @JoinColumn(name = "id_titul", referencedColumnName = "id_titul")
    @ManyToOne(optional = false)
    private Titul idTitul;

    public Vytisk() {
    }

    public Vytisk(Integer idVytisk) {
        this.idVytisk = idVytisk;
    }

    public Vytisk(Integer idVytisk, String carKod) {
        this.idVytisk = idVytisk;
        this.carKod = carKod;
    }

    public Integer getIdVytisk() {
        return idVytisk;
    }

    public String getCarKod() {
        return carKod;
    }

    public void setCarKod(String carKod) {
        this.carKod = carKod;
    }

    public Titul getIdTitul() {
        return idTitul;
    }

    public void setIdTitul(Titul idTitul) {
        this.idTitul = idTitul;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVytisk != null ? idVytisk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vytisk)) {
            return false;
        }
        Vytisk other = (Vytisk) object;
        if ((this.idVytisk == null && other.idVytisk != null) || (this.idVytisk != null && !this.idVytisk.equals(other.idVytisk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "knihovna.entity.Vytisk[ idVytisk=" + idVytisk + " ]";
    }
    
}
