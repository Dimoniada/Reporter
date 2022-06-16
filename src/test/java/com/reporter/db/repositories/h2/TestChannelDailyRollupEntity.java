package com.reporter.db.repositories.h2;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "channel_daily_rollup")
@IdClass(TestChannelDailyRollupEntityPK.class)
public class TestChannelDailyRollupEntity {
    private int channelId;
    private Date date_cdr;
    private byte month_cdr;
    private short year_cdr;
    private int smsSendCount;
    private int smsDeliveredCount;
    private Double price;
    private int mcc;
    private int mnc;
    private String alias_cdr;
    private int networkId;
    private int distributionId;
    private int partnerId;
    private int agregatorId;
    private int mobileOperatorId;
    private int mobileRegionId;
    private String senderName;
    private Integer msgSendCount;
    private Integer msgDeliveredCount;
    private int templateId;
    private TestMessageDirectionEnum direction;
    private int deliveryChannelTypeId;
    private String moPrefix;
    private TestTrafficTypeEnum trafficType;

    @Id
    @Column(name = "channel_id", nullable = false)
    public int getChannelId() {
        return channelId;
    }

    public TestChannelDailyRollupEntity setChannelId(int channelId) {
        this.channelId = channelId;
        return this;
    }

    @Id
    @Column(name = "date_cdr", nullable = false)
    public Date getDate_cdr() {
        return date_cdr;
    }

    public TestChannelDailyRollupEntity setDate_cdr(Date date_cdr) {
        this.date_cdr = date_cdr;
        return this;
    }

    @Basic
    @Column(name = "month_cdr", nullable = false)
    public byte getMonth_cdr() {
        return month_cdr;
    }

    public TestChannelDailyRollupEntity setMonth_cdr(byte month_cdr) {
        this.month_cdr = month_cdr;
        return this;
    }

    @Basic
    @Column(name = "year_cdr", nullable = false)
    public short getYear_cdr() {
        return year_cdr;
    }

    public TestChannelDailyRollupEntity setYear_cdr(short year_cdr) {
        this.year_cdr = year_cdr;
        return this;
    }

    @Basic
    @Column(name = "sms_send_count", nullable = false)
    public int getSmsSendCount() {
        return smsSendCount;
    }

    public TestChannelDailyRollupEntity setSmsSendCount(int smsSendCount) {
        this.smsSendCount = smsSendCount;
        return this;
    }

    @Basic
    @Column(name = "sms_delivered_count", nullable = false)
    public int getSmsDeliveredCount() {
        return smsDeliveredCount;
    }

    public TestChannelDailyRollupEntity setSmsDeliveredCount(int smsDeliveredCount) {
        this.smsDeliveredCount = smsDeliveredCount;
        return this;
    }

    @Basic
    @Column(name = "price", nullable = true, precision = 0)
    public Double getPrice() {
        return price;
    }

    public TestChannelDailyRollupEntity setPrice(Double price) {
        this.price = price;
        return this;
    }

    @Id
    @Column(name = "mcc", nullable = false)
    public int getMcc() {
        return mcc;
    }

    public TestChannelDailyRollupEntity setMcc(int mcc) {
        this.mcc = mcc;
        return this;
    }

    @Id
    @Column(name = "mnc", nullable = false)
    public int getMnc() {
        return mnc;
    }

    public TestChannelDailyRollupEntity setMnc(int mnc) {
        this.mnc = mnc;
        return this;
    }

    @Id
    @Column(name = "alias_cdr", nullable = false, length = 32)
    public String getAlias_cdr() {
        return alias_cdr;
    }

    public TestChannelDailyRollupEntity setAlias_cdr(String alias_cdr) {
        this.alias_cdr = alias_cdr;
        return this;
    }

    @Basic
    @Column(name = "network_id", nullable = false)
    public int getNetworkId() {
        return networkId;
    }

    public TestChannelDailyRollupEntity setNetworkId(int networkId) {
        this.networkId = networkId;
        return this;
    }

    @Id
    @Column(name = "distribution_id", nullable = false)
    public int getDistributionId() {
        return distributionId;
    }

    public TestChannelDailyRollupEntity setDistributionId(int distributionId) {
        this.distributionId = distributionId;
        return this;
    }

    @Id
    @Column(name = "partner_id", nullable = false)
    public int getPartnerId() {
        return partnerId;
    }

    public TestChannelDailyRollupEntity setPartnerId(int partnerId) {
        this.partnerId = partnerId;
        return this;
    }

    @Id
    @Column(name = "agregator_id", nullable = false)
    public int getAgregatorId() {
        return agregatorId;
    }

    public TestChannelDailyRollupEntity setAgregatorId(int agregatorId) {
        this.agregatorId = agregatorId;
        return this;
    }

    @Id
    @Column(name = "mobile_operator_id", nullable = false)
    public int getMobileOperatorId() {
        return mobileOperatorId;
    }

    public TestChannelDailyRollupEntity setMobileOperatorId(int mobileOperatorId) {
        this.mobileOperatorId = mobileOperatorId;
        return this;
    }

