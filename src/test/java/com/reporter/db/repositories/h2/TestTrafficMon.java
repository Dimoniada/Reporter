package com.reporter.db.repositories.h2;

import com.google.common.base.MoreObjects;

import javax.persistence.*;

@Entity
@Table(name = "traffic_mon")
public class TestTrafficMon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "client_name")
    protected String clientName;

    @Column(name = "date1_amount")
    protected Integer date1Amount;

    @Column(name = "date2_amount")
    protected Integer date2Amount;

    @Column(name = "abs_diff")
    protected Integer absDiff;

    @Column(name = "rel_diff_percent")
    protected String relDiffPercent;

    @Column(name = "login")
    protected String login;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private TestUsers userId;

    public TestTrafficMon(Long id) {
        this.id = id;
    }

    public TestTrafficMon() {
        /**/
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("id", id)
            .add("clientName", clientName)
            .add("date1Amount", date1Amount)
            .add("date2Amount", date2Amount)
            .add("absDiff", absDiff)
            .add("relDiffPercent", relDiffPercent)
            .add("login", login)
            .add("userId", userId)
            .add("parent", super.toString())
            .toString();
    }

    public Long getId() {
        return id;
    }

    public String getClientName() {
        return clientName;
    }

    public Integer getDate1Amount() {
        return date1Amount;
    }

    public Integer getDate2Amount() {
        return date2Amount;
    }

    public Integer getAbsDiff() {
        return absDiff;
    }

    public String getRelDiffPercent() {
        return relDiffPercent;
    }

    public String getLogin() {
        return login;
    }

    public TestUsers getUserId() {
        return userId;
    }
}
