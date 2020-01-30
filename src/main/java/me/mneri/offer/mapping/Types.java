package me.mneri.offer.mapping;

import lombok.NoArgsConstructor;
import me.mneri.offer.dto.OfferDto;
import me.mneri.offer.dto.UserDto;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class Types {
    //@formatter:off
    public static final Type OFFER_DTO_LIST_TYPE = new TypeToken<List<OfferDto>>() {}.getType();
    public static final Type USER_DTO_LIST_TYPE  = new TypeToken<List<UserDto>>() {}.getType();
    //@formatter:on
}
