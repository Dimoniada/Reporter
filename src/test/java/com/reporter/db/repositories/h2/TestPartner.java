package com.reporter.db.repositories.h2;

import com.google.common.base.MoreObjects;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "partners")
public class TestPartner
    implements Identifiable<Long>, Serializable, NameAware, CanonicalNameAware<Long>, ListLabelAware {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String name;
    protected String inn;

    @Column(name = "legal_person_name")
    protected String legalPersonName;

    @Column(name = "owner_partner_id")
    protected Long ownerPartnerId;

    @Column(name = "filial_id")
    protected Long filialId;

    @Column(name = "region_id")
    protected Long regionId;

    @Column(name = "comment")
    protected String comment;

    @Column(name = "registration_date")
    protected Date registrationDate;

    /**
     * Creates default partner object
     */
    public TestPartner() {
        /* */
    }

    public TestPartner(Long id) {
        this.id = id;
    }

    public static TestPartner fromId(Long id) {
        return new TestPartner(id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("id", id)
            .add("name", name)
            .add("person", super.toString())
            .add("inn", inn)
            .add("legalPersonName", legalPersonName)
            .add("ownerPartnerId", ownerPartnerId)
            .add("filialId", filialId)
            .add("regionId", regionId)
            .add("comment", comment)
            .add("registrationDate", registrationDate)
            .toString();
    }

    @Override
    public String getListLabel() {
        if (!StringUtils.hasText(getLegalPersonName())) {
            return getCanonicalName();
        }

        return
            String
                .format(
                    "#%d %s (%s)",
                    getId(),
                    getName(),
                    getLegalPersonName().trim()
                )
                .trim();
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public TestPartner setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;

        return this;
    }

    public Long getFilialId() {
        return filialId;
    }

    public TestPartner setFilialId(Long filialId) {
        this.filialId = filialId;

        return this;
    }

    public Long getRegionId() {
        return regionId;
    }

    public TestPartner setRegionId(Long regionId) {
        this.regionId = regionId;

        return this;
    }

    public Long getOwnerPartnerId() {
        return ownerPartnerId;
    }

    public TestPartner setOwnerPartnerId(Long ownerPartnerId) {
        this.ownerPartnerId = ownerPartnerId;

        return this;
    }

    public String getLegalPersonName() {
        return legalPersonName;
    }

    public TestPartner setLegalPersonName(String legalPersonName) {
        this.legalPersonName = legalPersonName;

        return this;
    }

    @Override
    public Long getId() {
        return id;
    }

    public TestPartner setId(Long id) {
        this.id = id;

        return this;
    }

    public String getName() {
        return name;
    }

    public TestPartner setName(String name) {
        this.name = name;

        return this;
    }

    public String getInn() {
        return inn;
    }

    public TestPartner setInn(String inn) {
        this.inn = inn;

        return this;
    }

    public String getComment() {
        return comment;
    }

    public TestPartner setComment(String comment) {
        this.comment = comment;

        return this;
    }
}