    @Id
    @Column(name = "mobile_region_id", nullable = false)
    public int getMobileRegionId() {
        return mobileRegionId;
    }

    public TestChannelDailyRollupEntity setMobileRegionId(int mobileRegionId) {
        this.mobileRegionId = mobileRegionId;
        return this;
    }

    @Id
    @Column(name = "sender_name", nullable = false, length = 32)
    public String getSenderName() {
        return senderName;
    }

    public TestChannelDailyRollupEntity setSenderName(String senderName) {
        this.senderName = senderName;
        return this;
    }

    @Basic
    @Column(name = "msg_send_count", nullable = true)
    public Integer getMsgSendCount() {
        return msgSendCount;
    }

    public TestChannelDailyRollupEntity setMsgSendCount(Integer msgSendCount) {
        this.msgSendCount = msgSendCount;
        return this;
    }

    @Basic
    @Column(name = "msg_delivered_count", nullable = true)
    public Integer getMsgDeliveredCount() {
        return msgDeliveredCount;
    }

    public TestChannelDailyRollupEntity setMsgDeliveredCount(Integer msgDeliveredCount) {
        this.msgDeliveredCount = msgDeliveredCount;
        return this;
    }

    @Id
    @Column(name = "template_id", nullable = false)
    public int getTemplateId() {
        return templateId;
    }

    public TestChannelDailyRollupEntity setTemplateId(int templateId) {
        this.templateId = templateId;
        return this;
    }

    @Id
    @Column(name = "direction", nullable = false)
    @Enumerated(EnumType.STRING)
    public TestMessageDirectionEnum getDirection() {
        return direction;
    }

    public TestChannelDailyRollupEntity setDirection(TestMessageDirectionEnum direction) {
        this.direction = direction;
        return this;
    }

    @Id
    @Column(name = "delivery_channel_type_id", nullable = false)
    public int getDeliveryChannelTypeId() {
        return deliveryChannelTypeId;
    }

    public TestChannelDailyRollupEntity setDeliveryChannelTypeId(int deliveryChannelTypeId) {
        this.deliveryChannelTypeId = deliveryChannelTypeId;
        return this;
    }

    @Id
    @Column(name = "mo_prefix", nullable = false, length = 64)
    public String getMoPrefix() {
        return moPrefix;
    }

    public TestChannelDailyRollupEntity setMoPrefix(String moPrefix) {
        this.moPrefix = moPrefix;
        return this;
    }

    @Id
    @Column(name = "traffic_type", nullable = false)
    @Enumerated(EnumType.STRING)
    public TestTrafficTypeEnum getTrafficType() {
        return trafficType;
    }

    public TestChannelDailyRollupEntity setTrafficType(TestTrafficTypeEnum trafficType) {
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

        final TestChannelDailyRollupEntity that = (TestChannelDailyRollupEntity) o;

        if (channelId != that.channelId) {
            return false;
        }
        if (month_cdr != that.month_cdr) {
            return false;
        }
        if (year_cdr != that.year_cdr) {
            return false;
        }
        if (smsSendCount != that.smsSendCount) {
            return false;
        }
        if (smsDeliveredCount != that.smsDeliveredCount) {
            return false;
        }
        if (mcc != that.mcc) {
            return false;
        }
        if (mnc != that.mnc) {
            return false;
        }
        if (networkId != that.networkId) {
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
        if (!Objects.equals(price, that.price)) {
            return false;
        }
        if (!Objects.equals(alias_cdr, that.alias_cdr)) {
            return false;
        }
        if (!Objects.equals(senderName, that.senderName)) {
            return false;
        }
        if (!Objects.equals(msgSendCount, that.msgSendCount)) {
            return false;
        }
        if (!Objects.equals(msgDeliveredCount, that.msgDeliveredCount)) {
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
        result = 31 * result + (int) month_cdr;
        result = 31 * result + (int) year_cdr;
        result = 31 * result + smsSendCount;
        result = 31 * result + smsDeliveredCount;
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + mcc;
        result = 31 * result + mnc;
        result = 31 * result + (alias_cdr != null ? alias_cdr.hashCode() : 0);
        result = 31 * result + networkId;
        result = 31 * result + distributionId;
        result = 31 * result + partnerId;
        result = 31 * result + agregatorId;
        result = 31 * result + mobileOperatorId;
        result = 31 * result + mobileRegionId;
        result = 31 * result + (senderName != null ? senderName.hashCode() : 0);
        result = 31 * result + (msgSendCount != null ? msgSendCount.hashCode() : 0);
        result = 31 * result + (msgDeliveredCount != null ? msgDeliveredCount.hashCode() : 0);
        result = 31 * result + templateId;
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        result = 31 * result + deliveryChannelTypeId;
        result = 31 * result + (moPrefix != null ? moPrefix.hashCode() : 0);
        result = 31 * result + (trafficType != null ? trafficType.hashCode() : 0);
        return result;
    }
}
