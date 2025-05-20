package ru.practicum.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.practicum.model.dto.AccountResponse;
import ru.practicum.model.entity.Account;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-20T20:56:46+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.1 (Amazon.com Inc.)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public AccountResponse toDto(Account account) {
        if ( account == null ) {
            return null;
        }

        AccountResponse.AccountResponseBuilder accountResponse = AccountResponse.builder();

        accountResponse.currency( account.getCurrency() );
        accountResponse.amount( account.getAmount() );

        return accountResponse.build();
    }
}
