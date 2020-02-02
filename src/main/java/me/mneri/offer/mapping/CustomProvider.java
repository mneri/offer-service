package me.mneri.offer.mapping;

import lombok.SneakyThrows;
import me.mneri.offer.entity.Offer;
import org.modelmapper.Provider;

/**
 * Custom mapping provider capable of creating {@link Offer} instances.
 *
 * @author mneri
 */
class CustomProvider implements Provider<Object> {
    @Override
    @SneakyThrows
    public Object get(ProvisionRequest<Object> request) {
        Class<?> type = request.getRequestedType();

        // Since the Offer class doesn't have a default constructor we have to tell the mapper how to create a new
        // instance. If the type is Offer we use its builder to obtain a new instance, otherwise we return null.
        // Returning null from this method tells ModelMapper to guess the construction by itself.
        if (type.equals(Offer.class)) {
            return Offer.builder().build();
        } else {
            return null;
        }
    }
}
