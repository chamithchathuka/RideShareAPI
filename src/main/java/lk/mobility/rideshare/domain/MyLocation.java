package lk.mobility.rideshare.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A MyLocation.
 */
@Entity
@Table(name = "my_location")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "mylocation")
public class MyLocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "real_location_name")
    private String realLocationName;

    @Column(name = "my_location_name")
    private String myLocationName;

    @Column(name = "lati")
    private String lati;

    @Column(name = "longi")
    private String longi;

    @ManyToOne
    private AppUser appUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRealLocationName() {
        return realLocationName;
    }

    public MyLocation realLocationName(String realLocationName) {
        this.realLocationName = realLocationName;
        return this;
    }

    public void setRealLocationName(String realLocationName) {
        this.realLocationName = realLocationName;
    }

    public String getMyLocationName() {
        return myLocationName;
    }

    public MyLocation myLocationName(String myLocationName) {
        this.myLocationName = myLocationName;
        return this;
    }

    public void setMyLocationName(String myLocationName) {
        this.myLocationName = myLocationName;
    }

    public String getLati() {
        return lati;
    }

    public MyLocation lati(String lati) {
        this.lati = lati;
        return this;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public String getLongi() {
        return longi;
    }

    public MyLocation longi(String longi) {
        this.longi = longi;
        return this;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public MyLocation appUser(AppUser appUser) {
        this.appUser = appUser;
        return this;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MyLocation myLocation = (MyLocation) o;
        if (myLocation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), myLocation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MyLocation{" +
            "id=" + getId() +
            ", realLocationName='" + getRealLocationName() + "'" +
            ", myLocationName='" + getMyLocationName() + "'" +
            ", lati='" + getLati() + "'" +
            ", longi='" + getLongi() + "'" +
            "}";
    }
}
