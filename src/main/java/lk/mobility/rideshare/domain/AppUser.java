package lk.mobility.rideshare.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import lk.mobility.rideshare.domain.enumeration.Gender;

/**
 * A AppUser.
 */
@Entity
@Table(name = "app_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "appuser")
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "iuser_id")
    private Long iuserId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "fb_id")
    private String fbID;

    @Column(name = "google_id")
    private String googleID;

    @Column(name = "linked_in_id")
    private String linkedInID;

    @OneToMany(mappedBy = "appUser")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Ride> rides = new HashSet<>();

    @OneToMany(mappedBy = "appUser")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RideDetails> rideDetails = new HashSet<>();

    @OneToMany(mappedBy = "appUser")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MyLocation> myLocations = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIuserId() {
        return iuserId;
    }

    public AppUser iuserId(Long iuserId) {
        this.iuserId = iuserId;
        return this;
    }

    public void setIuserId(Long iuserId) {
        this.iuserId = iuserId;
    }

    public String getFirstName() {
        return firstName;
    }

    public AppUser firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public AppUser lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public AppUser gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public AppUser address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public AppUser email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFbID() {
        return fbID;
    }

    public AppUser fbID(String fbID) {
        this.fbID = fbID;
        return this;
    }

    public void setFbID(String fbID) {
        this.fbID = fbID;
    }

    public String getGoogleID() {
        return googleID;
    }

    public AppUser googleID(String googleID) {
        this.googleID = googleID;
        return this;
    }

    public void setGoogleID(String googleID) {
        this.googleID = googleID;
    }

    public String getLinkedInID() {
        return linkedInID;
    }

    public AppUser linkedInID(String linkedInID) {
        this.linkedInID = linkedInID;
        return this;
    }

    public void setLinkedInID(String linkedInID) {
        this.linkedInID = linkedInID;
    }

    public Set<Ride> getRides() {
        return rides;
    }

    public AppUser rides(Set<Ride> rides) {
        this.rides = rides;
        return this;
    }

    public AppUser addRide(Ride ride) {
        this.rides.add(ride);
        ride.setAppUser(this);
        return this;
    }

    public AppUser removeRide(Ride ride) {
        this.rides.remove(ride);
        ride.setAppUser(null);
        return this;
    }

    public void setRides(Set<Ride> rides) {
        this.rides = rides;
    }

    public Set<RideDetails> getRideDetails() {
        return rideDetails;
    }

    public AppUser rideDetails(Set<RideDetails> rideDetails) {
        this.rideDetails = rideDetails;
        return this;
    }

    public AppUser addRideDetails(RideDetails rideDetails) {
        this.rideDetails.add(rideDetails);
        rideDetails.setAppUser(this);
        return this;
    }

    public AppUser removeRideDetails(RideDetails rideDetails) {
        this.rideDetails.remove(rideDetails);
        rideDetails.setAppUser(null);
        return this;
    }

    public void setRideDetails(Set<RideDetails> rideDetails) {
        this.rideDetails = rideDetails;
    }

    public Set<MyLocation> getMyLocations() {
        return myLocations;
    }

    public AppUser myLocations(Set<MyLocation> myLocations) {
        this.myLocations = myLocations;
        return this;
    }

    public AppUser addMyLocation(MyLocation myLocation) {
        this.myLocations.add(myLocation);
        myLocation.setAppUser(this);
        return this;
    }

    public AppUser removeMyLocation(MyLocation myLocation) {
        this.myLocations.remove(myLocation);
        myLocation.setAppUser(null);
        return this;
    }

    public void setMyLocations(Set<MyLocation> myLocations) {
        this.myLocations = myLocations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AppUser appUser = (AppUser) o;
        if (appUser.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), appUser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AppUser{" +
            "id=" + getId() +
            ", iuserId='" + getIuserId() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", gender='" + getGender() + "'" +
            ", address='" + getAddress() + "'" +
            ", email='" + getEmail() + "'" +
            ", fbID='" + getFbID() + "'" +
            ", googleID='" + getGoogleID() + "'" +
            ", linkedInID='" + getLinkedInID() + "'" +
            "}";
    }
}
