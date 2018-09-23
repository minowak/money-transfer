package com.minowak.api;

import com.minowak.model.Account;
import com.minowak.model.Transfer;
import com.minowak.model.User;
import com.minowak.service.TransferService;
import com.minowak.service.UsersService;
import jersey.repackaged.com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TransferResourceTest {
    private TransferService transferService;
    private UsersService usersService;

    private TransferResource transferResource;

    @Before
    public void setUp() {
        transferService = new TransferService();
        usersService = new UsersService();
        transferResource = new TransferResource(transferService, usersService);
    }

    @Test
    public void shouldGetTransfers() {
        // Given
        Transfer transfer1 = new Transfer(1L, "1234", "5678", BigInteger.valueOf(100));
        Transfer transfer2 = new Transfer(2L, "1234", "5678", BigInteger.valueOf(10));

        // When
        transferService.add(transfer1);
        transferService.add(transfer2);
        Collection<Transfer> transferResults = transferResource.getTransfers();

        // Then
        assertEquals(2, transferResults.size());
        assertTrue(transferResults.contains(transfer1));
        assertTrue(transferResults.contains(transfer2));
    }

    @Test
    public void shouldGetTransfer() {
        // Given
        Transfer transfer = new Transfer(1L, "1234", "5678", BigInteger.valueOf(100));

        // When
        transferService.add(transfer);
        Transfer transferResult = transferResource.getTransfer(transfer.getId());

        // Then
        assertEquals(transfer, transferResult);
    }

    @Test
    public void shouldAddTransfer() {
        // Given
        Account account1 = new Account("1234", Sets.newHashSet());
        Account account2 = new Account("5678", Sets.newHashSet());
        User user = new User(1L, "testName", "testSurname", Sets.newHashSet(account1, account2));
        Transfer transfer = new Transfer(1L, account1.getNumber(), account2.getNumber(), BigInteger.valueOf(100));

        // When
        usersService.add(user);
        transferResource.addTransfer(transfer);
        Transfer transferResult = transferService.get(transfer.getId());

        // Then
        assertEquals(transfer, transferResult);
    }

    @Test
    public void shouldNotAddTransferForNonExistingInputAccount() {
        // Given
        Account account = new Account("5678", Sets.newHashSet());
        User user = new User(1L, "testName", "testSurname", Sets.newHashSet(account));
        Transfer transfer = new Transfer(1L, "1234", account.getNumber(), BigInteger.valueOf(100));

        // When
        usersService.add(user);
        Response response = transferResource.addTransfer(transfer);

        // Then
        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
    }

    @Test
    public void shouldNotAddTransferForNonExistingOutputAccount() {
        // Given
        Account account = new Account("1234", Sets.newHashSet());
        User user = new User(1L, "testName", "testSurname", Sets.newHashSet(account));
        Transfer transfer = new Transfer(1L, account.getNumber(), "5678", BigInteger.valueOf(100));

        // When
        usersService.add(user);
        Response response = transferResource.addTransfer(transfer);

        // Then
        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
    }

    @Test
    public void shouldAddDepositTransfer() {
        // Given
        Account account = new Account("5678", Sets.newHashSet());
        User user = new User(1L, "testName", "testSurname", Sets.newHashSet(account));
        Transfer transfer = new Transfer(1L, null, account.getNumber(), BigInteger.valueOf(100));

        // When
        usersService.add(user);
        transferResource.addTransfer(transfer);
        Transfer transferResult = transferService.get(transfer.getId());

        // Then
        assertEquals(transfer, transferResult);
    }

    @Test
    public void shouldAddWithdrawalTransfer() {
        // Given
        Account account = new Account("5678", Sets.newHashSet());
        User user = new User(1L, "testName", "testSurname", Sets.newHashSet(account));
        Transfer transfer = new Transfer(1L, account.getNumber(), null, BigInteger.valueOf(100));

        // When
        usersService.add(user);
        transferResource.addTransfer(transfer);
        Transfer transferResult = transferService.get(transfer.getId());

        // Then
        assertEquals(transfer, transferResult);
    }
}