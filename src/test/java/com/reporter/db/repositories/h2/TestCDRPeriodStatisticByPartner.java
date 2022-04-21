package com.reporter.db.repositories.h2;

public interface TestCDRPeriodStatisticByPartner {
    Short getChannelId();

    String getSender();

    String getAliasCdr();

    Long getPartnerId();

    Long getSmsSendCount();

    Long getSmsDeliveredCount();

    Long getMsgSendCount();

    Long getMsgDeliveredCount();
}
