package ru.testservice.demo.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.testservice.demo.dto.WalletDto;
import ru.testservice.demo.exceptions.NoMoneyException;
import ru.testservice.demo.models.Wallet;

import java.util.UUID;

@Repository
@Transactional
public class WalletRepoImpl implements WalletRepo {

    @PersistenceContext
    EntityManager em;

    @Override
    public WalletDto incrementWallet(WalletDto wallet) {
        Wallet oldWallet = em.find(Wallet.class, wallet.getWalletId());
        wallet.setAmount(oldWallet.getAmount() + wallet.getAmount());
        em.merge(wallet.getWallet());
        em.close();
        return wallet;
    }

    @Override
    public WalletDto decrementWallet(WalletDto wallet) throws NoMoneyException {
        Wallet oldWallet = em.find(Wallet.class, wallet.getWalletId());
        if (oldWallet.getAmount() >= wallet.getAmount()) {
            wallet.setAmount(oldWallet.getAmount() - wallet.getAmount());
            em.merge(wallet.getWallet());
            em.close();
            return wallet;
        } else {
            throw new NoMoneyException();
        }

    }

    @Override
    public WalletDto getWallet(UUID uuid) {
        return new WalletDto(em.find(Wallet.class, uuid));
    }
}
