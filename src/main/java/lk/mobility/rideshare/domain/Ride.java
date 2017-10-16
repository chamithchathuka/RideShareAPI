package lk.mobility.rideshare.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import lk.mobility.rideshare.domain.enumeration.RideType;

import lk.mobility.rideshare.domain.enumeration.Privacy;

/**
 * A Ride.
 */
@Entity
@Table(name = "ride")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "ride")
public class Ride implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_location")
    private String startLocation;

    @Column(name = "end_location")
    private String endLocation;

    @Column(name = "start_locaion_lat")
    private String startLocaionLat;

    @Column(name = "start_location_long")
    private String startLocationLong;

    @Column(name = "end_locaion_lat")
    private String endLocaionLat;

    @Column(name = "end_location_long")
    private String endLocationLong;

    @Column(name = "start_date_time")
    private ZonedDateTime startDateTime;

    @Column(name = "created_date_time")
    private ZonedDateTime createdDateTime;

    @Column(name = "seat_capasity")
    private Integer seatCapasity;

    @Enumerated(EnumType.STRING)
    @Column(name = "ride_type")
    private RideType rideType;

    @Enumerated(EnumType.STRING)
    @Column(name = "privacy")
    private Privacy privacy;

    @OneToMany(mappedBy = "ride")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RideDetails> rideDetails = new HashSet<>();

    @ManyToOne
    private AppUser appUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public Ride startLocation(String startLocation) {
        this.startLocation = startLocation;
        return this;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public Ride endLocation(String endLocation) {
        this.endLocation = endLocation;
        return this;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getStartLocaionLat() {
        return startLocaionLat;
    }

    public Ride startLocaionLat(String startLocaionLat) {
        this.startLocaionLat = startLocaionLat;
        return this;
    }

    public void setStartLocaionLat(String startLocaionLat) {
        this.startLocaionLat = startLocaionLat;
    }

    public String getStartLocationLong() {
        return startLocationLong;
    }

    public Ride startLocationLong(String startLocationLong) {
        this.startLocationLong = startLocationLong;
        return this;
    }

    public void setStartLocationLong(String startLocationLong) {
        this.startLocationLong = startLocationLong;
    }

    public String getEndLocaionLat() {
        return endLocaionLat;
    }

    public Ride endLocaionLat(String endLocaionLat) {
        this.endLocaionLat = endLocaionLat;
        return this;
    }

    public void setEndLocaionLat(String endLocaionLat) {
        this.endLocaionLat = endLocaionLat;
    }

    public String getEndLocationLong() {
        return endLocationLong;
    }

    public Ride endLocationLong(String endLocationLong) {
        this.endLocationLong = endLocationLong;
        return this;
    }

    public void setEndLocationLong(String endLocationLong) {
        this.endLocationLong = endLocationLong;
    }

    public ZonedDateTime getStartDateTime() {
        return startDateTime;
    }

    public Ride startDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public void setStartDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public Ride createdDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    public void setCreatedDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Integer getSeatCapasity() {
        return seatCapasity;
    }

    public Ride seatCapasity(Integer seatCapasity) {
        this.seatCapasity = seatCapasity;
        return this;
    }

    public void setSeatCapasity(Integer seatCapasity) {
        this.seatCapasity = seatCapasity;
    }

    public RideType getRideType() {
        return rideType;
    }

    public Ride rideType(RideType rideType) {
        this.rideType = rideType;
        return this;
    }

    public void setRideType(RideType rideType) {
        this.rideType = rideType;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public Ride privacy(Privacy privacy) {
        this.privacy = privacy;
        return this;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public Set<RideDetails> getRideDetails() {
        return rideDetails;
    }

    public Ride rideDetails(Set<RideDetails> rideDetails) {
        this.rideDetails = rideDetails;
        return this;
    }

    public Ride addRideDetails(RideDetails rideDetails) {
        this.rideDetails.add(rideDetails);
        rideDetails.setRide(this);
        return this;
    }

    public Ride removeRideDetails(RideDetails rideDetails) {
        this.rideDetails.remove(rideDetails);
        rideDetails.setRide(null);
        return this;
    }

    public void setRideDetails(Set<RideDetails> rideDetails) {
        this.rideDetails = rideDetails;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public Ride appUser(AppUser appUser) {
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
        Ride ride = (Ride) o;
        if (ride.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ride.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Ride{" +
            "id=" + getId() +
            ", startLocation='" + getStartLocation() + "'" +
            ", endLocation='" + getEndLocation() + "'" +
            ", startLocaionLat='" + getStartLocaionLat() + "'" +
            ", startLocationLong='" + getStartLocationLong() + "'" +
            ", endLocaionLat='" + getEndLocaionLat() + "'" +
            ", endLocationLong='" + getEndLocationLong() + "'" +
            ", startDateTime='" + getStartDateTime() + "'" +
            ", createdDateTime='" + getCreatedDateTime() + "'" +
            ", seatCapasity='" + getSeatCapasity() + "'" +
            ", rideType='" + getRideType() + "'" +
            ", privacy='" + getPrivacy() + "'" +
            "}";
    }
}
