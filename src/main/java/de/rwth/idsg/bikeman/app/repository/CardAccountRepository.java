package de.rwth.idsg.bikeman.app.repository;

public interface CardAccountRepository {

    void setCurrentTariff(Long cardAccountId, Long tariffId);

    void setAutomaticRenewal(Long cardAccountId, Boolean status);

}
