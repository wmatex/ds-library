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
import javax.persistence.NamedStoredProcedureQuery;
import static javax.persistence.ParameterMode.IN;
import javax.persistence.StoredProcedureParameter;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author wmatex
 */
@Entity
@XmlRootElement
@NamedStoredProcedureQuery(
    name = "Autor.vloz_autora",
    procedureName = "vloz_autora",
    parameters = {
        @StoredProcedureParameter(mode=IN, name="_id_titul", type=Integer.class),
        @StoredProcedureParameter(mode=IN, name="_jmeno", type=String.class),
        @StoredProcedureParameter(mode=IN, name="_prijmeni", type=String.class)
    }
)
@NamedQueries({
    @NamedQuery(name = "Autor.findAll", query = "SELECT a FROM Autor a ORDER BY a.prijmeni, a.jmeno")})
public class Autor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_autor")
    private Integer idAutor;
    @Basic(optional = false)
    private String jmeno;
    @Basic(optional = false)
    private String prijmeni;

    public Autor() {
    }

    public Autor(Integer idAutor) {
        this.idAutor = idAutor;
    }

    public Autor(Integer idAutor, String jmeno, String prijmeni) {
        this.idAutor = idAutor;
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
    }

    public Integer getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(Integer idAutor) {
        this.idAutor = idAutor;
    }

    public String getJmeno() {
        return jmeno;
    }

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public void setPrijmeni(String prijmeni) {
        this.prijmeni = prijmeni;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAutor != null ? idAutor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Autor)) {
            return false;
        }
        Autor other = (Autor) object;
        if ((this.idAutor == null && other.idAutor != null) || (this.idAutor != null && !this.idAutor.equals(other.idAutor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "knihovna.entity.Autor[ idAutor=" + idAutor + " ]";
    }
    
}
