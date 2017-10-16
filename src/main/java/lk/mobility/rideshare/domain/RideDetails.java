package lk.mobility.rideshare.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import lk.mobility.rideshare.domain.enumeration.Status;

/**
 * A RideDetails.
 */
@Entity
@Table(name = "ride_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "ridedetails")
public class RideDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pickedup_on")
    private ZonedDateTime pickedupOn;

    @Column(name = "dropped_on")
    private ZonedDateTime droppedOn;

    @Column(name = "jhi_comment")
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToOne
    private AppUser appUser;

    @ManyToOne
    private Ride ride;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getPickedupOn() {
        return pickedupOn;
    }

    public RideDetails pickedupOn(ZonedDateTime pickedupOn) {
        this.pickedupOn = pickedupOn;
        return this;
    }

    public void setPickedupOn(ZonedDateTime pickedupOn) {
        this.pickedupOn = pickedupOn;
    }

    public ZonedDateTime getDroppedOn() {
        return droppedOn;
    }

    public RideDetails droppedOn(ZonedDateTime droppedOn) {
        this.droppedOn = droppedOn;
        return this;
    }

    public void setDroppedOn(ZonedDateTime droppedOn) {
        this.droppedOn = droppedOn;
    }

    public String getComment() {
        return comment;
    }

    public RideDetails comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Status getStatus() {
        return status;
    }

    public RideDetails status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public RideDetails appUser(AppUser appUser) {
        this.appUser = appUser;
        return this;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Ride getRide() {
        return ride;
    }

    public RideDetails ride(Ride ride) {
        this.ride = ride;
        return this;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RideDetails rideDetails = (RideDetails) o;
        if (rideDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rideDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RideDetails{" +
            "id=" + getId() +
            ", pickedupOn='" + getPickedupOn() + "'" +
            ", droppedOn='" + getDroppedOn() + "'" +
            ", comment='" + getComment() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
