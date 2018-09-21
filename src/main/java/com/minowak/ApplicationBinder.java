package com.minowak;

import com.minowak.service.TransferService;
import com.minowak.service.UsersService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class ApplicationBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(UsersService.class).to(UsersService.class).in(Singleton.class);
        bind(TransferService.class).to(TransferService.class).in(Singleton.class);
    }
}
