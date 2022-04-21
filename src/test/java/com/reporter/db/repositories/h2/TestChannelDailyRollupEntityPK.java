package com.reporter.db.repositories.h2;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

public class TestChannelDailyRollupEntityPK implements Serializable {
    private int channelId;
    private Date date_cdr;
    private int mcc;
    private int mnc;
    private String alias_cdr;
    private int distributionId;
    private int partnerId;
    private int agregatorId;
    private int mobileOperatorId;
    private int mobileRegionId;
    private String senderName;
    private int templateId;
    private TestMessageDirectionEnum direction;
    private int deliveryChannelTypeId;
    private String moPrefix;
    private TestTrafficTypeEnum trafficType;

    @Column(name = "channel_id", nullable = false)
    @Id
    public int getChannelId() {
        return channelId;
    }

    public TestChannelDailyRollupEntityPK setChannelId(int channelId) {
        this.channelId = channelId;
        return this;
    }

    @Column(name = "date_cdr", nullable = false)
    @Id
    public Date getDate_cdr() {
        return date_cdr;
    }

    public TestChannelDailyRollupEntityPK setDate_cdr(Date date_cdr) {
        this.date_cdr = date_cdr;
        return this;
    }

    @Column(name = "mcc", nullable = false)
    @Id
    public int getMcc() {
        return mcc;
    }

    public TestChannelDailyRollupEntityPK setMcc(int mcc) {
        this.mcc = mcc;
        return this;
    }

    @Column(name = "mnc", nullable = false)
    @Id
    public int getMnc() {
        return mnc;
    }

    public TestChannelDailyRollupEntityPK setMnc(int mnc) {
        this.mnc = mnc;
        return this;
    }

    @Column(name = "alias_cdr", nullable = false, length = 32)
    @Id
    public String getAlias_cdr() {
        return alias_cdr;
    }

    public TestChannelDailyRollupEntityPK setAlias_cdr(String alias_cdr) {
        this.alias_cdr = alias_cdr;
        return this;
    }

    @Column(name = "distribution_id", nullable = false)
    @Id
    public int getDistributionId() {
        return distributionId;
    }

    public TestChannelDailyRollupEntityPK setDistributionId(int distributionId) {
        this.distributionId = distributionId;
        return this;
    }

    @Column(name = "partner_id", nullable = false)
    @Id
    public int getPartnerId() {
        return partnerId;
    }

    public TestChannelDailyRollupEntityPK setPartnerId(int partnerId) {
        this.partnerId = partnerId;
        return this;
    }

    @Column(name = "agregator_id", nullable = false)
    @Id
    public int getAgregatorId() {
        return agregatorId;
    }

    public TestChannelDailyRollupEntityPK setAgregatorId(int agregatorId) {
        this.agregatorId = agregatorId;
        return this;
    }

    @Column(name = "mobile_operator_id", nullable = false)
    @Id
    public int getMobileOperatorId() {
        return mobileOperatorId;
    }

    public TestChannelDailyRollupEntityPK setMobileOperatorId(int mobileOperatorId) {
        this.mobileOperatorId = mobileOperatorId;
        return this;
    }

    @Column(name = "mobile_region_id", nullable = false)
    @Id
    public int getMobileRegionId() {
        return mobileRegionId;
    }

    public TestChannelDailyRollupEntityPK setMobileRegionId(int mobileRegionId) {
        this.mobileRegionId = mobileRegionId;
        return this;
    }

    @Column(name = "sender_name", nullable = false, length = 32)
    @Id
    public String getSenderName() {
        return senderName;
    }

    public TestChannelDailyRollupEntityPK setSenderName(String senderName) {
        this.senderName = senderName;
        return this;
    }

    @Column(name = "template_id", nullable = false)
    @Id
    public int getTemplateId() {
        return templateId;
    }

    public TestChannelDailyRollupEntityPK setTemplateId(int templateId) {
        this.templateId = templateId;
        return this;
    }

    @Column(name = "direction", nullable = false)
    @Id
    @Enumerated(EnumType.STRING)
    public TestMessageDirectionEnum getDirection() {
        return direction;
    }

    public TestChannelDailyRollupEntityPK setDirection(TestMessageDirectionEnum direction) {
        this.direction = direction;
        return this;
    }

    @Column(name = "delivery_channel_type_id", nullable = false)
    @Id
    public int getDeliveryChannelTypeId() {
        return deliveryChannelTypeId;
    }

    public TestChannelDailyRollupEntityPK setDeliveryChannelTypeId(int deliveryChannelTypeId) {
        this.deliveryChannelTypeId = deliveryChannelTypeId;
        return this;
    }

    @Column(name = "mo_prefix", nullable = false, length = 64)
    @Id
    public String getMoPrefix() {
        return moPrefix;
    }

    public TestChannelDailyRollupEntityPK setMoPrefix(String moPrefix) {
        this.moPrefix = moPrefix;
        return this;
    }

    @Column(name = "traffic_type", nullable = false)
    @Id
    @Enumerated(EnumType.STRING)
    public TestTrafficTypeEnum getTrafficType() {
        return trafficType;
    }

    public TestChannelDailyRollupEntityPK setTrafficType(TestTrafficTypeEnum trafficType) {
        this.trafficType = trafficType;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final TestChannelDailyRollupEntityPK that = (TestChannelDailyRollupEntityPK) o;

        if (channelId != that.channelId) {
            return false;
        }
        if (mcc != that.mcc) {
            return false;
        }
        if (mnc != that.mnc) {
            return false;
        }
        if (distributionId != that.distributionId) {
            return false;
        }
        if (partnerId != that.partnerId) {
            return false;
        }
        if (agregatorId != that.agregatorId) {
            return false;
        }
        if (mobileOperatorId != that.mobileOperatorId) {
            return false;
        }
        if (mobileRegionId != that.mobileRegionId) {
            return false;
        }
        if (templateId != that.templateId) {
            return false;
        }
        if (deliveryChannelTypeId != that.deliveryChannelTypeId) {
            return false;
        }
        if (!Objects.equals(date_cdr, that.date_cdr)) {
            return false;
        }
        if (!Objects.equals(alias_cdr, that.alias_cdr)) {
            return false;
        }
        if (!Objects.equals(senderName, that.senderName)) {
            return false;
        }
        if (!Objects.equals(direction, that.direction)) {
            return false;
        }
        if (!Objects.equals(moPrefix, that.moPrefix)) {
            return false;
        }
        if (!Objects.equals(trafficType, that.trafficType)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = channelId;
        result = 31 * result + (date_cdr != null ? date_cdr.hashCode() : 0);
        result = 31 * result + mcc;
        result = 31 * result + mnc;
        result = 31 * result + (alias_cdr != null ? alias_cdr.hashCode() : 0);
        result = 31 * result + distributionId;
        result = 31 * result + partnerId;
        result = 31 * result + agregatorId;
        result = 31 * result + mobileOperatorId;
        result = 31 * result + mobileRegionId;
        result = 31 * result + (senderName != null ? senderName.hashCode() : 0);
        result = 31 * result + templateId;
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        result = 31 * result + deliveryChannelTypeId;
        result = 31 * result + (moPrefix != null ? moPrefix.hashCode() : 0);
        result = 31 * result + (trafficType != null ? trafficType.hashCode() : 0);
        return result;
    }
}
