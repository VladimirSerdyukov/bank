package ru.testservice.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.testservice.demo.controllers.WalletController;
import ru.testservice.demo.dto.WalletDto;
import ru.testservice.demo.exceptions.NoMoneyException;
import ru.testservice.demo.exceptions.NoOperationException;
import ru.testservice.demo.staticEntity.Operation;

import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	WalletController walletController;

	@Test
	void contextLoads() {
		assertThat(walletController, is(notNullValue()));
	}

	@Test
	void getWalletTest() {
		ResponseEntity<WalletDto> entity = walletController.getWallet(UUID.fromString("643e2777-8cb8-446b-b1d1-5bb56679744d"));
		assertThat(entity.getStatusCode(), is(HttpStatus.OK));
		assertThat(entity.getBody().getWalletId(), is(UUID.fromString("643e2777-8cb8-446b-b1d1-5bb56679744d")));
		assertThat(entity.getBody().getAmount(), is(notNullValue()));
	}

	@Test
	void updateDepositTest() throws NoMoneyException, NoOperationException {
		WalletDto walletReq = new WalletDto();
		walletReq.setWalletId(UUID.fromString("643e2777-8cb8-446b-b1d1-5bb56679744d"));
		walletReq.setAmount(100L);
		walletReq.setOperationType(Operation.DEPOSIT);

		ResponseEntity<WalletDto> preTestResponse = walletController.getWallet(walletReq.getWalletId());
		WalletDto preTestWallet = preTestResponse.getBody();
		preTestWallet.setAmount(preTestWallet.getAmount() + walletReq.getAmount());
		preTestWallet.setOperationType(Operation.DEPOSIT);

		ResponseEntity<WalletDto> responseTestMethod = walletController.updateWallet(walletReq);
		WalletDto testWallet = responseTestMethod.getBody();

		assertThat(Objects.equals(testWallet, preTestWallet), is(true));
	}

	@Test
	void updateWithdrawTest() throws NoMoneyException, NoOperationException {
		WalletDto walletReq = new WalletDto();
		walletReq.setWalletId(UUID.fromString("643e2777-8cb8-446b-b1d1-5bb56679744d"));
		walletReq.setAmount(100L);
		walletReq.setOperationType(Operation.WITHDRAW);

		ResponseEntity<WalletDto> preTestResponse = walletController.getWallet(walletReq.getWalletId());
		WalletDto preTestWallet = preTestResponse.getBody();
		preTestWallet.setAmount(preTestWallet.getAmount() - walletReq.getAmount());
		preTestWallet.setOperationType(Operation.WITHDRAW);

		ResponseEntity<WalletDto> responseTestMethod = walletController.updateWallet(walletReq);
		WalletDto testWallet = responseTestMethod.getBody();

		assertThat(Objects.equals(testWallet, preTestWallet), is(true));
	}
}
