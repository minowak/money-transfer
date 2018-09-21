package com.minowak.api;

import com.minowak.model.Transfer;
import com.minowak.service.TransferService;
import com.minowak.service.UsersService;
import org.junit.Before;
import org.junit.Test;

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
        Transfer transfer = new Transfer(1L, "1234", "5678", BigInteger.valueOf(100));

        // When
        transferResource.addTransfer(transfer);
        Transfer transferResult = transferService.get(transfer.getId());

        // Then
        assertEquals(transfer, transferResult);
    }
}