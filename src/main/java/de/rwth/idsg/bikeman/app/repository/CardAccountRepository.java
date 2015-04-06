package de.rwth.idsg.bikeman.app.repository;

import de.rwth.idsg.bikeman.app.dto.ChangeTariffDTO;

public interface CardAccountRepository {

    public void setCurrentTariff(Long cardAccountId, Long tariffId);

    public void setAutomaticRenewal(Long cardAccountId, Boolean status);

}
