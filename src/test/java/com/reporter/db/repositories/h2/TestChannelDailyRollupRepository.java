package com.reporter.db.repositories.h2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TestChannelDailyRollupRepository
    extends JpaRepository<TestChannelDailyRollupEntity, Long> {
    @Query(
        nativeQuery = true,
        value = "SELECT      \"cdr\".\"channel_id\" AS \"channelId\",\n" +
            "            COALESCE(\"p\".\"owner_partner_id\",\"p\".\"id\") AS \"partnerId\",\n" +
            "            \"cdr\".\"alias_cdr\" AS \"alias_cdr\",\n" +
            "            \"cdr\".\"sender_name\" AS \"sender\",\n" +
            "            SUM(\"cdr\".\"sms_send_count\") AS \"smsSendCount\",\n" +
            "            SUM(\"cdr\".\"sms_delivered_count\") AS \"smsDeliveredCount\",\n" +
            "            SUM(\"cdr\".\"msg_send_count\") AS \"msgSendCount\",\n" +
            "            SUM(\"cdr\".\"msg_delivered_count\") AS \"msgDeliveredCount\"\n" +
            "            FROM \"channel_daily_rollup\" AS \"cdr\"\n" +
            "            INNER JOIN \"partners\" AS \"p\" ON (\"cdr\".\"partner_id\"=\"p\".\"id\")\n" +
            "            WHERE\n" +
            "            \"cdr\".\"date_cdr\" >= :startDate\n" +
            "            AND \"cdr\".\"date_cdr\" < :finishDate\n" +
            "            GROUP BY \"cdr\".\"sender_name\", \"cdr\".\"channel_id\", \"cdr\".\"alias_cdr\", COALESCE(\"p\".\"owner_partner_id\",\"p\".\"id\")"
    )
    List<TestCDRPeriodStatisticByPartner> getTrafficStatistics2ForPeriod(
        @Param("startDate") LocalDate startDate,
        @Param("finishDate") LocalDate finishDate
    );
}
